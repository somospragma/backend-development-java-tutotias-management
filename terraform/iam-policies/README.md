# Políticas IAM para Despliegue de Infraestructura Terraform

Este directorio contiene las políticas IAM genéricas necesarias para desplegar infraestructura en AWS usando Terraform. Es un artefacto reutilizable para cualquier proyecto.

## 📋 Estructura de Políticas

```
iam-policies/
├── README.md                    # Este archivo
├── 01-networking-policy.json    # Política para módulo networking
├── 02-security-policy.json      # Política para módulo security
├── 03-persistence-policy.json   # Política para módulo persistence
├── 04-workload-policy.json      # Política para módulo workload
├── 05-terraform-state-policy.json # Política genérica para backend de Terraform
├── deploy-policies.sh           # Script automatizado (OPCIONAL)
└── validate-policies.sh         # Validación de políticas (OPCIONAL)
```

## 🚀 Opciones de Despliegue

### Opción 1: Comandos Manuales (Recomendado para Control Granular)

#### 1. Crear Rol IAM para Terraform

```bash
# Crear trust policy para el rol
cat > trust-policy.json << EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::ACCOUNT-ID:root"
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

# Crear rol
aws iam create-role \
  --role-name TerraformDeploymentRole \
  --assume-role-policy-document file://trust-policy.json
```

#### 2. Crear y Asignar Políticas por Módulo

##### Política Networking
```bash
aws iam create-policy \
  --policy-name TerraformNetworkingPolicy \
  --policy-document file://01-networking-policy.json

aws iam attach-role-policy \
  --role-name TerraformDeploymentRole \
  --policy-arn arn:aws:iam::ACCOUNT-ID:policy/TerraformNetworkingPolicy
```

##### Política Security
```bash
aws iam create-policy \
  --policy-name TerraformSecurityPolicy \
  --policy-document file://02-security-policy.json

aws iam attach-role-policy \
  --role-name TerraformDeploymentRole \
  --policy-arn arn:aws:iam::ACCOUNT-ID:policy/TerraformSecurityPolicy
```

##### Política Persistence
```bash
aws iam create-policy \
  --policy-name TerraformPersistencePolicy \
  --policy-document file://03-persistence-policy.json

aws iam attach-role-policy \
  --role-name TerraformDeploymentRole \
  --policy-arn arn:aws:iam::ACCOUNT-ID:policy/TerraformPersistencePolicy
```

##### Política Workload
```bash
aws iam create-policy \
  --policy-name TerraformWorkloadPolicy \
  --policy-document file://04-workload-policy.json

aws iam attach-role-policy \
  --role-name TerraformDeploymentRole \
  --policy-arn arn:aws:iam::ACCOUNT-ID:policy/TerraformWorkloadPolicy
```

##### Política Terraform State
```bash
aws iam create-policy \
  --policy-name TerraformStatePolicy \
  --policy-document file://05-terraform-state-policy.json

aws iam attach-role-policy \
  --role-name TerraformDeploymentRole \
  --policy-arn arn:aws:iam::ACCOUNT-ID:policy/TerraformStatePolicy
```

#### 3. Asumir el Rol para Terraform

```bash
# Asumir el rol
aws sts assume-role \
  --role-arn arn:aws:iam::ACCOUNT-ID:role/TerraformDeploymentRole \
  --role-session-name terraform-session \
  --external-id terraform-deployment

# Configurar credenciales temporales
export AWS_ACCESS_KEY_ID="<AccessKeyId>"
export AWS_SECRET_ACCESS_KEY="<SecretAccessKey>"
export AWS_SESSION_TOKEN="<SessionToken>"
```

### Opción 2: Scripts Automatizados (OPCIONAL)

Si prefieres automatizar el proceso, puedes usar los scripts incluidos:

```bash
# Validar políticas antes del despliegue
./validate-policies.sh

# Desplegar todas las políticas automáticamente
./deploy-policies.sh create

# Eliminar políticas (si es necesario)
./deploy-policies.sh delete
```

## 🔧 Uso con Terraform

Para usar el rol en Terraform, configura el provider:

```hcl
provider "aws" {
  region = "us-east-1"
  
  assume_role {
    role_arn     = "arn:aws:iam::ACCOUNT-ID:role/TerraformDeploymentRole"
    session_name = "terraform-session"
    external_id  = "terraform-deployment"
  }
}
```

## ⚠️ Consideraciones de Seguridad

1. **Principio de Menor Privilegio**: Cada política contiene solo los permisos necesarios para su módulo específico
2. **Recursos Específicos**: Donde es posible, se limitan los recursos usando ARNs específicos
3. **Condiciones**: Se incluyen condiciones para restringir el acceso por región y servicios
4. **Roles vs Usuarios**: Los roles son más seguros que usuarios con access keys permanentes
5. **External ID**: Se usa external ID para mayor seguridad en assume role
6. **Sesiones Temporales**: Las credenciales del rol son temporales y se rotan automáticamente
7. **Monitoreo**: Habilitar CloudTrail para auditar el uso de estas políticas

## 🔄 Orden de Aplicación

Las políticas deben aplicarse en el siguiente orden para respetar las dependencias:

1. **Terraform State Policy** (siempre primero)
2. **Networking Policy**
3. **Security Policy** 
4. **Persistence Policy**
5. **Workload Policy**

## 🎯 Características del Artefacto

- **Genérico**: No contiene referencias específicas a proyectos
- **Reutilizable**: Puede usarse en múltiples proyectos
- **Modular**: Políticas separadas por dominio de infraestructura
- **Seguro**: Implementa mejores prácticas de seguridad de AWS
- **Flexible**: Comandos manuales o scripts automatizados

## 🧹 Limpieza

Para eliminar las políticas y rol:

```bash
#!/bin/bash
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ROLE_NAME="TerraformDeploymentRole"

# Desasociar políticas
for policy in Networking Security Persistence Workload State; do
  POLICY_NAME="Terraform${policy}Policy"
  aws iam detach-role-policy \
    --role-name $ROLE_NAME \
    --policy-arn arn:aws:iam::$ACCOUNT_ID:policy/$POLICY_NAME
  
  aws iam delete-policy \
    --policy-arn arn:aws:iam::$ACCOUNT_ID:policy/$POLICY_NAME
done

# Eliminar rol
aws iam delete-role --role-name $ROLE_NAME

# Eliminar trust policy temporal
rm -f trust-policy.json
```