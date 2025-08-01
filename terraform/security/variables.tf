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

variable "profile" {
  description = "Account AWS"
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

variable "aws_region" {
  description = "Región de AWS donde se desplegarán los recursos"
  type        = string
  default     = ""
}

variable "common_tags" {
  type        = map(string)
  description = "Common tags to be applied to the resources"
}

##############################################################
# Variables IAM
##############################################################
variable "iam_config" {
  type = list(object({
    functionality = string
    application   = string
    service       = string
    path          = string
    type          = string
    identifiers   = list(string)
    principal_conditions = list(object({
          test     = string
          variable = string
          values   = list(string)
    }))
    policies = list(object({
      policy_description = string
      policy_statements = list(object({
        sid       = string
        actions   = list(string)
        resources = list(string)
        effect    = string
        condition = list(object({
          test     = string
          variable = string
          values   = list(string)
        }))
      }))
    }))
    managed_policy_arns = optional(list(string), [])
  }))
  description = "IAM roles and policies configuration"
}