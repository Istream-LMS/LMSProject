Drop procedure IF EXISTS modifycolumns;
DELIMITER //
create procedure modifycolumns() 
Begin
IF EXISTS (
     SELECT * FROM information_schema.COLUMNS
     WHERE COLUMN_NAME IN( 'charge_time_enum','charge_calculation_enum','default_fee_amount')
     and TABLE_NAME = 'm_fee_master'
     and TABLE_SCHEMA = DATABASE())THEN
ALTER TABLE `m_fee_master` CHANGE COLUMN `charge_time_enum` `deposit_time_enum` SMALLINT(5) NOT NULL  , 
  CHANGE COLUMN `charge_calculation_enum` `deposit_calculation_enum` SMALLINT(5) NOT NULL  ,
  CHANGE COLUMN `default_fee_amount` `amount` DOUBLE(22,6) NULL DEFAULT NULL ;

END IF;
END //
DELIMITER ;
call modifycolumns();
Drop procedure IF EXISTS modifycolumns;

Drop procedure IF EXISTS addcolumns;
DELIMITER //
create procedure addcolumns() 
Begin
IF NOT EXISTS (
     SELECT * FROM information_schema.COLUMNS
     WHERE COLUMN_NAME = 'deposit_on_enum'
     and TABLE_NAME = 'm_fee_master'
     and TABLE_SCHEMA = DATABASE())THEN
ALTER TABLE `m_fee_master` ADD COLUMN `deposit_on_enum` SMALLINT(5) NOT NULL  AFTER `deposit_calculation_enum` ,
 DROP INDEX `fee_transaction_type` ;

END IF;
END //
DELIMITER ;
call addcolumns();
Drop procedure IF EXISTS addcolumns;
