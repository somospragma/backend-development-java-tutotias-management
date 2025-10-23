#!/bin/bash

# Script para desplegar políticas IAM para Terraform (Genérico)
# Uso: ./deploy-policies.sh [create|delete]
# NOTA: Este script es OPCIONAL. Puedes usar los comandos manuales del README.

set -e

ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ROLE_NAME="TerraformDeploymentRole"
REGION="us-east-1"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
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

# Función para crear políticas
create_policies() {
    log "Iniciando creación de políticas IAM genéricas para Terraform..."
    
    # Verificar que los archivos de políticas existen
    for policy in 01-networking 02-security 03-persistence 04-workload 05-terraform-state; do
        if [ ! -f "${policy}-policy.json" ]; then
            error "Archivo de política ${policy}-policy.json no encontrado"
            exit 1
        fi
    done
    
    # Crear trust policy para el rol
    log "Creando trust policy..."
    cat > trust-policy.json << EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::$ACCOUNT_ID:root"
      },
      "Action": "sts:AssumeRole",
      "Condition": {
        "StringEquals": {
          "sts:ExternalId": "terraform-deployment"
        }
      }
    }
  ]
}
EOF
    
    # Crear rol si no existe
    if aws iam get-role --role-name $ROLE_NAME >/dev/null 2>&1; then
        warn "Rol $ROLE_NAME ya existe"
    else
        log "Creando rol $ROLE_NAME..."
        aws iam create-role \
            --role-name $ROLE_NAME \
            --assume-role-policy-document file://trust-policy.json
        log "Rol $ROLE_NAME creado exitosamente"
    fi
    
    # Crear y asignar políticas
    declare -A policies=(
        ["01-networking"]="Networking"
        ["02-security"]="Security"
        ["03-persistence"]="Persistence"
        ["04-workload"]="Workload"
        ["05-terraform-state"]="State"
    )
    
    for policy_file in "${!policies[@]}"; do
        policy_name="Terraform${policies[$policy_file]}Policy"
        
        log "Creando política $policy_name..."
        
        # Verificar si la política ya existe
        if aws iam get-policy --policy-arn "arn:aws:iam::$ACCOUNT_ID:policy/$policy_name" >/dev/null 2>&1; then
            warn "Política $policy_name ya existe, actualizando..."
            # Crear nueva versión de la política
            aws iam create-policy-version \
                --policy-arn "arn:aws:iam::$ACCOUNT_ID:policy/$policy_name" \
                --policy-document "file://${policy_file}-policy.json" \
                --set-as-default
        else
            # Crear nueva política
            aws iam create-policy \
                --policy-name $policy_name \
                --policy-document "file://${policy_file}-policy.json" \
                --description "Política IAM genérica para módulo ${policies[$policy_file]} de Terraform"
        fi
        
        log "Asignando política $policy_name al rol $ROLE_NAME..."
        aws iam attach-role-policy \
            --role-name $ROLE_NAME \
            --policy-arn "arn:aws:iam::$ACCOUNT_ID:policy/$policy_name"
        
        log "Política $policy_name creada y asignada exitosamente"
    done
    
    log "Despliegue de políticas completado exitosamente!"
    log "Rol: $ROLE_NAME"
    log "Región: $REGION"
    log "Account ID: $ACCOUNT_ID"
    log "Para usar el rol: aws sts assume-role --role-arn arn:aws:iam::$ACCOUNT_ID:role/$ROLE_NAME --role-session-name terraform-session --external-id terraform-deployment"
    
    # Limpiar archivo temporal
    rm -f trust-policy.json
}

# Función para eliminar políticas
delete_policies() {
    log "Iniciando eliminación de políticas IAM genéricas para Terraform..."
    
    # Verificar si el rol existe
    if ! aws iam get-role --role-name $ROLE_NAME >/dev/null 2>&1; then
        warn "Rol $ROLE_NAME no existe"
        return 0
    fi
    
    # Desasociar y eliminar políticas
    declare -A policies=(
        ["Networking"]="TerraformNetworkingPolicy"
        ["Security"]="TerraformSecurityPolicy"
        ["Persistence"]="TerraformPersistencePolicy"
        ["Workload"]="TerraformWorkloadPolicy"
        ["State"]="TerraformStatePolicy"
    )
    
    for policy_type in "${!policies[@]}"; do
        policy_name="${policies[$policy_type]}"
        policy_arn="arn:aws:iam::$ACCOUNT_ID:policy/$policy_name"
        
        log "Desasociando política $policy_name del rol $ROLE_NAME..."
        aws iam detach-role-policy \
            --role-name $ROLE_NAME \
            --policy-arn $policy_arn 2>/dev/null || warn "Política $policy_name no estaba asociada"
        
        log "Eliminando política $policy_name..."
        aws iam delete-policy \
            --policy-arn $policy_arn 2>/dev/null || warn "Política $policy_name no existe"
    done
    
    # Eliminar rol
    log "Eliminando rol $ROLE_NAME..."
    aws iam delete-role --role-name $ROLE_NAME
    
    # Limpiar archivos temporales
    rm -f trust-policy.json
    
    log "Eliminación de políticas completada exitosamente!"
}

# Función para mostrar ayuda
show_help() {
    echo "Uso: $0 [create|delete|help]"
    echo ""
    echo "Comandos:"
    echo "  create  - Crear usuario y políticas IAM"
    echo "  delete  - Eliminar usuario y políticas IAM"
    echo "  help    - Mostrar esta ayuda"
    echo ""
    echo "Ejemplo:"
    echo "  $0 create"
    echo "  $0 delete"
}

# Main
case "${1:-help}" in
    create)
        create_policies
        ;;
    delete)
        delete_policies
        ;;
    help|--help|-h)
        show_help
        ;;
    *)
        error "Comando no válido: $1"
        show_help
        exit 1
        ;;
esac