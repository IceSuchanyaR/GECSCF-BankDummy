SET SQL_SAFE_UPDATES = 0;

DELETE FROM tbl_users_roles;
DELETE FROM tbl_user;

INSERT INTO tbl_user
  (id, username, password,full_name , account_expired, account_locked, credentials_expired, enabled)
VALUES
  (1, 'common_user', /*password*/'$2a$10$ULXJLtMlAH8cVcbhVimg2eHC3ea0Z1AffC2WHf7jAUrjvyU.9PSPK', 'Common Master', FALSE, FALSE, FALSE, TRUE);

INSERT INTO tbl_user
  (id, username, password,full_name ,create_by, update_by, account_expired, account_locked, credentials_expired, enabled)
VALUES
  (2, 'bank_user', /*password*/'$2a$10$ULXJLtMlAH8cVcbhVimg2eHC3ea0Z1AffC2WHf7jAUrjvyU.9PSPK', 'Bank User', 1, 1, FALSE, FALSE, FALSE, TRUE);

INSERT INTO tbl_user
  (id, username, password,full_name ,create_by, update_by, account_expired, account_locked, credentials_expired, enabled)
VALUES
  (3, 'sponsor_user', /*password*/'$2a$10$ULXJLtMlAH8cVcbhVimg2eHC3ea0Z1AffC2WHf7jAUrjvyU.9PSPK', 'Sponsor User', 1, 1, FALSE, FALSE, FALSE, TRUE);

INSERT INTO tbl_users_roles
  (user_id, role_id)
VALUES
  (1, 1);
INSERT INTO tbl_users_roles
  (user_id, role_id)
VALUES
  (2, 2);
INSERT INTO tbl_users_roles
  (user_id, role_id)
VALUES
  (3, 3);
  
SET SQL_SAFE_UPDATES = 1;