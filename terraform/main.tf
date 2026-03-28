terraform {
  required_version = ">= 1.5.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# -------------------------------------------------------------------------
# VPC Placeholder
# -------------------------------------------------------------------------
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.1.1"

  name = "enterprise-shop-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["${var.aws_region}a", "${var.aws_region}b", "${var.aws_region}c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = true

  tags = {
    Environment = var.environment
    Project     = "enterprise-shop"
  }
}

# -------------------------------------------------------------------------
# Managed DB Placeholder (RDS PostgreSQL)
# -------------------------------------------------------------------------
resource "aws_db_instance" "postgres" {
  identifier           = "enterprise-shop-db-${var.environment}"
  engine               = "postgres"
  engine_version       = "15.3"
  instance_class       = "db.t3.micro"
  allocated_storage    = 20
  username             = var.db_username
  password             = var.db_password # NOTE: Fetch from Secrets Manager in production!
  parameter_group_name = "default.postgres15"
  skip_final_snapshot  = true
  vpc_security_group_ids = []
  db_subnet_group_name   = "" # Add db subnet group from VPC module
}

# -------------------------------------------------------------------------
# Container Registry Placeholder (ECR)
# -------------------------------------------------------------------------
resource "aws_ecr_repository" "services" {
  for_each = toset([
    "config-service",
    "discovery-service",
    "api-gateway",
    "auth-service",
    "product-service",
    "order-service",
    "inventory-service",
    "notification-service"
  ])

  name                 = "enterprise-shop/${each.key}"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }
}

# -------------------------------------------------------------------------
# Notes on Secrets Management
# -------------------------------------------------------------------------
# In production, DO NOT hardcode secrets in Terraform variables.
# Use AWS Secrets Manager or Hashicorp Vault:
# 
# data "aws_secretsmanager_secret_version" "db_password" {
#   secret_id = aws_secretsmanager_secret.db_password.id
# }
# 
# Use the retrieved secret in resources: password = data.aws_secretsmanager_secret_version.db_password.secret_string
