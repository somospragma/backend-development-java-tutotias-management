module "vpc" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-vpc-terraform.git?ref=v1.0.1"
  providers = {
    aws.project = aws.principal
  }
  client         = var.client
  functionality  = var.functionality
  environment    = var.environment
  
  cidr_block           = "10.60.0.0/22"
  instance_tenancy     = "default"
  enable_dns_support   = true
  enable_dns_hostnames = true
  flow_log_retention_in_days = 7
  
  subnet_config = {
    public-subnet = {
      public     = true
      include_nat = false
      subnets = [
        {
          cidr_block        = "10.60.0.0/25"
          availability_zone = "us-east-1a"
        },
        {
          cidr_block        = "10.60.0.128/25"
          availability_zone = "us-east-1b"
        }
      ]
       custom_routes = [
       ]
    }
    private-subnet = {
      public     = false
      include_nat = true
      subnets = [
        {
          cidr_block        = "10.60.1.0/25"
          availability_zone = "us-east-1a"
        },
        {
          cidr_block        = "10.60.1.128/25"
          availability_zone = "us-east-1b"
        }
      ]
       custom_routes = [
       ]
    },
    service-subnet = {
      public     = false
      include_nat = true
      subnets = [
        {
          cidr_block        = "10.60.2.0/25"
          availability_zone = "us-east-1a"
        },
        {
          cidr_block        = "10.60.2.128/25"
          availability_zone = "us-east-1b"
        }
      ]
       custom_routes = [
       ]
    },
    database-subnet = {
      public     = false
      include_nat = true
      subnets = [
        {
          cidr_block        = "10.60.3.0/25"
          availability_zone = "us-east-1a"
        },
        {
          cidr_block        = "10.60.3.128/25"
          availability_zone = "us-east-1b"
        }
      ]
       custom_routes = [
       ]
    }
  }
  create_igw = true
  create_nat = true
}