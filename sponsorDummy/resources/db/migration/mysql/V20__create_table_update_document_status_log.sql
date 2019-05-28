SET SQL_SAFE_UPDATES = 0;
CREATE TABLE tbl_update_document_status_log (
	id VARCHAR(64) NOT NULL PRIMARY KEY,
	request_message TEXT NULL,
	request_header TEXT NOT NULL,
    request_time DATETIME NOT NULL
);
SET SQL_SAFE_UPDATES = 1;