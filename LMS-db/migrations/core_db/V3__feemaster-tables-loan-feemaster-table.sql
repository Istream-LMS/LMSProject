
CREATE TABLE IF NOT EXISTS `m_fee_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fee_code` varchar(10) DEFAULT NULL,
  `fee_description` varchar(100) DEFAULT NULL,
  `transaction_type` varchar(100) DEFAULT NULL,
  `charge_time_enum` smallint(5) NOT NULL,
  `charge_calculation_enum` smallint(5) NOT NULL,
  `default_fee_amount` double(22,6) DEFAULT NULL,
  `is_deleted` varchar(1) DEFAULT 'N',
  `is_refundable` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fee_code` (`fee_code`),
  UNIQUE KEY `fee_transaction_type` (`transaction_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `m_product_loan_feemaster` (
  `product_loan_id` bigint(20) NOT NULL,
  `feemaster_id` int(25) NOT NULL,
  PRIMARY KEY (`product_loan_id`,`feemaster_id`),
  KEY `feemaster_id` (`feemaster_id`),
  CONSTRAINT `m_product_loan_feemaster_ibfk_2` FOREIGN KEY (`product_loan_id`) REFERENCES `m_product_loan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT ignore INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) 
VALUES ('organization', 'CREATE_FEEMASTER', 'FEEMASTER', 'CREATE', 0);

INSERT ignore INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) 
VALUES ('organization', 'UPDATE_FEEMASTER', 'FEEMASTER', 'UPDATE', 0);

INSERT ignore INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) 
VALUES ('organization', 'DELETE_FEEMASTER', 'FEEMASTER', 'DELETE', 0);

INSERT ignore INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) 
VALUES ('organization', 'READ_FEEMASTER', 'FEEMASTER', 'READ', 0);

