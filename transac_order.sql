CREATE TABLE `transac_order` (
	`transaction_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`trade_id` INT UNSIGNED NULL DEFAULT NULL COMMENT 'TradeID',
	`version` INT UNSIGNED NULL DEFAULT NULL COMMENT 'Version',
	`quantity` INT NULL DEFAULT NULL COMMENT 'Quantity',
	`security_code` VARCHAR(32) NULL DEFAULT NULL COMMENT 'securityCode' COLLATE 'utf8mb4_general_ci',
	`command` VARCHAR(32) NULL DEFAULT NULL COMMENT 'Command' COLLATE 'utf8mb4_general_ci',
	`trade_mark` VARCHAR(32) NULL DEFAULT NULL COMMENT 'TradeMark' COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`transaction_id`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;