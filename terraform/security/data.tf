###############################################################
# Data Sources
###############################################################
data "aws_caller_identity" "current" {
  provider = aws.principal
}

data "aws_region" "current" {
  provider = aws.principal
}

###########################################
# Data VPC 
###########################################

data "aws_vpc" "vpc" {
  provider = aws.principal
  
  filter {
    name   = "tag:Name"
    values = ["pragma-tutorias-dev-vpc"]
  }
}

