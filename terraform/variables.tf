variable "aws_region" {
  description = "AWS region for infrastructure deployment"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Environment name (e.g. dev, staging, prod)"
  type        = string
  default     = "dev"
}

variable "db_username" {
  description = "Database administrator username"
  type        = string
  default     = "postgres_admin"
}

variable "db_password" {
  description = "Database administrator password. Should be passed via environment variables (TF_VAR_db_password) or fetched from a secrets manager in prod."
  type        = string
  sensitive   = true
}
