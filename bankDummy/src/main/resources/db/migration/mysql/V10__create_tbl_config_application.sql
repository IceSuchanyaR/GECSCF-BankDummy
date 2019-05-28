CREATE TABLE `tbl_config_application` (
  `config_id` MEDIUMINT NOT NULL AUTO_INCREMENT,
  `config_group` VARCHAR(50) NOT NULL,
  `config_value` VARCHAR(100) NOT NULL,
  `config_display` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255)  NULL,
  `create_time` DATETIME NOT NULL,
  `create_by` MEDIUMINT NOT NULL,
  `update_time` DATETIME NOT NULL,
  `update_by` MEDIUMINT NOT NULL,
  `version` INT NOT NULL,
  PRIMARY KEY (`config_id`));