alter table tbl_user
ADD create_by_username VARCHAR(255) DEFAULT '' AFTER update_by_username;

UPDATE `tbl_user` SET `update_by_username`='developer', `create_by_username`='developer' WHERE `id`='1';
UPDATE `tbl_user` SET `create_by_username`='common_user' WHERE `id`='2';
UPDATE `tbl_user` SET `create_by_username`='common_user' WHERE `id`='3';
