#!/bin/bash

# Script para validar políticas IAM antes del despliegue
# Uso: ./validate-policies.sh

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para logging
log() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

info() {
    echo -e "${BLUE}[VALIDATE]${NC} $1"
}

# Función para validar JSON
validate_json() {
    local file=$1
    if jq empty "$file" 2>/dev/null; then
        log "✓ $file - JSON válido"
        return 0
    else
        error "✗ $file - JSON inválido"
        return 1
    fi
}

# Función para validar estructura de política IAM
validate_policy_structure() {
    local file=$1
    local policy_name=$(basename "$file" .json)
    
    info "Validando estructura de $policy_name..."
    
    # Verificar campos requeridos
    if ! jq -e '.Version' "$file" >/dev/null 2>&1; then
        error "✗ $file - Falta campo 'Version'"
        return 1
    fi
    
    if ! jq -e '.Statement' "$file" >/dev/null 2>&1; then
        error "✗ $file - Falta campo 'Statement'"
        return 1
    fi
    
    # Verificar que Statement es un array
    if ! jq -e '.Statement | type == "array"' "$file" >/dev/null 2>&1; then
        error "✗ $file - 'Statement' debe ser un array"
        return 1
    fi
    
    # Verificar cada statement
    local statement_count=$(jq '.Statement | length' "$file")
    for ((i=0; i<statement_count; i++)); do
        local statement=".Statement[$i]"
        
        # Verificar campos requeridos en cada statement
        for field in "Effect" "Action" "Resource"; do
            if ! jq -e "$statement.$field" "$file" >/dev/null 2>&1; then
                error "✗ $file - Statement $i falta campo '$field'"
                return 1
            fi
        done
        
        # Verificar que Effect es Allow o Deny
        local effect=$(jq -r "$statement.Effect" "$file")
        if [[ "$effect" != "Allow" && "$effect" != "Deny" ]]; then
            error "✗ $file - Statement $i Effect debe ser 'Allow' o 'Deny'"
            return 1
        fi
    done
    
    log "✓ $file - Estructura válida"
    return 0
}

# Función para mostrar estadísticas de la política
show_policy_stats() {
    local file=$1
    local policy_name=$(basename "$file" .json)
    
    info "Estadísticas de $policy_name:"
    
    local statement_count=$(jq '.Statement | length' "$file")
    echo "  - Statements: $statement_count"
    
    local total_actions=0
    for ((i=0; i<statement_count; i++)); do
        local actions=$(jq ".Statement[$i].Action | if type == \"array\" then length else 1 end" "$file")
        total_actions=$((total_actions + actions))
    done
    echo "  - Total acciones: $total_actions"
    
    # Mostrar servicios únicos
    local services=$(jq -r '.Statement[].Action | if type == "array" then .[] else . end' "$file" | cut -d':' -f1 | sort -u | tr '\n' ' ')
    echo "  - Servicios AWS: $services"
    
    # Verificar si tiene condiciones
    local has_conditions=$(jq '.Statement[] | select(.Condition) | length' "$file" 2>/dev/null | wc -l)
    echo "  - Statements con condiciones: $has_conditions"
}

# Función principal de validación
validate_all_policies() {
    log "Iniciando validación de políticas IAM..."
    
    local policies=(
        "01-networking-policy.json"
        "02-security-policy.json"
        "03-persistence-policy.json"
        "04-workload-policy.json"
        "05-terraform-state-policy.json"
    )
    
    local valid_count=0
    local total_count=${#policies[@]}
    
    for policy_file in "${policies[@]}"; do
        if [ ! -f "$policy_file" ]; then
            error "✗ Archivo $policy_file no encontrado"
            continue
        fi
        
        echo ""
        info "=== Validando $policy_file ==="
        
        # Validar JSON
        if ! validate_json "$policy_file"; then
            continue
        fi
        
        # Validar estructura
        if ! validate_policy_structure "$policy_file"; then
            continue
        fi
        
        # Mostrar estadísticas
        show_policy_stats "$policy_file"
        
        valid_count=$((valid_count + 1))
    done
    
    echo ""
    log "=== Resumen de Validación ==="
    log "Políticas válidas: $valid_count/$total_count"
    
    if [ $valid_count -eq $total_count ]; then
        log "✓ Todas las políticas son válidas"
        return 0
    else
        error "✗ Algunas políticas tienen errores"
        return 1
    fi
}

# Función para simular política (requiere AWS CLI)
simulate_policy() {
    local policy_file=$1
    local policy_name=$(basename "$policy_file" .json)
    
    info "Simulando política $policy_name..."
    
    # Verificar si AWS CLI está configurado
    if ! aws sts get-caller-identity >/dev/null 2>&1; then
        warn "AWS CLI no configurado, saltando simulación"
        return 0
    fi
    
    # Simular algunas acciones comunes
    local test_actions=()
    case "$policy_name" in
        "01-networking-policy")
            test_actions=("ec2:DescribeVpcs" "ec2:CreateVpc")
            ;;
        "02-security-policy")
            test_actions=("iam:CreateRole" "kms:CreateKey")
            ;;
        "03-persistence-policy")
            test_actions=("rds:CreateDBCluster" "rds:DescribeDBClusters")
            ;;
        "04-workload-policy")
            test_actions=("ecs:CreateCluster" "ecr:CreateRepository")
            ;;
        "05-terraform-state-policy")
            test_actions=("s3:GetObject" "dynamodb:GetItem")
            ;;
    esac
    
    for action in "${test_actions[@]}"; do
        local result=$(aws iam simulate-principal-policy \
            --policy-source-arn "arn:aws:iam::$(aws sts get-caller-identity --query Account --output text):user/test" \
            --policy-input-list "file://$policy_file" \
            --action-names "$action" \
            --query 'EvaluationResults[0].EvalDecision' \
            --output text 2>/dev/null || echo "Error")
        
        if [ "$result" = "allowed" ]; then
            log "  ✓ $action: Permitido"
        else
            warn "  ⚠ $action: $result"
        fi
    done
}

# Main
case "${1:-validate}" in
    validate)
        validate_all_policies
        ;;
    simulate)
        if [ -z "$2" ]; then
            error "Especifica el archivo de política para simular"
            echo "Uso: $0 simulate <policy-file.json>"
            exit 1
        fi
        simulate_policy "$2"
        ;;
    help|--help|-h)
        echo "Uso: $0 [validate|simulate|help]"
        echo ""
        echo "Comandos:"
        echo "  validate           - Validar todas las políticas"
        echo "  simulate <file>    - Simular política específica"
        echo "  help              - Mostrar esta ayuda"
        ;;
    *)
        error "Comando no válido: $1"
        exit 1
        ;;
esac