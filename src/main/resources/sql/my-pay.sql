/*
 Navicat Premium Data Transfer

 Source Server         : aliyun
 Source Server Type    : MySQL
 Source Server Version : 50739 (5.7.39)
 Source Host           : 47.100.174.176:3306
 Source Schema         : my-pay

 Target Server Type    : MySQL
 Target Server Version : 50739 (5.7.39)
 File Encoding         : 65001

 Date: 14/04/2023 18:42:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for my_pay_config
-- ----------------------------
DROP TABLE IF EXISTS `my_pay_config`;
CREATE TABLE `my_pay_config` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `source` varchar(128) DEFAULT NULL,
  `sid` varchar(4096) DEFAULT NULL COMMENT '渠道号',
  `pay_way` tinyint(2) DEFAULT NULL COMMENT '1支付宝 2微信 3苹果 4易宝 5谷歌支付 6快手支付 7华为支付 99兑换 ',
  `app_id` varchar(32) DEFAULT NULL COMMENT '微信appId/支付宝appId/易宝appKey/华为client_id',
  `app_secret` varchar(255) DEFAULT NULL COMMENT '微信应用秘钥/华为client_secret',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '微信商户号/易宝商户编号/谷歌应用名称',
  `pay_secret` varchar(255) DEFAULT NULL COMMENT '微信支付秘钥',
  `serial_no` varchar(64) DEFAULT NULL COMMENT '证书号',
  `api_v3_key` varchar(64) DEFAULT NULL COMMENT '商户支付v3key，有值微信v3支付，空就是v2支付',
  `public_key` varchar(512) DEFAULT NULL COMMENT '支付宝公钥/易宝公钥',
  `private_key` varchar(2048) DEFAULT NULL COMMENT '支付宝应用私钥/易宝平台私钥',
  `key_path` varchar(128) DEFAULT NULL COMMENT '微信p12证书地址/支付宝appCertPublicKey.crt/谷歌p12证书',
  `private_key_path` varchar(128) DEFAULT NULL COMMENT '微信v3支付apiclient_key.pem证书地址/支付宝alipayCertPublicKey_RSA2.crt证书地址',
  `private_cert_path` varchar(128) DEFAULT NULL COMMENT '微信v3支付apiclient_cert.pem证书地址/支付宝alipayRootCert.crt证书地址',
  `notify_url` varchar(64) DEFAULT NULL COMMENT '通知地址',
  `weight` int(8) NOT NULL DEFAULT '1' COMMENT '权重',
  `status` int(1) DEFAULT '0' COMMENT '是否生效  0不生效 1生效  -1 作废',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `PAY_CONFIG_INDX01` (`source`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付配置表';

-- ----------------------------
-- Table structure for my_pay_list
-- ----------------------------
DROP TABLE IF EXISTS `my_pay_list`;
CREATE TABLE `my_pay_list` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `source` varchar(64) DEFAULT NULL,
  `sid` varchar(64) NOT NULL COMMENT '渠道号',
  `ali_h5` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付宝h5 0关闭 1显示 备注：支付宝h5和app都走h5支付',
  `ali_app` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付宝app 0关闭 1显示',
  `ali_applet` tinyint(2) NOT NULL DEFAULT '0' COMMENT '支付宝小程序 0关闭 1显示',
  `ali_logo_url` varchar(128) DEFAULT NULL COMMENT '支付宝logo',
  `wechat_h5` tinyint(2) NOT NULL DEFAULT '0' COMMENT '微信h5 0关闭 1显示',
  `wechat_app` tinyint(2) NOT NULL DEFAULT '0' COMMENT '微信客户端 0关闭 1显示',
  `wechat_jsapi` tinyint(2) NOT NULL DEFAULT '0' COMMENT '微信内原生、小程序 0关闭 1显示',
  `wechat_logo_url` varchar(128) DEFAULT NULL COMMENT '微信logo',
  `apple` tinyint(2) NOT NULL DEFAULT '0' COMMENT '苹果支付 0关闭 1显示',
  `apple_logo_url` varchar(128) DEFAULT NULL COMMENT '苹果logo',
  `yee_pay_quick` tinyint(2) NOT NULL DEFAULT '0' COMMENT '易宝快捷 0关闭 1显示',
  `yee_pay_wallet` tinyint(2) NOT NULL DEFAULT '0' COMMENT '易宝钱包 0关闭 1显示',
  `yee_pay_logo_url` varchar(128) DEFAULT NULL COMMENT '易宝logo',
  `google_pay` tinyint(2) NOT NULL DEFAULT '0' COMMENT '谷歌支付',
  `google_logo_url` varchar(128) DEFAULT NULL COMMENT '谷歌支付logo',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `UNION_KEY` (`source`,`sid`) USING BTREE,
  KEY `PAY_LIST_INDX01` (`source`),
  KEY `PAY_LIST_INDX02` (`sid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付列表';

-- ----------------------------
-- Table structure for my_pay_order
-- ----------------------------
DROP TABLE IF EXISTS `my_pay_order`;
CREATE TABLE `my_pay_order` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT,
  `source` varchar(64) DEFAULT NULL,
  `sid` varchar(64) DEFAULT NULL COMMENT '渠道号',
  `pay_config_id` bigint(19) DEFAULT NULL COMMENT '支付配置表id',
  `product_id` varchar(64) DEFAULT NULL COMMENT '产品id',
  `user_name` varchar(64) DEFAULT NULL COMMENT '用户名',
  `order_id` varchar(64) NOT NULL COMMENT '订单id、聚合订单id(nt_pay_master_order)',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '交易订单号 支付宝同字段、微信和苹果：transaction_id',
  `trade_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0支付中，1成功，2失败，3退款， 4处理中，5关单',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '金额',
  `type` tinyint(2) NOT NULL COMMENT '1h5 2小程序 3app 4微信原生jsapi 5沙盒 6钱包 7快捷 8球币兑换 9微信(四方支付) 10支付宝(四方支付) 11扫码(微信、支付宝)',
  `pay_way` tinyint(2) NOT NULL COMMENT '1支付宝 2微信 3苹果 4yeePay 5谷歌 6快手 7华为支付 99金币兑换',
  `currency_type` varchar(16) NOT NULL DEFAULT 'CNY' COMMENT '货币种类   CNY：人民币,USD：美元,HKD：港币,JPY：日元,GBP：英镑,EUR：欧元',
  `sign` blob COMMENT '苹果支付 sign、易宝支付token、快手的小程序平台订单号',
  `business_code` tinyint(2) NOT NULL DEFAULT '0' COMMENT '充当开通业务code，例如支付成功通知后，发送mq自动处理',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注\n1.易宝1级收款商户\n2.易宝2级默认收款方式 银行卡、钱包\n3.苹果支付验签结果\n4.快手是否结算',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人 备注：快手存入用户openId',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNION_KEY` (`order_id`,`pay_way`) USING BTREE COMMENT '订单id、支付方式联合索引',
  KEY `PAY_ORDER_INDX01` (`trade_status`) USING BTREE,
  KEY `PAY_ORDER_INDX02` (`user_name`) USING BTREE,
  KEY `PAY_ORDER_INDX03` (`trade_no`) USING BTREE,
  KEY `PAY_ORDER_INDX04` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付订单表明细表';

SET FOREIGN_KEY_CHECKS = 1;
