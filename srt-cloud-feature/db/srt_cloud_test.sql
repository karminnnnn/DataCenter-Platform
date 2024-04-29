/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql8
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3307
 Source Schema         : srt_cloud_test

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 23/01/2023 12:07:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for people
-- ----------------------------
DROP TABLE IF EXISTS `people`;
CREATE TABLE `people`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '性别',
  `card_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '证件类型',
  `card_code` varchar(75) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '证件号码',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `nation` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '民族',
  `edu_level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '教育程度',
  `address` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `org` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '单位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_RE_05EB`(`card_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 620332 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '人口信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of people
-- ----------------------------
INSERT INTO `people` VALUES (1, '小明', '男', '身份证', '371326199503235538', '2023-01-20 12:04:48', '汉族', '本科', '测试地址', 28, '13355676787', '测试单位');
INSERT INTO `people` VALUES (2, '小红', '女', '居住证', '12345678', '2023-01-20 12:06:13', '维吾尔族', '研究生', '小红的地址', 28, '17877676768', '小红的单位');
INSERT INTO `people` VALUES (3, '小刚', '男', '军官证', '16788787898', '2023-01-20 12:07:16', '土家族', '本科', '小刚的地址', 29, '18788767876', '小刚的单位');
INSERT INTO `people` VALUES (4, '小芳', '女', '身份证', '13566767678', '2023-01-20 22:45:11', '汉族', '本科', '小芳的地址', 22, '14566766787', '小芳的单位');

-- ----------------------------
-- Table structure for people_cdc_test
-- ----------------------------
DROP TABLE IF EXISTS `people_cdc_test`;
CREATE TABLE `people_cdc_test`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '性别',
  `card_type` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '证件类型',
  `card_code` varchar(75) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '证件号码',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `nation` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '民族',
  `edu_level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '教育程度',
  `address` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `org` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '单位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_RE_05EB`(`card_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '人口信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of people_cdc_test
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
