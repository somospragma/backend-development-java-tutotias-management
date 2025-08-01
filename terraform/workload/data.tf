##############################################################
# Data sources para VPC y subredes
##############################################################

# Obtener información de la VPC por filtro de nombre
data "aws_vpc" "vpc" {
  provider = aws.principal
  filter {
    name   = "tag:Name"
    values = ["pragma-tutorias-dev-vpc"]
  }
}

data "aws_subnets" "service" {
  provider = aws.principal
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.vpc.id]
  }
  filter {
    name   = "tag:Name"
    values = ["pragma-tutorias-dev-subnet-service-subnet-*"]
  }
}

data "aws_subnets" "public" {
  provider = aws.principal
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.vpc.id]
  }
  filter {
    name   = "tag:Name"
    values = ["pragma-tutorias-dev-subnet-public-subnet-*"]
  }
}

data "aws_security_group" "ecs" {
  provider = aws.principal
  name     = "pragma-tutorias-dev-sg-ecs-tutorias"
}

data "aws_security_group" "elb" {
  provider = aws.principal
  name     = "pragma-tutorias-dev-sg-alb-tutorias"
}

data "aws_iam_role" "execution_role" {
  provider = aws.principal
  name     = "pragma-tutorias-dev-role-execution-core-tutorias"
}

data "aws_iam_role" "task_role" {
  provider = aws.principal
  name     = "pragma-tutorias-dev-role-task-core-tutorias"
}

data "aws_secretsmanager_secret" "mi_secreto" {
  provider = aws.principal
  name     = "rds!cluster-588a6261-75a3-47c3-bfff-865777dce2e0"
}

data "aws_rds_cluster" "tutorias" {
  provider           = aws.principal
  cluster_identifier = "pragma-tutorias-dev-cluster-tutorias-us-east-1-0-db"
}

