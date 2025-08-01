##############################################################
# Config Providers Terraform - AWS
##############################################################
provider "aws" {
  region = var.aws_region
  alias  = "principal"
  profile = var.profile
  
  default_tags {
    tags = var.common_tags
  }
}

##############################################################
# Config Baceknd Terraform
##############################################################
terraform {
  required_version = ">= 1.10.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">= 4.20.1"
    }
  }
  backend "s3" {  
    bucket       = "pragma-tutorias-poc-terraform-tfstate"  
    key          = "tutorias/security/terraform.tfstate"  
    region       = "us-east-1"  
    encrypt      = true  
    use_lockfile = true 
    profile      = "pra_backend_dev"
  }  
}