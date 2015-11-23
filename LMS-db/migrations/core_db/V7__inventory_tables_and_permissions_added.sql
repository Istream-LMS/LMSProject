CREATE TABLE IF NOT EXISTS `b_supplier` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `supplier_code` varchar(10) NOT NULL,
  `supplier_description` varchar(100) DEFAULT NULL,
  `supplier_address` varchar(100) DEFAULT NULL,
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `supplier_code` (`supplier_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_charge_codes` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `charge_code` varchar(10) NOT NULL,
  `charge_description` varchar(100) DEFAULT NULL,
  `charge_type` varchar(15) DEFAULT NULL,
  `charge_duration` int(20) NOT NULL,
  `duration_type` varchar(20) NOT NULL,
  `tax_inclusive` tinyint(1) NOT NULL DEFAULT '0',
  `billfrequency_code` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `chargecode` (`charge_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_item_master` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `item_code` varchar(10) NOT NULL,
  `item_description` varchar(50) NOT NULL,
  `item_class` varchar(20) NOT NULL,
  `units` varchar(20) NOT NULL,
  `charge_code` varchar(10) NOT NULL,
  `unit_price` decimal(22,6) NOT NULL,
  `warranty` int(2) DEFAULT NULL,
  `warranty_expiry_date` date DEFAULT NULL,
  `manufacturer` varchar(50) DEFAULT NULL,
  `is_deleted` char(1) DEFAULT 'N',
  `reorder_level` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_code` (`item_code`),
  KEY `fk_im_cc` (`charge_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_item_price` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` int(20) NOT NULL,
  `region_id` varchar(30) NOT NULL,
  `price` decimal(22,6) NOT NULL,
  `is_deleted` char(1) DEFAULT 'N',
  PRIMARY KEY (`id`),
  UNIQUE KEY `itemid_with_region_uniquekey` (`item_id`,`region_id`),
  KEY `fk_item_price_id` (`item_id`),
  CONSTRAINT `fk_item_price_id` FOREIGN KEY (`item_id`) REFERENCES `b_item_master` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_itemsale` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) NOT NULL,
  `purchase_from` int(10) NOT NULL,
  `purchase_date` datetime NOT NULL,
  `order_quantity` bigint(20) NOT NULL,
  `received_quantity` bigint(20) NOT NULL DEFAULT '0',
  `status` varchar(20) DEFAULT NULL,
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  `charge_code` varchar(20) DEFAULT NULL,
  `purchase_by` bigint(10) NOT NULL,
  `agent_id` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `charge_code_key` (`charge_code`),
  CONSTRAINT `charge_code_key` FOREIGN KEY (`charge_code`) REFERENCES `b_charge_codes` (`charge_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `m_invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sale_id` bigint(20) NOT NULL,
  `invoice_date` datetime NOT NULL,
  `charge_amount` double(24,4) NOT NULL,
  `tax_percantage` double NOT NULL,
  `tax_amount` double(24,4) NOT NULL,
  `invoice_amount` double NOT NULL,
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_mrn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `requested_date` datetime NOT NULL,
  `item_master_id` int(20) NOT NULL,
  `from_office` bigint(20) NOT NULL,
  `to_office` bigint(20) NOT NULL,
  `orderd_quantity` bigint(20) NOT NULL,
  `received_quantity` bigint(20) NOT NULL DEFAULT '0',
  `status` varchar(20) NOT NULL DEFAULT 'New',
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_item_master_id1` (`item_master_id`),
  CONSTRAINT `fk_item_master_id1` FOREIGN KEY (`item_master_id`) REFERENCES `b_item_master` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `b_item_audit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `itemmaster_id` int(10) NOT NULL,
  `region_id` varchar(20) DEFAULT NULL,
  `item_code` varchar(10) NOT NULL,
  `unit_price` decimal(22,6) NOT NULL,
  `changed_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_grn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_date` datetime DEFAULT NULL,
  `supplier_id` int(20) DEFAULT NULL,
  `item_master_id` int(20) NOT NULL,
  `orderd_quantity` bigint(20) NOT NULL,
  `received_quantity` bigint(20) NOT NULL,
  `stock_quantity` bigint(20) NOT NULL,
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  `office_id` bigint(10) NOT NULL,
  `po_no` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `purchase_no_code_key` (`po_no`),
  KEY `fk_grn_usr` (`createdby_id`),
  KEY `fk_grn` (`supplier_id`),
  KEY `fk_grn_imid` (`item_master_id`),
  CONSTRAINT `fk_grn` FOREIGN KEY (`supplier_id`) REFERENCES `b_supplier` (`id`),
  CONSTRAINT `fk_grn_imid` FOREIGN KEY (`item_master_id`) REFERENCES `b_item_master` (`id`),
  CONSTRAINT `fk_grn_sid` FOREIGN KEY (`supplier_id`) REFERENCES `b_supplier` (`id`),
  CONSTRAINT `fk_grn_usr` FOREIGN KEY (`createdby_id`) REFERENCES `m_appuser` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_item_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_master_id` int(20) NOT NULL,
  `serial_no` varchar(100) DEFAULT NULL,
  `grn_id` bigint(20) NOT NULL,
  `provisioning_serialno` varchar(100) DEFAULT NULL,
  `quality` varchar(20) DEFAULT NULL,
  `received_quantity` bigint(20) NOT NULL,
  `status` varchar(20) DEFAULT NULL,
  `office_id` bigint(20) DEFAULT NULL,
  `client_id` bigint(20) DEFAULT NULL,
  `warranty` bigint(20) DEFAULT NULL,
  `warranty_date` datetime DEFAULT NULL,
  `remarks` varchar(100) DEFAULT NULL,
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  `item_model` varchar(60) DEFAULT NULL,
  `is_deleted` char(2) DEFAULT 'N',
  `location_id` bigint(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serial_no_constraint` (`serial_no`),
  KEY `fk_id_cid` (`client_id`),
  KEY `fk_id_usr` (`createdby_id`),
  KEY `fk_id_oid` (`office_id`),
  KEY `fk_itd_imid` (`item_master_id`),
  KEY `fk_itd_gid` (`grn_id`),
  KEY `idx_bid_imid` (`item_master_id`),
  KEY `idx_bid_cid` (`client_id`),
  CONSTRAINT `fk_id_cid` FOREIGN KEY (`client_id`) REFERENCES `m_client` (`id`),
  CONSTRAINT `fk_id_oid` FOREIGN KEY (`office_id`) REFERENCES `m_office` (`id`),
  CONSTRAINT `fk_id_usr` FOREIGN KEY (`createdby_id`) REFERENCES `m_appuser` (`id`),
  CONSTRAINT `fk_itd_gid` FOREIGN KEY (`grn_id`) REFERENCES `b_grn` (`id`),
  CONSTRAINT `fk_itd_imid` FOREIGN KEY (`item_master_id`) REFERENCES `b_item_master` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `b_item_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_id` bigint(20) NOT NULL,
  `ref_type` varchar(45) DEFAULT NULL,
  `item_master_id` int(20) NOT NULL,
  `serial_number` varchar(100) NOT NULL,
  `transaction_date` datetime NOT NULL,
  `from_office` bigint(20) NOT NULL,
  `to_office` bigint(20) NOT NULL,
  `createdby_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `lastmodified_date` datetime DEFAULT NULL,
  `lastmodifiedby_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ihis_fromoffice` (`from_office`),
  KEY `idx_ihis_tooffice` (`to_office`),
  KEY `idx_ihis_reftype` (`ref_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 'CREATE_SUPPLIER', 'SUPPLIER', 'CREATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 'UPDATE_SUPPLIER', 'SUPPLIER', 'UPDATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'CREATE_ITEM', 'ITEM', 'CREATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'UPDATE_ITEM', 'ITEM', 'UPDATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'DELETE_ITEM', 'ITEM', 'DELETE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'CREATE_MRN', 'MRN', 'CREATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'MOVE_MRN', 'MRN', 'MOVE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'MOVEITEM_MRN', 'MRN', 'MOVEITEM', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'CREATE_GRN', 'GRN', 'CREATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 'CREATE_INVENTORY', 'INVENTORY', 'CREATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 
'UPDATE_GRN', 'GRN', 'UPDATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 'CREATE_ITEMSALE', 'ITEMSALE', 'CREATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 'UPDATE_INVENTORY', 'INVENTORY', 'UPDATE', 0);

INSERT IGNORE INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('inventory', 'DELETE_INVENTORY', 'INVENTORY', 'DELETE', 0);






