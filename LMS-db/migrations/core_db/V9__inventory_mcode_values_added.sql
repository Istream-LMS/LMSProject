
-- Item Quality in codes
INSERT IGNORE INTO m_code VALUES(NULL,'Item Quality',1);
SET @ID=(select id from m_code where code_name='Item Quality');
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Good',0);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Refurbished',0);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Defective',1);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Scrap',2);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Faulty',3);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Stolen',4);

-- Manufacturer Codes in codes
INSERT IGNORE INTO m_code VALUES(NULL,'Manufacturer Codes',1);
SET @ID=(select id from m_code where code_name='Manufacturer Codes');
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Bandix',0);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Castrol',1);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'GM',2);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Prestone',3);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Purolator',4);
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'John Deer',5);

-- Charge Codes in codes
INSERT IGNORE INTO m_code VALUES(NULL,'Charge Codes',1);
SET @ID=(select id from m_code where code_name='Charge Codes');
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'One Time',0);
