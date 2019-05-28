CREATE TABLE `tbl_bank_account` (
  `account_id` MEDIUMINT NOT NULL AUTO_INCREMENT,
  `bank_code` VARCHAR(50) NOT NULL,
  `account_no` VARCHAR(20) NOT NULL,
  `customer_id` VARCHAR(20)  NULL,
  `account_type` VARCHAR(20) NOT NULL,
  `credit_limit` DECIMAL(18,2) NULL,
  `outstanding` DECIMAL(18,2) NULL,
  `ledger_balance` DECIMAL(18,2) NOT NULL,
  `available` DECIMAL(18,2) NOT NULL,
  `account_status` VARCHAR(20) NOT NULL,
  `customer_code` VARCHAR(20)  NULL,
  `customer_credit_limit` tinyint NOT NULL,
  `create_time` DATETIME NOT NULL,
  `create_by` MEDIUMINT NOT NULL,
  `update_time` DATETIME NOT NULL,
  `update_by` MEDIUMINT NOT NULL,
  `version` INT NOT NULL,
  PRIMARY KEY (`account_id`));
