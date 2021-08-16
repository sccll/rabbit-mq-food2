/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : take_out_food

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2021-08-16 10:22:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for delivery
-- ----------------------------
DROP TABLE IF EXISTS `delivery`;
CREATE TABLE `delivery` (
  `delivery_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '骑手id',
  `delivery_name` varchar(36) DEFAULT NULL COMMENT '名称',
  `status` varchar(36) DEFAULT NULL COMMENT '状态',
  `date` datetime DEFAULT NULL COMMENT '时间',
  `district` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`delivery_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of delivery
-- ----------------------------
INSERT INTO `delivery` VALUES ('1', 'wangxiaoer', '可配送', '2020-06-10 20:30:17', null);

-- ----------------------------
-- Table structure for order_header
-- ----------------------------
DROP TABLE IF EXISTS `order_header`;
CREATE TABLE `order_header` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `status` varchar(36) DEFAULT NULL COMMENT '状态',
  `address` varchar(64) DEFAULT NULL COMMENT '订单地址',
  `account_id` int(11) DEFAULT NULL COMMENT '用户id',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `delivery_id` int(11) DEFAULT NULL COMMENT '骑手id',
  `settlement_id` int(11) DEFAULT NULL COMMENT '结算id',
  `reward_id` int(11) DEFAULT NULL COMMENT '积分奖励id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `date` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=463 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of order_header
-- ----------------------------
INSERT INTO `order_header` VALUES ('430', 'ORDER_CREATED', '北京', '1', '1', '1', '1170', '10', '23.25', '2021-08-02 23:44:20');
INSERT INTO `order_header` VALUES ('431', 'ORDER_CREATED', '北京', '1', '1', '1', '1171', '11', '23.25', '2021-08-02 23:53:49');
INSERT INTO `order_header` VALUES ('432', 'ORDER_CREATED', '北京', '1', '1', '1', '1172', '12', '23.25', '2021-08-03 01:47:16');
INSERT INTO `order_header` VALUES ('433', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 06:19:21');
INSERT INTO `order_header` VALUES ('434', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 07:22:52');
INSERT INTO `order_header` VALUES ('435', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 07:27:00');
INSERT INTO `order_header` VALUES ('436', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 07:55:37');
INSERT INTO `order_header` VALUES ('437', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 07:56:26');
INSERT INTO `order_header` VALUES ('438', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 07:56:37');
INSERT INTO `order_header` VALUES ('439', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 08:06:52');
INSERT INTO `order_header` VALUES ('440', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 08:07:51');
INSERT INTO `order_header` VALUES ('441', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 08:08:58');
INSERT INTO `order_header` VALUES ('442', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 08:43:33');
INSERT INTO `order_header` VALUES ('443', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 08:50:27');
INSERT INTO `order_header` VALUES ('444', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 09:02:09');
INSERT INTO `order_header` VALUES ('445', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 09:09:14');
INSERT INTO `order_header` VALUES ('446', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-05 09:27:07');
INSERT INTO `order_header` VALUES ('447', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-06 01:16:47');
INSERT INTO `order_header` VALUES ('448', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-06 06:12:17');
INSERT INTO `order_header` VALUES ('449', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-06 06:18:09');
INSERT INTO `order_header` VALUES ('450', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-06 06:45:28');
INSERT INTO `order_header` VALUES ('451', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-06 06:56:50');
INSERT INTO `order_header` VALUES ('452', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-13 03:34:54');
INSERT INTO `order_header` VALUES ('453', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-13 03:37:41');
INSERT INTO `order_header` VALUES ('454', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-13 03:38:53');
INSERT INTO `order_header` VALUES ('455', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-13 09:50:43');
INSERT INTO `order_header` VALUES ('456', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-16 01:34:42');
INSERT INTO `order_header` VALUES ('457', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-16 01:46:04');
INSERT INTO `order_header` VALUES ('458', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-16 01:47:07');
INSERT INTO `order_header` VALUES ('459', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-16 02:03:39');
INSERT INTO `order_header` VALUES ('460', 'ORDER_CREATING', '北京', '1', '1', null, null, null, null, '2021-08-16 02:05:42');
INSERT INTO `order_header` VALUES ('461', 'ORDER_CREATED', '北京', '1', '1', '1', '1173', '13', '23.25', '2021-08-14 18:16:38');
INSERT INTO `order_header` VALUES ('462', 'ORDER_CREATED', '北京', '1', '1', '1', '1174', '14', '23.25', '2021-08-14 18:21:01');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品id',
  `product_name` varchar(36) DEFAULT NULL COMMENT '名称',
  `price` decimal(9,2) DEFAULT NULL COMMENT '单价',
  `restaurant_id` int(11) DEFAULT NULL COMMENT '地址',
  `status` varchar(36) DEFAULT NULL COMMENT '状态',
  `date` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', '鱼香肉丝套餐', '23.25', '1', '可售卖', '2020-05-06 19:19:04');

-- ----------------------------
-- Table structure for restaurant
-- ----------------------------
DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE `restaurant` (
  `restaurant_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '餐厅id',
  `restaurant_name` varchar(36) DEFAULT NULL COMMENT '名称',
  `address` varchar(36) DEFAULT NULL COMMENT '地址',
  `status` varchar(36) DEFAULT NULL COMMENT '状态',
  `settlement_id` int(11) DEFAULT NULL COMMENT '结算id',
  `date` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`restaurant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of restaurant
-- ----------------------------
INSERT INTO `restaurant` VALUES ('1', '老妈妈饭馆', '北京', '营业中', '1', '2020-05-06 19:19:39');
INSERT INTO `restaurant` VALUES ('2', '阿香米线', '北京', '营业中', '2', '2021-08-04 14:05:15');

-- ----------------------------
-- Table structure for reward
-- ----------------------------
DROP TABLE IF EXISTS `reward`;
CREATE TABLE `reward` (
  `reward_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '奖励id',
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `amount` decimal(9,2) DEFAULT NULL COMMENT '积分量',
  `status` varchar(36) DEFAULT NULL COMMENT '状态',
  `date` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`reward_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of reward
-- ----------------------------
INSERT INTO `reward` VALUES ('8', '429', '23.25', '成功', '2021-08-04 07:01:32');
INSERT INTO `reward` VALUES ('9', '429', '23.25', '成功', '2021-08-04 07:43:33');
INSERT INTO `reward` VALUES ('11', '431', '23.25', '成功', '2021-08-04 07:53:58');
INSERT INTO `reward` VALUES ('12', '432', '23.25', '成功', '2021-08-04 09:47:27');
INSERT INTO `reward` VALUES ('13', '461', '23.25', '成功', '2021-08-16 02:18:56');
INSERT INTO `reward` VALUES ('14', '462', '23.25', '成功', '2021-08-16 02:21:01');

-- ----------------------------
-- Table structure for settlement
-- ----------------------------
DROP TABLE IF EXISTS `settlement`;
CREATE TABLE `settlement` (
  `settlement_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '结算id',
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `transaction_id` varchar(255) DEFAULT NULL COMMENT '交易id',
  `amount` decimal(9,2) DEFAULT NULL COMMENT '金额',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `date` datetime DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`settlement_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1175 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of settlement
-- ----------------------------
INSERT INTO `settlement` VALUES ('1169', '429', '15791457894564', '23.25', '结算成功', '2021-08-04 07:01:20');
INSERT INTO `settlement` VALUES ('1170', '430', '15791457894564', '23.25', '结算成功', '2021-08-04 07:44:28');
INSERT INTO `settlement` VALUES ('1171', '431', '15791457894564', '23.25', '结算成功', '2021-08-04 07:53:56');
INSERT INTO `settlement` VALUES ('1172', '432', '15791457894564', '23.25', '结算成功', '2021-08-04 09:47:25');
INSERT INTO `settlement` VALUES ('1173', '461', '15791457894564', '23.25', '结算成功', '2021-08-16 02:17:49');
INSERT INTO `settlement` VALUES ('1174', '462', '15791457894564', '23.25', '结算成功', '2021-08-16 02:21:01');

-- ----------------------------
-- Table structure for trans_message
-- ----------------------------
DROP TABLE IF EXISTS `trans_message`;
CREATE TABLE `trans_message` (
  `id` varchar(255) NOT NULL,
  `service` varchar(255) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `exchange` varchar(255) DEFAULT NULL,
  `routing_key` varchar(255) DEFAULT NULL,
  `queue` varchar(255) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `payload` text,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`service`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of trans_message
-- ----------------------------
INSERT INTO `trans_message` VALUES ('1', 'orderHeaderService', '1', 'restaurantExchange', 'key.restaurant', null, '6', '\"{\\\"accountId\\\":1,\\\"orderId\\\":454,\\\"productId\\\":1}\"', '2021-08-10 19:38:53');
INSERT INTO `trans_message` VALUES ('32bd6d46-dcb9-4efb-9740-9e243b6b74ee', 'restaurantService', '2', 'restaurantExchange', 'key.restaurant', 'restaurantQueue', '0', '\"{\\\"accountId\\\":1,\\\"orderId\\\":457,\\\"productId\\\":1}\"', '2021-08-16 02:02:54');
INSERT INTO `trans_message` VALUES ('3ae3d083-3d6e-446a-ab80-595c85eb351b', 'orderHeaderService', '1', 'restaurantExchange', 'key.restaurant', null, '0', '\"{\\\"accountId\\\":1,\\\"orderId\\\":460,\\\"productId\\\":1}\"', '2021-08-16 02:05:47');
INSERT INTO `trans_message` VALUES ('3ae3d083-3d6e-446a-ab80-595c85eb351b', 'restaurantService', '2', 'restaurantExchange', 'key.restaurant', 'restaurantQueue', '0', '\"{\\\"accountId\\\":1,\\\"orderId\\\":460,\\\"productId\\\":1}\"', '2021-08-16 02:06:07');
INSERT INTO `trans_message` VALUES ('63e42598-f89b-4ad6-96cf-fb36cf068639', 'restaurantService', '2', 'restaurantExchange', 'key.restaurant', 'restaurantQueue', '0', '\"{\\\"accountId\\\":1,\\\"orderId\\\":459,\\\"productId\\\":1}\"', '2021-08-16 02:03:49');
INSERT INTO `trans_message` VALUES ('9454ea62-32af-41ab-9149-0283ebb5ad91', 'restaurantService', '2', 'restaurantExchange', 'key.restaurant', 'restaurantQueue', '0', '\"{\\\"accountId\\\":1,\\\"orderId\\\":458,\\\"productId\\\":1}\"', '2021-08-16 02:03:00');
INSERT INTO `trans_message` VALUES ('bebb8225-896c-4d1f-8fd0-b1257826f2de', 'orderHeaderService', '1', 'restaurantExchange', 'key.restaurant', null, '0', '\"{\\\"accountId\\\":1,\\\"orderId\\\":455,\\\"productId\\\":1}\"', '2021-08-13 09:50:43');
INSERT INTO `trans_message` VALUES ('f4459fb9-823c-4bb0-a9c9-e06d3e046292', 'orderHeaderService', '1', 'restaurantExchange', 'key.restaurant', null, '0', '\"{\\\"accountId\\\":1,\\\"orderId\\\":456,\\\"productId\\\":1}\"', '2021-08-16 01:34:53');
