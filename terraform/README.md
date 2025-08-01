# Arquitectura de Microservicios con ECS - Sistema de Tutorías

Este repositorio contiene la infraestructura como código (IaC) para desplegar una arquitectura de microservicios completa en AWS utilizando Amazon ECS (Elastic Container Service). La arquitectura está diseñada para soportar el sistema de gestión de tutorías de Pragma.

## 📋 Tabla de Contenidos

- [Arquitectura General](#arquitectura-general)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Orden de Despliegue](#orden-de-despliegue)
- [Módulos de Infraestructura](#módulos-de-infraestructura)
- [Consideraciones Importantes](#consideraciones-importantes)
- [Requisitos Previos](#requisitos-previos)
- [Instrucciones de Despliegue](#instrucciones-de-despliegue)
- [Variables de Entorno](#variables-de-entorno)

## 🏗️ Arquitectura General

La arquitectura implementa un patrón de microservicios containerizados con las siguientes características:

- **Networking**: VPC multi-AZ con subredes públicas, privadas, de servicios y de base de datos
- **Security**: Grupos de seguridad, roles IAM, KMS para cifrado y AWS Secrets Manager
- **Persistence**: Base de datos Aurora PostgreSQL Serverless
- **Workload**: Servicios ECS con Application Load Balancer y Service Discovery

```
┌─────────────────────────────────────────────────────────────────┐
│                          Internet Gateway                        │
└─────────────────────────┬───────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│                    Public Subnets                               │
│  ┌─────────────────┐              ┌─────────────────┐           │
│  │   us-east-1a    │              │   us-east-1b    │           │
│  │  10.60.0.0/25   │              │  10.60.0.128/25 │           │
│  └─────────────────┘              └─────────────────┘           │
│              │                              │                   │
│         ┌────▼────┐                    ┌────▼────┐              │
│         │   ALB   │                    │   NAT   │              │
│         └─────────┘                    └─────────┘              │
└─────────────────────────────────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│                   Service Subnets                               │
│  ┌─────────────────┐              ┌─────────────────┐           │
│  │   us-east-1a    │              │   us-east-1b    │           │
│  │  10.60.2.0/25   │              │  10.60.2.128/25 │           │
│  │  ┌───────────┐  │              │  ┌───────────┐  │           │
│  │  │ECS Service│  │              │  │ECS Service│  │           │
│  │  └───────────┘  │              │  └───────────┘  │           │
│  └─────────────────┘              └─────────────────┘           │
└─────────────────────────────────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────┐
│                  Database Subnets                               │
│  ┌─────────────────┐              ┌─────────────────┐           │
│  │   us-east-1a    │              │   us-east-1b    │           │
│  │  10.60.3.0/25   │              │  10.60.3.128/25 │           │
│  │ ┌─────────────┐ │              │ ┌─────────────┐ │           │
│  │ │Aurora       │ │              │ │Aurora       │ │           │
│  │ │PostgreSQL   │ │              │ │PostgreSQL   │ │           │
│  │ └─────────────┘ │              │ └─────────────┘ │           │
│  └─────────────────┘              └─────────────────┘           │
└─────────────────────────────────────────────────────────────────┘
```

## 📁 Estructura del Proyecto

```
terraform/
├── networking/          # Módulo de red (VPC, subnets, routing)
│   ├── main.tf
│   ├── variables.tf
│   ├── outputs.tf
│   ├── data.tf
│   ├── providers.tf
│   └── environments/
│       ├── dev/
│       ├── qa/
│       └── pdn/
├── security/           # Módulo de seguridad (IAM, SG, KMS, Secrets)
│   ├── main.tf
│   ├── variables.tf
│   ├── outputs.tf
│   ├── data.tf
│   ├── providers.tf
│   └── environments/
│       ├── dev/
│       ├── qa/
│       └── produccion/
├── persistence/        # Módulo de persistencia (RDS Aurora)
│   ├── main.tf
│   ├── variables.tf
│   ├── outputs.tf
│   ├── data.tf
│   ├── providers.tf
│   └── environments/
│       ├── dev/
│       ├── qa/
│       └── pdn/
└── workload/          # Módulo de carga de trabajo (ECS, ALB, ECR)
    ├── main.tf
    ├── variables.tf
    ├── outputs.tf
    ├── data.tf
    ├── providers.tf
    └── environments/
        ├── dev/
        ├── qa/
        └── prod/
```

## 🚀 Orden de Despliegue

**⚠️ IMPORTANTE**: Los módulos deben desplegarse en el siguiente orden debido a las dependencias entre ellos:

### 1. Networking (Primero)
```bash
cd networking/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"
```

### 2. Security (Segundo)
```bash
cd ../security/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"
```

### 3. Persistence (Tercero)
```bash
cd ../persistence/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"
```

### 4. Workload (Cuarto)
```bash
cd ../workload/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"
```

## 🏗️ Módulos de Infraestructura

### 1. 🌐 Networking

**Propósito**: Establece la base de red para toda la arquitectura.

**Recursos desplegados**:
- **VPC**: Red virtual privada con CIDR `10.60.0.0/22`
- **Subnets**:
  - **Public Subnets** (2): Para ALB y NAT Gateway
    - `10.60.0.0/25` (us-east-1a)
    - `10.60.0.128/25` (us-east-1b)
  - **Private Subnets** (2): Para recursos internos
    - `10.60.1.0/25` (us-east-1a)
    - `10.60.1.128/25` (us-east-1b)
  - **Service Subnets** (2): Para servicios ECS
    - `10.60.2.0/25` (us-east-1a)
    - `10.60.2.128/25` (us-east-1b)
  - **Database Subnets** (2): Para RDS Aurora
    - `10.60.3.0/25` (us-east-1a)
    - `10.60.3.128/25` (us-east-1b)
- **Internet Gateway**: Para acceso a internet
- **NAT Gateway**: Para salida a internet desde subnets privadas
- **Route Tables**: Configuración de enrutamiento

**Data Sources utilizados**:
- `aws_caller_identity`: Información de la cuenta AWS
- `aws_region`: Región actual

### 2. 🔒 Security

**Propósito**: Implementa todas las medidas de seguridad necesarias.

**Recursos desplegados**:
- **KMS Keys**:
  - Clave para Secrets Manager
  - Clave para RDS (cifrado en reposo)
- **Secrets Manager**:
  - Secret para credenciales de base de datos
- **Security Groups**:
  - **ALB Security Group**: Permite tráfico HTTP/HTTPS desde IPs específicas
  - **ECS Security Group**: Permite tráfico desde ALB en puerto 8080
  - **RDS Security Group**: Permite tráfico MySQL desde ECS
- **IAM Roles**:
  - **Task Role**: Para tareas ECS (acceso a CloudWatch Logs y Secrets Manager)
  - **Execution Role**: Para ejecución de contenedores (ECR, CloudWatch, Secrets Manager)

**Data Sources utilizados**:
- `aws_caller_identity`: Información de la cuenta AWS
- `aws_region`: Región actual
- `aws_vpc`: Referencia a la VPC creada en networking

**Consideraciones de seguridad**:
- Cifrado en tránsito y en reposo
- Principio de menor privilegio en IAM
- Acceso restringido por Security Groups
- Rotación automática de claves KMS habilitada

### 3. 💾 Persistence

**Propósito**: Proporciona la capa de persistencia de datos.

**Recursos desplegados**:
- **Aurora MySQL Serverless**:
  - Engine: `aurora-mysql` versión 8.0.mysql_aurora.3.02.0
  - Modo: Serverless v2 (escalado automático)
  - Capacidad: 0.5 - 2 ACUs
  - Auto-pause: 3600 segundos
  - Cifrado habilitado con KMS
  - Backups automáticos (7 días de retención)
  - Performance Insights habilitado
  - Logs de MySQL exportados a CloudWatch (error, general, slowquery)

**Data Sources utilizados**:
- `aws_vpc`: Referencia a la VPC
- `aws_subnets`: Subnets de base de datos
- `aws_security_group`: Security Group para RDS
- `aws_kms_alias`: Clave KMS para cifrado

**Características**:
- **Serverless**: Escalado automático basado en demanda
- **Multi-AZ**: Alta disponibilidad
- **Cifrado**: Datos cifrados en reposo y en tránsito
- **Monitoreo**: Performance Insights y CloudWatch Logs

### 4. ⚙️ Workload

**Propósito**: Despliega la aplicación containerizada y sus componentes.

**Recursos desplegados**:
- **Application Load Balancer (ALB)**:
  - Balanceador público en subnets públicas
  - Target Group para servicios ECS
  - Health checks configurados
- **ECS Cluster**:
  - Cluster con Fargate
  - Container Insights habilitado
- **ECR Repository**:
  - Repositorio para imágenes Docker
  - Escaneo de vulnerabilidades habilitado
- **ECS Service**:
  - Servicio `tutorias-core`
  - Fargate con 512 CPU / 1024 MB RAM
  - Service Connect habilitado
  - Auto Scaling configurado
- **CloudMap**:
  - Service Discovery para comunicación entre servicios

**Data Sources utilizados**:
- `aws_vpc`: Referencia a la VPC
- `aws_subnets`: Subnets públicas y de servicios
- `aws_security_group`: Security Groups para ALB y ECS
- `aws_iam_role`: Roles de ejecución y tarea
- `aws_rds_cluster`: Endpoint de la base de datos
- `aws_secretsmanager_secret`: Secretos de base de datos

**Configuración del contenedor**:
- **Imagen**: Desde ECR
- **Puerto**: 8080
- **Variables de entorno**: Spring profiles, configuración de DB
- **Secretos**: Credenciales de DB desde Secrets Manager
- **Logs**: CloudWatch Logs con retención de 7 días

## ⚠️ Consideraciones Importantes

### Dependencias entre Módulos

1. **Security** depende de **Networking** (VPC ID)
2. **Persistence** depende de **Networking** (subnets) y **Security** (SG, KMS)
3. **Workload** depende de todos los anteriores

### Configuración de Entornos

Cada módulo tiene configuraciones específicas por entorno en la carpeta `environments/`:
- `dev/`: Desarrollo
- `qa/`: Quality Assurance  
- `pdn/prod/produccion/`: Producción

### Etiquetado

Todos los recursos utilizan un esquema de etiquetado consistente:
```hcl
common_tags = {
  client      = "pragma"
  environment = "dev"
  project     = "tutorias"
  owner       = "jamer.pinto@pragma.com.co"
  area        = "infrastructure"
  provisioned = "terraform"
  application = "tutorias"
}
```

## 📋 Requisitos Previos

1. **Terraform** >= 1.0
2. **AWS CLI** configurado
3. **Perfil AWS** configurado (`pra_backend_dev`)
4. **Permisos IAM** necesarios para crear todos los recursos
5. **Imagen Docker** disponible para el servicio

## 🚀 Instrucciones de Despliegue

### Despliegue Completo

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd terraform/

# 2. Desplegar Networking
cd networking/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"

# 3. Desplegar Security
cd ../security/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"

# 4. Desplegar Persistence
cd ../persistence/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"

# 5. Desplegar Workload
cd ../workload/
terraform init
terraform plan -var-file="environments/dev/terraform.tfvars"
terraform apply -var-file="environments/dev/terraform.tfvars"
```

### Verificación del Despliegue

```bash
# Verificar ECS Service
aws ecs describe-services --cluster pragma-tutorias-dev-cluster-tutorias --services tutorias-core

# Verificar ALB
aws elbv2 describe-load-balancers --names pragma-tutorias-dev-alb-tutorias

# Verificar RDS
aws rds describe-db-clusters --db-cluster-identifier pragma-tutorias-dev-cluster-tutorias
```

## 🔧 Variables de Entorno

### Variables Globales (Todos los módulos)
- `client`: Cliente (pragma)
- `project`: Proyecto (tutorias)
- `environment`: Entorno (dev/qa/pdn)
- `aws_region`: Región AWS (us-east-1)
- `profile`: Perfil AWS

### Variables Específicas por Módulo

Consultar los archivos `variables.tf` en cada módulo para variables específicas.
