Drop procedure IF EXISTS addcolumns;
DELIMITER //
create procedure addcolumns() 
Begin
IF NOT EXISTS (
     SELECT * FROM b_item_master.COLUMNS
     WHERE COLUMN_NAME = 'item_price'
     and TABLE_NAME = 'b_item_master'
     and TABLE_SCHEMA = DATABASE())THEN
ALTER TABLE `b_item_master` ADD COLUMN `item_price` NOT NULL AFTER `reorder_level`;

END IF;
END //
DELIMITER ;
call addcolumns();
Drop procedure IF EXISTS addcolumns;



