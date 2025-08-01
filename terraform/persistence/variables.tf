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

variable "aws_region_principal" {
  description = "Región de AWS donde se desplegarán los recursos Region Principal"
  type        = string
  default     = ""
}

variable "aws_region_secondary" {
  description = "Región de AWS donde se desplegarán los recursos Region Secondary"
  type        = string
  default     = ""
}

variable "common_tags" {
  type        = map(string)
  description = "Common tags to be applied to the resources"
}

variable "profile" {
  description = "Account AWS"
  type        = string
}

variable "region" {
  type        = string
  description = "Región de AWS donde se desplegarán los recursos"
  default     = "us-east-1"
}

##############################################################
# Variables RDS
##############################################################


