# ************************************************************
# Sequel Pro SQL dump
# Version (null)
#
# https://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 8.0.19)
# Database: cloud
# Generation Time: 2020-08-14 07:18:51 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table base_account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `base_account`;

CREATE TABLE `base_account` (
  `account_id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户Id',
  `account` varchar(100) NOT NULL DEFAULT '' COMMENT '标识：手机号、邮箱、 用户名、或第三方应用的唯一标识',
  `password` varchar(255) NOT NULL COMMENT '密码凭证：站内的保存密码、站外的不保存或保存token）',
  `account_type` varchar(50) NOT NULL DEFAULT '' COMMENT '登录类型:password-密码、mobile-手机号、email-邮箱、weixin-微信、weibo-微博、qq-等等',
  `domain` varchar(100) DEFAULT NULL COMMENT '账户域:@admin.com,@developer.com',
  `register_ip` varchar(50) DEFAULT NULL COMMENT '注册IP',
  `status` int DEFAULT NULL COMMENT '状态:0-禁用 1-启用 2-锁定',
  `created_date` datetime DEFAULT NULL COMMENT '注册时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`account_id`),
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='登录账号';

LOCK TABLES `base_account` WRITE;
/*!40000 ALTER TABLE `base_account` DISABLE KEYS */;

INSERT INTO `base_account` (`account_id`, `user_id`, `account`, `password`, `account_type`, `domain`, `register_ip`, `status`, `created_date`, `last_modified_date`)
VALUES
	(521677655368531968,521677655146233856,'admin','$2a$10$A7EHximvrsa4ESX1uSlkJupbg2PLO2StzDzy67NX4YV25MxmbGvXu','username','@admin.com',NULL,1,'2019-07-03 17:11:59','2019-10-15 09:48:24'),
	(557063237787451392,557063237640650752,'test','$2a$10$SdqHS7Y8VcrR0WfCf9FI3uhcUfYKu58per0fVJLW.iPOBt.bFYp0y','username','@admin.com',NULL,1,'2019-07-03 17:12:02','2019-10-15 09:48:33');

/*!40000 ALTER TABLE `base_account` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table base_account_logs
# ------------------------------------------------------------

DROP TABLE IF EXISTS `base_account_logs`;

CREATE TABLE `base_account_logs` (
  `id` bigint NOT NULL COMMENT '主键',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(50) NOT NULL DEFAULT '' COMMENT '登录Ip',
  `login_agent` varchar(255) NOT NULL DEFAULT '' COMMENT '登录设备',
  `login_nums` int NOT NULL COMMENT '登录次数',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `account` varchar(100) NOT NULL DEFAULT '' COMMENT '账号',
  `account_type` varchar(50) NOT NULL DEFAULT '' COMMENT '账号类型',
  `account_id` bigint NOT NULL COMMENT '账号ID',
  `domain` varchar(255) DEFAULT NULL COMMENT '账号域',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='登录日志';



# Dump of table base_app
# ------------------------------------------------------------

DROP TABLE IF EXISTS `base_app`;

CREATE TABLE `base_app` (
  `app_id` varchar(50) NOT NULL COMMENT '客户端ID',
  `api_key` varchar(255) DEFAULT NULL COMMENT 'API访问key',
  `secret_key` varchar(255) NOT NULL COMMENT 'API访问密钥',
  `app_name` varchar(255) NOT NULL COMMENT 'app名称',
  `app_name_en` varchar(255) NOT NULL COMMENT 'app英文名称',
  `app_icon` varchar(255) NOT NULL COMMENT '应用图标',
  `app_type` varchar(50) NOT NULL COMMENT 'app类型:server-服务应用 app-手机应用 pc-PC网页应用 wap-手机网页应用',
  `app_desc` varchar(255) DEFAULT NULL COMMENT 'app描述',
  `app_os` varchar(25) DEFAULT NULL COMMENT '移动应用操作系统:ios-苹果 android-安卓',
  `website` varchar(255) NOT NULL COMMENT '官网地址',
  `developer_id` bigint NOT NULL DEFAULT '0' COMMENT '开发者ID:默认为0',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态:0-无效 1-有效',
  `is_persist` int NOT NULL DEFAULT '0' COMMENT '保留数据0-否 1-是 不允许删除',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统应用-基础信息';

LOCK TABLES `base_app` WRITE;
/*!40000 ALTER TABLE `base_app` DISABLE KEYS */;

INSERT INTO `base_app` (`app_id`, `api_key`, `secret_key`, `app_name`, `app_name_en`, `app_icon`, `app_type`, `app_desc`, `app_os`, `website`, `developer_id`, `status`, `is_persist`, `created_date`, `last_modified_date`)
VALUES
	('1552274783265','7gBZcbsC7kLIWCdELIl8nxcs','0osTIhce7uPvDKHz6aa67bhCukaKoYl4','认证服务器','cloud-owl','','server','资源服务器','','http://www.baidu.com',0,1,1,'2018-11-12 17:48:45','2019-07-11 18:31:05');

/*!40000 ALTER TABLE `base_app` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table base_developer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `base_developer`;

CREATE TABLE `base_developer` (
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `user_type` varchar(20) DEFAULT 'isp' COMMENT '开发者类型: isp-服务提供商 normal-自研开发者',
  `company_id` bigint DEFAULT NULL COMMENT '企业ID',
  `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
  `status` int DEFAULT '1' COMMENT '状态:0-禁用 1-正常 2-锁定',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-开发者信息';



# Dump of table base_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `base_user`;

CREATE TABLE `base_user` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '登陆账号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '' COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `user_type` varchar(20) DEFAULT 'normal' COMMENT '用户类型:super-超级管理员 normal-普通管理员',
  `company_id` bigint DEFAULT NULL COMMENT '企业ID',
  `user_desc` varchar(255) DEFAULT '' COMMENT '描述',
  `status` int DEFAULT '1' COMMENT '状态:0-禁用 1-正常 2-锁定',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='系统用户-管理员信息';

LOCK TABLES `base_user` WRITE;
/*!40000 ALTER TABLE `base_user` DISABLE KEYS */;

INSERT INTO `base_user` (`user_id`, `user_name`, `nick_name`, `avatar`, `email`, `mobile`, `user_type`, `company_id`, `user_desc`, `status`, `created_date`, `last_modified_date`)
VALUES
	(521677655146233856,'admin','超级管理员','https://portrait.gitee.com/uploads/avatars/user/174/522460_utao_1596791720.png!avatar200','youtao531@163.com','18668120973','super',NULL,'111',1,'2018-12-10 13:20:45',NULL),
	(557063237640650752,'test','测试用户','','youtao531@163.com','18668120973','normal',NULL,'',1,'2019-03-18 04:50:25',NULL);

/*!40000 ALTER TABLE `base_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table gateway_ip_limit
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_ip_limit`;

CREATE TABLE `gateway_ip_limit` (
  `policy_id` bigint NOT NULL COMMENT '策略ID',
  `policy_name` varchar(100) NOT NULL COMMENT '策略名称',
  `policy_type` int NOT NULL DEFAULT '1' COMMENT '策略类型:0-拒绝/黑名单 1-允许/白名单',
  `ip_address` varchar(255) NOT NULL DEFAULT '' COMMENT 'ip地址/IP段:多个用隔开;最多10个',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `last_modified_date` datetime NOT NULL COMMENT '最近一次修改时间',
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='网关（IP访问控制策略）';

LOCK TABLES `gateway_ip_limit` WRITE;
/*!40000 ALTER TABLE `gateway_ip_limit` DISABLE KEYS */;

INSERT INTO `gateway_ip_limit` (`policy_id`, `policy_name`, `policy_type`, `ip_address`, `created_date`, `last_modified_date`)
VALUES
	(1149260136337063937,'IP白名单',1,'baidu.com','2019-07-11 18:12:23','2020-07-24 11:05:41');

/*!40000 ALTER TABLE `gateway_ip_limit` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table gateway_ip_limit_api
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_ip_limit_api`;

CREATE TABLE `gateway_ip_limit_api` (
  `policy_id` bigint NOT NULL COMMENT '策略ID',
  `api_id` bigint NOT NULL COMMENT '接口资源ID',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  KEY `policy_id` (`policy_id`) USING BTREE,
  KEY `api_id` (`api_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='网关（IP访问控制API接口映射）';



# Dump of table gateway_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_log`;

CREATE TABLE `gateway_log` (
  `log_id` bigint NOT NULL COMMENT '日志ID',
  `log_service_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志所属服务',
  `log_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志访问路径',
  `log_method` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志请求方法',
  `log_params` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '日志请求数据',
  `log_headers` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '日志请求头',
  `log_user_agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志客户端标识',
  `log_ip` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志请求IP',
  `log_http_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志响应状态',
  `log_request_time` datetime DEFAULT NULL COMMENT '日志访问时间',
  `log_response_time` datetime DEFAULT NULL COMMENT '日志响应时间',
  `log_use_millis` bigint DEFAULT '0' COMMENT '日志耗时（毫秒）',
  `log_region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志区域',
  `log_authentication` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '日志认证信息',
  `log_error` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '日志错误信息',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='网关（访问日志）';



# Dump of table gateway_rate_limit
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_rate_limit`;

CREATE TABLE `gateway_rate_limit` (
  `policy_id` bigint NOT NULL COMMENT '策略ID',
  `policy_name` varchar(255) DEFAULT NULL COMMENT '策略名称',
  `policy_type` varchar(255) DEFAULT NULL COMMENT '限流规则类型:url,origin,user',
  `limit_quota` bigint NOT NULL DEFAULT '0' COMMENT '限流数',
  `interval_unit` varchar(10) NOT NULL DEFAULT 'seconds' COMMENT '单位时间:seconds-秒,minutes-分钟,hours-小时,days-天',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `last_modified_date` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`policy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='网关（流量控制策略）';

LOCK TABLES `gateway_rate_limit` WRITE;
/*!40000 ALTER TABLE `gateway_rate_limit` DISABLE KEYS */;

INSERT INTO `gateway_rate_limit` (`policy_id`, `policy_name`, `policy_type`, `limit_quota`, `interval_unit`, `created_date`, `last_modified_date`)
VALUES
	(1149260244919205889,'接口限流','url',10,'seconds','2019-07-11 18:12:49','2019-10-14 13:38:14');

/*!40000 ALTER TABLE `gateway_rate_limit` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table gateway_rate_limit_api
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_rate_limit_api`;

CREATE TABLE `gateway_rate_limit_api` (
  `policy_id` bigint NOT NULL DEFAULT '0' COMMENT '策略ID',
  `api_id` bigint NOT NULL DEFAULT '1' COMMENT '时间间隔(秒)',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  KEY `policy_id` (`policy_id`) USING BTREE,
  KEY `api_id` (`api_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='网关（流量控制API接口映射）';

LOCK TABLES `gateway_rate_limit_api` WRITE;
/*!40000 ALTER TABLE `gateway_rate_limit_api` DISABLE KEYS */;

INSERT INTO `gateway_rate_limit_api` (`policy_id`, `api_id`, `created_date`, `last_modified_date`)
VALUES
	(1149260244919205889,1153544495210569729,'2019-07-30 15:43:46','2019-07-30 15:43:46'),
	(1149260244919205889,1149168013994549249,'2019-07-30 15:43:46','2019-07-30 15:43:46'),
	(1149260244919205889,1149168208614449153,'2019-07-30 15:43:46','2019-07-30 15:43:46');

/*!40000 ALTER TABLE `gateway_rate_limit_api` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table gateway_route
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gateway_route`;

CREATE TABLE `gateway_route` (
  `route_id` bigint NOT NULL COMMENT '路由ID',
  `route_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '路由说明',
  `route_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '路由名称',
  `route_path` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '路由路径',
  `route_service_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '路由所属服务（负载均衡）',
  `route_url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '路由地址（URL反向代理）',
  `route_strip_prefix` int NOT NULL DEFAULT '1' COMMENT '路由忽略前缀（0不忽略 1忽略）',
  `route_retryable` int NOT NULL DEFAULT '0' COMMENT '路由重试（0不重试 1重试）',
  `route_state` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '10' COMMENT '路由状态（10启用 20禁用 30锁定）',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`route_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='网关（路由）';

LOCK TABLES `gateway_route` WRITE;
/*!40000 ALTER TABLE `gateway_route` DISABLE KEYS */;

INSERT INTO `gateway_route` (`route_id`, `route_desc`, `route_name`, `route_path`, `route_service_id`, `route_url`, `route_strip_prefix`, `route_retryable`, `route_state`, `created_date`, `last_modified_date`)
VALUES
	(556587504019439617,'平台基础服务','cloud-hippo','/base/**','cloud-hippo','',0,0,'10','2019-09-18 10:23:29','2019-09-18 10:23:59'),
	(556587504019439618,'平台认证服务','cloud-owl','/uaa/**','cloud-owl','',0,0,'10','2019-09-18 10:23:29','2019-09-18 10:23:59');

/*!40000 ALTER TABLE `gateway_route` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table oauth_access_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `oauth_access_token`;

CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2访问令牌';



# Dump of table oauth_approvals
# ------------------------------------------------------------

DROP TABLE IF EXISTS `oauth_approvals`;

CREATE TABLE `oauth_approvals` (
  `userId` varchar(256) DEFAULT NULL,
  `clientId` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `expiresAt` datetime DEFAULT NULL,
  `lastModifiedAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2已授权客户端';



# Dump of table oauth_client_details
# ------------------------------------------------------------

DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(128) NOT NULL,
  `client_secret` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `scope` varchar(1024) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(2048) DEFAULT NULL,
  `access_token_validity` int DEFAULT NULL,
  `refresh_token_validity` int DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2客户端信息';

LOCK TABLES `oauth_client_details` WRITE;
/*!40000 ALTER TABLE `oauth_client_details` DISABLE KEYS */;

INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `resource_ids`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`)
VALUES
	('7gBZcbsC7kLIWCdELIl8nxcs','$2a$10$4di0sSQdr9yk4uTKWtZqzedsxI8sWXoR67x.G.Qmy4K2L7ZaFQt6W','','userProfile','authorization_code,client_credentials,password','http://localhost:8888/login,http://localhost:8888/webjars/springfox-swagger-ui/o2c.html,http://www.baidu.com','',43200,2592000,'{\"website\":\"http://www.baidu.com\",\"apiKey\":\"7gBZcbsC7kLIWCdELIl8nxcs\",\"secretKey\":\"0osTIhce7uPvDKHz6aa67bhCukaKoYl4\",\"appName\":\"平台用户认证服务器\",\"updateTime\":1562841065000,\"isPersist\":1,\"appOs\":\"\",\"appIcon\":\"\",\"developerId\":0,\"createTime\":1542016125000,\"appType\":\"server\",\"appDesc\":\"资源服务器\",\"appId\":\"1552274783265\",\"appNameEn\":\"cloud-owl\",\"status\":1}',''),
	('rOOM15Zbc3UFWgW2h71gRFvi','$2a$10$NSb94sKA5i9kS/F0mUBehuBs9Gtvlv8wdxQOYMg3WxMRt0nyP2Xn.','','userProfile','authorization_code,client_credentials,password','http://localhost:2222/login','',43200,2592000,'{\"website\":\"http://www.baidu.com\",\"apiKey\":\"rOOM15Zbc3UFWgW2h71gRFvi\",\"secretKey\":\"2Iw2B0UCMYd1cZjt8Fpr4KJUx75wQCwW\",\"appName\":\"SSO单点登录DEMO\",\"updateTime\":1568611165000,\"isPersist\":1,\"appOs\":\"\",\"appIcon\":\"\",\"developerId\":0,\"createTime\":1542016125000,\"appType\":\"pc\",\"appDesc\":\"sso\",\"appId\":\"1552294656514\",\"appNameEn\":\"sso-demo\",\"status\":1}','');

/*!40000 ALTER TABLE `oauth_client_details` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table oauth_client_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `oauth_client_token`;

CREATE TABLE `oauth_client_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(128) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2客户端令牌';



# Dump of table oauth_code
# ------------------------------------------------------------

DROP TABLE IF EXISTS `oauth_code`;

CREATE TABLE `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2授权码';



# Dump of table oauth_refresh_token
# ------------------------------------------------------------

DROP TABLE IF EXISTS `oauth_refresh_token`;

CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='oauth2刷新令牌';



# Dump of table rbac_element
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_element`;

CREATE TABLE `rbac_element` (
  `element_id` bigint unsigned NOT NULL COMMENT '页面元素ID',
  `element_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '页面元素名',
  `element_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '页面元素编码',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='页面元素表（对应页面按钮）';

LOCK TABLES `rbac_element` WRITE;
/*!40000 ALTER TABLE `rbac_element` DISABLE KEYS */;

INSERT INTO `rbac_element` (`element_id`, `element_name`, `element_code`, `created_date`, `last_modified_date`)
VALUES
	(1131849293404176385,'查看','systemMenuView','2020-08-11 10:12:03',NULL),
	(1131849510572654593,'编辑','systemMenuEdit','2020-08-11 10:12:03',NULL),
	(1131858946338992129,'查看','systemRoleView','2020-08-11 10:12:03',NULL),
	(1131863248310775809,'编辑','systemRoleEdit','2020-08-11 10:12:03',NULL),
	(1131863723722551297,'查看','systemAppView','2020-08-11 10:12:03',NULL),
	(1131863775899693057,'编辑','systemAppEdit','2020-08-11 10:12:03',NULL),
	(1131864400507056130,'查看','systemUserView','2020-08-11 10:12:03',NULL),
	(1131864444878598146,'编辑','systemUserEdit','2020-08-11 10:12:03',NULL),
	(1131864827252322305,'查看','gatewayIpLimitView','2020-08-11 10:12:03',NULL),
	(1131864864267055106,'编辑','gatewayIpLimitEdit','2020-08-11 10:12:03',NULL),
	(1131865040289411074,'查看','gatewayRouteView','2020-08-11 10:12:03',NULL),
	(1131865075609645057,'编辑','gatewayRouteEdit','2020-08-11 10:12:03',NULL),
	(1131865482314526722,'查看','systemApiView','2020-08-11 10:12:03',NULL),
	(1131865520738545666,'编辑','systemApiEdit','2020-08-11 10:12:03',NULL),
	(1131865772929462274,'查看','accessLogView','2020-08-11 10:12:03',NULL),
	(1131865931146997761,'查看','gatewayRateLimitView','2020-08-11 10:12:03',NULL),
	(1131865974704844802,'编辑','gatewayRateLimitEdit','2020-08-11 10:12:03',NULL),
	(1164422088547635202,'查看','developerView','2020-08-11 10:12:03',NULL),
	(1164422211189084162,'编辑','developerEdit','2020-08-11 10:12:03',NULL);

/*!40000 ALTER TABLE `rbac_element` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_group
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_group`;

CREATE TABLE `rbac_group` (
  `group_id` bigint unsigned NOT NULL COMMENT '组织ID',
  `group_parent_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '组织父级ID',
  `group_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '组织名',
  `group_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '组织类型（g-集团，c-公司，d-部门，t-小组）',
  `group_state` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '10' COMMENT '组织状态（10-启用，20-禁用，30-锁定）',
  `exist_child` tinyint(1) NOT NULL DEFAULT '0' COMMENT '组织是否存在子节点（0-不存在 1-存在）',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织表';

LOCK TABLES `rbac_group` WRITE;
/*!40000 ALTER TABLE `rbac_group` DISABLE KEYS */;

INSERT INTO `rbac_group` (`group_id`, `group_parent_id`, `group_name`, `group_type`, `group_state`, `exist_child`, `created_date`, `last_modified_date`)
VALUES
	(1,0,'浙江吉利控股集团','g','10',1,'2020-06-23 09:23:20','2020-06-23 09:25:19'),
	(2,0,'杭州冒险元素网络技术有限公司','c','10',1,'2020-06-23 09:24:48','2020-06-23 09:25:31'),
	(3,1,'杭州的蓝科技有限公司','c','20',0,'2020-06-23 09:27:29',NULL),
	(4,2,'总经办','d','30',0,'2020-06-23 09:30:30',NULL),
	(5,2,'财务部','d','20',0,'2020-06-23 09:31:00',NULL),
	(6,2,'技术部','d','10',1,'2020-06-23 09:31:59',NULL),
	(7,6,'产品部','d','10',0,'2020-06-23 09:33:23',NULL),
	(8,6,'技术研发中心','d','10',0,'2020-09-23 09:33:49',NULL),
	(9,2,'金融科技事业部','d','10',1,'2020-09-23 09:43:24',NULL),
	(10,9,'印度贷超组','t','10',0,'2020-07-23 10:08:34',NULL),
	(121053842608488448,9,'印度现金贷组','t','10',0,'2020-07-23 10:23:23',NULL);

/*!40000 ALTER TABLE `rbac_group` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_group_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_group_role`;

CREATE TABLE `rbac_group_role` (
  `group_id` bigint unsigned NOT NULL COMMENT '组织ID',
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `UK_GROUP_ROLE` (`group_id`,`role_id`),
  KEY `FK_GR_ROLE` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织角色关联表';

LOCK TABLES `rbac_group_role` WRITE;
/*!40000 ALTER TABLE `rbac_group_role` DISABLE KEYS */;

INSERT INTO `rbac_group_role` (`group_id`, `role_id`, `created_date`, `last_modified_date`)
VALUES
	(9,1,'2020-08-04 16:59:59',NULL);

/*!40000 ALTER TABLE `rbac_group_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_group_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_group_user`;

CREATE TABLE `rbac_group_user` (
  `group_id` bigint unsigned NOT NULL COMMENT '组织ID',
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `UK_GROUP_USER` (`group_id`,`user_id`),
  KEY `FK_GU_USER` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织用户关联表';

LOCK TABLES `rbac_group_user` WRITE;
/*!40000 ALTER TABLE `rbac_group_user` DISABLE KEYS */;

INSERT INTO `rbac_group_user` (`group_id`, `user_id`, `created_date`, `last_modified_date`)
VALUES
	(9,1,'2020-08-04 17:00:59',NULL),
	(9,557063237640650752,'2020-08-04 17:18:28',NULL),
	(9,557063237640650753,'2020-08-04 17:31:38',NULL),
	(9,557063237640650754,'2020-08-04 17:34:27',NULL),
	(9,557063237640650755,'2020-08-04 17:34:31',NULL),
	(9,557063237640650756,'2020-08-04 17:34:36',NULL),
	(9,557063237640650757,'2020-08-04 17:34:40',NULL),
	(9,557063237640650758,'2020-08-04 17:34:43',NULL),
	(9,557063237640650759,'2020-08-04 17:34:48',NULL),
	(9,557063237640650760,'2020-08-04 17:34:52',NULL),
	(9,557063237640650761,'2020-08-04 17:34:57',NULL),
	(9,557063237640650762,'2020-08-04 17:35:16',NULL);

/*!40000 ALTER TABLE `rbac_group_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_menu`;

CREATE TABLE `rbac_menu` (
  `menu_id` bigint unsigned NOT NULL COMMENT '菜单ID',
  `menu_parent_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '菜单父级ID',
  `menu_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单名',
  `menu_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单编码',
  `menu_icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `menu_schema` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '/' COMMENT '菜单打开方式（/-路由，http://-HTTP，https://-HTTPs）',
  `menu_path` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单路径',
  `menu_target` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '_self' COMMENT '菜单窗口目标（_self-当前标签，_blank-新标签）',
  `menu_state` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '10' COMMENT '菜单状态（10-启用，20-禁用，30-锁定）',
  `menu_sorted` int NOT NULL DEFAULT '0' COMMENT '菜单排序',
  `exist_child` tinyint(1) NOT NULL DEFAULT '0' COMMENT '菜单是否存在子节点（0-不存在 1-存在）',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`menu_id`),
  UNIQUE KEY `UK_RM_MENU_CODE` (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

LOCK TABLES `rbac_menu` WRITE;
/*!40000 ALTER TABLE `rbac_menu` DISABLE KEYS */;

INSERT INTO `rbac_menu` (`menu_id`, `menu_parent_id`, `menu_name`, `menu_code`, `menu_icon`, `menu_schema`, `menu_path`, `menu_target`, `menu_state`, `menu_sorted`, `exist_child`, `created_date`, `last_modified_date`)
VALUES
	(121053842608488401,0,'老菜单','system','logo-buffer','/','','_self','20',1,1,'2020-08-07 11:39:25','2020-08-13 10:25:16'),
	(121053842608488402,121053842608488401,'IP控制','gatewayIpLimit','ios-exit','/','gateway/ip-limit/index','_self','30',3,0,'2020-08-07 11:39:25','2020-08-12 09:20:05'),
	(121053842608488404,122534432100843520,'路由','accessRoute','ios-wifi','/','access/route/index','_self','10',10,0,'2020-08-07 11:39:25','2020-08-12 10:24:12'),
	(121053842608488406,121053842608488401,'角色管理','systemRole','md-people','/','system/role/index','_self','20',0,0,'2020-08-07 11:39:25',NULL),
	(121053842608488407,121053842608488401,'应用管理','systemApp','md-apps','/','system/app/index','_self','20',0,0,'2020-08-07 11:39:25',NULL),
	(121053842608488408,121053842608488401,'用户管理','systemUser','md-person','/','system/user/index','_self','20',0,0,'2020-08-07 11:39:25',NULL),
	(121053842608488409,122534432100843520,'调试','accessDebug','ios-rewind','http://','127.0.0.1:8888/doc.html','_self','10',12,0,'2020-08-07 11:39:25','2020-08-12 10:23:53'),
	(121053842608488410,122534432100843520,'日志','accessLog','ios-book','/','access/log/index','_self','10',11,0,'2020-08-07 11:39:25','2020-08-12 10:23:37'),
	(121053842608488412,121053842608488401,'流量控制','gatewayRateLimit','md-pulse','/','gateway/rate-limit/index','_self','30',2,0,'2020-08-07 11:39:25','2020-08-12 09:19:59'),
	(122534432100843520,0,'访问控制','access','md-key','/','','_self','10',3,1,'2020-08-07 11:39:25','2020-08-11 09:53:07'),
	(122535792368156672,122534432100843520,'元素','accessElement','ios-rewind','/','access/element/index','_self','10',7,0,'2020-08-07 11:39:25','2020-08-11 11:39:14'),
	(122535848070610944,122534432100843520,'菜单','accessMenu','md-list','/','access/menu/index','_self','10',1,0,'2020-08-07 11:39:25',NULL),
	(122535897334808576,122534432100843520,'操作','accessOperation','ios-exit','/','access/operation/index','_self','10',6,0,'2020-08-07 11:39:25','2020-08-11 11:38:46'),
	(122535924717322240,122534432100843520,'角色','accessRole','md-people','/','access/role/index','_self','10',3,0,'2020-08-07 11:39:25','2020-08-12 10:49:37'),
	(122535938061500416,122534432100843520,'用户','accessUser','md-person','/','access/user/index','_self','10',4,0,'2020-08-07 11:39:25','2020-08-12 10:49:29'),
	(122535954421383168,122534432100843520,'机构','accessGroup','ios-cube','/','access/group/index','_self','10',2,0,'2020-08-07 11:39:25','2020-08-12 10:49:44'),
	(123444448041369600,122534432100843520,'权限','accessPrivilege','ios-alert','/','access/privilege/index','_self','10',5,0,'2020-08-07 11:39:25','2020-08-12 10:49:22');

/*!40000 ALTER TABLE `rbac_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_operation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_operation`;

CREATE TABLE `rbac_operation` (
  `operation_id` bigint unsigned NOT NULL COMMENT '操作ID',
  `operation_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作编码',
  `operation_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作名',
  `operation_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作描述',
  `operation_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作拦截的URL前缀',
  `operation_method` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作方法类型（GET，POST，PUT，DELETE等）',
  `operation_auth` tinyint(1) NOT NULL DEFAULT '1' COMMENT '操作是否需要认证（0不需要 1需要）',
  `operation_open` tinyint(1) NOT NULL DEFAULT '1' COMMENT '操作是否对外开放（0不开放 1开放）',
  `operation_state` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '10' COMMENT '操作状态（10-启用，20-禁用，30-锁定）',
  `operation_service_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '操作所属服务ID',
  `operation_method_name` varchar(32) DEFAULT NULL COMMENT '操作方法名',
  `operation_content_type` varchar(32) DEFAULT NULL COMMENT '操作媒体类型',
  `operation_class_name` varchar(128) DEFAULT NULL COMMENT '操作全限定类名',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='功能操作表';

LOCK TABLES `rbac_operation` WRITE;
/*!40000 ALTER TABLE `rbac_operation` DISABLE KEYS */;

INSERT INTO `rbac_operation` (`operation_id`, `operation_code`, `operation_name`, `operation_desc`, `operation_path`, `operation_method`, `operation_auth`, `operation_open`, `operation_state`, `operation_service_id`, `operation_method_name`, `operation_content_type`, `operation_class_name`, `created_date`, `last_modified_date`)
VALUES
	(125078546809880576,'4e2f81cff73bf0b5e31f03ad22bd373c','用户:菜单','用户:菜单','/access/current/menus','GET',1,1,'10','cloud-hippo','currentUserMenus',NULL,'CurrentController','2020-08-14 15:14:59',NULL),
	(125078546864406528,'c118eb940bb34e2479ffd2f65db76dbb','元素:删除','元素:删除','/access/elements/{elementId}','DELETE',1,1,'10','cloud-hippo','removeElement',NULL,'ElementController','2020-08-14 15:14:59',NULL),
	(125078546868600832,'8d0b9e73d40b1dd7256f50bf55441489','元素:分页','元素:分页','/access/elements/page','GET',1,1,'10','cloud-hippo','getElementsPage',NULL,'ElementController','2020-08-14 15:14:59',NULL),
	(125078546870697984,'09eb7b1d0db3670caa1d08588d2fdda0','元素:添加','元素:添加','/access/elements','POST',1,1,'10','cloud-hippo','createElement','application/json','ElementController','2020-08-14 15:14:59',NULL),
	(125078546879086592,'a1c265ad78e57e1272ab44114bd42551','元素:修改','元素:修改','/access/elements/{elementId}','PUT',1,1,'10','cloud-hippo','modifyElement','application/json','ElementController','2020-08-14 15:14:59',NULL),
	(125078546883280896,'f9a74e2e312b17f47cae61c3ba9a6aae','元素:详情','元素:详情','/access/elements/{elementId}','GET',1,1,'10','cloud-hippo','viewElement',NULL,'ElementController','2020-08-14 15:14:59',NULL),
	(125078546887475200,'9c1630b2f42daa876e267a05be442c15','组织:添加','组织:添加','/access/groups','POST',1,1,'10','cloud-hippo','createGroup','application/json','GroupController','2020-08-14 15:14:59',NULL),
	(125078546889572352,'c976710b1be1bb4650accd6f3852c6b0','组织:子级:查询','组织:子级:查询','/access/groups/children','GET',1,1,'10','cloud-hippo','getGroupChildren',NULL,'GroupController','2020-08-14 15:14:59',NULL),
	(125078546895863808,'efed71e1abe7d48fcc95369bc949c2de','组织:修改','组织:修改','/access/groups/{groupId}','PUT',1,1,'10','cloud-hippo','modifyGroup','application/json','GroupController','2020-08-14 15:14:59',NULL),
	(125078546897960960,'137e872eb081b61250168ec4fa865d66','组织:删除','组织:删除','/access/groups/{groupId}','DELETE',1,1,'10','cloud-hippo','removeGroup',NULL,'GroupController','2020-08-14 15:14:59',NULL),
	(125078546900058112,'03929f90e83ba009e8c70af8f3993601','组织:详情','组织:详情','/access/groups/{groupId}','GET',1,1,'10','cloud-hippo','viewGroup',NULL,'GroupController','2020-08-14 15:14:59',NULL),
	(125078546902155264,'fdc85507f9d8f5cae603b6de7b22cabc','组织:角色:所有','查询组织{groupId}下的直接角色（不查询下下级数据）','/access/groups/{groupId}/roles','GET',1,1,'10','cloud-hippo','getGroupRoles',NULL,'GroupController','2020-08-14 15:14:59',NULL),
	(125078546906349568,'f0888a929da37bb9a9fae1dba4df88a3','组织:角色:分配','为组织{groupId}分配角色','/access/groups/{groupId}/roles','POST',1,1,'10','cloud-hippo','grantRole','application/json','GroupController','2020-08-14 15:14:59',NULL),
	(125078546910543872,'4ca357323dad28cefe754a5fe76470a2','组织:角色:删除','移除组织{groupId}已分配的角色{roleIds}','/access/groups/{groupId}/roles/{roleIds}','DELETE',1,1,'10','cloud-hippo','removeGroupRole',NULL,'GroupController','2020-08-14 15:14:59',NULL),
	(125078546912641024,'1263c3b9994543a7c39a0337cacc9f60','组织:用户:所有','查询组织{groupId}下的直接用户（不查询下下级数据）','/access/groups/{groupId}/users','GET',1,1,'10','cloud-hippo','getGroupUsers',NULL,'GroupController','2020-08-14 15:14:59',NULL),
	(125078546914738176,'b723c0129749a58828fe1bacd714a188','组织:用户:分配','为组织{groupId}分配用户','/access/groups/{groupId}/users','POST',1,1,'10','cloud-hippo','grantUser','application/json','GroupController','2020-08-14 15:14:59',NULL),
	(125078546921029632,'744202a6ccfe4b986831368958484dd8','组织:用户:删除','移除组织{groupId}已分配的用户{userIds}','/access/groups/{groupId}/users/{userIds}','DELETE',1,1,'10','cloud-hippo','removeGroupUser',NULL,'GroupController','2020-08-14 15:14:59',NULL),
	(125078546923126784,'0170ef9539fc147b3bc197ae5e9679ca','菜单:添加','菜单:添加','/access/menus','POST',1,1,'10','cloud-hippo','createMenu','application/json','MenuController','2020-08-14 15:14:59',NULL),
	(125078546925223936,'146a8960ced1af219c51a47a9ab7843c','菜单:修改','菜单:修改','/access/menus/{menuId}','PUT',1,1,'10','cloud-hippo','modifyMenu','application/json','MenuController','2020-08-14 15:14:59',NULL),
	(125078546925223937,'4275231ef41613b53390f3827b15e5ed','菜单:移除','菜单:移除','/access/menus/{menuId}','DELETE',1,1,'10','cloud-hippo','removeMenu',NULL,'MenuController','2020-08-14 15:14:59',NULL),
	(125078546927321088,'1eaad2eaa1e5a0b89c8c531cf1c8dd9b','菜单:查询','菜单:查询','/access/menus','GET',1,1,'10','cloud-hippo','getMenus',NULL,'MenuController','2020-08-14 15:14:59',NULL),
	(125078546929418240,'0685a29ce64c969c49be9ff1bef4d9f0','菜单:详情','菜单:详情','/access/menus/{menuId}','GET',1,1,'10','cloud-hippo','viewMenu',NULL,'MenuController','2020-08-14 15:14:59',NULL),
	(125078546931515392,'e0f37f532f4f1f2cc3c049136fe1c73c','菜单:子级:查询','菜单:子级:查询','/access/menus/children','GET',1,1,'10','cloud-hippo','getMenuChildren',NULL,'MenuController','2020-08-14 15:14:59',NULL),
	(125078546933612544,'05175f93c366f66c2d20cd5b8bb5e070','操作:分页','操作:分页','/access/operations/page','GET',1,1,'10','cloud-hippo','getOperationsPage',NULL,'OperationController','2020-08-14 15:14:59',NULL),
	(125078546935709696,'a1ca2b91f151ec48bb7ec4cfd2d97947','权限:分页','权限:分页','/access/privileges/page','GET',1,1,'10','cloud-hippo','getPrivilegesPage',NULL,'PrivilegeController','2020-08-14 15:14:59',NULL),
	(125078546937806848,'24326c0e250ae3b701fa1b5ffda7f012','权限:操作:所有','权限:操作:所有','/access/privileges/operations','GET',1,1,'10','cloud-hippo','getPrivilegeOperations',NULL,'PrivilegeController','2020-08-14 15:14:59',NULL),
	(125078546939904000,'8ddc34bd10956a694a66f901671807be','角色:所有','角色:所有','/access/roles','GET',1,1,'10','cloud-hippo','getRoles',NULL,'RoleController','2020-08-14 15:14:59',NULL),
	(125078546942001152,'be2395ac85f120d57ef7bcca61aab8dc','角色:详情','角色:详情','/access/roles/{roleId}','GET',1,1,'10','cloud-hippo','viewRole',NULL,'RoleController','2020-08-14 15:14:59',NULL),
	(125078546944098304,'6a0b79ab392a0070fadc3db306f137ba','角色:分页','角色:分页','/access/roles/page','GET',1,1,'10','cloud-hippo','getRolesPage',NULL,'RoleController','2020-08-14 15:14:59',NULL),
	(125078546946195456,'ddb31b8e46376cef6325fdd3945286a4','角色:添加','角色:添加','/access/roles','POST',1,1,'10','cloud-hippo','createRole','application/json','RoleController','2020-08-14 15:14:59',NULL),
	(125078546948292608,'b80ecc7ee0204c27535db16dc15bb577','角色:修改','角色:修改','/access/roles/{roleId}','PUT',1,1,'10','cloud-hippo','modifyRole','application/json','RoleController','2020-08-14 15:14:59',NULL),
	(125078546950389760,'d5d724fa5e8fc3bd6400477340ab9ca3','角色:删除','角色:删除','/access/roles/{roleId}','DELETE',1,1,'10','cloud-hippo','removeRole',NULL,'RoleController','2020-08-14 15:14:59',NULL),
	(125078546952486912,'4bbc15f16d46d40d65e3504db55107e3','角色:权限:分配','为角色{roleId}授予权限','/access/roles/{roleId}/privileges','POST',1,1,'10','cloud-hippo','grantPrivilege','application/json','RoleController','2020-08-14 15:14:59',NULL),
	(125078546954584064,'64142cac513dd74694850c1f4d154c4f','角色:权限:删除','移除角色{roleId}已授权的权限{privilegeIds}','/access/roles/{roleId}/privileges/{privilegeIds}','DELETE',1,1,'10','cloud-hippo','removeRolePrivilege',NULL,'RoleController','2020-08-14 15:14:59',NULL),
	(125078546958778368,'d0fb82b64d8d36302015d4516fedbbef','用户:角色:分配','为用户{userId}分配角色','/access/users/{userId}/roles','POST',1,1,'10','cloud-hippo','grantRole','application/json','UserController','2020-08-14 15:14:59',NULL),
	(125078546960875520,'e23b6bf672ddf0630aaac5d2365bd0d9','用户:详情','用户:详情','/access/users/{userId}','GET',1,1,'10','cloud-hippo','viewUser',NULL,'UserController','2020-08-14 15:14:59',NULL),
	(125078546962972672,'bc918cd7c36ed2efc0a69c2e15a7b62c','用户:分页','用户:分页','/access/users/page','GET',1,1,'10','cloud-hippo','getUsersPage',NULL,'UserController','2020-08-14 15:14:59',NULL),
	(125078546965069824,'905c2a9f0ee87310ff9f8242e490ca34','用户:添加','用户:添加','/access/users','POST',1,1,'10','cloud-hippo','createUser','application/json','UserController','2020-08-14 15:14:59',NULL),
	(125078546967166976,'08faa0623f230ea05e2dc2c992326f52','用户:修改','用户:修改','/access/users/{userId}','PUT',1,1,'10','cloud-hippo','modifyUser','application/json','UserController','2020-08-14 15:14:59',NULL),
	(125078546967166977,'fe56915d206f3430047158409cbf61e9','用户:删除','用户:删除','/access/users/{userId}','DELETE',1,1,'10','cloud-hippo','removeUser',NULL,'UserController','2020-08-14 15:14:59',NULL),
	(125078546969264128,'231d1aed64acf45eeb924f3aeda56c95','用户:角色:删除','移除用户{userId}已分配的角色{roleIds}','/access/users/{userId}/roles/{roleIds}','DELETE',1,1,'10','cloud-hippo','removeUserRole',NULL,'UserController','2020-08-14 15:14:59',NULL),
	(125078546971361280,'3b929ad9cf59a9be201b5da8560ca0bb','获取路由列表','仅限内部调用','/gateway/api/route','GET',1,1,'10','cloud-hippo','getApiRouteList',NULL,'GatewayController','2020-08-14 15:14:59',NULL),
	(125078546973458432,'71651a32a7afc413511bebae5dba7cbf','获取服务列表','获取服务列表','/gateway/service/list','GET',1,1,'10','cloud-hippo','getServiceList',NULL,'GatewayController','2020-08-14 15:14:59',NULL),
	(125078546973458433,'85d1c0a5889c8d4aa6851c269213a7f5','获取接口黑名单列表','仅限内部调用','/gateway/api/blackList','GET',1,1,'10','cloud-hippo','getApiBlackList',NULL,'GatewayController','2020-08-14 15:14:59',NULL),
	(125078546975555584,'81758299d27614a71a28c5240a12dd05','获取接口白名单列表','仅限内部调用','/gateway/api/whiteList','GET',1,1,'10','cloud-hippo','getApiWhiteList',NULL,'GatewayController','2020-08-14 15:14:59',NULL),
	(125078546977652736,'229f93d41062ffa1db00067ce821b51f','获取限流列表','仅限内部调用','/gateway/api/rateLimit','GET',1,1,'10','cloud-hippo','getApiRateLimitList',NULL,'GatewayController','2020-08-14 15:14:59',NULL),
	(125078546979749888,'c6753c50c23b2f83e2bb8d4dc15ccf04','绑定API','一个API只能绑定一个策略','/gateway/limit/ip/api/add','POST',1,1,'10','cloud-hippo','addIpLimitApis','application/json','GatewayIpLimitController','2020-08-14 15:14:59',NULL),
	(125078546979749889,'7d66303521e2f0e2f8110ded78751a62','获取分页接口列表','获取分页接口列表','/gateway/limit/ip','GET',1,1,'10','cloud-hippo','getIpLimitListPage',NULL,'GatewayIpLimitController','2020-08-14 15:14:59',NULL),
	(125078546981847040,'2bf0a8a800631792a3abba282254c28e','获取IP限制','获取IP限制','/gateway/limit/ip/{policyId}/info','GET',1,1,'10','cloud-hippo','getIpLimit',NULL,'GatewayIpLimitController','2020-08-14 15:14:59',NULL),
	(125078546983944192,'96527a7b57549c1b231ff22b31bd8aa8','查询策略已绑定API列表','获取分页接口列表','/gateway/limit/ip/api/list','GET',1,1,'10','cloud-hippo','getIpLimitApiList',NULL,'GatewayIpLimitController','2020-08-14 15:14:59',NULL),
	(125078546986041344,'0222fbe92b2fd4ecc94cec6bb2099ac8','添加IP限制','添加IP限制','/gateway/limit/ip/add','POST',1,1,'10','cloud-hippo','addIpLimit','application/json','GatewayIpLimitController','2020-08-14 15:14:59',NULL),
	(125078546988138496,'0790dd17fcf03d37efd0278a93be5ee3','编辑IP限制','编辑IP限制','/gateway/limit/ip/update','POST',1,1,'10','cloud-hippo','updateIpLimit','application/json','GatewayIpLimitController','2020-08-14 15:14:59',NULL),
	(125078546990235648,'ccdd9063950747d136b53538f8d28aa1','移除IP限制','移除IP限制','/gateway/limit/ip/remove','POST',1,1,'10','cloud-hippo','removeIpLimit','application/json','GatewayIpLimitController','2020-08-14 15:14:59',NULL),
	(125078546990235649,'3d2dbcacf8a7345a1217e5e0f870cc16','绑定API','一个API只能绑定一个策略','/gateway/limit/rate/api/add','POST',1,1,'10','cloud-hippo','addRateLimitApis','application/json','GatewayRateLimitController','2020-08-14 15:14:59',NULL),
	(125078546992332800,'07f74808c90cff97baa281c0a070b217','获取分页接口列表','获取分页接口列表','/gateway/limit/rate','GET',1,1,'10','cloud-hippo','getRateLimitListPage',NULL,'GatewayRateLimitController','2020-08-14 15:14:59',NULL),
	(125078546992332801,'e28717f192dd23e8aa4bd238598cf492','获取流量控制','获取流量控制','/gateway/limit/rate/{policyId}/info','GET',1,1,'10','cloud-hippo','getRateLimit',NULL,'GatewayRateLimitController','2020-08-14 15:14:59',NULL),
	(125078546994429952,'3101bda4ba15d219c82d521c66723a3c','查询策略已绑定API列表','获取分页接口列表','/gateway/limit/rate/api/list','GET',1,1,'10','cloud-hippo','getRateLimitApiList',NULL,'GatewayRateLimitController','2020-08-14 15:14:59',NULL),
	(125078546994429953,'21be8d5ec22554d34d1afcc73bcae1d0','添加流量控制','添加流量控制','/gateway/limit/rate/add','POST',1,1,'10','cloud-hippo','addRateLimit','application/json','GatewayRateLimitController','2020-08-14 15:14:59',NULL),
	(125078546996527104,'8592603f23cee68c3d80d6d03e0ea464','编辑流量控制','编辑流量控制','/gateway/limit/rate/update','POST',1,1,'10','cloud-hippo','updateRateLimit','application/json','GatewayRateLimitController','2020-08-14 15:14:59',NULL),
	(125078546996527105,'acefe752de49adf8a19191dd34ae7462','移除流量控制','移除流量控制','/gateway/limit/rate/remove','POST',1,1,'10','cloud-hippo','removeRateLimit','application/json','GatewayRateLimitController','2020-08-14 15:14:59',NULL),
	(125078546998624256,'f2796f8c3f7d2a76c6ffb44bdebb089d','添加路由','添加路由','/gateway/route/add','POST',1,1,'10','cloud-hippo','addRoute','application/json','GatewayRouteController','2020-08-14 15:14:59',NULL),
	(125078546998624257,'a96ebf65a4cf965d7071cddc8546db36','获取路由','获取路由','/gateway/route/{routeId}/info','GET',1,1,'10','cloud-hippo','getRoute',NULL,'GatewayRouteController','2020-08-14 15:14:59',NULL),
	(125078547000721408,'02452c47c21bd9abb6b4a6bb6cd0c40b','编辑路由','编辑路由','/gateway/route/update','POST',1,1,'10','cloud-hippo','updateRoute','application/json','GatewayRouteController','2020-08-14 15:14:59',NULL),
	(125078547002818560,'b7d0c7e22133e6e6d74fb4c92fe5c305','移除路由','移除路由','/gateway/route/remove','POST',1,1,'10','cloud-hippo','removeRoute','application/json','GatewayRouteController','2020-08-14 15:14:59',NULL),
	(125078547002818561,'de68daccdad732b06125bd63bc53f18a','获取分页路由列表','获取分页路由列表','/gateway/route','GET',1,1,'10','cloud-hippo','getRouteListPage',NULL,'GatewayRouteController','2020-08-14 15:14:59',NULL),
	(125078547004915712,'65ce95a5dc24323c56595471f24b31c2','日志:分页','日志:分页','/gateways/logs/page','GET',1,1,'10','cloud-hippo','getLogsPage',NULL,'LogController','2020-08-14 15:14:59',NULL),
	(125078547007012864,'c2246d82087ba660c2ef59c3a4709112','路由:分页','路由:分页','/gateways/routes/page','GET',1,1,'10','cloud-hippo','getRoutesPage',NULL,'RouteController','2020-08-14 15:14:59',NULL),
	(125078547011207168,'3c157d0fefd3876e991f1b4a706b3533','获取应用详情','获取应用详情','/app/{appId}/info','GET',1,1,'10','cloud-hippo','getApp',NULL,'BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547011207169,'3b085dde2c79cbaff46930fe48c8d148','获取分页应用列表','获取分页应用列表','/app','GET',1,1,'10','cloud-hippo','getAppListPage',NULL,'BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547013304320,'63a1f29ac6b56e20d0dfb36cc747e7fa','添加应用信息','添加应用信息','/app/add','POST',1,1,'10','cloud-hippo','addApp','application/json','BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547013304321,'cc6ca49a6b9032707dc3f9659fb69f19','编辑应用信息','编辑应用信息','/app/update','POST',1,1,'10','cloud-hippo','updateApp','application/json','BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547015401472,'84d3b696241c9cd4f96b8cab5884c198','重置应用秘钥','重置应用秘钥','/app/reset','POST',1,1,'10','cloud-hippo','resetAppSecret','application/json','BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547015401473,'bd24c975052bcf5853a295830019343a','获取应用开发配置信息','获取应用开发配置信息','/app/client/{clientId}/info','GET',1,1,'10','cloud-hippo','getAppClientInfo',NULL,'BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547017498624,'ffe74bf17bd9fa147f911058e7b85908','完善应用开发信息','完善应用开发信息','/app/client/update','POST',1,1,'10','cloud-hippo','updateAppClientInfo','application/json','BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547017498625,'95af25bf3b92a1dc1b903580fc636b09','删除应用信息','删除应用信息','/app/remove','POST',1,1,'10','cloud-hippo','removeApp','application/json','BaseAppController','2020-08-14 15:14:59',NULL),
	(125078547019595776,'9c16d32671ab7ba1df187e91aac1fbf8','添加开发者用户','添加开发者用户','/developer/add','POST',1,1,'10','cloud-hippo','addUser','application/json','BaseDeveloperController','2020-08-14 15:14:59',NULL),
	(125078547019595777,'3b4c424e1c4f7bcc12ebfbb85bbac57a','获取账号登录信息','仅限系统内部调用','/developer/login','GET',1,1,'10','cloud-hippo','developerLogin',NULL,'BaseDeveloperController','2020-08-14 15:14:59',NULL),
	(125078547021692928,'bb7a6f175df455345744979e792bc1d4','系统分页用户列表','系统分页用户列表','/developer','GET',1,1,'10','cloud-hippo','getUserList',NULL,'BaseDeveloperController','2020-08-14 15:14:59',NULL),
	(125078547021692929,'84fc170c3c485022664193720b2bb2b0','获取所有用户列表','获取所有用户列表','/developer/all','GET',1,1,'10','cloud-hippo','getUserAllList',NULL,'BaseDeveloperController','2020-08-14 15:14:59',NULL),
	(125078547023790080,'228ad858fae386424ed55dba5e65fb12','注册第三方系统登录账号','仅限系统内部调用','/developer/add/thirdParty','POST',1,1,'10','cloud-hippo','addDeveloperThirdParty','application/json','BaseDeveloperController','2020-08-14 15:14:59',NULL),
	(125078547023790081,'4659a4e8ef327885d1b10aa723f544ce','更新开发者用户','更新开发者用户','/developer/update','POST',1,1,'10','cloud-hippo','updateUser','application/json','BaseDeveloperController','2020-08-14 15:14:59',NULL),
	(125078547025887232,'8bfdca0a64e1d90c9023c251d79afcba','修改用户密码','修改用户密码','/developer/update/password','POST',1,1,'10','cloud-hippo','updatePassword','application/json','BaseDeveloperController','2020-08-14 15:14:59',NULL),
	(125078547025887233,'2e8537eec0d51372cfbb950a9d0ccc2a','获取账号登录信息','仅限系统内部调用','/user/login','GET',1,1,'10','cloud-hippo','userLogin',NULL,'CurrentUserController','2020-08-14 15:14:59',NULL),
	(125078547027984384,'e442bc7f1a745b154b7bd924359241f8','修改当前登录用户密码','修改当前登录用户密码','/current/user/rest/password','GET',1,1,'10','cloud-hippo','restPassword',NULL,'CurrentUserController','2020-08-14 15:14:59',NULL),
	(125078547027984385,'ed3e8eee1f986454fdf703e434ace28d','修改当前登录用户基本信息','修改当前登录用户基本信息','/current/user/update','POST',1,1,'10','cloud-hippo','updateUserInfo','application/json','CurrentUserController','2020-08-14 15:14:59',NULL),
	(125078547030081536,'0991b26f72cfeadd101e376aa0066bd3','error',NULL,'/error',NULL,1,1,'10','cloud-hippo','error',NULL,'org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController','2020-08-14 15:14:59',NULL),
	(125078653374562304,'8a5054c51fa298a7b8aaaeaa004bf95a','自定义Oauth2错误处理','自定义Oauth2错误处理','/oauth/error','GET',1,1,'10','cloud-owl','handleError',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthenticationOauth2Controller','2020-08-14 15:15:50',NULL),
	(125078653376659456,'d0411a85044621bded08c5129d7df483','登录回调（QQ）','登录回调（QQ）','/oauth/qq/callback','GET',1,1,'10','cloud-owl','qq',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthenticationOauth2Controller','2020-08-14 15:15:50',NULL),
	(125078653378756608,'a8fdac416e329cc146de4b254a37b537','登录回调（码云）','登录回调（码云）','/oauth/gitee/callback','GET',1,1,'10','cloud-owl','gitee',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthenticationOauth2Controller','2020-08-14 15:15:50',NULL),
	(125078653380853760,'06fc75e9d5fa4ecd92f71c3630cafed9','登录回调（微信）','登录回调（微信）','/oauth/wechat/callback','GET',1,1,'10','cloud-owl','wechat',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthenticationOauth2Controller','2020-08-14 15:15:50',NULL),
	(125078653380853761,'bae23e5b5611d27fea3902a73391b1ba','获取第三方登录配置','任何人都可访问','/login/config','GET',1,1,'10','cloud-owl','getLoginConfig',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthenticationOauth2Controller','2020-08-14 15:15:50',NULL),
	(125078653382950912,'3c305cca7927e9a5731fa45688264228','确认授权','确认授权','/oauth/confirm_access','GET',1,1,'10','cloud-owl','confirm_access',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthenticationOauth2Controller','2020-08-14 15:15:50',NULL),
	(125078653385048064,'dfec819caabc2b642709e71b8af45f74','获取当前登录用户信息-SSO单点登录','获取当前登录用户信息-SSO单点登录','/current/user/sso','GET',1,1,'10','cloud-owl','principal',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthorizationController','2020-08-14 15:15:50',NULL),
	(125078653387145216,'6adef58e99461cd29161959b91e2b53d','获取用户基础信息','获取用户基础信息','/current/user','GET',1,1,'10','cloud-owl','getUserProfile',NULL,'com.smart4y.cloud.owl.interfaces.web.AuthorizationController','2020-08-14 15:15:50',NULL),
	(125078653387145217,'856bf12b90cfffde3383d18aa9701f52','获取用户访问令牌','基于oauth2密码模式登录,无需签名,返回access_token','/login/token','POST',1,1,'10','cloud-owl','getLoginToken','application/json','com.smart4y.cloud.owl.interfaces.web.AuthorizationController','2020-08-14 15:15:50',NULL),
	(125078653391339520,'22f1d3bb3d6f6c56a41178e4aa17739e','退出移除令牌','退出移除令牌','/logout/token','POST',1,1,'10','cloud-owl','removeToken','application/json','com.smart4y.cloud.owl.interfaces.web.AuthorizationController','2020-08-14 15:15:50',NULL),
	(125078653391339521,'466dd22f429d95ddfd2d825783370548','登录页','登录页','/login','GET',1,1,'10','cloud-owl','login',NULL,'com.smart4y.cloud.owl.interfaces.web.IndexController','2020-08-14 15:15:50',NULL),
	(125078653393436672,'792bf200b10acd64b04c6c2c905a2fc5','欢迎页','欢迎页','/','GET',1,1,'10','cloud-owl','welcome',NULL,'com.smart4y.cloud.owl.interfaces.web.IndexController','2020-08-14 15:15:50',NULL),
	(125078653399728128,'7abff68a9f24fd80387f1475b4f78daf','error',NULL,'/error',NULL,1,1,'10','cloud-owl','error',NULL,'org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController','2020-08-14 15:15:50',NULL);

/*!40000 ALTER TABLE `rbac_operation` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_privilege
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_privilege`;

CREATE TABLE `rbac_privilege` (
  `privilege_id` bigint unsigned NOT NULL COMMENT '权限ID',
  `privilege` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '权限标识',
  `privilege_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '权限类型（m-菜单，e-页面元素，o-功能操作）',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限表';

LOCK TABLES `rbac_privilege` WRITE;
/*!40000 ALTER TABLE `rbac_privilege` DISABLE KEYS */;

INSERT INTO `rbac_privilege` (`privilege_id`, `privilege`, `privilege_type`, `created_date`, `last_modified_date`)
VALUES
	(125078022647709696,'systemMenuView','e','2020-08-14 15:10:49',NULL),
	(125078022649806848,'systemMenuEdit','e','2020-08-14 15:10:49',NULL),
	(125078022651904000,'systemRoleView','e','2020-08-14 15:10:49',NULL),
	(125078022651904001,'systemRoleEdit','e','2020-08-14 15:10:49',NULL),
	(125078022654001152,'systemAppView','e','2020-08-14 15:10:49',NULL),
	(125078022654001153,'systemAppEdit','e','2020-08-14 15:10:49',NULL),
	(125078022658195456,'systemUserView','e','2020-08-14 15:10:49',NULL),
	(125078022660292608,'systemUserEdit','e','2020-08-14 15:10:49',NULL),
	(125078022662389760,'gatewayIpLimitView','e','2020-08-14 15:10:49',NULL),
	(125078022662389761,'gatewayIpLimitEdit','e','2020-08-14 15:10:49',NULL),
	(125078022664486912,'gatewayRouteView','e','2020-08-14 15:10:49',NULL),
	(125078022664486913,'gatewayRouteEdit','e','2020-08-14 15:10:49',NULL),
	(125078022666584064,'systemApiView','e','2020-08-14 15:10:49',NULL),
	(125078022666584065,'systemApiEdit','e','2020-08-14 15:10:49',NULL),
	(125078022668681216,'accessLogView','e','2020-08-14 15:10:49',NULL),
	(125078022668681217,'gatewayRateLimitView','e','2020-08-14 15:10:49',NULL),
	(125078022670778368,'gatewayRateLimitEdit','e','2020-08-14 15:10:49',NULL),
	(125078022670778369,'developerView','e','2020-08-14 15:10:49',NULL),
	(125078022672875520,'developerEdit','e','2020-08-14 15:10:49',NULL),
	(125078022756761600,'system','m','2020-08-14 15:10:49',NULL),
	(125078022758858752,'gatewayIpLimit','m','2020-08-14 15:10:49',NULL),
	(125078022758858753,'accessRoute','m','2020-08-14 15:10:49',NULL),
	(125078022758858754,'systemRole','m','2020-08-14 15:10:49',NULL),
	(125078022758858755,'systemApp','m','2020-08-14 15:10:49',NULL),
	(125078022758858756,'systemUser','m','2020-08-14 15:10:49',NULL),
	(125078022760955904,'accessDebug','m','2020-08-14 15:10:49',NULL),
	(125078022760955905,'accessLog','m','2020-08-14 15:10:49',NULL),
	(125078022760955906,'gatewayRateLimit','m','2020-08-14 15:10:49',NULL),
	(125078022763053056,'access','m','2020-08-14 15:10:49',NULL),
	(125078022763053057,'accessElement','m','2020-08-14 15:10:49',NULL),
	(125078022763053058,'accessMenu','m','2020-08-14 15:10:49',NULL),
	(125078022765150208,'accessOperation','m','2020-08-14 15:10:49',NULL),
	(125078022765150209,'accessRole','m','2020-08-14 15:10:49',NULL),
	(125078022765150210,'accessUser','m','2020-08-14 15:10:49',NULL),
	(125078022765150211,'accessGroup','m','2020-08-14 15:10:49',NULL),
	(125078022767247360,'accessPrivilege','m','2020-08-14 15:10:49',NULL),
	(125078547174785024,'4e2f81cff73bf0b5e31f03ad22bd373c','o','2020-08-14 15:14:59',NULL),
	(125078547178979328,'c118eb940bb34e2479ffd2f65db76dbb','o','2020-08-14 15:14:59',NULL),
	(125078547178979329,'8d0b9e73d40b1dd7256f50bf55441489','o','2020-08-14 15:14:59',NULL),
	(125078547181076480,'09eb7b1d0db3670caa1d08588d2fdda0','o','2020-08-14 15:14:59',NULL),
	(125078547183173632,'a1c265ad78e57e1272ab44114bd42551','o','2020-08-14 15:14:59',NULL),
	(125078547183173633,'f9a74e2e312b17f47cae61c3ba9a6aae','o','2020-08-14 15:14:59',NULL),
	(125078547187367936,'9c1630b2f42daa876e267a05be442c15','o','2020-08-14 15:14:59',NULL),
	(125078547187367937,'c976710b1be1bb4650accd6f3852c6b0','o','2020-08-14 15:14:59',NULL),
	(125078547191562240,'efed71e1abe7d48fcc95369bc949c2de','o','2020-08-14 15:14:59',NULL),
	(125078547193659392,'137e872eb081b61250168ec4fa865d66','o','2020-08-14 15:14:59',NULL),
	(125078547193659393,'03929f90e83ba009e8c70af8f3993601','o','2020-08-14 15:14:59',NULL),
	(125078547193659394,'fdc85507f9d8f5cae603b6de7b22cabc','o','2020-08-14 15:14:59',NULL),
	(125078547195756544,'f0888a929da37bb9a9fae1dba4df88a3','o','2020-08-14 15:14:59',NULL),
	(125078547195756545,'4ca357323dad28cefe754a5fe76470a2','o','2020-08-14 15:14:59',NULL),
	(125078547195756546,'1263c3b9994543a7c39a0337cacc9f60','o','2020-08-14 15:14:59',NULL),
	(125078547197853696,'b723c0129749a58828fe1bacd714a188','o','2020-08-14 15:14:59',NULL),
	(125078547197853697,'744202a6ccfe4b986831368958484dd8','o','2020-08-14 15:14:59',NULL),
	(125078547199950848,'0170ef9539fc147b3bc197ae5e9679ca','o','2020-08-14 15:14:59',NULL),
	(125078547199950849,'146a8960ced1af219c51a47a9ab7843c','o','2020-08-14 15:14:59',NULL),
	(125078547199950850,'4275231ef41613b53390f3827b15e5ed','o','2020-08-14 15:14:59',NULL),
	(125078547202048000,'1eaad2eaa1e5a0b89c8c531cf1c8dd9b','o','2020-08-14 15:14:59',NULL),
	(125078547202048001,'0685a29ce64c969c49be9ff1bef4d9f0','o','2020-08-14 15:14:59',NULL),
	(125078547202048002,'e0f37f532f4f1f2cc3c049136fe1c73c','o','2020-08-14 15:14:59',NULL),
	(125078547204145152,'05175f93c366f66c2d20cd5b8bb5e070','o','2020-08-14 15:14:59',NULL),
	(125078547204145153,'a1ca2b91f151ec48bb7ec4cfd2d97947','o','2020-08-14 15:14:59',NULL),
	(125078547204145154,'24326c0e250ae3b701fa1b5ffda7f012','o','2020-08-14 15:14:59',NULL),
	(125078547206242304,'8ddc34bd10956a694a66f901671807be','o','2020-08-14 15:14:59',NULL),
	(125078547206242305,'be2395ac85f120d57ef7bcca61aab8dc','o','2020-08-14 15:14:59',NULL),
	(125078547206242306,'6a0b79ab392a0070fadc3db306f137ba','o','2020-08-14 15:14:59',NULL),
	(125078547208339456,'ddb31b8e46376cef6325fdd3945286a4','o','2020-08-14 15:14:59',NULL),
	(125078547208339457,'b80ecc7ee0204c27535db16dc15bb577','o','2020-08-14 15:14:59',NULL),
	(125078547208339458,'d5d724fa5e8fc3bd6400477340ab9ca3','o','2020-08-14 15:14:59',NULL),
	(125078547210436608,'4bbc15f16d46d40d65e3504db55107e3','o','2020-08-14 15:14:59',NULL),
	(125078547210436609,'64142cac513dd74694850c1f4d154c4f','o','2020-08-14 15:14:59',NULL),
	(125078547210436610,'d0fb82b64d8d36302015d4516fedbbef','o','2020-08-14 15:14:59',NULL),
	(125078547212533760,'e23b6bf672ddf0630aaac5d2365bd0d9','o','2020-08-14 15:14:59',NULL),
	(125078547212533761,'bc918cd7c36ed2efc0a69c2e15a7b62c','o','2020-08-14 15:14:59',NULL),
	(125078547212533762,'905c2a9f0ee87310ff9f8242e490ca34','o','2020-08-14 15:14:59',NULL),
	(125078547214630912,'08faa0623f230ea05e2dc2c992326f52','o','2020-08-14 15:14:59',NULL),
	(125078547214630913,'fe56915d206f3430047158409cbf61e9','o','2020-08-14 15:14:59',NULL),
	(125078547216728064,'231d1aed64acf45eeb924f3aeda56c95','o','2020-08-14 15:14:59',NULL),
	(125078547216728065,'3b929ad9cf59a9be201b5da8560ca0bb','o','2020-08-14 15:14:59',NULL),
	(125078547218825216,'71651a32a7afc413511bebae5dba7cbf','o','2020-08-14 15:14:59',NULL),
	(125078547218825217,'85d1c0a5889c8d4aa6851c269213a7f5','o','2020-08-14 15:14:59',NULL),
	(125078547218825218,'81758299d27614a71a28c5240a12dd05','o','2020-08-14 15:14:59',NULL),
	(125078547218825219,'229f93d41062ffa1db00067ce821b51f','o','2020-08-14 15:14:59',NULL),
	(125078547220922368,'c6753c50c23b2f83e2bb8d4dc15ccf04','o','2020-08-14 15:14:59',NULL),
	(125078547220922369,'7d66303521e2f0e2f8110ded78751a62','o','2020-08-14 15:14:59',NULL),
	(125078547220922370,'2bf0a8a800631792a3abba282254c28e','o','2020-08-14 15:14:59',NULL),
	(125078547223019520,'96527a7b57549c1b231ff22b31bd8aa8','o','2020-08-14 15:14:59',NULL),
	(125078547223019521,'0222fbe92b2fd4ecc94cec6bb2099ac8','o','2020-08-14 15:14:59',NULL),
	(125078547223019522,'0790dd17fcf03d37efd0278a93be5ee3','o','2020-08-14 15:14:59',NULL),
	(125078547225116672,'ccdd9063950747d136b53538f8d28aa1','o','2020-08-14 15:14:59',NULL),
	(125078547225116673,'3d2dbcacf8a7345a1217e5e0f870cc16','o','2020-08-14 15:14:59',NULL),
	(125078547227213824,'07f74808c90cff97baa281c0a070b217','o','2020-08-14 15:14:59',NULL),
	(125078547227213825,'e28717f192dd23e8aa4bd238598cf492','o','2020-08-14 15:14:59',NULL),
	(125078547229310976,'3101bda4ba15d219c82d521c66723a3c','o','2020-08-14 15:14:59',NULL),
	(125078547229310977,'21be8d5ec22554d34d1afcc73bcae1d0','o','2020-08-14 15:14:59',NULL),
	(125078547231408128,'8592603f23cee68c3d80d6d03e0ea464','o','2020-08-14 15:14:59',NULL),
	(125078547231408129,'acefe752de49adf8a19191dd34ae7462','o','2020-08-14 15:14:59',NULL),
	(125078547231408130,'f2796f8c3f7d2a76c6ffb44bdebb089d','o','2020-08-14 15:14:59',NULL),
	(125078547231408131,'a96ebf65a4cf965d7071cddc8546db36','o','2020-08-14 15:14:59',NULL),
	(125078547233505280,'02452c47c21bd9abb6b4a6bb6cd0c40b','o','2020-08-14 15:14:59',NULL),
	(125078547233505281,'b7d0c7e22133e6e6d74fb4c92fe5c305','o','2020-08-14 15:14:59',NULL),
	(125078547233505282,'de68daccdad732b06125bd63bc53f18a','o','2020-08-14 15:14:59',NULL),
	(125078547235602432,'65ce95a5dc24323c56595471f24b31c2','o','2020-08-14 15:14:59',NULL),
	(125078547235602433,'c2246d82087ba660c2ef59c3a4709112','o','2020-08-14 15:14:59',NULL),
	(125078547235602434,'3c157d0fefd3876e991f1b4a706b3533','o','2020-08-14 15:14:59',NULL),
	(125078547235602435,'3b085dde2c79cbaff46930fe48c8d148','o','2020-08-14 15:14:59',NULL),
	(125078547237699584,'63a1f29ac6b56e20d0dfb36cc747e7fa','o','2020-08-14 15:14:59',NULL),
	(125078547237699585,'cc6ca49a6b9032707dc3f9659fb69f19','o','2020-08-14 15:14:59',NULL),
	(125078547237699586,'84d3b696241c9cd4f96b8cab5884c198','o','2020-08-14 15:14:59',NULL),
	(125078547237699587,'bd24c975052bcf5853a295830019343a','o','2020-08-14 15:14:59',NULL),
	(125078547237699588,'ffe74bf17bd9fa147f911058e7b85908','o','2020-08-14 15:14:59',NULL),
	(125078547239796736,'95af25bf3b92a1dc1b903580fc636b09','o','2020-08-14 15:14:59',NULL),
	(125078547239796737,'9c16d32671ab7ba1df187e91aac1fbf8','o','2020-08-14 15:14:59',NULL),
	(125078547239796738,'3b4c424e1c4f7bcc12ebfbb85bbac57a','o','2020-08-14 15:14:59',NULL),
	(125078547239796739,'bb7a6f175df455345744979e792bc1d4','o','2020-08-14 15:14:59',NULL),
	(125078547241893888,'84fc170c3c485022664193720b2bb2b0','o','2020-08-14 15:14:59',NULL),
	(125078547241893889,'228ad858fae386424ed55dba5e65fb12','o','2020-08-14 15:14:59',NULL),
	(125078547241893890,'4659a4e8ef327885d1b10aa723f544ce','o','2020-08-14 15:14:59',NULL),
	(125078547241893891,'8bfdca0a64e1d90c9023c251d79afcba','o','2020-08-14 15:14:59',NULL),
	(125078547243991040,'2e8537eec0d51372cfbb950a9d0ccc2a','o','2020-08-14 15:14:59',NULL),
	(125078547243991041,'e442bc7f1a745b154b7bd924359241f8','o','2020-08-14 15:14:59',NULL),
	(125078547243991042,'ed3e8eee1f986454fdf703e434ace28d','o','2020-08-14 15:14:59',NULL),
	(125078547243991043,'0991b26f72cfeadd101e376aa0066bd3','o','2020-08-14 15:14:59',NULL),
	(125078653435379712,'8a5054c51fa298a7b8aaaeaa004bf95a','o','2020-08-14 15:15:50',NULL),
	(125078653437476864,'d0411a85044621bded08c5129d7df483','o','2020-08-14 15:15:50',NULL),
	(125078653437476865,'a8fdac416e329cc146de4b254a37b537','o','2020-08-14 15:15:50',NULL),
	(125078653439574016,'06fc75e9d5fa4ecd92f71c3630cafed9','o','2020-08-14 15:15:50',NULL),
	(125078653439574017,'bae23e5b5611d27fea3902a73391b1ba','o','2020-08-14 15:15:50',NULL),
	(125078653439574018,'3c305cca7927e9a5731fa45688264228','o','2020-08-14 15:15:50',NULL),
	(125078653441671168,'dfec819caabc2b642709e71b8af45f74','o','2020-08-14 15:15:50',NULL),
	(125078653441671169,'6adef58e99461cd29161959b91e2b53d','o','2020-08-14 15:15:50',NULL),
	(125078653441671170,'856bf12b90cfffde3383d18aa9701f52','o','2020-08-14 15:15:50',NULL),
	(125078653443768320,'22f1d3bb3d6f6c56a41178e4aa17739e','o','2020-08-14 15:15:50',NULL),
	(125078653443768321,'466dd22f429d95ddfd2d825783370548','o','2020-08-14 15:15:50',NULL),
	(125078653443768322,'792bf200b10acd64b04c6c2c905a2fc5','o','2020-08-14 15:15:50',NULL),
	(125078653445865472,'7abff68a9f24fd80387f1475b4f78daf','o','2020-08-14 15:15:50',NULL);

/*!40000 ALTER TABLE `rbac_privilege` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_privilege_element
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_privilege_element`;

CREATE TABLE `rbac_privilege_element` (
  `privilege_id` bigint unsigned NOT NULL COMMENT '权限ID',
  `element_id` bigint unsigned NOT NULL COMMENT '页面元素ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `UK_PRIVILEGE_OPERATION` (`privilege_id`,`element_id`),
  KEY `FK_PE_ELEMENT` (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限页面元素关联表';

LOCK TABLES `rbac_privilege_element` WRITE;
/*!40000 ALTER TABLE `rbac_privilege_element` DISABLE KEYS */;

INSERT INTO `rbac_privilege_element` (`privilege_id`, `element_id`, `created_date`, `last_modified_date`)
VALUES
	(125078022647709696,1131849293404176385,'2020-08-14 15:10:49',NULL),
	(125078022649806848,1131849510572654593,'2020-08-14 15:10:49',NULL),
	(125078022651904000,1131858946338992129,'2020-08-14 15:10:49',NULL),
	(125078022651904001,1131863248310775809,'2020-08-14 15:10:49',NULL),
	(125078022654001152,1131863723722551297,'2020-08-14 15:10:49',NULL),
	(125078022654001153,1131863775899693057,'2020-08-14 15:10:49',NULL),
	(125078022658195456,1131864400507056130,'2020-08-14 15:10:49',NULL),
	(125078022660292608,1131864444878598146,'2020-08-14 15:10:49',NULL),
	(125078022662389760,1131864827252322305,'2020-08-14 15:10:49',NULL),
	(125078022662389761,1131864864267055106,'2020-08-14 15:10:49',NULL),
	(125078022664486912,1131865040289411074,'2020-08-14 15:10:49',NULL),
	(125078022664486913,1131865075609645057,'2020-08-14 15:10:49',NULL),
	(125078022666584064,1131865482314526722,'2020-08-14 15:10:49',NULL),
	(125078022666584065,1131865520738545666,'2020-08-14 15:10:49',NULL),
	(125078022668681216,1131865772929462274,'2020-08-14 15:10:49',NULL),
	(125078022668681217,1131865931146997761,'2020-08-14 15:10:49',NULL),
	(125078022670778368,1131865974704844802,'2020-08-14 15:10:49',NULL),
	(125078022670778369,1164422088547635202,'2020-08-14 15:10:49',NULL),
	(125078022672875520,1164422211189084162,'2020-08-14 15:10:49',NULL);

/*!40000 ALTER TABLE `rbac_privilege_element` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_privilege_menu
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_privilege_menu`;

CREATE TABLE `rbac_privilege_menu` (
  `privilege_id` bigint unsigned NOT NULL COMMENT '权限ID',
  `menu_id` bigint unsigned NOT NULL COMMENT '菜单ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `UK_PRIVILEGE_MENU` (`privilege_id`,`menu_id`),
  KEY `FK_PM_MENU` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限菜单关联表';

LOCK TABLES `rbac_privilege_menu` WRITE;
/*!40000 ALTER TABLE `rbac_privilege_menu` DISABLE KEYS */;

INSERT INTO `rbac_privilege_menu` (`privilege_id`, `menu_id`, `created_date`, `last_modified_date`)
VALUES
	(125078022756761600,121053842608488401,'2020-08-14 15:10:49',NULL),
	(125078022758858752,121053842608488402,'2020-08-14 15:10:49',NULL),
	(125078022758858753,121053842608488404,'2020-08-14 15:10:49',NULL),
	(125078022758858754,121053842608488406,'2020-08-14 15:10:49',NULL),
	(125078022758858755,121053842608488407,'2020-08-14 15:10:49',NULL),
	(125078022758858756,121053842608488408,'2020-08-14 15:10:49',NULL),
	(125078022760955904,121053842608488409,'2020-08-14 15:10:49',NULL),
	(125078022760955905,121053842608488410,'2020-08-14 15:10:49',NULL),
	(125078022760955906,121053842608488412,'2020-08-14 15:10:49',NULL),
	(125078022763053056,122534432100843520,'2020-08-14 15:10:49',NULL),
	(125078022763053057,122535792368156672,'2020-08-14 15:10:49',NULL),
	(125078022763053058,122535848070610944,'2020-08-14 15:10:49',NULL),
	(125078022765150208,122535897334808576,'2020-08-14 15:10:49',NULL),
	(125078022765150209,122535924717322240,'2020-08-14 15:10:49',NULL),
	(125078022765150210,122535938061500416,'2020-08-14 15:10:49',NULL),
	(125078022765150211,122535954421383168,'2020-08-14 15:10:49',NULL),
	(125078022767247360,123444448041369600,'2020-08-14 15:10:49',NULL);

/*!40000 ALTER TABLE `rbac_privilege_menu` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_privilege_operation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_privilege_operation`;

CREATE TABLE `rbac_privilege_operation` (
  `privilege_id` bigint unsigned NOT NULL COMMENT '权限ID',
  `operation_id` bigint unsigned NOT NULL COMMENT '操作ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `UK_PRIVILEGE_OPERATION` (`privilege_id`,`operation_id`),
  KEY `FK_PO_OPERATION` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限操作关联表';

LOCK TABLES `rbac_privilege_operation` WRITE;
/*!40000 ALTER TABLE `rbac_privilege_operation` DISABLE KEYS */;

INSERT INTO `rbac_privilege_operation` (`privilege_id`, `operation_id`, `created_date`, `last_modified_date`)
VALUES
	(125078547174785024,125078546809880576,'2020-08-14 15:14:59',NULL),
	(125078547178979328,125078546864406528,'2020-08-14 15:14:59',NULL),
	(125078547178979329,125078546868600832,'2020-08-14 15:14:59',NULL),
	(125078547181076480,125078546870697984,'2020-08-14 15:14:59',NULL),
	(125078547183173632,125078546879086592,'2020-08-14 15:14:59',NULL),
	(125078547183173633,125078546883280896,'2020-08-14 15:14:59',NULL),
	(125078547187367936,125078546887475200,'2020-08-14 15:14:59',NULL),
	(125078547187367937,125078546889572352,'2020-08-14 15:14:59',NULL),
	(125078547191562240,125078546895863808,'2020-08-14 15:14:59',NULL),
	(125078547193659392,125078546897960960,'2020-08-14 15:14:59',NULL),
	(125078547193659393,125078546900058112,'2020-08-14 15:14:59',NULL),
	(125078547193659394,125078546902155264,'2020-08-14 15:14:59',NULL),
	(125078547195756544,125078546906349568,'2020-08-14 15:14:59',NULL),
	(125078547195756545,125078546910543872,'2020-08-14 15:14:59',NULL),
	(125078547195756546,125078546912641024,'2020-08-14 15:14:59',NULL),
	(125078547197853696,125078546914738176,'2020-08-14 15:14:59',NULL),
	(125078547197853697,125078546921029632,'2020-08-14 15:14:59',NULL),
	(125078547199950848,125078546923126784,'2020-08-14 15:14:59',NULL),
	(125078547199950849,125078546925223936,'2020-08-14 15:14:59',NULL),
	(125078547199950850,125078546925223937,'2020-08-14 15:14:59',NULL),
	(125078547202048000,125078546927321088,'2020-08-14 15:14:59',NULL),
	(125078547202048001,125078546929418240,'2020-08-14 15:14:59',NULL),
	(125078547202048002,125078546931515392,'2020-08-14 15:14:59',NULL),
	(125078547204145152,125078546933612544,'2020-08-14 15:14:59',NULL),
	(125078547204145153,125078546935709696,'2020-08-14 15:14:59',NULL),
	(125078547204145154,125078546937806848,'2020-08-14 15:14:59',NULL),
	(125078547206242304,125078546939904000,'2020-08-14 15:14:59',NULL),
	(125078547206242305,125078546942001152,'2020-08-14 15:14:59',NULL),
	(125078547206242306,125078546944098304,'2020-08-14 15:14:59',NULL),
	(125078547208339456,125078546946195456,'2020-08-14 15:14:59',NULL),
	(125078547208339457,125078546948292608,'2020-08-14 15:14:59',NULL),
	(125078547208339458,125078546950389760,'2020-08-14 15:14:59',NULL),
	(125078547210436608,125078546952486912,'2020-08-14 15:14:59',NULL),
	(125078547210436609,125078546954584064,'2020-08-14 15:14:59',NULL),
	(125078547210436610,125078546958778368,'2020-08-14 15:14:59',NULL),
	(125078547212533760,125078546960875520,'2020-08-14 15:14:59',NULL),
	(125078547212533761,125078546962972672,'2020-08-14 15:14:59',NULL),
	(125078547212533762,125078546965069824,'2020-08-14 15:14:59',NULL),
	(125078547214630912,125078546967166976,'2020-08-14 15:14:59',NULL),
	(125078547214630913,125078546967166977,'2020-08-14 15:14:59',NULL),
	(125078547216728064,125078546969264128,'2020-08-14 15:14:59',NULL),
	(125078547216728065,125078546971361280,'2020-08-14 15:14:59',NULL),
	(125078547218825216,125078546973458432,'2020-08-14 15:14:59',NULL),
	(125078547218825217,125078546973458433,'2020-08-14 15:14:59',NULL),
	(125078547218825218,125078546975555584,'2020-08-14 15:14:59',NULL),
	(125078547218825219,125078546977652736,'2020-08-14 15:14:59',NULL),
	(125078547220922368,125078546979749888,'2020-08-14 15:14:59',NULL),
	(125078547220922369,125078546979749889,'2020-08-14 15:14:59',NULL),
	(125078547220922370,125078546981847040,'2020-08-14 15:14:59',NULL),
	(125078547223019520,125078546983944192,'2020-08-14 15:14:59',NULL),
	(125078547223019521,125078546986041344,'2020-08-14 15:14:59',NULL),
	(125078547223019522,125078546988138496,'2020-08-14 15:14:59',NULL),
	(125078547225116672,125078546990235648,'2020-08-14 15:14:59',NULL),
	(125078547225116673,125078546990235649,'2020-08-14 15:14:59',NULL),
	(125078547227213824,125078546992332800,'2020-08-14 15:14:59',NULL),
	(125078547227213825,125078546992332801,'2020-08-14 15:14:59',NULL),
	(125078547229310976,125078546994429952,'2020-08-14 15:14:59',NULL),
	(125078547229310977,125078546994429953,'2020-08-14 15:14:59',NULL),
	(125078547231408128,125078546996527104,'2020-08-14 15:14:59',NULL),
	(125078547231408129,125078546996527105,'2020-08-14 15:14:59',NULL),
	(125078547231408130,125078546998624256,'2020-08-14 15:14:59',NULL),
	(125078547231408131,125078546998624257,'2020-08-14 15:14:59',NULL),
	(125078547233505280,125078547000721408,'2020-08-14 15:14:59',NULL),
	(125078547233505281,125078547002818560,'2020-08-14 15:14:59',NULL),
	(125078547233505282,125078547002818561,'2020-08-14 15:14:59',NULL),
	(125078547235602432,125078547004915712,'2020-08-14 15:14:59',NULL),
	(125078547235602433,125078547007012864,'2020-08-14 15:14:59',NULL),
	(125078547235602434,125078547011207168,'2020-08-14 15:14:59',NULL),
	(125078547235602435,125078547011207169,'2020-08-14 15:14:59',NULL),
	(125078547237699584,125078547013304320,'2020-08-14 15:14:59',NULL),
	(125078547237699585,125078547013304321,'2020-08-14 15:14:59',NULL),
	(125078547237699586,125078547015401472,'2020-08-14 15:14:59',NULL),
	(125078547237699587,125078547015401473,'2020-08-14 15:14:59',NULL),
	(125078547237699588,125078547017498624,'2020-08-14 15:14:59',NULL),
	(125078547239796736,125078547017498625,'2020-08-14 15:14:59',NULL),
	(125078547239796737,125078547019595776,'2020-08-14 15:14:59',NULL),
	(125078547239796738,125078547019595777,'2020-08-14 15:14:59',NULL),
	(125078547239796739,125078547021692928,'2020-08-14 15:14:59',NULL),
	(125078547241893888,125078547021692929,'2020-08-14 15:14:59',NULL),
	(125078547241893889,125078547023790080,'2020-08-14 15:14:59',NULL),
	(125078547241893890,125078547023790081,'2020-08-14 15:14:59',NULL),
	(125078547241893891,125078547025887232,'2020-08-14 15:14:59',NULL),
	(125078547243991040,125078547025887233,'2020-08-14 15:14:59',NULL),
	(125078547243991041,125078547027984384,'2020-08-14 15:14:59',NULL),
	(125078547243991042,125078547027984385,'2020-08-14 15:14:59',NULL),
	(125078547243991043,125078547030081536,'2020-08-14 15:14:59',NULL),
	(125078653435379712,125078653374562304,'2020-08-14 15:15:50',NULL),
	(125078653437476864,125078653376659456,'2020-08-14 15:15:50',NULL),
	(125078653437476865,125078653378756608,'2020-08-14 15:15:50',NULL),
	(125078653439574016,125078653380853760,'2020-08-14 15:15:50',NULL),
	(125078653439574017,125078653380853761,'2020-08-14 15:15:50',NULL),
	(125078653439574018,125078653382950912,'2020-08-14 15:15:50',NULL),
	(125078653441671168,125078653385048064,'2020-08-14 15:15:50',NULL),
	(125078653441671169,125078653387145216,'2020-08-14 15:15:50',NULL),
	(125078653441671170,125078653387145217,'2020-08-14 15:15:50',NULL),
	(125078653443768320,125078653391339520,'2020-08-14 15:15:50',NULL),
	(125078653443768321,125078653391339521,'2020-08-14 15:15:50',NULL),
	(125078653443768322,125078653393436672,'2020-08-14 15:15:50',NULL),
	(125078653445865472,125078653399728128,'2020-08-14 15:15:50',NULL);

/*!40000 ALTER TABLE `rbac_privilege_operation` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_role`;

CREATE TABLE `rbac_role` (
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色名',
  `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '角色编码',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';

LOCK TABLES `rbac_role` WRITE;
/*!40000 ALTER TABLE `rbac_role` DISABLE KEYS */;

INSERT INTO `rbac_role` (`role_id`, `role_name`, `role_code`, `created_date`, `last_modified_date`)
VALUES
	(1,'超级管理员','ROLE_ADMIN','2020-08-04 16:59:23','2020-08-07 13:54:56');

/*!40000 ALTER TABLE `rbac_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_role_privilege
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_role_privilege`;

CREATE TABLE `rbac_role_privilege` (
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `privilege_id` bigint unsigned NOT NULL COMMENT '权限ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `UK_ROLE_PRIVILEGE` (`role_id`,`privilege_id`),
  KEY `FK_RP_PRIVILEGE` (`privilege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';

LOCK TABLES `rbac_role_privilege` WRITE;
/*!40000 ALTER TABLE `rbac_role_privilege` DISABLE KEYS */;

INSERT INTO `rbac_role_privilege` (`role_id`, `privilege_id`, `created_date`, `last_modified_date`)
VALUES
	(1,125078022647709696,'2020-08-14 15:10:49',NULL),
	(1,125078022649806848,'2020-08-14 15:10:49',NULL),
	(1,125078022651904000,'2020-08-14 15:10:49',NULL),
	(1,125078022651904001,'2020-08-14 15:10:49',NULL),
	(1,125078022654001152,'2020-08-14 15:10:49',NULL),
	(1,125078022654001153,'2020-08-14 15:10:49',NULL),
	(1,125078022658195456,'2020-08-14 15:10:49',NULL),
	(1,125078022660292608,'2020-08-14 15:10:49',NULL),
	(1,125078022662389760,'2020-08-14 15:10:49',NULL),
	(1,125078022662389761,'2020-08-14 15:10:49',NULL),
	(1,125078022664486912,'2020-08-14 15:10:49',NULL),
	(1,125078022664486913,'2020-08-14 15:10:49',NULL),
	(1,125078022666584064,'2020-08-14 15:10:49',NULL),
	(1,125078022666584065,'2020-08-14 15:10:49',NULL),
	(1,125078022668681216,'2020-08-14 15:10:49',NULL),
	(1,125078022668681217,'2020-08-14 15:10:49',NULL),
	(1,125078022670778368,'2020-08-14 15:10:49',NULL),
	(1,125078022670778369,'2020-08-14 15:10:49',NULL),
	(1,125078022672875520,'2020-08-14 15:10:49',NULL),
	(1,125078022756761600,'2020-08-14 15:10:49',NULL),
	(1,125078022758858752,'2020-08-14 15:10:49',NULL),
	(1,125078022758858753,'2020-08-14 15:10:49',NULL),
	(1,125078022758858754,'2020-08-14 15:10:49',NULL),
	(1,125078022758858755,'2020-08-14 15:10:49',NULL),
	(1,125078022758858756,'2020-08-14 15:10:49',NULL),
	(1,125078022760955904,'2020-08-14 15:10:49',NULL),
	(1,125078022760955905,'2020-08-14 15:10:49',NULL),
	(1,125078022760955906,'2020-08-14 15:10:49',NULL),
	(1,125078022763053056,'2020-08-14 15:10:49',NULL),
	(1,125078022763053057,'2020-08-14 15:10:49',NULL),
	(1,125078022763053058,'2020-08-14 15:10:49',NULL),
	(1,125078022765150208,'2020-08-14 15:10:49',NULL),
	(1,125078022765150209,'2020-08-14 15:10:49',NULL),
	(1,125078022765150210,'2020-08-14 15:10:49',NULL),
	(1,125078022765150211,'2020-08-14 15:10:49',NULL),
	(1,125078022767247360,'2020-08-14 15:10:49',NULL),
	(1,125078547174785024,'2020-08-14 15:14:59',NULL),
	(1,125078547178979328,'2020-08-14 15:14:59',NULL),
	(1,125078547178979329,'2020-08-14 15:14:59',NULL),
	(1,125078547181076480,'2020-08-14 15:14:59',NULL),
	(1,125078547183173632,'2020-08-14 15:14:59',NULL),
	(1,125078547183173633,'2020-08-14 15:14:59',NULL),
	(1,125078547187367936,'2020-08-14 15:14:59',NULL),
	(1,125078547187367937,'2020-08-14 15:14:59',NULL),
	(1,125078547191562240,'2020-08-14 15:14:59',NULL),
	(1,125078547193659392,'2020-08-14 15:14:59',NULL),
	(1,125078547193659393,'2020-08-14 15:14:59',NULL),
	(1,125078547193659394,'2020-08-14 15:14:59',NULL),
	(1,125078547195756544,'2020-08-14 15:14:59',NULL),
	(1,125078547195756545,'2020-08-14 15:14:59',NULL),
	(1,125078547195756546,'2020-08-14 15:14:59',NULL),
	(1,125078547197853696,'2020-08-14 15:14:59',NULL),
	(1,125078547197853697,'2020-08-14 15:14:59',NULL),
	(1,125078547199950848,'2020-08-14 15:14:59',NULL),
	(1,125078547199950849,'2020-08-14 15:14:59',NULL),
	(1,125078547199950850,'2020-08-14 15:14:59',NULL),
	(1,125078547202048000,'2020-08-14 15:14:59',NULL),
	(1,125078547202048001,'2020-08-14 15:14:59',NULL),
	(1,125078547202048002,'2020-08-14 15:14:59',NULL),
	(1,125078547204145152,'2020-08-14 15:14:59',NULL),
	(1,125078547204145153,'2020-08-14 15:14:59',NULL),
	(1,125078547204145154,'2020-08-14 15:14:59',NULL),
	(1,125078547206242304,'2020-08-14 15:14:59',NULL),
	(1,125078547206242305,'2020-08-14 15:14:59',NULL),
	(1,125078547206242306,'2020-08-14 15:14:59',NULL),
	(1,125078547208339456,'2020-08-14 15:14:59',NULL),
	(1,125078547208339457,'2020-08-14 15:14:59',NULL),
	(1,125078547208339458,'2020-08-14 15:14:59',NULL),
	(1,125078547210436608,'2020-08-14 15:14:59',NULL),
	(1,125078547210436609,'2020-08-14 15:14:59',NULL),
	(1,125078547210436610,'2020-08-14 15:14:59',NULL),
	(1,125078547212533760,'2020-08-14 15:14:59',NULL),
	(1,125078547212533761,'2020-08-14 15:14:59',NULL),
	(1,125078547212533762,'2020-08-14 15:14:59',NULL),
	(1,125078547214630912,'2020-08-14 15:14:59',NULL),
	(1,125078547214630913,'2020-08-14 15:14:59',NULL),
	(1,125078547216728064,'2020-08-14 15:14:59',NULL),
	(1,125078547216728065,'2020-08-14 15:14:59',NULL),
	(1,125078547218825216,'2020-08-14 15:14:59',NULL),
	(1,125078547218825217,'2020-08-14 15:14:59',NULL),
	(1,125078547218825218,'2020-08-14 15:14:59',NULL),
	(1,125078547218825219,'2020-08-14 15:14:59',NULL),
	(1,125078547220922368,'2020-08-14 15:14:59',NULL),
	(1,125078547220922369,'2020-08-14 15:14:59',NULL),
	(1,125078547220922370,'2020-08-14 15:14:59',NULL),
	(1,125078547223019520,'2020-08-14 15:14:59',NULL),
	(1,125078547223019521,'2020-08-14 15:14:59',NULL),
	(1,125078547223019522,'2020-08-14 15:14:59',NULL),
	(1,125078547225116672,'2020-08-14 15:14:59',NULL),
	(1,125078547225116673,'2020-08-14 15:14:59',NULL),
	(1,125078547227213824,'2020-08-14 15:14:59',NULL),
	(1,125078547227213825,'2020-08-14 15:14:59',NULL),
	(1,125078547229310976,'2020-08-14 15:14:59',NULL),
	(1,125078547229310977,'2020-08-14 15:14:59',NULL),
	(1,125078547231408128,'2020-08-14 15:14:59',NULL),
	(1,125078547231408129,'2020-08-14 15:14:59',NULL),
	(1,125078547231408130,'2020-08-14 15:14:59',NULL),
	(1,125078547231408131,'2020-08-14 15:14:59',NULL),
	(1,125078547233505280,'2020-08-14 15:14:59',NULL),
	(1,125078547233505281,'2020-08-14 15:14:59',NULL),
	(1,125078547233505282,'2020-08-14 15:14:59',NULL),
	(1,125078547235602432,'2020-08-14 15:14:59',NULL),
	(1,125078547235602433,'2020-08-14 15:14:59',NULL),
	(1,125078547235602434,'2020-08-14 15:14:59',NULL),
	(1,125078547235602435,'2020-08-14 15:14:59',NULL),
	(1,125078547237699584,'2020-08-14 15:14:59',NULL),
	(1,125078547237699585,'2020-08-14 15:14:59',NULL),
	(1,125078547237699586,'2020-08-14 15:14:59',NULL),
	(1,125078547237699587,'2020-08-14 15:14:59',NULL),
	(1,125078547237699588,'2020-08-14 15:14:59',NULL),
	(1,125078547239796736,'2020-08-14 15:14:59',NULL),
	(1,125078547239796737,'2020-08-14 15:14:59',NULL),
	(1,125078547239796738,'2020-08-14 15:14:59',NULL),
	(1,125078547239796739,'2020-08-14 15:14:59',NULL),
	(1,125078547241893888,'2020-08-14 15:14:59',NULL),
	(1,125078547241893889,'2020-08-14 15:14:59',NULL),
	(1,125078547241893890,'2020-08-14 15:14:59',NULL),
	(1,125078547241893891,'2020-08-14 15:14:59',NULL),
	(1,125078547243991040,'2020-08-14 15:14:59',NULL),
	(1,125078547243991041,'2020-08-14 15:14:59',NULL),
	(1,125078547243991042,'2020-08-14 15:14:59',NULL),
	(1,125078547243991043,'2020-08-14 15:14:59',NULL),
	(1,125078653435379712,'2020-08-14 15:15:50',NULL),
	(1,125078653437476864,'2020-08-14 15:15:50',NULL),
	(1,125078653437476865,'2020-08-14 15:15:50',NULL),
	(1,125078653439574016,'2020-08-14 15:15:50',NULL),
	(1,125078653439574017,'2020-08-14 15:15:50',NULL),
	(1,125078653439574018,'2020-08-14 15:15:50',NULL),
	(1,125078653441671168,'2020-08-14 15:15:50',NULL),
	(1,125078653441671169,'2020-08-14 15:15:50',NULL),
	(1,125078653441671170,'2020-08-14 15:15:50',NULL),
	(1,125078653443768320,'2020-08-14 15:15:50',NULL),
	(1,125078653443768321,'2020-08-14 15:15:50',NULL),
	(1,125078653443768322,'2020-08-14 15:15:50',NULL),
	(1,125078653445865472,'2020-08-14 15:15:50',NULL);

/*!40000 ALTER TABLE `rbac_role_privilege` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_user`;

CREATE TABLE `rbac_user` (
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

LOCK TABLES `rbac_user` WRITE;
/*!40000 ALTER TABLE `rbac_user` DISABLE KEYS */;

INSERT INTO `rbac_user` (`user_id`, `user_name`, `created_date`, `last_modified_date`)
VALUES
	(1,'小涛','2020-08-04 17:00:40',NULL),
	(521677655146233856,'admin','2020-08-07 15:28:09',NULL),
	(557063237640650752,'游涛','2020-08-04 17:17:38',NULL),
	(557063237640650753,'大涛','2020-08-04 17:29:42',NULL),
	(557063237640650754,'哈哈','2020-08-04 17:31:51',NULL),
	(557063237640650755,'嘎嘎','2020-08-04 17:31:57',NULL),
	(557063237640650756,'加加','2020-08-04 17:32:07',NULL),
	(557063237640650757,'娜娜','2020-08-04 17:32:19',NULL),
	(557063237640650758,'丽丽','2020-08-04 17:32:25',NULL),
	(557063237640650759,'薇薇','2020-08-04 17:32:32',NULL),
	(557063237640650760,'琪琪','2020-08-04 17:32:47',NULL),
	(557063237640650761,'可可','2020-08-04 17:33:13',NULL),
	(557063237640650762,'乐乐','2020-08-04 17:33:46',NULL);

/*!40000 ALTER TABLE `rbac_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table rbac_user_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `rbac_user_role`;

CREATE TABLE `rbac_user_role` (
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `role_id` bigint unsigned NOT NULL COMMENT '角色ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  UNIQUE KEY `UK_USER_ROLE` (`user_id`,`role_id`),
  KEY `FK_UR_ROLE` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';

LOCK TABLES `rbac_user_role` WRITE;
/*!40000 ALTER TABLE `rbac_user_role` DISABLE KEYS */;

INSERT INTO `rbac_user_role` (`user_id`, `role_id`, `created_date`, `last_modified_date`)
VALUES
	(1,1,'2020-08-04 17:00:59',NULL),
	(521677655146233856,1,'2020-08-07 15:28:31',NULL);

/*!40000 ALTER TABLE `rbac_user_role` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
