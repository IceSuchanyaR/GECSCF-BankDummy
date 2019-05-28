CREATE TABLE tbl_privilege (
   id BIGINT auto_increment,
   name VARCHAR(255),
   PRIMARY KEY (id)
);

ALTER TABLE tbl_privilege ADD CONSTRAINT privilege_name UNIQUE(name);

CREATE TABLE tbl_role (
   id BIGINT auto_increment,
   name VARCHAR(255),
   PRIMARY KEY (id)
);

ALTER TABLE tbl_role ADD CONSTRAINT role_name UNIQUE(name);

CREATE TABLE tbl_roles_privileges (
   role_id BIGINT NOT NULL,
   privilege_id BIGINT NOT NULL,
   PRIMARY KEY (role_id, privilege_id)
);

ALTER TABLE tbl_roles_privileges ADD CONSTRAINT roles_privileges_privilege
   FOREIGN KEY (privilege_id) REFERENCES tbl_privilege(id);

ALTER TABLE tbl_roles_privileges ADD CONSTRAINT roles_privileges_role
   FOREIGN KEY (role_id) REFERENCES tbl_role(id);

CREATE TABLE tbl_user (
  id BIGINT auto_increment,
  password VARCHAR(255),
  username VARCHAR(255),
  full_name VARCHAR(255),
  email VARCHAR(255),
  account_expired BOOLEAN,
  account_locked BOOLEAN,
  credentials_expired BOOLEAN,
  enabled BOOLEAN,
  PRIMARY KEY (id)
);

ALTER TABLE tbl_user ADD CONSTRAINT user_user_name UNIQUE(username);

CREATE TABLE tbl_users_roles (
   user_id BIGINT NOT NULL,
   role_id BIGINT NOT NULL,
   PRIMARY KEY (user_id, role_id)
);

ALTER TABLE tbl_users_roles ADD CONSTRAINT users_roles_role
   FOREIGN KEY (role_id) REFERENCES tbl_role(id);

ALTER TABLE tbl_users_roles ADD CONSTRAINT users_roles_user_
   FOREIGN KEY (user_id) REFERENCES tbl_user(id);