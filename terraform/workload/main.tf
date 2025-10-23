######################################################################
# Módulo AWS ALB
######################################################################
module "elb" {
  source      = "git::https://github.com/somospragma/cloudops-ref-repo-aws-elb-terraform.git?ref=feature/elb-module-maps"
  client      = var.client
  project     = var.project
  environment = var.environment

  providers = {
    aws.project = aws.principal
  }

  lb_config = {
    "tutorias" = {
      internal                   = false
      load_balancer_type         = "application"
      drop_invalid_header_fields = false
      idle_timeout               = 60
      enable_deletion_protection = false
      subnets                    = data.aws_subnets.public.ids
      security_groups            = [data.aws_security_group.elb.id]
      application_id             = "tutorias"

      listeners = [
        # Listener HTTPS (puerto 443) - TRÁFICO REAL
        {
          protocol                = "HTTPS"
          port                    = "443"
          certificate             = "arn:aws:acm:us-east-1:026090553765:certificate/f0073135-89b0-4e87-8b6b-4a8bf3dd12b9"
          default_target_group_id = "core"
          rules                   = []  # Sin reglas adicionales, usa default action
        }
      ]
      target_groups = [
        {
          target_application_id = "core"
          port                  = "8080"
          protocol              = "HTTP"
          vpc_id                = data.aws_vpc.vpc.id
          target_type           = "ip"
          healthy_threshold     = "5"
          interval              = "60"
          path                  = "/actuator/health"
          unhealthy_threshold   = "10"
          matcher               = "200"
        }
      ]
    }
  }

}

######################################################################
# Módulo ECS Cluster
######################################################################
module "ecs_cluster" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-ecs-cluster-terraform.git?ref=v1.0.0"

  providers = {
    aws.project = aws.principal
  }

  client      = var.client
  project     = var.project
  environment = var.environment

  cluster_config = var.cluster_config
}

######################################################################
# Módulo ECS Cloudmap
######################################################################
module "cloudmap" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-cloudmap-terraform.git?ref=feature/init-cloudmap"

  providers = {
    aws.project = aws.principal
  }

  client      = var.client
  project     = var.project
  environment = var.environment

  namespaces = var.namespaces
}

######################################################################
# Módulo ECR
######################################################################
module "ecr" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-ecr-terraform.git?ref=feature/ecr-module-init"

  providers = {
    aws.project = aws.principal
  }

  # Common configuration
  client      = var.client
  project     = var.project
  environment = var.environment
  application = var.application

  # ECR configuration
  ecr_config = var.ecr_config
}

######################################################################
# Módulo ECS Services
#####################################################################
module "ecs_services" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-ecs-service-terraform.git?ref=v1.0.0"
  providers = {
    aws.project = aws.principal
  }

  client      = var.client
  project     = var.project
  environment = var.environment

  ecs_services = {
    "tutorias-core" = {
      cluster_name              = module.ecs_cluster.cluster_info["tutorias"].cluster_name
      desired_count             = 1
      task_cpu                  = 512
      task_memory               = 1024
      subnets                   = data.aws_subnets.service.ids
      security_groups           = [data.aws_security_group.ecs.id]
      execution_role_arn        = data.aws_iam_role.execution_role.arn
      task_role_arn             = data.aws_iam_role.task_role.arn
      health_check_grace_period = 60
      load_balancer = {
        target_group_arn = module.elb.target_group_info["core"].target_arn
        container_name   = "tutorias-core"
        container_port   = 8080
      }

      # Habilitar Service Connect (opcional)
      service_connect_config = {
        enabled   = true
        namespace = module.cloudmap.namespaces["service-connect"].arn
        service = {
          port_name      = "tutorias-core"
          discovery_name = "pragma-tutorias-dev-backend-core"
          client_alias = [
            {
              port     = 8080
              dns_name = "tutorias-core"
            }
          ]
        }
        log_configuration = {
          log_driver = "awslogs"
          options = {
            "awslogs-group"         = "/ecs/service-connect/bs-vulcano"
            "awslogs-region"        = "us-east-1"
            "awslogs-stream-prefix" = "service-connect"
            "awslogs-create-group"  = "true"
          }
        }
      }

      # Configuración de contenedores
      containers = {
        # Contenedor principal 
        "tutorias-core" = {
          image                    = "${module.ecr.ecr_info["core"].repository_url}:latest"
          cpu                      = 256
          memory                   = 768
          essential                = true
          readonly_root_filesystem = false

          port_mappings = [
            {
              name           = "tutorias-core" # Nombre para Service Connect
              container_port = 8080
              host_port      = 8080
              protocol       = "tcp"
            }
          ]

          environment = [
            {
              name  = "SPRING_PROFILES_ACTIVE",
              value = "dev"
            },
            {
              name  = "DATABASE_NAME",
              value = "tutorias"
            },
            {
              name  = "DATABASE_PORT",
              value = "3306"
            },
            {
              name  = "DATABASE_HOST"
              value = "${data.aws_rds_cluster.tutorias.endpoint}"
            }
          ]

          secrets = [
            {
              name       = "DATABASE_USER"
              value_from = "${data.aws_secretsmanager_secret.mi_secreto.arn}:username::"
            },
            {
              name       = "DATABASE_PASSWORD"
              value_from = "${data.aws_secretsmanager_secret.mi_secreto.arn}:password::"
            }
          ]

          log_configuration = {
            log_driver = "awslogs"
            options = {
              "awslogs-group"         = "/ecs/tutorias-core"
              "awslogs-region"        = "us-east-1"
              "awslogs-stream-prefix" = "ecs"
              "awslogs-create-group"  = "true"
              "mode"                  = "non-blocking"
              "max-buffer-size"       = "25m"
            }
          }
        }
      }
      # Configuración de logs
      log_retention_days = 7
    },
  }
  depends_on = [module.ecs_cluster, module.cloudmap, module.elb]
}