/*
SQLyog Enterprise - MySQL GUI v8.14 
MySQL - 5.5.38 : Database - bank_risk_control
*********************************************************************
*/


drop database if exists bank_risk_control;

create database bank_risk_control CHARACTER SET utf8;

USE `bank_risk_control`;

/*Table structure for table `area_city` */

DROP TABLE IF EXISTS `area_city`;

CREATE TABLE `area_city` (
  `AREA_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREE_CODE` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `PARENT_AREA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`AREA_ID`),
  KEY `FK_4m82opq65qxepoqtbm4yoiv5a` (`PARENT_AREA_ID`),
  CONSTRAINT `FK_4m82opq65qxepoqtbm4yoiv5a` FOREIGN KEY (`PARENT_AREA_ID`) REFERENCES `area_city` (`AREA_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `organization`;

CREATE TABLE `organization` (
  `org_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` longtext,
  `category` int(11) NOT NULL,
  `contacts_cellphone` varchar(32) DEFAULT NULL,
  `contacts_mail` varchar(128) DEFAULT NULL,
  `contacts_name` varchar(32) DEFAULT NULL,
  `contacts_phone` varchar(32) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `ORG_NO` varchar(255) DEFAULT NULL,
  `secret_key` varchar(255) DEFAULT NULL,
  `area_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `moneyCount` bigint(20) DEFAULT 0,
  `canControl` TINYINT DEFAULT 0,
  PRIMARY KEY (`org_id`),
  KEY `FK_32hcsg300q8ct0uvyia4pxdb0` (`area_id`),
  KEY `FK_57byxcy430qbl2gl7liup0py1` (`parent_id`),
  CONSTRAINT `FK_32hcsg300q8ct0uvyia4pxdb0` FOREIGN KEY (`area_id`) REFERENCES `area_city` (`AREA_ID`),
  CONSTRAINT `FK_57byxcy430qbl2gl7liup0py1` FOREIGN KEY (`parent_id`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area_code` varchar(255) DEFAULT NULL,
  `cellphone` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_online` TINYINT DEFAULT 0,
  `passwd` varchar(255) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FK_lc5e5iqs5b0tnxb8lyx5ljs9l` (`ORG_ID`),
  CONSTRAINT `FK_lc5e5iqs5b0tnxb8lyx5ljs9l` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `PERMISSION_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `GROUP_NAME` varchar(255) DEFAULT NULL,
  `MODULE_NAME` varchar(255) DEFAULT NULL,
  `PERMISSION_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`PERMISSION_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;

/*Table structure for table `role_permissions` */

DROP TABLE IF EXISTS `role_permissions`;

CREATE TABLE `role_permissions` (
  `PERMISSION_ID` bigint(20) NOT NULL,
  `ROLE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`PERMISSION_ID`,`ROLE_ID`),
  KEY `FK_7ftfclev5mf6hvg1f6ed1q9ua` (`ROLE_ID`),
  CONSTRAINT `FK_7ftfclev5mf6hvg1f6ed1q9ua` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`role_id`),
  CONSTRAINT `FK_a4out73p7dodcqui5lp0pcgxk` FOREIGN KEY (`PERMISSION_ID`) REFERENCES `permission` (`PERMISSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `user_roles` */

DROP TABLE IF EXISTS `user_roles`;

CREATE TABLE `user_roles` (
  `ROLE_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  KEY `FK_5gd2yindui3v6lg65nwviwhep` (`USER_ID`),
  CONSTRAINT `FK_5gd2yindui3v6lg65nwviwhep` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_a81e7td2mpf06vxpll8j03mxy` FOREIGN KEY (`ROLE_ID`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*Table structure for table `risk_data_file` */

DROP TABLE IF EXISTS `risk_data_file`;

CREATE TABLE `risk_data_file` (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) DEFAULT NULL,
  `file_size` int(11) DEFAULT NULL,
  `source_file_name` varchar(255) DEFAULT NULL,
  `upload_time` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`file_id`),
  KEY `FK_rvye2sarsdg994p24q3qqofsm` (`user_id`),
  CONSTRAINT `FK_rvye2sarsdg994p24q3qqofsm` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `risk_category` */

DROP TABLE IF EXISTS `risk_category`;

CREATE TABLE `risk_category` (
  `risk_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `allow_report` TINYINT DEFAULT 0,
  `cycle` int(11) NOT NULL,
  `end_day` int(11) DEFAULT NULL,
  `end_hour` int(11) DEFAULT NULL,
  `max_value` int(11) DEFAULT NULL,
  `min_value` int(11) DEFAULT NULL,
  `risk_code` varchar(255) DEFAULT NULL,
  `risk_name` varchar(255) DEFAULT NULL,
  `start_day` int(11) DEFAULT NULL,
  `start_hour` int(11) DEFAULT NULL,
  `is_use_threshold_value` TINYINT DEFAULT 0,
  `has_leaf` TINYINT DEFAULT 0,
  PRIMARY KEY (`risk_id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8;


/*Table structure for table `electronic_active_user_change_analyze` */

DROP TABLE IF EXISTS `electronic_active_user_change_analyze`;

CREATE TABLE `electronic_active_user_change_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` float NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m09q8emk1ppobiyoj0ckqq2ir` (`RISK_ID`),
  CONSTRAINT `FK_m09q8emk1ppobiyoj0ckqq2ir` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `electronic_active_user_change_rate` */

DROP TABLE IF EXISTS `electronic_active_user_change_rate`;

CREATE TABLE `electronic_active_user_change_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `CURRENT_ACTIVE_USER` int(11) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `LAST_ACTIVE_USER` int(11) DEFAULT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_b9js59rn10vscgp6bkorswdp3` (`ORG_ID`),
  KEY `FK_9qawfbm9qou8jy9sx0uovrey6` (`RISK_ID`),
  KEY `FK_gmj1wob9kmdn09lcx62klr959` (`FILE_ID`),
  KEY `FK_roq95wpg094ltbrbsc295svql` (`USER_ID`),
  CONSTRAINT `FK_9qawfbm9qou8jy9sx0uovrey6` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`),
  CONSTRAINT `FK_b9js59rn10vscgp6bkorswdp3` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_gmj1wob9kmdn09lcx62klr959` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`),
  CONSTRAINT `FK_roq95wpg094ltbrbsc295svql` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `electronic_transaction_change_analyze` */

DROP TABLE IF EXISTS `electronic_transaction_change_analyze`;

CREATE TABLE `electronic_transaction_change_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` float NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_65bx4g5em6fgplxp4b6eh3noo` (`RISK_ID`),
  CONSTRAINT `FK_65bx4g5em6fgplxp4b6eh3noo` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `electronic_transaction_change_rate` */

DROP TABLE IF EXISTS `electronic_transaction_change_rate`;

CREATE TABLE `electronic_transaction_change_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `CURRENT_TRADING_VOLUME` bigint(20) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `LAST_TRADING_VOLUME` bigint(20) DEFAULT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mrylvwekkjkjujtwn4695ueix` (`ORG_ID`),
  KEY `FK_pjavesr6pp3sjegviilhs1opk` (`RISK_ID`),
  KEY `FK_8g1ud14hbfopjaev33i71tg4q` (`FILE_ID`),
  KEY `FK_5q9e0d19spagx7c36p2tgtg18` (`USER_ID`),
  CONSTRAINT `FK_5q9e0d19spagx7c36p2tgtg18` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_8g1ud14hbfopjaev33i71tg4q` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`),
  CONSTRAINT `FK_mrylvwekkjkjujtwn4695ueix` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_pjavesr6pp3sjegviilhs1opk` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `extra_report_info` */

DROP TABLE IF EXISTS `extra_report_info`;

CREATE TABLE `extra_report_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_audit` TINYINT DEFAULT 0,
  `begin_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `period` int(11) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `report_date` datetime DEFAULT NULL,
  `is_reported` TINYINT DEFAULT 0,
  `type` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_qa6uaatmp54jadsxqebtkoafm` (`ORG_ID`),
  KEY `FK_4yis17j5il5bjlcytnhkpshmd` (`RISK_ID`),
  KEY `FK_99ovbwm2l6n1ja77je63qihas` (`USER_ID`),
  CONSTRAINT `FK_4yis17j5il5bjlcytnhkpshmd` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`),
  CONSTRAINT `FK_99ovbwm2l6n1ja77je63qihas` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_qa6uaatmp54jadsxqebtkoafm` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `fake_site_attachment_analyze` */

DROP TABLE IF EXISTS `fake_site_attachment_analyze`;

CREATE TABLE `fake_site_attachment_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` float NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lmruj0el9nyqu77ic1ijyes9g` (`RISK_ID`),
  CONSTRAINT `FK_lmruj0el9nyqu77ic1ijyes9g` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `fake_site_attachment_rate` */

DROP TABLE IF EXISTS `fake_site_attachment_rate`;

CREATE TABLE `fake_site_attachment_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aofw` int(11) NOT NULL,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `cfw` int(11) NOT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_apcbbbk7g9gunmoarf98gc8ls` (`ORG_ID`),
  KEY `FK_s5cxgm83pqhog8ya4sowx7rul` (`RISK_ID`),
  KEY `FK_24pa2t601i3ryaboalag7dk2o` (`FILE_ID`),
  KEY `FK_gkjwcu29uo9dm394wdq0twbjr` (`USER_ID`),
  CONSTRAINT `FK_24pa2t601i3ryaboalag7dk2o` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`),
  CONSTRAINT `FK_apcbbbk7g9gunmoarf98gc8ls` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_gkjwcu29uo9dm394wdq0twbjr` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_s5cxgm83pqhog8ya4sowx7rul` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `info_technology_risk_event_analyze` */

DROP TABLE IF EXISTS `info_technology_risk_event_analyze`;

CREATE TABLE `info_technology_risk_event_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` int(11) NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ryt471kld2yc1lshp10y3wteu` (`RISK_ID`),
  CONSTRAINT `FK_ryt471kld2yc1lshp10y3wteu` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `info_technology_risk_event_count` */

DROP TABLE IF EXISTS `info_technology_risk_event_count`;

CREATE TABLE `info_technology_risk_event_count` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `COUNTS` bigint(20) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9v4724b95266uh2q35wfrdhqd` (`ORG_ID`),
  KEY `FK_97k6svvqtrai1ac5qgcytwcdk` (`RISK_ID`),
  KEY `FK_nh3yqmvncwu0s8whn1mfxo2a4` (`FILE_ID`),
  KEY `FK_7313n955b3r3alv2v09nic8sa` (`USER_ID`),
  CONSTRAINT `FK_7313n955b3r3alv2v09nic8sa` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_97k6svvqtrai1ac5qgcytwcdk` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`),
  CONSTRAINT `FK_9v4724b95266uh2q35wfrdhqd` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_nh3yqmvncwu0s8whn1mfxo2a4` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `login_log` */

DROP TABLE IF EXISTS `login_log`;

CREATE TABLE `login_log` (
  `LOG_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LOGIN_TIME` datetime DEFAULT NULL,
  `status` int(11) NOT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`LOG_ID`),
  KEY `FK_jxin6n7x26ui6f58at26n82hd` (`USER_ID`),
  CONSTRAINT `FK_jxin6n7x26ui6f58at26n82hd` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*Table structure for table `operate_log` */

DROP TABLE IF EXISTS `operate_log`;

CREATE TABLE `operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIONS` varchar(255) DEFAULT NULL,
  `ACTIONEN` varchar(255) DEFAULT NULL,
  `OPERATE_TIME` datetime DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_se35b3hvloyg73ppqten0xlxd` (`USER_ID`),
  CONSTRAINT `FK_se35b3hvloyg73ppqten0xlxd` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8;



/*Table structure for table `operation_change_success_analyze` */

DROP TABLE IF EXISTS `operation_change_success_analyze`;

CREATE TABLE `operation_change_success_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` float NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6wwbiycqt51dx4us139ssi4mq` (`RISK_ID`),
  CONSTRAINT `FK_6wwbiycqt51dx4us139ssi4mq` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `operation_change_success_rate` */

DROP TABLE IF EXISTS `operation_change_success_rate`;

CREATE TABLE `operation_change_success_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `dc` int(11) NOT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `sdc` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pr2wl3f4u44hg6ivufnk9lw23` (`ORG_ID`),
  KEY `FK_9gl42fafyb6csun2pliqeye8u` (`RISK_ID`),
  KEY `FK_oadw1nmeby8ehd3gdixo06mti` (`FILE_ID`),
  KEY `FK_aotkj1558jalnyn44sye8m87y` (`USER_ID`),
  CONSTRAINT `FK_9gl42fafyb6csun2pliqeye8u` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`),
  CONSTRAINT `FK_aotkj1558jalnyn44sye8m87y` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_oadw1nmeby8ehd3gdixo06mti` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`),
  CONSTRAINT `FK_pr2wl3f4u44hg6ivufnk9lw23` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `outside_attack_change_analyze` */

DROP TABLE IF EXISTS `outside_attack_change_analyze`;

CREATE TABLE `outside_attack_change_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` float NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t7eap69m83u34v8g6r2ekjlrx` (`RISK_ID`),
  CONSTRAINT `FK_t7eap69m83u34v8g6r2ekjlrx` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `outside_attack_change_rate` */

DROP TABLE IF EXISTS `outside_attack_change_rate`;

CREATE TABLE `outside_attack_change_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `idswp` int(11) NOT NULL,
  `ipswp` int(11) NOT NULL,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `ldswp` int(11) NOT NULL,
  `lpswp` int(11) NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8e9tsc19ffs0ep5bhsotans1e` (`ORG_ID`),
  KEY `FK_imxw59ihtpnny19celfwnak30` (`RISK_ID`),
  KEY `FK_87imd2ifnuilbntfexx2eot5s` (`FILE_ID`),
  KEY `FK_fh3dln88dj119n8l77x2kagvu` (`USER_ID`),
  CONSTRAINT `FK_87imd2ifnuilbntfexx2eot5s` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`),
  CONSTRAINT `FK_8e9tsc19ffs0ep5bhsotans1e` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_fh3dln88dj119n8l77x2kagvu` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_imxw59ihtpnny19celfwnak30` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/*Table structure for table `system_available_analyze` */

DROP TABLE IF EXISTS `system_available_analyze`;

CREATE TABLE `system_available_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` float NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_jlsgim3oqye1xpxcrriyqupk7` (`RISK_ID`),
  CONSTRAINT `FK_jlsgim3oqye1xpxcrriyqupk7` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `system_available_rate` */

DROP TABLE IF EXISTS `system_available_rate`;

CREATE TABLE `system_available_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `ltsp` bigint(20) NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `pd` int(11) NOT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ud` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fw9ko6ijt256eixmel50ba0f2` (`ORG_ID`),
  KEY `FK_dyd825scy3kwq4hpbp6tshxv1` (`RISK_ID`),
  KEY `FK_5ns3hvkf2220sj34bkv7k2xmq` (`FILE_ID`),
  KEY `FK_qdbdwi8stvrmb9jua164sra9k` (`USER_ID`),
  CONSTRAINT `FK_5ns3hvkf2220sj34bkv7k2xmq` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`),
  CONSTRAINT `FK_dyd825scy3kwq4hpbp6tshxv1` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`),
  CONSTRAINT `FK_fw9ko6ijt256eixmel50ba0f2` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_qdbdwi8stvrmb9jua164sra9k` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `system_transaction_success_analyze` */

DROP TABLE IF EXISTS `system_transaction_success_analyze`;

CREATE TABLE `system_transaction_success_analyze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `datas` float NOT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_nm3aosg1tmho853ycxhntg7n3` (`RISK_ID`),
  CONSTRAINT `FK_nm3aosg1tmho853ycxhntg7n3` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `system_transaction_success_rate` */

DROP TABLE IF EXISTS `system_transaction_success_rate`;

CREATE TABLE `system_transaction_success_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `aosst` bigint(20) NOT NULL,
  `aost` bigint(20) NOT NULL,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATA_MONTH` int(11) DEFAULT NULL,
  `DATA_WEEK` int(11) DEFAULT NULL,
  `DATA_YEAR` int(11) DEFAULT NULL,
  `IS_EXTRAL` TINYINT DEFAULT 0,
  `KIP_CODE` varchar(255) DEFAULT NULL,
  `ORG_CODE` varchar(255) DEFAULT NULL,
  `period` int(11) NOT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `REPORT_DATE` datetime DEFAULT NULL,
  `RISK_CODE` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `RISK_ID` bigint(20) DEFAULT NULL,
  `FILE_ID` bigint(20) DEFAULT NULL,
  `USER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1rbtmw0rj9dxk1f1g5kkgmd03` (`ORG_ID`),
  KEY `FK_nhyryhry6m85m9o1r4nguhv6h` (`RISK_ID`),
  KEY `FK_41ldkfuxpi9xy4yl6o3caup9x` (`FILE_ID`),
  KEY `FK_cqvkpiihm5jwq5gx96enpsbts` (`USER_ID`),
  CONSTRAINT `FK_1rbtmw0rj9dxk1f1g5kkgmd03` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_41ldkfuxpi9xy4yl6o3caup9x` FOREIGN KEY (`FILE_ID`) REFERENCES `risk_data_file` (`file_id`),
  CONSTRAINT `FK_cqvkpiihm5jwq5gx96enpsbts` FOREIGN KEY (`USER_ID`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_nhyryhry6m85m9o1r4nguhv6h` FOREIGN KEY (`RISK_ID`) REFERENCES `risk_category` (`risk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `app_system` */

DROP TABLE IF EXISTS `app_system`;

CREATE TABLE `app_system` (
  `APP_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APP_NAME` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`APP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `computer_room_info` */

DROP TABLE IF EXISTS `computer_room_info`;

CREATE TABLE `computer_room_info` (
  `CR_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `area` double NOT NULL,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `power` int(11) NOT NULL,
  `PRECISION_AC_COUNT` int(11) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `SERVER_TIME` datetime DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPS_COUNT` int(11) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`CR_ID`),
  KEY `FK_ka5lft8ywcros2h4uu20lt27v` (`ORG_ID`),
  CONSTRAINT `FK_ka5lft8ywcros2h4uu20lt27v` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `precision_ac_info` */

DROP TABLE IF EXISTS `precision_ac_info`;

CREATE TABLE `precision_ac_info` (
  `AC_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `RETURN_HUMIDITY` varchar(255) DEFAULT NULL,
  `ROOM_UV` varchar(255) DEFAULT NULL,
  `SUPPLY_HUMIDITY` varchar(255) DEFAULT NULL,
  `SUPPLY_TEMP` varchar(255) DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CR_ID` bigint(20) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`AC_ID`),
  KEY `FK_30opp8kt40teiqraicmje30i4` (`CR_ID`),
  KEY `FK_i3vvfqdbj955q1069pnkogm38` (`ORG_ID`),
  CONSTRAINT `FK_30opp8kt40teiqraicmje30i4` FOREIGN KEY (`CR_ID`) REFERENCES `computer_room_info` (`CR_ID`),
  CONSTRAINT `FK_i3vvfqdbj955q1069pnkogm38` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `ups_info` */

DROP TABLE IF EXISTS `ups_info`;

CREATE TABLE `ups_info` (
  `UPS_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `INPUT_FREQUENCY` varchar(255) DEFAULT NULL,
  `INPUT_VOLTAGE` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `OUTPUT_FREQUENCY` varchar(255) DEFAULT NULL,
  `OUTPUT_VOLTAGE` varchar(255) DEFAULT NULL,
  `power` varchar(255) DEFAULT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `ROOM_UV` varchar(255) DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CR_ID` bigint(20) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`UPS_ID`),
  KEY `FK_68vg2hxa9ckw7w4p31wsusu7p` (`CR_ID`),
  KEY `FK_6pe54eu82rrb056jqqa8vkyf9` (`ORG_ID`),
  CONSTRAINT `FK_68vg2hxa9ckw7w4p31wsusu7p` FOREIGN KEY (`CR_ID`) REFERENCES `computer_room_info` (`CR_ID`),
  CONSTRAINT `FK_6pe54eu82rrb056jqqa8vkyf9` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pc_server` */

DROP TABLE IF EXISTS `pc_server`;

CREATE TABLE `pc_server` (
  `SERVER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AREE_CODE` varchar(255) DEFAULT NULL,
  `category` int(11) NOT NULL,
  `cpu` varchar(255) DEFAULT NULL,
  `CPU_COUNT` int(11) DEFAULT NULL,
  `HARD_DISK_SIZE` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `MEMORY_SIZE` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `SERIAL_NUMBER` varchar(255) DEFAULT NULL,
  `SERVICE_TIME` datetime DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`SERVER_ID`),
  KEY `FK_jupx8v8gc0ebtkcefeo9fcjak` (`ORG_ID`),
  CONSTRAINT `FK_jupx8v8gc0ebtkcefeo9fcjak` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Table structure for table `operate_system_info` */

DROP TABLE IF EXISTS `operate_system_info`;

CREATE TABLE `operate_system_info` (
  `OS_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `appSystem` varchar(255) DEFAULT NULL,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `patch` varchar(255) DEFAULT NULL,
  `pcserver` tinyblob,
  `RECORD_TIME` datetime DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `pcserver_SERVER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`OS_ID`),
  KEY `FK_j3pniml2bhwccweu7ys5d2olv` (`ORG_ID`),
  KEY `FK_dixvhbx3g3js3488i66i4yfjv` (`pcserver_SERVER_ID`),
  CONSTRAINT `FK_dixvhbx3g3js3488i66i4yfjv` FOREIGN KEY (`pcserver_SERVER_ID`) REFERENCES `pc_server` (`SERVER_ID`),
  CONSTRAINT `FK_j3pniml2bhwccweu7ys5d2olv` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Table structure for table `database_info` */

DROP TABLE IF EXISTS `database_info`;

CREATE TABLE `database_info` (
  `DB_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `appSystem` varchar(255) DEFAULT NULL,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `DATABASE_NAME` varchar(255) DEFAULT NULL,
  `OPERATE_SYSTEM` varchar(255) DEFAULT NULL,
  `patch` varchar(255) DEFAULT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `SERVER_TIME` datetime DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `osinfo_OS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`DB_ID`),
  KEY `FK_1i66ex27yr61xyc68q08mrkcr` (`ORG_ID`),
  KEY `FK_hkn624ssskn6sd89mjmuqinpb` (`osinfo_OS_ID`),
  CONSTRAINT `FK_1i66ex27yr61xyc68q08mrkcr` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_hkn624ssskn6sd89mjmuqinpb` FOREIGN KEY (`osinfo_OS_ID`) REFERENCES `operate_system_info` (`OS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


/*Table structure for table `database_app` */

DROP TABLE IF EXISTS `database_app`;

CREATE TABLE `database_app` (
  `APP_ID` bigint(20) NOT NULL,
  `DB_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`APP_ID`,`DB_ID`),
  KEY `FK_n83jvd697y0boe85my3u3nq28` (`DB_ID`),
  CONSTRAINT `FK_hxr0clj05gm3m3a3k0hglwovj` FOREIGN KEY (`APP_ID`) REFERENCES `app_system` (`APP_ID`),
  CONSTRAINT `FK_n83jvd697y0boe85my3u3nq28` FOREIGN KEY (`DB_ID`) REFERENCES `database_info` (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `os_app` */

DROP TABLE IF EXISTS `os_app`;

CREATE TABLE `os_app` (
  `OS_ID` bigint(20) NOT NULL,
  `APP_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`OS_ID`,`APP_ID`),
  KEY `FK_5qp3r4rrr340396n2mdr62a9u` (`APP_ID`),
  CONSTRAINT `FK_5qp3r4rrr340396n2mdr62a9u` FOREIGN KEY (`APP_ID`) REFERENCES `app_system` (`APP_ID`),
  CONSTRAINT `FK_ym8id8d7asplkmed0ekw7nwf` FOREIGN KEY (`OS_ID`) REFERENCES `operate_system_info` (`OS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `middleware_info` */

DROP TABLE IF EXISTS `middleware_info`;

CREATE TABLE `middleware_info` (
  `MW_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `appSystem` varchar(255) DEFAULT NULL,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `middlewareName` varchar(255) DEFAULT NULL,
  `OPERATE_SYSTEM` varchar(255) DEFAULT NULL,
  `patch` varchar(255) DEFAULT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `SERVER_TIME` datetime DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  `osinfo_OS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`MW_ID`),
  KEY `FK_4y8wsfc30rxh065v881khgswb` (`ORG_ID`),
  KEY `FK_c4rkni5jleb4xws3ca3cqte93` (`osinfo_OS_ID`),
  CONSTRAINT `FK_4y8wsfc30rxh065v881khgswb` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_c4rkni5jleb4xws3ca3cqte93` FOREIGN KEY (`osinfo_OS_ID`) REFERENCES `operate_system_info` (`OS_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `middleware_app` */

DROP TABLE IF EXISTS `middleware_app`;

CREATE TABLE `middleware_app` (
  `APP_ID` bigint(20) NOT NULL,
  `MW_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`APP_ID`,`MW_ID`),
  KEY `FK_6t09piulof06p7ocuf2bpar34` (`MW_ID`),
  CONSTRAINT `FK_6t09piulof06p7ocuf2bpar34` FOREIGN KEY (`MW_ID`) REFERENCES `middleware_info` (`MW_ID`),
  CONSTRAINT `FK_siqyts9mcp15u41kh8vmwqwru` FOREIGN KEY (`APP_ID`) REFERENCES `app_system` (`APP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `network_equipment_info` */

DROP TABLE IF EXISTS `network_equipment_info`;

CREATE TABLE `network_equipment_info` (
  `NE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `appSystem` varchar(255) DEFAULT NULL,
  `AREA_CODE` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `PORT_COUNT` int(11) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `RECORD_TIME` datetime DEFAULT NULL,
  `SERIAL_NUMBER` varchar(255) DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `UNIQUE_VAL` varchar(255) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `ORG_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`NE_ID`),
  KEY `FK_t9d0yjf41ss9b9vjkr7ycjbt` (`ORG_ID`),
  CONSTRAINT `FK_t9d0yjf41ss9b9vjkr7ycjbt` FOREIGN KEY (`ORG_ID`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `ne_app` */

DROP TABLE IF EXISTS `ne_app`;

CREATE TABLE `ne_app` (
  `APP_ID` bigint(20) NOT NULL,
  `NE_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`APP_ID`,`NE_ID`),
  KEY `FK_nes7suppkorp030ijf0x3e2xl` (`NE_ID`),
  CONSTRAINT `FK_gwy8le2mvjujiwmvujbow9cq9` FOREIGN KEY (`APP_ID`) REFERENCES `app_system` (`APP_ID`),
  CONSTRAINT `FK_nes7suppkorp030ijf0x3e2xl` FOREIGN KEY (`NE_ID`) REFERENCES `network_equipment_info` (`NE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `storage_system_info` */

DROP TABLE IF EXISTS `storage_system_info`;

CREATE TABLE `storage_system_info` (
  `ss_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appSystem` varchar(255) DEFAULT NULL,
  `area_code` varchar(255) DEFAULT NULL,
  `capacity_info` varchar(255) DEFAULT NULL,
  `disk_spec` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `manufacturer` varchar(255) DEFAULT NULL,
  `microcode_version` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `purpose` varchar(255) DEFAULT NULL,
  `record_time` datetime DEFAULT NULL,
  `serial_number` varchar(255) DEFAULT NULL,
  `sever_time` datetime DEFAULT NULL,
  `storage_array_size` int(11) DEFAULT NULL,
  `storage_cache_size` int(11) DEFAULT NULL,
  `storage_raid_mode` varchar(255) DEFAULT NULL,
  `storage_tape_media_count` int(11) DEFAULT NULL,
  `storage_tape_media_type` varchar(255) DEFAULT NULL,
  `TYPES` varchar(255) DEFAULT NULL,
  `unique_val` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ss_id`),
  KEY `FK_9oqvi83nm71iy4am22gx9pgyp` (`org_id`),
  CONSTRAINT `FK_9oqvi83nm71iy4am22gx9pgyp` FOREIGN KEY (`org_id`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Table structure for table `ss_app` */

DROP TABLE IF EXISTS `ss_app`;

CREATE TABLE `ss_app` (
  `APP_ID` bigint(20) NOT NULL,
  `SS_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`APP_ID`,`SS_ID`),
  KEY `FK_h5ac5x2uyxo6ogh11k6gmf2mt` (`SS_ID`),
  CONSTRAINT `FK_h5ac5x2uyxo6ogh11k6gmf2mt` FOREIGN KEY (`SS_ID`) REFERENCES `storage_system_info` (`ss_id`),
  CONSTRAINT `FK_tljw9misxo1glslu7v7hcsne6` FOREIGN KEY (`APP_ID`) REFERENCES `app_system` (`APP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bank_message`;

CREATE TABLE `bank_message` (
  `messageId` bigint(20) NOT NULL AUTO_INCREMENT,
  `critical` int(11) NOT NULL,
  `message` longtext,
  `orgs` varchar(255) DEFAULT NULL,
  `sendTime` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `sendUser_org_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`messageId`),
  KEY `FK_2nyldgm1twgw4wgecdf2kkeer` (`sendUser_org_id`),
  CONSTRAINT `FK_2nyldgm1twgw4wgecdf2kkeer` FOREIGN KEY (`sendUser_org_id`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bank_message_receiver`;

CREATE TABLE `bank_message_receiver` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `download` bit(1) NOT NULL,
  `hasRead` bit(1) NOT NULL,
  `message_messageId` bigint(20) DEFAULT NULL,
  `receiver_org_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1uage24lkpmalpkufnjnocfam` (`message_messageId`),
  KEY `FK_ssokt957vg8efbta46h68ycrq` (`receiver_org_id`),
  CONSTRAINT `FK_ssokt957vg8efbta46h68ycrq` FOREIGN KEY (`receiver_org_id`) REFERENCES `organization` (`org_id`),
  CONSTRAINT `FK_1uage24lkpmalpkufnjnocfam` FOREIGN KEY (`message_messageId`) REFERENCES `bank_message` (`messageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `risk_tca`;
CREATE TABLE `risk_tca` (
  `tcaId` bigint(20) NOT NULL AUTO_INCREMENT,
  `period` int(11) NOT NULL,
  `reportDate` datetime DEFAULT NULL,
  `currentValue` double NOT NULL,
  `org_org_id` bigint(20) DEFAULT NULL,
  `riskCategory_risk_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`tcaId`),
  KEY `FK_15rbm54r8majk6c9pmxaelkyk` (`org_org_id`),
  KEY `FK_t8kx5itqm7g538cocuigfa84` (`riskCategory_risk_id`),
  CONSTRAINT `FK_t8kx5itqm7g538cocuigfa84` FOREIGN KEY (`riskCategory_risk_id`) REFERENCES `risk_category` (`risk_id`),
  CONSTRAINT `FK_15rbm54r8majk6c9pmxaelkyk` FOREIGN KEY (`org_org_id`) REFERENCES `organization` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `backupdb`;
CREATE TABLE `backupdb` (
  `db_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `daily` int(11) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `port` int(11) NOT NULL,
  `taskstatus` bit(1) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `checknum` bigint(20) DEFAULT 1,
  PRIMARY KEY (`db_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `global_parameter`;
CREATE TABLE `global_parameter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fakesiteattachment` int(11) NOT NULL,
  `operationchanges` int(11) NOT NULL,
  `systemavailable` int(11) NOT NULL,
  `systemtransaction` int(11) NOT NULL,
  `continueDecline` int(11) NOT NULL,
  `continueDeclineEnable` TINYINT DEFAULT 0,
  `deviateAverage` int(11) NOT NULL,
  `deviateAverageEnable` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `warning_info`;
CREATE TABLE `warning_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org` tinyblob,
  `period` int(11) NOT NULL,
  `reportDate` datetime DEFAULT NULL,
  `riskName` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `warningType` varchar(255) DEFAULT NULL,
  `memos` varchar(255) DEFAULT NULL,
  `cycle` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

