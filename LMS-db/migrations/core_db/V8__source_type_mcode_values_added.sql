-- Source Type in codes
INSERT IGNORE INTO m_code VALUES(NULL,'Source Type',1);
SET @ID=(select id from m_code where code_name='Source Type');
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'News Paper',0);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Friend',1);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Tv Add',2);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Others',3);
