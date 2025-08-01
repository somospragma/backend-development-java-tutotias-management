##############################################################
# Variables Globales
##############################################################
variable "client" {
  description = "Nombre del cliente asociado a las zonas Route 53"
  type        = string
}

variable "project" {
  description = "Nombre del proyecto asociado a las zonas Route 53"
  type        = string
}

variable "environment" {
  description = "Entorno en el que se desplegarán las zonas Route 53 (develop, dev, qa, pdn)"
  type        = string
  validation {
    condition     = contains(["develop", "dev", "qa", "pdn"], var.environment)
    error_message = "El entorno debe ser uno de: develop, dev, qa, pdn."
  }
}

variable "common_tags" {
  type        = map(string)
  description = "Common tags to be applied to the resources"
}

variable "aws_region" {
  type        = string
  description = "Región de AWS donde se desplegarán los recursos"
  default     = "us-east-1"
}

variable "profile" {
  description = "Account AWS"
  type        = string
}

###############################################################
# Variables Cluster ECS
###############################################################
variable "cluster_config" {
  type = map(object({
    containerInsights       = string
    enableCapacityProviders = bool
    additional_tags         = optional(map(string), {})
  }))
  description = <<EOF
    Mapa de configuraciones de clusters ECS donde la clave es el nombre de la aplicación.
    
    Ejemplo:
    ```
    cluster_config = {
      "app01" = {
        containerInsights       = "enabled"
        enableCapacityProviders = true
        additional_tags         = {
          service-tier = "standard"
          backup-policy = "daily"
        }
      }
    }
    ```
    
    Parámetros:
    - containerInsights: (string) Value to assign to the setting. Valid values: enabled, disabled.
    - enableCapacityProviders: (bool) If true, is enabled FARGATE and FARGATE_SPOT.
    - additional_tags: (map) Additional tags to apply to the ECS cluster.
  EOF

  validation {
    condition = alltrue([
      for k, config in var.cluster_config :
      contains(["enabled", "disabled"], config.containerInsights)
    ])
    error_message = "containerInsights must be either 'enabled' or 'disabled'."
  }
  
  validation {
    condition = alltrue([
      for k, _ in var.cluster_config :
      can(regex("^[a-z0-9-]+$", k))
    ])
    error_message = "Application names (keys in cluster_config) must contain only lowercase letters, numbers, and hyphens."
  }
}


###############################################################
# Variables NameSpace CloudMap
###############################################################
variable "namespaces" {
  description = "Configuración de los namespaces de Cloud Map"
  type = map(object({
    name        = optional(string, null)
    description = optional(string, "")
    type        = optional(string, "HTTP")  # HTTP, DNS_PRIVATE, DNS_PUBLIC
    vpc_id      = optional(string, null)    # Requerido para DNS_PRIVATE
    
    # Configuración de servicios
    services = optional(map(object({
      description = optional(string, "")
      
      # Configuración de DNS (para namespaces DNS)
      dns_records = optional(list(object({
        ttl  = optional(number, 10)
        type = optional(string, "A")
      })), [])
      
      # Configuración de comprobaciones de salud
      health_check_config = optional(object({
        type                = string  # HTTP, HTTPS, TCP
        resource_path       = optional(string, "/health")
        failure_threshold   = optional(number, 1)
      }), null)
      
      health_check_custom_config = optional(object({
        failure_threshold = optional(number, 1)
      }), null)
      
      # Etiquetas adicionales
      additional_tags = optional(map(string), {})
    })), {})
    
    # Solo para namespaces DNS
    dns_properties = optional(object({
      dns_ttl             = optional(number, 60)
      routing_policy      = optional(string, "MULTIVALUE")  # MULTIVALUE o WEIGHTED
      soa = optional(object({
        ttl     = optional(number, 900)
        contact = optional(string, "")
      }), null)
    }), null)
    
    # Etiquetas adicionales
    additional_tags = optional(map(string), {})
  }))
  default = {}
}

###############################################################
# Variables ECR Service
###############################################################
variable "ecr_config" {
  type = list(object({
    force_delete = bool
    image_tag_mutability = string
    encryption_configuration = list(object({
      encryption_type = string
      kms_key = string
    }))
    image_scanning_configuration = list(object({
      scan_on_push = string
    }))
    functionality = string
    access_type    = string
    lifecycle_rules = optional(list(object({
      rulePriority = number
      description  = string
      selection = object({
        tagStatus   = string
        countType   = string
        countUnit   = string
        countNumber = number
      })
      action = object({
        type = string
      })
    })), [])
  }))
  description = "ECR repositories configuration"
}

variable "application" {
  type = string
  description = "application ECR"
}