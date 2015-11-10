INSERT ignore INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) 
VALUES ('organization', 'CREATE_LOANCALCULATOR', 'LOANCALCULATOR', 'CREATE', 0);

Drop procedure IF EXISTS addtaxCodeToUniqueKey;
DELIMITER //
create procedure addtaxCodeToUniqueKey() 
Begin
  IF  EXISTS (
     SELECT * FROM information_schema. KEY_COLUMN_USAGE
     WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME ='m_tax_mapping'
      and CONSTRAINT_NAME = 'tax_code_UNIQUE')THEN

alter table `m_tax_mapping` DROP KEY `tax_code_UNIQUE`, ADD UNIQUE `tax_code_UNIQUE`(`tax_code`);
END IF;
END //
DELIMITER ;
call addtaxCodeToUniqueKey();
Drop procedure IF EXISTS addtaxCodeToUniqueKey;
