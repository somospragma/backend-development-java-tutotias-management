##############################################################
# Data sources para VPC y subredes
##############################################################

# Obtener información de la VPC por filtro de nombre
data "aws_vpc" "selected" {
  provider = aws.principal
  filter {
    name   = "tag:Name"
    values = ["pragma-tutorias-dev-vpc"]
  }
}

data "aws_subnets" "database" {
  provider = aws.principal
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.selected.id]
  }
  filter {
    name   = "tag:Name"
    values = ["pragma-tutorias-dev-subnet-database-*"]
  }
}

data "aws_security_group" "rds" {
  provider = aws.principal
  name     = "pragma-tutorias-dev-sg-rds-mysql"
}


data "aws_kms_alias" "kms-database" {
  provider = aws.principal
  name = "alias/pragma-tutorias-dev-kms-rds"
}