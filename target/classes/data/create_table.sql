CREATE TABLE `raw_check_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` varchar(64) NOT NULL,
  `posting_date` date DEFAULT NULL,
  `effective_date` date DEFAULT NULL,
  `trans_type` varchar(75) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `check_number` bigint(20) DEFAULT NULL,
  `ref_number` varchar(18) NOT NULL,
  `description` varchar(96) DEFAULT NULL,
  `trans_category` varchar(50) DEFAULT NULL,
  `type` varchar(32) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_m6flh6fjjf00hb2fkj2qbat9b` (`ref_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;