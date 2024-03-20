# SQL-DB-Query
SQL Query for the database creation with all the info

# SQL Info
```
Database Name: CouponsProject
```
```
Port: 3306
```
```
User Name: root
```
```
Password: 1234
```
```
Coupons-REST-Port: 8080
```
```
Angular Port: 4200
```
```
Spring Micro Service: 8888
```
```
CREATE DATABASE `CouponsProject` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

CREATE TABLE CouponsProject.`Company` (
  `id` bigint(45) NOT NULL,
  `company_name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `company_name_UNIQUE` (`company_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE CouponsProject.`Coupon` (
  `id` bigint(45) NOT NULL,
  `title` varchar(45) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `message` varchar(45) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `image` varchar(455) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_UNIQUE` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE CouponsProject.`Customer` (
  `id` bigint(45) NOT NULL,
  `customer_name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_name_UNIQUE` (`customer_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE CouponsProject.`company_coupon` (
  `company_id` bigint(45) NOT NULL,
  `coupon_id` bigint(45) NOT NULL,
  PRIMARY KEY (`company_id`,`coupon_id`),
  UNIQUE KEY `coupon_id_UNIQUE` (`coupon_id`),
  KEY `couponid_idx` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE CouponsProject.`customer_coupon` (
  `customer_id` bigint(45) NOT NULL,
  `coupon_id` bigint(45) NOT NULL,
  PRIMARY KEY (`customer_id`,`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
