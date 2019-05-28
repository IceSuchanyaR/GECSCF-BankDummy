CREATE TABLE `tbl_mapping_code_detail` (
  `mapping_detail_id` MEDIUMINT NOT NULL AUTO_INCREMENT,
  `mapping_id` MEDIUMINT NOT NULL,
  `bankcase_code` VARCHAR(100) NOT NULL,
  `transaction_status` VARCHAR(20) NOT NULL,
  `failure_reason_code` VARCHAR(255)  NULL,
  `failure_reason` VARCHAR(255)  NULL,
  `update_time` DATETIME NOT NULL,
  PRIMARY KEY (`mapping_detail_id`),
  FOREIGN KEY (mapping_id) REFERENCES tbl_mapping_code(mapping_id));