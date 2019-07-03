CREATE DATABASE  IF NOT EXISTS `customer_base`;

USE `customer_base`;


-- Table structure for table `customer_base`

DROP TABLE IF EXISTS `customer_base`;
CREATE TABLE `customer` (
	 `id` int(11) NOT NULL AUTO_INCREMENT,
	`first_name` varchar(45) DEFAULT NULL,
	`last_name` varchar(45) DEFAULT NULL,
	`email` varchar(45) DEFAULT NULL,
	PRIMARY KEY (`id`)
);

