alter table tbl_user
CHANGE COLUMN email email VARCHAR(255) DEFAULT '' UNIQUE,
CHANGE COLUMN password password VARCHAR(255) NOT NULL,
ADD update_by_username VARCHAR(255) DEFAULT '' AFTER update_by;

alter table oauth_access_token
ADD last_update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

UPDATE `tbl_user` SET `update_by_username`='developer' WHERE `id`='1';
UPDATE `tbl_user` SET `update_by_username`='common_user' WHERE `id`='2';
UPDATE `tbl_user` SET `update_by_username`='common_user' WHERE `id`='3';
