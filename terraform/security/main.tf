##############################################################
# Modulo KMS
##############################################################
module "kms" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-kms-terraform.git?ref=feature/kms-module-map"

  providers = {
    aws.project = aws.principal
  }
  client          = var.client
  project         = var.project
  environment     = var.environment
  kms_config = {
    "sm" = {
      description         = "Key encrypting Secrets Manager"
      enable_key_rotation = true
      statements = [
        {
          sid = "AllowSecretsManagerServiceInAccount"
          actions = [
            "kms:Encrypt",
            "kms:Decrypt",
            "kms:ReEncrypt*",
            "kms:GenerateDataKey*",
            "kms:DescribeKey"
          ]
          resources   = ["*"]
          effect      = "Allow"
          type        = "Service"
          identifiers = ["secretsmanager.amazonaws.com"]
          condition = [
            {
              test     = "StringEquals"
              variable = "aws:SourceAccount"
              values   = [data.aws_caller_identity.current.account_id]
            }
          ]
        }
      ]
      additional_tags = {
        "Service"        = "SecretsManager",
        "SecurityLevel"  = "High",
        "DataType"       = "Confidential",
        "LeastPrivilege" = "True"
      }
    },
    "rds" = {
      description         = "Key encrypting RDS databases"
      enable_key_rotation = true
      statements = [
        {
          sid = "AllowRDSServiceInAccount"
          actions = [
            "kms:Encrypt",
            "kms:Decrypt",
            "kms:ReEncrypt*",
            "kms:GenerateDataKey*",
            "kms:DescribeKey"
          ]
          resources   = ["*"]
          effect      = "Allow"
          type        = "Service"
          identifiers = ["rds.amazonaws.com"]
          condition = [
            {
              test     = "StringEquals"
              variable = "aws:SourceAccount"
              values   = [data.aws_caller_identity.current.account_id]
            }
          ]
        }
      ]
      additional_tags = {
        "Service"        = "RDS",
        "SecurityLevel"  = "High",
        "DataType"       = "Confidential",
        "LeastPrivilege" = "True"
      }
    },
  }

}
##############################################################
# Modulo SM
##############################################################
module "sm" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-sm-terraform.git?ref=v1.0.0"
  providers = {
    aws.project = aws.principal
  }
  client      = var.client
  project     = var.project
  environment = var.environment
  secrets_config = {
    "tutorias" = {
      description                    = "Secret Manager - Tutorias"
      kms_key_id                     = module.kms.kms_info["sm"].key_id
      recovery_window_in_days        = 7
      force_overwrite_replica_secret = true
      replica                        = []
      create_secret_version          = false
    }
  }
  depends_on = [module.kms]
}

##############################################################
# Modulo SG
##############################################################
module "sg" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-sg-terraform.git?ref=feature/sg-module-mapobject"
  providers = {
    aws.project = aws.principal
  }

  client      = var.client
  project     = var.project
  environment = var.environment

  sg_config = {
    "elb" = {
      service     = "alb"
      application = "tutorias"
      description = "Security group for alb tutorias"
      vpc_id      = data.aws_vpc.vpc.id
      additional_tags = {
        application-tier = "backend"
      }

      ingress = [
        {
          from_port       = 80
          to_port         = 80
          protocol        = "tcp"
          cidr_blocks     = ["181.32.109.172/32"]
          security_groups = []
          prefix_list_ids = []
          self            = false
          description     = "Allow HTTP inbound Felipe"
        },
        {
          from_port       = 80
          to_port         = 80
          protocol        = "tcp"
          cidr_blocks     = ["191.110.124.113/32"]
          security_groups = []
          prefix_list_ids = []
          self            = false
          description     = "Allow HTTP inbound Jammer"
        },        
        {
          from_port       = 443
          to_port         = 443
          protocol        = "tcp"
          cidr_blocks     = ["181.32.109.172/32"]
          security_groups = []
          prefix_list_ids = []
          self            = false
          description     = "Allow HTTP inbound Felipe"
        },
        {
          from_port       = 443
          to_port         = 443
          protocol        = "tcp"
          cidr_blocks     = ["191.110.124.113/32"]
          security_groups = []
          prefix_list_ids = []
          self            = false
          description     = "Allow HTTP inbound Jammer"
        },
      ]

      egress = [
        {
          from_port       = 0
          to_port         = 0
          protocol        = "-1"
          cidr_blocks     = ["0.0.0.0/0"]
          prefix_list_ids = []
          security_groups = []
          self            = false
          description     = "Allow all outbound traffic"
        }
      ]
    },
    "ecs" = {
      service     = "ecs"
      application = "tutorias"
      description = "Security group for ecs tutorias"
      vpc_id      = data.aws_vpc.vpc.id
      additional_tags = {
        application-tier = "tutorias"
      }

      ingress = [
        {
          from_port       = 8080
          to_port         = 8080
          protocol        = "tcp"
          cidr_blocks     = []
          security_groups = ["elb"]
          prefix_list_ids = []
          self            = false
          description     = "Allow traffic on port 7008 from ALB delivery"
        },
        {
          from_port       = 8080
          to_port         = 8080
          protocol        = "tcp"
          cidr_blocks     = []
          security_groups = []
          prefix_list_ids = []
          self            = true
          description     = "Allow traffic on port 7008 from the same security group delivery"
        }
      ]

      egress = [
        {
          from_port       = 0
          to_port         = 0
          protocol        = "-1"
          cidr_blocks     = ["0.0.0.0/0"]
          prefix_list_ids = []
          security_groups = []
          self            = false
          description     = "Allow all outbound traffic"
        }
      ]
    }
    "rds" = {
      service     = "rds"
      application = "mysql"
      description = "Security group for Aurora Mysql"
      vpc_id      = data.aws_vpc.vpc.id
      additional_tags = {
        application-tier = "database"
      }

      ingress = [
        {
          from_port       = 3306
          to_port         = 3306
          protocol        = "tcp"
          cidr_blocks     = []
          security_groups = ["ecs"]
          prefix_list_ids = []
          self            = false
          description     = "Allow Mysql traffic from ECS security group"
        }
      ]

      egress = [
        {
          from_port       = 0
          to_port         = 0
          protocol        = "-1"
          cidr_blocks     = ["0.0.0.0/0"]
          prefix_list_ids = []
          security_groups = []
          self            = false
          description     = "Allow all outbound traffic"
        }
      ]
    }
  }
}

##############################################################
# Modulo IAM
##############################################################
module "iam" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-iam-terraform.git?ref=feature/iam-module-init"

  providers = {
    aws.project = aws.principal
  }

  client      = var.client
  project     = var.project
  environment = var.environment

  iam_config = var.iam_config
}
