<#ftl encoding="utf-8"/>
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `${tableName}`
-- ----------------------------
DROP TABLE IF EXISTS `${tableName}`;
CREATE TABLE `${tableName}`(
 	`${keyId}` char(32) NOT NULL COMMENT 'id主键',
	PRIMARY KEY (`${keyId}`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;