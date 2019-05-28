INSERT INTO tbl_role(id, name) VALUES (1, 'COMMON_USER');
INSERT INTO tbl_role(id, name) VALUES (2, 'BANK_USER');
INSERT INTO tbl_role(id, name) VALUES (3, 'SPONSOR_USER');

INSERT INTO tbl_privilege(id, name) VALUES (1, 'MANAGE_ALL');
INSERT INTO tbl_privilege(id, name) VALUES (2, 'MANAGE_BANK');
INSERT INTO tbl_privilege(id, name) VALUES (3, 'MANAGE_SPONSOR');

INSERT INTO tbl_roles_privileges(role_id, privilege_id) VALUES (1, 1);
INSERT INTO tbl_roles_privileges(role_id, privilege_id) VALUES (2, 2);
INSERT INTO tbl_roles_privileges(role_id, privilege_id) VALUES (3, 3);
