DELETE FROM `m_tax_mapping` WHERE `tax_code`='ACCT.TAX';
DELETE FROM `m_tax_mapping` WHERE `tax_code`='TAX_DEP';

INSERT IGNORE INTO m_tax_mapping VALUES(NULL, 'ACCT.TAX', 'Disbursement', 'Percentage', '2015-11-07 00:00:00', '0.2', '1');
INSERT IGNORE INTO m_tax_mapping VALUES(NULL, 'TAX_DEP', 'Disbursement', 'Percentage', '2015-11-07 00:00:00', '0.18', '1');

Drop procedure IF EXISTS droptaxCodeToUniqueKey;
DELIMITER //
create procedure droptaxCodeToUniqueKey() 
Begin
  IF  EXISTS (
     SELECT * FROM information_schema. KEY_COLUMN_USAGE
     WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME ='m_tax_mapping'
      and CONSTRAINT_NAME = 'tax_code_UNIQUE')THEN

alter table `m_tax_mapping` DROP KEY `tax_code_UNIQUE`;
END IF;
END //
DELIMITER ;
call droptaxCodeToUniqueKey();
Drop procedure IF EXISTS droptaxCodeToUniqueKey;

Drop procedure IF EXISTS addcolumns;
DELIMITER //
create procedure addcolumns() 
Begin
IF NOT EXISTS (
     SELECT * FROM information_schema.COLUMNS
     WHERE COLUMN_NAME in ('end_date','is_new')
     and TABLE_NAME = 'm_tax_mapping'
     and TABLE_SCHEMA = DATABASE())THEN
ALTER TABLE `m_tax_mapping` ADD COLUMN `end_date` datetime NULL  DEFAULT NULL AFTER `start_date`,
 ADD COLUMN `is_new`  tinyint(1) DEFAULT '1' AFTER `tax_inclusive`;

END IF;
END //
DELIMITER ;
call addcolumns();
Drop procedure IF EXISTS addcolumns;
