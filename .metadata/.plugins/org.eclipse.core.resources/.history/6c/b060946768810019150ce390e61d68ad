ALTER TABLE tbl_privilege 
CHANGE COLUMN name name VARCHAR(255) DEFAULT '';

ALTER TABLE tbl_role 
CHANGE COLUMN name name VARCHAR(255) DEFAULT '';

ALTER TABLE tbl_user 
CHANGE COLUMN full_name full_name VARCHAR(255) DEFAULT '',
CHANGE COLUMN email email VARCHAR(255) DEFAULT '';

UPDATE `tbl_user` SET `email`='' WHERE `id`='1';
UPDATE `tbl_user` SET `email`='' WHERE `id`='2';
UPDATE `tbl_user` SET `email`='' WHERE `id`='3';

UPDATE `gecscf_sponsor_dummy_dev`.`tbl_user` SET `email`='gec.master@gec.co.th' WHERE `id`='1';
UPDATE `gecscf_sponsor_dummy_dev`.`tbl_user` SET `email`='bank.user@gmail.com' WHERE `id`='2';
UPDATE `gecscf_sponsor_dummy_dev`.`tbl_user` SET `email`='sponsor.user@hotmail.com' WHERE `id`='3';
