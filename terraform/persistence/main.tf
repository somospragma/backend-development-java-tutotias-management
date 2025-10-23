##############################################################
# Módulo RDS
##############################################################

module "rds" {
  source = "git::https://github.com/somospragma/cloudops-ref-repo-aws-rds-terraform.git?ref=feature/rds-aurora-serverless"
  providers = {
    aws.principal = aws.principal
    aws.secondary = aws.principal
  }

  client          = var.client
  project         = var.project
  environment     = var.environment
  service         = "db"
  master_password = ""

  rds_config = [
    {
      create_global_cluster = false
      cluster_application   = "tutorias"
      engine                = "aurora-mysql"
      engine_version        = "8.0.mysql_aurora.3.10.0"
      database_name         = "tutorias"
      deletion_protection   = false
      storage_encrypted     = true
      serverless_deploy     = true
      cluster_config = [
        {
          principal                       = true
          region                          = var.region
          engine_mode                     = "provisioned"
          manage_master_user_password     = true
          master_username                 = "tutorias"
          vpc_security_group_ids          = [data.aws_security_group.rds.id]
          subnet_ids                      = data.aws_subnets.database.ids
          backup_retention_period         = 7
          skip_final_snapshot             = true
          preferred_backup_window         = "03:00-04:00"
          kms_key_id                      = data.aws_kms_alias.kms-database.target_key_arn
          performance_insights_kms_key_id = data.aws_kms_alias.kms-database.target_key_arn
          port                            = "3306"
          service                         = "tutorias"
          enabled_cloudwatch_logs_exports = ["error", "general", "slowquery"]
          copy_tags_to_snapshot           = true
          enable_http_endpoint            = true
          cluster_parameter = {
            family      = "aurora-mysql8.0"
            description = "Cluster parameter group for tutorias"
            parameters  = []
          }
          cluster_scaling_configuration = {
            max_capacity             = "2"
            min_capacity             = "0"
            seconds_until_auto_pause = "3600"
          }
          instance_parameter = {
            family     = "aurora-mysql8.0"
            parameters = []
          }
          cluster_instances = [
            {
              record_id                             = "tutorias-instance-1"
              instance_class                        = "db.serverless"
              publicly_accessible                   = false
              auto_minor_version_upgrade            = true
              performance_insights_enabled          = true
              performance_insights_retention_period = 7
              monitoring_interval                   = 0
              monitoring_role_arn                   = ""
            }
          ]
        }
      ]
    }
  ]
}
