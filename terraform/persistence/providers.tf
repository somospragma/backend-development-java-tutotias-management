######################################################################
# Provider AWS
######################################################################
provider "aws" {
  alias   = "principal"
  region  = var.aws_region_principal
  profile = var.profile

  default_tags {
    tags = var.common_tags
  }
}

provider "aws" {
  alias   = "secondary"
  region  = var.aws_region_secondary

  default_tags {
    tags = var.common_tags
  }
  profile = var.profile
}


###########################################
#Version definition - Terraform - Providers
###########################################

terraform {
  required_version = ">= 1.10.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = ">=4.31.0"
    }
  }
  backend "s3" {  
    bucket       = "pragma-tutorias-poc-terraform-tfstate"  
    key          = "tutorias/persistence/terraform.tfstate"  
    region       = "us-east-1"  
    encrypt      = true  
    use_lockfile = true 
    profile      = "pra_backend_dev"
  }
}