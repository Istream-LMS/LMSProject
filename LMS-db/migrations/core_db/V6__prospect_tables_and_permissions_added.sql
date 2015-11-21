CREATE TABLE IF NOT EXISTS `m_prospect` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `mobile_no` varchar(20) DEFAULT NULL,
  `email_id` varchar(50) DEFAULT NULL,
  `source_of_publicity` varchar(50) DEFAULT NULL,
  `preferred_loan_product` varchar(100) DEFAULT NULL,
  `preferred_calling_time` datetime DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `tin` varchar(60) DEFAULT NULL,
  `status` varchar(100) DEFAULT 'New',
  `location` varchar(100) DEFAULT NULL,
  `is_deleted` char(1) DEFAULT 'N',
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_id` (`email_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `m_prospect_loan_calculator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `term` int(20) NOT NULL,
  `vehicle_cost_price` decimal(19,6) NOT NULL,
  `interest_rate` decimal(19,6) NOT NULL,
  `deposit_amount` decimal(19,6) DEFAULT NULL,
  `cof` decimal(19,6) DEFAULT NULL,
  `maintenance` decimal(19,6) DEFAULT NULL,
  `replacement_tyres` decimal(19,6) DEFAULT NULL,
  `insurance` decimal(19,6) DEFAULT NULL,
  `deprecisation` decimal(19,6) DEFAULT NULL,
  `residual_cost_VEP` decimal(19,6) DEFAULT NULL,
  `residual_amount_VEP` decimal(19,6) DEFAULT NULL,
  `residual_amount_VIP` decimal(19,6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `m_prospect_loan_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prospect_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `client_id` bigint(20) DEFAULT NULL,
  `term` int(20) NOT NULL,
  `vehicle_cost_price` decimal(19,6) NOT NULL,
  `interest_rate` decimal(19,6) NOT NULL,
  `deposit_amount` decimal(19,6) DEFAULT NULL,
  `cof` decimal(19,6) DEFAULT NULL,
  `maintenance` decimal(19,6) DEFAULT NULL,
  `replacement_tyres` decimal(19,6) DEFAULT NULL,
  `insurance` decimal(19,6) DEFAULT NULL,
  `deprecisation` decimal(19,6) DEFAULT NULL,
  `residual_cost_VEP` decimal(19,6) DEFAULT NULL,
  `residual_amount_VEP` decimal(19,6) DEFAULT NULL,
  `residual_amount_VIP` decimal(19,6) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_prospect_id` (`prospect_id`),
  CONSTRAINT `fk_prospect_id` FOREIGN KEY (`prospect_id`) REFERENCES `m_prospect` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('organization', 'CREATE_PROSPECT', 'PROSPECT', 'CREATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('organization', 'READ_PROSPECT', 'PROSPECT', 'READ', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('organization', 'CONVERTTOCLIENT_PROSPECT', 'PROSPECT', 'CONVERTTOCLIENT', 0);








