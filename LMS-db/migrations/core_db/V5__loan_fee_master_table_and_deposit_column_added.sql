Drop procedure IF EXISTS addDepositColumn;
DELIMITER //
create procedure addDepositColumn() 
Begin
IF NOT EXISTS (
     SELECT * FROM information_schema.COLUMNS
     WHERE COLUMN_NAME = 'deposit_amount'
     and TABLE_NAME = 'm_loan'
     and TABLE_SCHEMA = DATABASE())THEN
ALTER TABLE `m_loan` ADD COLUMN `deposit_amount` DECIMAL(19,6) NULL DEFAULT NULL  AFTER `residual_amount` ;
END IF;
END //
DELIMITER ;
call addDepositColumn();
Drop procedure IF EXISTS addDepositColumn;

CREATE TABLE IF NOT EXISTS `m_loan_fee_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loan_id` int(11) NOT NULL,
  `fee_master_id` int(11) NOT NULL,
  `fee_code` varchar(10) DEFAULT NULL,
  `fee_description` varchar(100) DEFAULT NULL,
  `transaction_type` varchar(100) DEFAULT NULL,
  `deposit_time_enum` smallint(5) NOT NULL,
  `deposit_calculation_enum` smallint(5) NOT NULL,
  `deposit_on_enum` smallint(5) NOT NULL,
  `calculation_percentage` decimal(19,6) DEFAULT NULL,
  `calculation_on_amount` decimal(19,6) DEFAULT NULL,
  `deposit_amount_or_percentage` decimal(19,6) DEFAULT NULL,
  `amount` double(22,6) DEFAULT NULL,
  `is_deleted` varchar(1) DEFAULT 'N',
  `is_refundable` varchar(1) DEFAULT NULL,
  `min_cap` decimal(19,6) DEFAULT NULL,
  `max_cap` decimal(19,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Transaction Type in codes
INSERT IGNORE INTO m_code VALUES(NULL,'Transaction Type',1);
SET @ID=(select id from m_code where code_name='Transaction Type');
INSERT IGNORE INTO m_code_value VALUES (NULL,@ID,'Deposit',0);


