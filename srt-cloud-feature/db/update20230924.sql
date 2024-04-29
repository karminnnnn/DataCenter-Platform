/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql8
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : srt_cloud

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 24/09/2023 17:27:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dict_type_id` bigint(20) NOT NULL COMMENT '字典类型ID',
  `dict_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 162 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '停用', '0', '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (2, 1, '正常', '1', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (3, 2, '男', '0', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (4, 2, '女', '1', '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (5, 2, '未知', '2', '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (6, 3, '正常', '1', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (7, 3, '停用', '0', '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (8, 4, '全部数据', '0', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (9, 4, '本部门及子部门数据', '1', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (10, 4, '本部门数据', '2', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (11, 4, '本人数据', '3', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (12, 4, '自定义数据', '4', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (13, 5, '禁用', '0', '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (14, 5, '启用', '1', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (15, 6, '失败', '0', '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (16, 6, '成功', '1', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (17, 7, '登录成功', '0', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (18, 7, '退出成功', '1', '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (19, 7, '验证码错误', '2', '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (20, 7, '账号密码错误', '3', '', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_data` VALUES (21, 8, '阿里云', '0', '', 0, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_dict_data` VALUES (22, 8, '腾讯云', '1', '', 1, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_dict_data` VALUES (23, 8, '七牛云', '2', '', 2, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_dict_data` VALUES (24, 8, '华为云', '3', '', 3, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_dict_data` VALUES (25, 9, '默认', 'default', '', 0, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_dict_data` VALUES (26, 9, '数据生产', 'data_production', '', 2, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2023-01-19 16:51:31');
INSERT INTO `sys_dict_data` VALUES (27, 10, '暂停', '0', '', 0, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_dict_data` VALUES (28, 10, '正常', '1', '', 1, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_dict_data` VALUES (29, 11, '停用', '0', '', 0, 0, 0, 10000, '2022-09-27 12:00:10', 10000, '2022-09-27 12:00:10');
INSERT INTO `sys_dict_data` VALUES (30, 11, '启用', '1', '', 0, 0, 0, 10000, '2022-09-27 12:00:17', 10000, '2022-09-27 12:00:17');
INSERT INTO `sys_dict_data` VALUES (31, 12, 'MYSQL', '1', '', 1, 0, 0, 10000, '2022-10-09 17:17:20', 10000, '2022-10-09 17:17:20');
INSERT INTO `sys_dict_data` VALUES (32, 12, 'ORACLE', '2', '', 2, 0, 0, 10000, '2022-10-09 17:17:30', 10000, '2022-10-09 17:17:30');
INSERT INTO `sys_dict_data` VALUES (33, 12, 'SQLSERVER2000', '3', '', 5, 0, 0, 10000, '2022-10-09 17:17:46', 10000, '2022-10-09 17:17:46');
INSERT INTO `sys_dict_data` VALUES (34, 12, 'SQLSERVER', '4', '', 4, 0, 0, 10000, '2022-10-09 17:17:55', 10000, '2022-10-09 17:17:55');
INSERT INTO `sys_dict_data` VALUES (35, 12, 'POSTGRESQL', '5', '', 3, 0, 0, 10000, '2022-10-09 17:18:06', 10000, '2022-10-09 17:18:06');
INSERT INTO `sys_dict_data` VALUES (36, 12, 'GREENPLUM', '6', '', 12, 0, 0, 10000, '2022-10-09 17:18:20', 10000, '2022-10-09 17:18:20');
INSERT INTO `sys_dict_data` VALUES (37, 12, 'MARIADB', '7', '', 7, 0, 0, 10000, '2022-10-09 17:18:31', 10000, '2022-10-09 17:18:31');
INSERT INTO `sys_dict_data` VALUES (38, 12, 'DB2', '8', '', 6, 0, 0, 10000, '2022-10-09 17:18:40', 10000, '2022-10-09 17:18:40');
INSERT INTO `sys_dict_data` VALUES (39, 12, 'DM', '9', '', 8, 0, 0, 10000, '2022-10-09 17:19:00', 10000, '2022-10-09 17:19:00');
INSERT INTO `sys_dict_data` VALUES (40, 12, 'KINGBASE', '10', '', 11, 0, 0, 10000, '2022-10-09 17:19:16', 10000, '2022-10-09 17:19:16');
INSERT INTO `sys_dict_data` VALUES (41, 12, 'OSCAR', '11', '', 15, 0, 0, 10000, '2022-10-09 17:19:25', 10000, '2022-10-09 17:19:25');
INSERT INTO `sys_dict_data` VALUES (42, 12, 'GBASE8A', '12', '', 13, 0, 0, 10000, '2022-10-09 17:19:37', 10000, '2022-10-09 17:19:37');
INSERT INTO `sys_dict_data` VALUES (43, 12, 'HIVE', '13', '', 9, 0, 0, 10000, '2022-10-09 17:19:57', 10000, '2022-10-09 17:19:57');
INSERT INTO `sys_dict_data` VALUES (44, 12, 'SQLITE3', '14', '', 10, 0, 0, 10000, '2022-10-09 17:20:09', 10000, '2022-10-09 17:20:09');
INSERT INTO `sys_dict_data` VALUES (45, 12, 'SYBASE', '15', '', 14, 0, 0, 10000, '2022-10-09 17:20:19', 10000, '2022-10-09 17:20:19');
INSERT INTO `sys_dict_data` VALUES (46, 11, '断开', '0', '', 0, 0, 1, 10000, '2022-10-09 17:23:42', 10000, '2022-10-09 17:23:52');
INSERT INTO `sys_dict_data` VALUES (47, 13, '断开', '0', '', 0, 0, 0, 10000, '2022-10-09 17:24:31', 10000, '2022-10-09 17:24:31');
INSERT INTO `sys_dict_data` VALUES (48, 13, '正常', '1', '', 0, 0, 0, 10000, '2022-10-09 17:24:37', 10000, '2022-10-09 17:24:37');
INSERT INTO `sys_dict_data` VALUES (49, 14, '否', '0', '', 0, 0, 0, 10000, '2022-10-09 17:25:13', 10000, '2022-10-09 17:25:13');
INSERT INTO `sys_dict_data` VALUES (50, 14, '是', '1', '', 0, 0, 0, 10000, '2022-10-09 17:25:18', 10000, '2022-10-09 17:25:18');
INSERT INTO `sys_dict_data` VALUES (51, 15, '实时同步', '1', '', 1, 0, 1, 10000, '2022-10-24 17:47:15', 10000, '2023-01-21 14:50:00');
INSERT INTO `sys_dict_data` VALUES (52, 15, '一次性全量同步', '2', '', 2, 0, 0, 10000, '2022-10-24 17:47:26', 10000, '2022-10-24 17:47:26');
INSERT INTO `sys_dict_data` VALUES (53, 15, '周期性增量（全量比对）', '3', '', 3, 0, 0, 10000, '2022-10-24 17:47:41', 10000, '2023-06-25 16:36:05');
INSERT INTO `sys_dict_data` VALUES (54, 16, '等待中', '1', '', 1, 0, 0, 10000, '2022-10-24 17:49:04', 10000, '2022-10-24 17:49:04');
INSERT INTO `sys_dict_data` VALUES (55, 16, '运行中', '2', '', 2, 0, 0, 10000, '2022-10-24 17:49:13', 10000, '2022-10-24 17:49:13');
INSERT INTO `sys_dict_data` VALUES (56, 16, '正常结束', '3', '', 3, 0, 0, 10000, '2022-10-24 17:49:23', 10000, '2022-10-24 17:49:23');
INSERT INTO `sys_dict_data` VALUES (57, 16, '异常结束', '4', '', 4, 0, 0, 10000, '2022-10-24 17:49:33', 10000, '2022-10-24 17:49:33');
INSERT INTO `sys_dict_data` VALUES (58, 17, '未发布', '0', '', 0, 0, 0, 10000, '2022-10-24 17:50:23', 10000, '2022-10-24 17:50:23');
INSERT INTO `sys_dict_data` VALUES (59, 17, '已发布', '1', '', 1, 0, 0, 10000, '2022-10-24 17:50:31', 10000, '2022-10-24 17:50:31');
INSERT INTO `sys_dict_data` VALUES (60, 18, 'Sql', '1', '', 1, 0, 0, 10000, '2022-11-26 20:29:36', 10000, '2023-01-10 17:09:02');
INSERT INTO `sys_dict_data` VALUES (61, 18, 'FlinkSql', '2', '', 2, 0, 0, 10000, '2022-11-26 20:29:49', 10000, '2023-01-10 17:09:08');
INSERT INTO `sys_dict_data` VALUES (62, 19, 'Standalone', '1', NULL, 1, 0, 0, 10000, '2022-12-03 10:41:18', 10000, '2022-12-03 10:41:23');
INSERT INTO `sys_dict_data` VALUES (63, 19, 'Yarn Session', '2', NULL, 2, 0, 0, 10000, '2022-12-03 10:41:55', 10000, '2022-12-03 10:42:00');
INSERT INTO `sys_dict_data` VALUES (64, 19, 'Yarn Per-Job', '3', NULL, 3, 0, 0, 10000, '2022-12-03 10:42:29', 10000, '2022-12-03 10:42:35');
INSERT INTO `sys_dict_data` VALUES (65, 19, 'Yarn Application', '4', NULL, 4, 0, 0, 10000, '2022-12-03 10:43:02', 10000, '2022-12-03 10:43:08');
INSERT INTO `sys_dict_data` VALUES (66, 19, 'K8s Session', '5', NULL, 5, 0, 1, 10000, '2022-12-03 10:43:41', 10000, '2023-01-03 21:37:16');
INSERT INTO `sys_dict_data` VALUES (67, 19, 'K8s Application', '6', NULL, 6, 0, 1, 10000, '2022-12-03 10:44:24', 10000, '2023-01-03 21:37:19');
INSERT INTO `sys_dict_data` VALUES (68, 19, 'Local', '0', '', 0, 0, 0, 10000, '2022-12-21 11:35:54', 10000, '2022-12-21 11:35:54');
INSERT INTO `sys_dict_data` VALUES (69, 20, 'Flink On Yarn', 'Yarn', '', 0, 0, 0, 10000, '2022-12-22 09:31:02', 10000, '2022-12-22 09:31:02');
INSERT INTO `sys_dict_data` VALUES (70, 21, 'INITIALIZE', '0', '', 0, 0, 0, 10000, '2023-01-03 20:47:20', 10000, '2023-01-03 20:47:20');
INSERT INTO `sys_dict_data` VALUES (71, 21, 'RUNNING', '1', '', 1, 0, 0, 10000, '2023-01-03 20:47:27', 10000, '2023-01-03 20:47:27');
INSERT INTO `sys_dict_data` VALUES (72, 21, 'SUCCESS', '2', '', 2, 0, 0, 10000, '2023-01-03 20:47:36', 10000, '2023-01-03 20:47:36');
INSERT INTO `sys_dict_data` VALUES (73, 21, 'FAILED', '3', '', 3, 0, 0, 10000, '2023-01-03 20:47:43', 10000, '2023-01-03 20:47:43');
INSERT INTO `sys_dict_data` VALUES (74, 22, 'INITIALIZING', 'INITIALIZING', '', 0, 0, 0, 10000, '2023-01-03 20:50:05', 10000, '2023-01-03 20:50:05');
INSERT INTO `sys_dict_data` VALUES (75, 22, 'CREATED', 'CREATED', '', 1, 0, 0, 10000, '2023-01-03 20:50:15', 10000, '2023-01-03 20:50:15');
INSERT INTO `sys_dict_data` VALUES (76, 22, 'RUNNING', 'RUNNING', '', 2, 0, 0, 10000, '2023-01-03 20:50:22', 10000, '2023-01-03 20:50:22');
INSERT INTO `sys_dict_data` VALUES (77, 22, 'FAILING', 'FAILING', '', 3, 0, 0, 10000, '2023-01-03 20:50:29', 10000, '2023-01-03 20:50:29');
INSERT INTO `sys_dict_data` VALUES (78, 22, 'FAILED', 'FAILED', '', 4, 0, 0, 10000, '2023-01-03 20:50:36', 10000, '2023-01-03 20:50:36');
INSERT INTO `sys_dict_data` VALUES (79, 22, 'CANCELLING', 'CANCELLING', '', 5, 0, 0, 10000, '2023-01-03 20:50:44', 10000, '2023-01-03 20:50:44');
INSERT INTO `sys_dict_data` VALUES (80, 22, 'CANCELED', 'CANCELED', '', 6, 0, 0, 10000, '2023-01-03 20:50:51', 10000, '2023-01-03 20:50:51');
INSERT INTO `sys_dict_data` VALUES (81, 22, 'FINISHED', 'FINISHED', '', 7, 0, 0, 10000, '2023-01-03 20:50:58', 10000, '2023-01-03 20:50:58');
INSERT INTO `sys_dict_data` VALUES (82, 22, 'RESTARTING', 'RESTARTING', '', 8, 0, 0, 10000, '2023-01-03 20:51:05', 10000, '2023-01-03 20:51:05');
INSERT INTO `sys_dict_data` VALUES (83, 22, 'SUSPENDED', 'SUSPENDED', '', 9, 0, 0, 10000, '2023-01-03 20:51:11', 10000, '2023-01-03 20:51:11');
INSERT INTO `sys_dict_data` VALUES (84, 22, 'RECONCILING', 'RECONCILING', '', 10, 0, 0, 10000, '2023-01-03 20:51:18', 10000, '2023-01-03 20:51:18');
INSERT INTO `sys_dict_data` VALUES (85, 22, 'UNKNOWN', 'UNKNOWN', '', 11, 0, 0, 10000, '2023-01-03 20:51:25', 10000, '2023-01-03 20:51:25');
INSERT INTO `sys_dict_data` VALUES (86, 23, '禁用', '0', '', 0, 0, 0, 10000, '2023-01-09 21:23:38', 10000, '2023-01-09 21:23:38');
INSERT INTO `sys_dict_data` VALUES (87, 23, '最近一次', '1', '', 1, 0, 0, 10000, '2023-01-09 21:23:49', 10000, '2023-01-09 21:23:53');
INSERT INTO `sys_dict_data` VALUES (88, 23, '最早一次', '2', '', 2, 0, 0, 10000, '2023-01-09 21:24:02', 10000, '2023-01-09 21:24:02');
INSERT INTO `sys_dict_data` VALUES (89, 23, '指定一次', '3', '', 3, 0, 0, 10000, '2023-01-09 21:24:12', 10000, '2023-01-09 21:24:17');
INSERT INTO `sys_dict_data` VALUES (90, 18, 'FlinkSqlCommon', '4', 'FlinkSql的公共代码块，例如一些初始化的ddl等', 4, 0, 0, 10000, '2023-01-10 17:07:45', 10000, '2023-01-10 17:09:12');
INSERT INTO `sys_dict_data` VALUES (91, 24, '手动', '1', '', 0, 0, 0, 10000, '2023-01-18 15:13:22', 10000, '2023-01-18 15:13:22');
INSERT INTO `sys_dict_data` VALUES (92, 24, '调度', '2', '', 1, 0, 0, 10000, '2023-01-18 15:13:30', 10000, '2023-01-18 15:13:30');
INSERT INTO `sys_dict_data` VALUES (93, 9, '数据接入', 'data_access', '', 1, 0, 0, 10000, '2023-01-19 16:51:55', 10000, '2023-01-19 16:51:55');
INSERT INTO `sys_dict_data` VALUES (94, 25, '文件夹', '1', '', 0, 0, 0, 10000, '2023-01-30 11:34:40', 10000, '2023-01-30 11:34:40');
INSERT INTO `sys_dict_data` VALUES (95, 25, 'API 目录', '2', '', 0, 0, 0, 10000, '2023-01-30 11:34:46', 10000, '2023-01-30 11:34:55');
INSERT INTO `sys_dict_data` VALUES (96, 26, 'GET', 'GET', '', 0, 0, 0, 10000, '2023-02-12 11:26:50', 10000, '2023-02-12 11:26:50');
INSERT INTO `sys_dict_data` VALUES (97, 26, 'POST', 'POST', '', 1, 0, 0, 10000, '2023-02-12 11:26:58', 10000, '2023-02-12 11:26:58');
INSERT INTO `sys_dict_data` VALUES (98, 26, 'PUT', 'PUT', '', 2, 0, 0, 10000, '2023-02-12 11:27:05', 10000, '2023-02-12 11:27:05');
INSERT INTO `sys_dict_data` VALUES (99, 26, 'DELETE', 'DELETE', '', 3, 0, 0, 10000, '2023-02-12 11:27:15', 10000, '2023-02-12 11:27:15');
INSERT INTO `sys_dict_data` VALUES (100, 27, 'application/json', 'application/json', '', 0, 0, 0, 10000, '2023-02-12 11:33:16', 10000, '2023-02-12 11:33:16');
INSERT INTO `sys_dict_data` VALUES (101, 28, '10分钟', '10min', '', 0, 0, 0, 10000, '2023-02-16 14:44:06', 10000, '2023-02-16 14:44:22');
INSERT INTO `sys_dict_data` VALUES (102, 28, '1小时', '1hour', '', 1, 0, 0, 10000, '2023-02-16 14:44:36', 10000, '2023-02-16 14:44:36');
INSERT INTO `sys_dict_data` VALUES (103, 28, '1天', '1day', '', 2, 0, 0, 10000, '2023-02-16 14:44:52', 10000, '2023-02-16 14:44:57');
INSERT INTO `sys_dict_data` VALUES (104, 28, '30天', '30day', '', 3, 0, 0, 10000, '2023-02-16 14:45:59', 10000, '2023-02-16 14:46:03');
INSERT INTO `sys_dict_data` VALUES (105, 28, '仅一次', 'once', '', 4, 0, 0, 10000, '2023-02-16 14:46:19', 10000, '2023-02-16 14:46:25');
INSERT INTO `sys_dict_data` VALUES (106, 28, '永不失效', 'forever', '', 5, 0, 0, 10000, '2023-02-16 14:46:40', 10000, '2023-02-16 14:46:40');
INSERT INTO `sys_dict_data` VALUES (107, 29, '数字', '1', '', 0, 0, 0, 10000, '2023-03-28 16:02:20', 10000, '2023-03-28 16:02:20');
INSERT INTO `sys_dict_data` VALUES (108, 29, '字符串', '2', '', 1, 0, 0, 10000, '2023-03-28 16:02:27', 10000, '2023-03-28 16:02:31');
INSERT INTO `sys_dict_data` VALUES (109, 30, '文本框', '1', '', 0, 0, 0, 10000, '2023-03-28 16:03:10', 10000, '2023-03-28 16:03:10');
INSERT INTO `sys_dict_data` VALUES (110, 31, '全量', '0', '', 0, 0, 0, 10000, '2023-04-01 09:53:29', 10000, '2023-04-01 09:53:29');
INSERT INTO `sys_dict_data` VALUES (111, 31, '增量', '1', '', 1, 0, 0, 10000, '2023-04-01 09:53:35', 10000, '2023-04-01 09:53:35');
INSERT INTO `sys_dict_data` VALUES (112, 32, '一次性', '1', '', 1, 0, 0, 10000, '2023-04-01 09:54:05', 10000, '2023-04-01 12:26:03');
INSERT INTO `sys_dict_data` VALUES (113, 32, '周期性', '2', '', 2, 0, 0, 10000, '2023-04-01 09:54:11', 10000, '2023-04-01 09:54:14');
INSERT INTO `sys_dict_data` VALUES (114, 33, '数据库', '1', '', 1, 0, 0, 10000, '2023-04-01 09:54:45', 10000, '2023-04-01 09:54:48');
INSERT INTO `sys_dict_data` VALUES (115, 33, '中台库', '2', '', 2, 0, 0, 10000, '2023-04-01 09:54:55', 10000, '2023-04-01 09:54:55');
INSERT INTO `sys_dict_data` VALUES (116, 9, '数据治理', 'data_governance', '', 3, 0, 0, 10000, '2023-04-03 17:12:43', 10000, '2023-04-03 17:12:58');
INSERT INTO `sys_dict_data` VALUES (117, 34, '运行中', '2', '', 0, 0, 0, 10000, '2023-04-05 16:20:05', 10000, '2023-04-05 16:20:05');
INSERT INTO `sys_dict_data` VALUES (118, 34, '成功', '1', '', 1, 0, 0, 10000, '2023-04-05 16:20:11', 10000, '2023-04-05 16:20:11');
INSERT INTO `sys_dict_data` VALUES (119, 34, '失败', '0', '', 2, 0, 0, 10000, '2023-04-05 16:20:23', 10000, '2023-04-05 16:20:23');
INSERT INTO `sys_dict_data` VALUES (120, 35, '标准字段', '1', '', 1, 0, 0, 10000, '2023-05-08 15:52:49', 10000, '2023-05-08 15:52:49');
INSERT INTO `sys_dict_data` VALUES (121, 35, '标准码表', '2', '', 2, 0, 0, 10000, '2023-05-08 15:52:57', 10000, '2023-05-08 15:52:57');
INSERT INTO `sys_dict_data` VALUES (122, 36, '数字', '1', '', 1, 0, 0, 10000, '2023-05-18 16:24:32', 10000, '2023-05-18 16:24:32');
INSERT INTO `sys_dict_data` VALUES (123, 36, '字符串', '2', '', 2, 0, 0, 10000, '2023-05-18 16:24:39', 10000, '2023-05-18 16:24:39');
INSERT INTO `sys_dict_data` VALUES (124, 36, '日期', '3', '', 3, 0, 0, 10000, '2023-05-18 16:24:47', 10000, '2023-05-18 16:24:47');
INSERT INTO `sys_dict_data` VALUES (125, 36, '小数', '4', '', 4, 0, 0, 10000, '2023-05-18 16:24:56', 10000, '2023-05-18 16:24:56');
INSERT INTO `sys_dict_data` VALUES (126, 37, '唯一性', '1', '', 1, 0, 0, 10000, '2023-05-28 09:49:18', 10000, '2023-05-28 09:49:18');
INSERT INTO `sys_dict_data` VALUES (127, 37, '规范性', '2', '', 2, 0, 0, 10000, '2023-05-28 09:49:24', 10000, '2023-05-28 09:49:24');
INSERT INTO `sys_dict_data` VALUES (128, 37, '有效性', '3', '', 3, 0, 0, 10000, '2023-05-28 09:49:32', 10000, '2023-05-28 09:49:32');
INSERT INTO `sys_dict_data` VALUES (129, 37, '完整性', '4', '', 4, 0, 0, 10000, '2023-05-28 09:49:40', 10000, '2023-05-28 09:49:40');
INSERT INTO `sys_dict_data` VALUES (130, 37, '一致性', '5', '', 5, 0, 0, 10000, '2023-05-28 09:49:47', 10000, '2023-05-28 09:49:47');
INSERT INTO `sys_dict_data` VALUES (131, 37, '及时性', '6', '', 6, 0, 0, 10000, '2023-05-28 09:49:57', 10000, '2023-05-28 09:49:57');
INSERT INTO `sys_dict_data` VALUES (132, 37, '准确性', '7', '', 7, 0, 0, 10000, '2023-05-28 09:50:08', 10000, '2023-05-28 09:50:08');
INSERT INTO `sys_dict_data` VALUES (133, 38, '一次性', '1', '', 1, 0, 0, 10000, '2023-05-29 11:52:19', 10000, '2023-05-29 11:52:51');
INSERT INTO `sys_dict_data` VALUES (134, 38, '周期性', '2', '', 2, 0, 0, 10000, '2023-05-29 11:52:27', 10000, '2023-05-29 11:52:57');
INSERT INTO `sys_dict_data` VALUES (135, 39, '单字段唯一', '1', '', 1, 0, 0, 10000, '2023-05-30 12:43:31', 10000, '2023-05-30 12:43:31');
INSERT INTO `sys_dict_data` VALUES (136, 39, '组合字段唯一', '2', '', 2, 0, 0, 10000, '2023-05-30 12:43:40', 10000, '2023-05-30 12:43:40');
INSERT INTO `sys_dict_data` VALUES (137, 40, 'TiDB(MySql)', '1', '', 1, 0, 0, 10000, '2023-06-03 11:07:47', 10000, '2023-06-03 11:07:47');
INSERT INTO `sys_dict_data` VALUES (138, 40, 'ORACLE', '2', '', 2, 0, 0, 10000, '2023-06-13 16:57:00', 10000, '2023-06-13 16:57:00');
INSERT INTO `sys_dict_data` VALUES (139, 40, 'POSTGRESQL', '5', '', 5, 0, 0, 10000, '2023-06-13 17:12:18', 10000, '2023-09-24 17:07:36');
INSERT INTO `sys_dict_data` VALUES (140, 12, 'DORIS', '16', '', 16, 0, 0, 10000, '2023-06-19 17:46:51', 10000, '2023-06-19 17:46:51');
INSERT INTO `sys_dict_data` VALUES (141, 40, 'DORIS', '16', '', 16, 0, 0, 10000, '2023-06-23 10:24:52', 10000, '2023-06-23 10:24:52');
INSERT INTO `sys_dict_data` VALUES (142, 40, 'GREENPLUM', '6', '', 6, 0, 0, 10000, '2023-06-23 10:25:53', 10000, '2023-06-23 10:25:53');
INSERT INTO `sys_dict_data` VALUES (143, 41, '未上架', '0', '', 0, 0, 0, 10000, '2023-07-07 10:51:35', 10000, '2023-07-07 10:51:35');
INSERT INTO `sys_dict_data` VALUES (144, 41, '已上架', '1', '', 1, 0, 0, 10000, '2023-07-07 10:51:42', 10000, '2023-07-07 10:51:42');
INSERT INTO `sys_dict_data` VALUES (145, 42, '未挂载', '0', '', 0, 0, 0, 10000, '2023-07-07 10:52:09', 10000, '2023-07-07 10:52:09');
INSERT INTO `sys_dict_data` VALUES (146, 42, '已挂载', '1', '', 1, 0, 0, 10000, '2023-07-07 10:52:17', 10000, '2023-07-07 10:52:24');
INSERT INTO `sys_dict_data` VALUES (147, 43, '全部', '1', '', 1, 0, 0, 10000, '2023-07-07 15:09:02', 10000, '2023-07-07 15:09:02');
INSERT INTO `sys_dict_data` VALUES (148, 43, '角色', '2', '', 2, 0, 0, 10000, '2023-07-07 15:09:10', 10000, '2023-07-07 15:09:10');
INSERT INTO `sys_dict_data` VALUES (149, 43, '用户', '3', '', 3, 0, 0, 10000, '2023-07-07 15:09:16', 10000, '2023-07-07 15:09:16');
INSERT INTO `sys_dict_data` VALUES (150, 44, '数据库表', '1', '', 1, 0, 0, 10000, '2023-07-14 14:00:48', 10000, '2023-07-14 14:00:48');
INSERT INTO `sys_dict_data` VALUES (151, 44, 'API', '2', '', 2, 0, 0, 10000, '2023-07-14 14:00:52', 10000, '2023-07-14 14:00:57');
INSERT INTO `sys_dict_data` VALUES (152, 44, '文件', '3', '', 3, 0, 0, 10000, '2023-07-14 14:01:05', 10000, '2023-07-14 14:01:05');
INSERT INTO `sys_dict_data` VALUES (153, 9, '数据质量', 'data_quality', '', 4, 0, 0, 10000, '2023-07-21 10:03:30', 10000, '2023-07-21 10:03:38');
INSERT INTO `sys_dict_data` VALUES (154, 45, '待审核', '0', '', 0, 0, 0, 10000, '2023-08-17 14:29:51', 10000, '2023-08-17 14:29:51');
INSERT INTO `sys_dict_data` VALUES (155, 45, '已通过', '1', '', 1, 0, 0, 10000, '2023-08-17 14:29:58', 10000, '2023-08-17 14:29:58');
INSERT INTO `sys_dict_data` VALUES (156, 45, '未通过', '2', '', 2, 0, 0, 10000, '2023-08-17 14:30:09', 10000, '2023-08-17 14:30:09');
INSERT INTO `sys_dict_data` VALUES (157, 46, '极差', '1', '', 1, 0, 0, 10000, '2023-08-27 10:25:31', 10000, '2023-08-27 10:25:31');
INSERT INTO `sys_dict_data` VALUES (158, 46, '失望', '2', '', 2, 0, 0, 10000, '2023-08-27 10:25:37', 10000, '2023-08-27 10:25:37');
INSERT INTO `sys_dict_data` VALUES (159, 46, '一般', '3', '', 3, 0, 0, 10000, '2023-08-27 10:25:46', 10000, '2023-08-27 10:25:46');
INSERT INTO `sys_dict_data` VALUES (160, 46, '满意', '4', '', 4, 0, 0, 10000, '2023-08-27 10:25:56', 10000, '2023-08-27 10:25:56');
INSERT INTO `sys_dict_data` VALUES (161, 46, '惊喜', '5', '', 5, 0, 0, 10000, '2023-08-27 10:26:03', 10000, '2023-08-27 10:26:03');
INSERT INTO `sys_dict_data` VALUES (162, 40, 'SQLSERVER2000', '3', '', 3, 0, 0, 10000, '2023-09-24 17:08:02', 10000, '2023-09-24 17:08:02');
INSERT INTO `sys_dict_data` VALUES (163, 40, 'SQLSERVER', '4', '', 4, 0, 0, 10000, '2023-09-24 17:08:11', 10000, '2023-09-24 17:08:11');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型',
  `dict_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, 'post_status', '状态', '岗位管理', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (2, 'user_gender', '性别', '用户管理', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (3, 'user_status', '状态', '用户管理', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (4, 'role_data_scope', '数据范围', '角色管理', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (5, 'enable_disable', '状态', '功能状态：启用 | 禁用 ', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (6, 'success_fail', '状态', '操作状态：成功 | 失败', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (7, 'login_operation', '操作信息', '登录管理', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (8, 'sms_platform', '平台类型', '短信管理', 0, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_dict_type` VALUES (9, 'schedule_group', '任务组名', '定时任务', 0, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_dict_type` VALUES (10, 'schedule_status', '状态', '定时任务', 0, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_dict_type` VALUES (11, 'project_status', '项目状态', '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_dict_type` VALUES (12, 'database_type', '数据库类型', '', 0, 0, 0, 10000, '2022-10-09 16:07:56', 10000, '2022-10-09 16:07:56');
INSERT INTO `sys_dict_type` VALUES (13, 'database_status', '数据库状态', '', 0, 0, 0, 10000, '2022-10-09 17:24:20', 10000, '2022-10-09 17:24:20');
INSERT INTO `sys_dict_type` VALUES (14, 'yes_or_no', '是否', '', 0, 0, 0, 10000, '2022-10-09 17:25:01', 10000, '2022-10-09 17:25:01');
INSERT INTO `sys_dict_type` VALUES (15, 'task_type', '任务类型', '', 0, 0, 0, 10000, '2022-10-24 17:46:19', 10000, '2022-10-24 17:46:19');
INSERT INTO `sys_dict_type` VALUES (16, 'run_status', '运行状态', '', 0, 0, 0, 10000, '2022-10-24 17:48:48', 10000, '2022-10-24 17:48:48');
INSERT INTO `sys_dict_type` VALUES (17, 'release_status', '发布状态', '', 0, 0, 0, 10000, '2022-10-24 17:50:07', 10000, '2022-10-24 17:50:07');
INSERT INTO `sys_dict_type` VALUES (18, 'production_task_type', '数据生产作业类型', '数据生产作业类型', 0, 0, 0, 10000, '2022-11-26 20:28:20', 10000, '2022-11-26 20:28:20');
INSERT INTO `sys_dict_type` VALUES (19, 'production_cluster_type', '数据生产集群实例类型', '数据生产集群实例类型', 0, 0, 0, 10000, '2022-12-03 10:40:37', 10000, '2022-12-03 10:40:43');
INSERT INTO `sys_dict_type` VALUES (20, 'production_cluster_configuration_type', '集群配置类型', '', 0, 0, 0, 10000, '2022-12-22 09:30:02', 10000, '2022-12-22 09:30:02');
INSERT INTO `sys_dict_type` VALUES (21, 'task_status', '作业状态', '', 0, 0, 0, 10000, '2023-01-03 20:46:59', 10000, '2023-01-03 20:46:59');
INSERT INTO `sys_dict_type` VALUES (22, 'instance_status', '作业实例状态', '', 0, 0, 0, 10000, '2023-01-03 20:49:16', 10000, '2023-01-03 20:49:16');
INSERT INTO `sys_dict_type` VALUES (23, 'savepoint_strategy', 'flink的savepoint策略', '', 0, 0, 0, 10000, '2023-01-09 21:22:38', 10000, '2023-01-09 21:22:38');
INSERT INTO `sys_dict_type` VALUES (24, 'execute_type', '执行类型', '', 0, 0, 0, 10000, '2023-01-18 15:13:05', 10000, '2023-01-18 15:13:05');
INSERT INTO `sys_dict_type` VALUES (25, 'api_group_type', 'api分组类型', '', 0, 0, 0, 10000, '2023-01-30 11:34:24', 10000, '2023-01-30 11:34:24');
INSERT INTO `sys_dict_type` VALUES (26, 'api_type', 'api请求方式', '', 0, 0, 0, 10000, '2023-02-12 11:26:37', 10000, '2023-02-12 11:26:37');
INSERT INTO `sys_dict_type` VALUES (27, 'content_type', '请求参数类型', '', 0, 0, 0, 10000, '2023-02-12 11:32:28', 10000, '2023-02-12 11:32:28');
INSERT INTO `sys_dict_type` VALUES (28, 'api_expire_desc', 'api有效期', '', 0, 0, 0, 10000, '2023-02-16 14:43:43', 10000, '2023-02-16 14:43:43');
INSERT INTO `sys_dict_type` VALUES (29, 'model_property_data_type', '元模型属性数据类型', '', 0, 0, 0, 10000, '2023-03-28 16:02:07', 10000, '2023-03-28 16:02:07');
INSERT INTO `sys_dict_type` VALUES (30, 'model_property_input_type', '元模型属性输入类型', '', 0, 0, 0, 10000, '2023-03-28 16:02:59', 10000, '2023-03-28 16:02:59');
INSERT INTO `sys_dict_type` VALUES (31, 'metadata_collect_strategy', '元数据采集策略', '', 0, 0, 0, 10000, '2023-04-01 09:53:13', 10000, '2023-04-01 09:53:13');
INSERT INTO `sys_dict_type` VALUES (32, 'metadata_collect_type', '元数据采集类型', '', 0, 0, 0, 10000, '2023-04-01 09:53:55', 10000, '2023-04-01 09:53:55');
INSERT INTO `sys_dict_type` VALUES (33, 'db_type', '数据源类型', '', 0, 0, 0, 10000, '2023-04-01 09:54:35', 10000, '2023-04-01 09:54:35');
INSERT INTO `sys_dict_type` VALUES (34, 'metadata_collect_status', '元数据采集运行状态', '', 0, 0, 0, 10000, '2023-04-05 16:19:14', 10000, '2023-04-05 16:19:14');
INSERT INTO `sys_dict_type` VALUES (35, 'standard_type', '标准类型', '', 0, 0, 0, 10000, '2023-05-08 15:52:35', 10000, '2023-05-08 15:52:35');
INSERT INTO `sys_dict_type` VALUES (36, 'standard_data_type', '标准字段-数据类型', '', 0, 0, 0, 10000, '2023-05-18 16:23:44', 10000, '2023-05-18 16:23:44');
INSERT INTO `sys_dict_type` VALUES (37, 'quality_rule_type', '质量规则类型', '', 0, 0, 0, 10000, '2023-05-28 09:49:04', 10000, '2023-05-28 09:49:04');
INSERT INTO `sys_dict_type` VALUES (38, 'quality_config_task_type', '质量规则任务类型', '', 0, 0, 0, 10000, '2023-05-29 11:52:07', 10000, '2023-05-29 11:52:07');
INSERT INTO `sys_dict_type` VALUES (39, 'quality_unique_type', '质量唯一类型', '', 0, 0, 0, 10000, '2023-05-30 12:43:05', 10000, '2023-05-30 12:43:05');
INSERT INTO `sys_dict_type` VALUES (40, 'data_house_type', '数仓类型', '', 0, 0, 0, 10000, '2023-06-03 11:07:31', 10000, '2023-06-03 11:07:31');
INSERT INTO `sys_dict_type` VALUES (41, 'ground_status', '上架状态', '', 0, 0, 0, 10000, '2023-07-07 10:51:19', 10000, '2023-07-07 10:51:19');
INSERT INTO `sys_dict_type` VALUES (42, 'mount_status', '挂载状态', '', 0, 0, 0, 10000, '2023-07-07 10:51:56', 10000, '2023-07-07 10:51:56');
INSERT INTO `sys_dict_type` VALUES (43, 'open_type', '开放类型', '', 0, 0, 0, 10000, '2023-07-07 15:08:42', 10000, '2023-07-07 15:08:42');
INSERT INTO `sys_dict_type` VALUES (44, 'mount_type', '挂载资源类型', '', 0, 0, 0, 10000, '2023-07-14 14:00:25', 10000, '2023-07-14 14:00:25');
INSERT INTO `sys_dict_type` VALUES (45, 'check_status', '审核状态', '', 0, 0, 0, 10000, '2023-08-17 14:29:38', 10000, '2023-08-17 14:29:38');
INSERT INTO `sys_dict_type` VALUES (46, 'comment_level', '评价等级', '', 0, 0, 0, 10000, '2023-08-27 10:24:56', 10000, '2023-08-27 10:24:56');

SET FOREIGN_KEY_CHECKS = 1;
