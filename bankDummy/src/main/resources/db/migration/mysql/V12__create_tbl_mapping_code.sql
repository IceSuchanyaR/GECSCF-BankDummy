CREATE TABLE `tbl_mapping_code` (
  `mapping_id` MEDIUMINT NOT NULL AUTO_INCREMENT,
  `bank_code` VARCHAR(20) NOT NULL,
  `service_type` VARCHAR(50) NOT NULL,
  `delay` INT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `create_by` MEDIUMINT NOT NULL,
  `update_time` DATETIME NOT NULL,
  `update_by` MEDIUMINT NOT NULL,
  `version` INT NOT NULL,
  PRIMARY KEY (`mapping_id`));