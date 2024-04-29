--20230603
ALTER TABLE `srt_cloud`.`data_project`
ADD COLUMN `db_type` int(1) NOT NULL DEFAULT 1 COMMENT '数仓类型- 字典  data_house_type' AFTER `db_password`;

ALTER TABLE `srt_cloud`.`data_access_task`
MODIFY COLUMN `table_success_count` bigint(11) NULL DEFAULT 0 COMMENT '成功表数量' AFTER `data_count`,
MODIFY COLUMN `table_fail_count` bigint(11) NULL DEFAULT 0 COMMENT '失败表数量' AFTER `table_success_count`,
MODIFY COLUMN `byte_count` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 0 COMMENT '更新大小' AFTER `table_fail_count`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_governance_quality_task
-- ----------------------------
DROP TABLE IF EXISTS `data_governance_quality_task`;
CREATE TABLE `data_governance_quality_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `quality_config_id` bigint(11) NOT NULL COMMENT '规则配置id',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `status` int(1) NULL DEFAULT 1 COMMENT '运行状态（ 1-等待中 2-运行中 3-正常结束 4-异常结束）',
  `check_count` int(11) NULL DEFAULT NULL COMMENT '检测条数',
  `pass_count` int(11) NULL DEFAULT NULL COMMENT '检测通过数',
  `not_pass_count` int(11) NULL DEFAULT NULL COMMENT '未通过数',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `error_log` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '错误日志',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据治理-质量任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_governance_quality_task
-- ----------------------------
INSERT INTO `data_governance_quality_task` VALUES (12, 3, '测试手机号 - 2023-06-25 16:17:00', 3, 5, 4, 1, '2023-06-25 16:17:00', '2023-06-25 16:17:00', NULL, 10002, 0, NULL, NULL, '2023-06-25 16:17:00', NULL, '2023-06-25 16:17:00');
INSERT INTO `data_governance_quality_task` VALUES (13, 3, '测试手机号 - 2023-06-25 16:17:30', 3, 5, 4, 1, '2023-06-25 16:17:30', '2023-06-25 16:17:30', NULL, 10002, 0, NULL, NULL, '2023-06-25 16:17:30', NULL, '2023-06-25 16:17:30');
INSERT INTO `data_governance_quality_task` VALUES (14, 3, '测试手机号 - 2023-06-25 16:18:00', 3, 5, 4, 1, '2023-06-25 16:18:00', '2023-06-25 16:18:00', NULL, 10002, 0, NULL, NULL, '2023-06-25 16:18:00', NULL, '2023-06-25 16:18:00');
INSERT INTO `data_governance_quality_task` VALUES (16, 4, '日期判断 - 2023-06-25 16:20:36', 3, 5, 0, 5, '2023-06-25 16:20:36', '2023-06-25 16:20:37', NULL, 10002, 0, NULL, NULL, '2023-06-25 16:20:37', NULL, '2023-06-25 16:20:37');
INSERT INTO `data_governance_quality_task` VALUES (17, 5, '日期判断 - 2023-06-25 16:23:57', 3, 5, 5, 0, '2023-06-25 16:23:57', '2023-06-25 16:23:57', NULL, 10002, 0, NULL, NULL, '2023-06-25 16:23:58', NULL, '2023-06-25 16:23:58');
INSERT INTO `data_governance_quality_task` VALUES (18, 5, '日期判断 - 2023-06-25 16:26:01', 3, 5, 4, 1, '2023-06-25 16:26:01', '2023-06-25 16:26:01', NULL, 10002, 0, NULL, NULL, '2023-06-25 16:26:01', NULL, '2023-06-25 16:26:02');

-- ----------------------------
-- Table structure for data_governance_quality_task_column
-- ----------------------------
DROP TABLE IF EXISTS `data_governance_quality_task_column`;
CREATE TABLE `data_governance_quality_task_column`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `quality_task_id` bigint(11) NOT NULL COMMENT '质量任务id',
  `quality_task_table_id` bigint(11) NOT NULL COMMENT '表检测记录id',
  `check_row` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '被检测的数据行',
  `not_pass_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '未通过详情',
  `check_time` datetime(0) NOT NULL COMMENT '检测时间',
  `check_result` int(1) NOT NULL COMMENT '0-不通过 1-通过',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `quality_task_table_id_idx`(`quality_task_table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据治理-字段检测记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_governance_quality_task_column
-- ----------------------------
INSERT INTO `data_governance_quality_task_column` VALUES (46, 12, 11, '{\"phone\":\"13355676787\"}', NULL, '2023-06-25 16:17:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:00', NULL, '2023-06-25 16:17:00');
INSERT INTO `data_governance_quality_task_column` VALUES (47, 12, 11, '{\"phone\":\"17877676768\"}', NULL, '2023-06-25 16:17:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:00', NULL, '2023-06-25 16:17:00');
INSERT INTO `data_governance_quality_task_column` VALUES (48, 12, 11, '{\"phone\":\"18788767876\"}', NULL, '2023-06-25 16:17:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:00', NULL, '2023-06-25 16:17:00');
INSERT INTO `data_governance_quality_task_column` VALUES (49, 12, 11, '{\"phone\":\"14566766787\"}', NULL, '2023-06-25 16:17:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:00', NULL, '2023-06-25 16:17:00');
INSERT INTO `data_governance_quality_task_column` VALUES (50, 12, 11, '{\"phone\":null}', '【phone】字段不合规', '2023-06-25 16:17:00', 0, 10002, 0, NULL, NULL, '2023-06-25 16:17:00', NULL, '2023-06-25 16:17:00');
INSERT INTO `data_governance_quality_task_column` VALUES (51, 13, 12, '{\"phone\":\"13355676787\"}', NULL, '2023-06-25 16:17:30', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:30', NULL, '2023-06-25 16:17:30');
INSERT INTO `data_governance_quality_task_column` VALUES (52, 13, 12, '{\"phone\":\"17877676768\"}', NULL, '2023-06-25 16:17:30', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:30', NULL, '2023-06-25 16:17:30');
INSERT INTO `data_governance_quality_task_column` VALUES (53, 13, 12, '{\"phone\":\"18788767876\"}', NULL, '2023-06-25 16:17:30', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:30', NULL, '2023-06-25 16:17:30');
INSERT INTO `data_governance_quality_task_column` VALUES (54, 13, 12, '{\"phone\":\"14566766787\"}', NULL, '2023-06-25 16:17:30', 1, 10002, 0, NULL, NULL, '2023-06-25 16:17:30', NULL, '2023-06-25 16:17:30');
INSERT INTO `data_governance_quality_task_column` VALUES (55, 13, 12, '{\"phone\":null}', '【phone】字段不合规', '2023-06-25 16:17:30', 0, 10002, 0, NULL, NULL, '2023-06-25 16:17:30', NULL, '2023-06-25 16:17:30');
INSERT INTO `data_governance_quality_task_column` VALUES (56, 14, 13, '{\"phone\":\"13355676787\"}', NULL, '2023-06-25 16:18:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:18:00', NULL, '2023-06-25 16:18:00');
INSERT INTO `data_governance_quality_task_column` VALUES (57, 14, 13, '{\"phone\":\"17877676768\"}', NULL, '2023-06-25 16:18:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:18:00', NULL, '2023-06-25 16:18:00');
INSERT INTO `data_governance_quality_task_column` VALUES (58, 14, 13, '{\"phone\":\"18788767876\"}', NULL, '2023-06-25 16:18:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:18:00', NULL, '2023-06-25 16:18:00');
INSERT INTO `data_governance_quality_task_column` VALUES (59, 14, 13, '{\"phone\":\"14566766787\"}', NULL, '2023-06-25 16:18:00', 1, 10002, 0, NULL, NULL, '2023-06-25 16:18:00', NULL, '2023-06-25 16:18:00');
INSERT INTO `data_governance_quality_task_column` VALUES (60, 14, 13, '{\"phone\":null}', '【phone】字段不合规', '2023-06-25 16:18:00', 0, 10002, 0, NULL, NULL, '2023-06-25 16:18:00', NULL, '2023-06-25 16:18:00');
INSERT INTO `data_governance_quality_task_column` VALUES (66, 16, 15, '{\"name\":\"小明\"}', '【name】字段不合规', '2023-06-25 16:20:37', 0, 10002, 0, NULL, NULL, '2023-06-25 16:20:37', NULL, '2023-06-25 16:20:37');
INSERT INTO `data_governance_quality_task_column` VALUES (67, 16, 15, '{\"name\":\"小红\"}', '【name】字段不合规', '2023-06-25 16:20:37', 0, 10002, 0, NULL, NULL, '2023-06-25 16:20:37', NULL, '2023-06-25 16:20:37');
INSERT INTO `data_governance_quality_task_column` VALUES (68, 16, 15, '{\"name\":\"小刚\"}', '【name】字段不合规', '2023-06-25 16:20:37', 0, 10002, 0, NULL, NULL, '2023-06-25 16:20:37', NULL, '2023-06-25 16:20:37');
INSERT INTO `data_governance_quality_task_column` VALUES (69, 16, 15, '{\"name\":\"小芳\"}', '【name】字段不合规', '2023-06-25 16:20:37', 0, 10002, 0, NULL, NULL, '2023-06-25 16:20:37', NULL, '2023-06-25 16:20:37');
INSERT INTO `data_governance_quality_task_column` VALUES (70, 16, 15, '{\"name\":\"小明\"}', '【name】字段不合规', '2023-06-25 16:20:37', 0, 10002, 0, NULL, NULL, '2023-06-25 16:20:37', NULL, '2023-06-25 16:20:37');
INSERT INTO `data_governance_quality_task_column` VALUES (71, 17, 16, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:23:57', 1, 10002, 0, NULL, NULL, '2023-06-25 16:23:58', NULL, '2023-06-25 16:23:58');
INSERT INTO `data_governance_quality_task_column` VALUES (72, 17, 16, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:23:57', 1, 10002, 0, NULL, NULL, '2023-06-25 16:23:58', NULL, '2023-06-25 16:23:58');
INSERT INTO `data_governance_quality_task_column` VALUES (73, 17, 16, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:23:57', 1, 10002, 0, NULL, NULL, '2023-06-25 16:23:58', NULL, '2023-06-25 16:23:58');
INSERT INTO `data_governance_quality_task_column` VALUES (74, 17, 16, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:23:57', 1, 10002, 0, NULL, NULL, '2023-06-25 16:23:58', NULL, '2023-06-25 16:23:58');
INSERT INTO `data_governance_quality_task_column` VALUES (75, 17, 16, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:23:57', 1, 10002, 0, NULL, NULL, '2023-06-25 16:23:58', NULL, '2023-06-25 16:23:58');
INSERT INTO `data_governance_quality_task_column` VALUES (76, 18, 17, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:26:01', 1, 10002, 0, NULL, NULL, '2023-06-25 16:26:01', NULL, '2023-06-25 16:26:01');
INSERT INTO `data_governance_quality_task_column` VALUES (77, 18, 17, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:26:01', 1, 10002, 0, NULL, NULL, '2023-06-25 16:26:01', NULL, '2023-06-25 16:26:01');
INSERT INTO `data_governance_quality_task_column` VALUES (78, 18, 17, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:26:01', 1, 10002, 0, NULL, NULL, '2023-06-25 16:26:01', NULL, '2023-06-25 16:26:01');
INSERT INTO `data_governance_quality_task_column` VALUES (79, 18, 17, '{\"create_time\":\"2023-06-25T16:21:45\"}', NULL, '2023-06-25 16:26:01', 1, 10002, 0, NULL, NULL, '2023-06-25 16:26:01', NULL, '2023-06-25 16:26:01');
INSERT INTO `data_governance_quality_task_column` VALUES (80, 18, 17, '{\"create_time\":null}', '【create_time】字段不合规', '2023-06-25 16:26:01', 0, 10002, 0, NULL, NULL, '2023-06-25 16:26:01', NULL, '2023-06-25 16:26:01');

-- ----------------------------
-- Table structure for data_governance_quality_task_table
-- ----------------------------
DROP TABLE IF EXISTS `data_governance_quality_task_table`;
CREATE TABLE `data_governance_quality_task_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `quality_task_id` bigint(11) NOT NULL COMMENT '质量任务id',
  `table_metadata_id` bigint(20) NOT NULL COMMENT '被检测的表id',
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '被检测的表',
  `column_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '被检测的字段信息',
  `check_count` int(11) NULL DEFAULT NULL COMMENT '检测条数',
  `pass_count` int(11) NULL DEFAULT NULL COMMENT '检测通过数',
  `not_pass_count` int(11) NULL DEFAULT NULL COMMENT '未通过数',
  `check_time` datetime(0) NOT NULL COMMENT '检测时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `error_log` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '错误日志',
  `status` int(1) NOT NULL COMMENT '运行状态（ 1-等待中 2-运行中 3-正常结束 4-异常结束）',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `quality_task_id_idx`(`quality_task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '数据治理-表检测记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_governance_quality_task_table
-- ----------------------------
INSERT INTO `data_governance_quality_task_table` VALUES (11, 12, 3, 'people', '[{\"columnMetadataId\":14,\"columnName\":\"phone\"}]', 5, 4, 1, '2023-06-25 16:17:00', '2023-06-25 16:17:00', '2023-06-25 16:17:00', NULL, 3, 10002, 0, NULL, NULL, '2023-06-25 16:17:00', NULL, '2023-06-25 16:17:00');
INSERT INTO `data_governance_quality_task_table` VALUES (12, 13, 3, 'people', '[{\"columnMetadataId\":14,\"columnName\":\"phone\"}]', 5, 4, 1, '2023-06-25 16:17:30', '2023-06-25 16:17:30', '2023-06-25 16:17:30', NULL, 3, 10002, 0, NULL, NULL, '2023-06-25 16:17:30', NULL, '2023-06-25 16:17:30');
INSERT INTO `data_governance_quality_task_table` VALUES (13, 14, 3, 'people', '[{\"columnMetadataId\":14,\"columnName\":\"phone\"}]', 5, 4, 1, '2023-06-25 16:18:00', '2023-06-25 16:18:00', '2023-06-25 16:18:00', NULL, 3, 10002, 0, NULL, NULL, '2023-06-25 16:18:00', NULL, '2023-06-25 16:18:00');
INSERT INTO `data_governance_quality_task_table` VALUES (15, 16, 3, 'people', '[{\"columnMetadataId\":5,\"columnName\":\"name\"}]', 5, 0, 5, '2023-06-25 16:20:36', '2023-06-25 16:20:36', '2023-06-25 16:20:37', NULL, 3, 10002, 0, NULL, NULL, '2023-06-25 16:20:37', NULL, '2023-06-25 16:20:37');
INSERT INTO `data_governance_quality_task_table` VALUES (16, 17, 3, 'people', '[{\"columnMetadataId\":50,\"columnName\":\"create_time\"}]', 5, 5, 0, '2023-06-25 16:23:57', '2023-06-25 16:23:57', '2023-06-25 16:23:57', NULL, 3, 10002, 0, NULL, NULL, '2023-06-25 16:23:58', NULL, '2023-06-25 16:23:58');
INSERT INTO `data_governance_quality_task_table` VALUES (17, 18, 3, 'people', '[{\"columnMetadataId\":50,\"columnName\":\"create_time\"}]', 5, 4, 1, '2023-06-25 16:26:01', '2023-06-25 16:26:01', '2023-06-25 16:26:01', NULL, 3, 10002, 0, NULL, NULL, '2023-06-25 16:26:01', NULL, '2023-06-25 16:26:02');

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '上级ID，一级菜单为0',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `authority` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '类型   0：菜单   1：按钮   2：接口',
  `open_style` tinyint(4) NULL DEFAULT NULL COMMENT '打开方式   0：内部   1：外部',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 181 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 0, 'icon-setting', 21, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2023-01-12 10:28:59');
INSERT INTO `sys_menu` VALUES (2, 1, '菜单管理', 'sys/menu/index', NULL, 0, 0, 'icon-menu', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (3, 2, '查看', '', 'sys:menu:list', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (4, 2, '新增', '', 'sys:menu:save', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (5, 2, '修改', '', 'sys:menu:update,sys:menu:info', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (6, 2, '删除', '', 'sys:menu:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (7, 1, '数据字典', 'sys/dict/type', '', 0, 0, 'icon-insertrowabove', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (8, 7, '查询', '', 'sys:dict:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (9, 7, '新增', '', 'sys:dict:save', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (10, 7, '修改', '', 'sys:dict:update,sys:dict:info', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (11, 7, '删除', '', 'sys:dict:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (13, 1, '岗位管理', 'sys/post/index', '', 0, 0, 'icon-solution', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:06:32');
INSERT INTO `sys_menu` VALUES (14, 13, '查询', '', 'sys:post:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (15, 13, '新增', '', 'sys:post:save', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (16, 13, '修改', '', 'sys:post:update,sys:post:info', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (17, 13, '删除', '', 'sys:post:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (18, 1, '机构管理', 'sys/org/index', '', 0, 0, 'icon-cluster', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:06:25');
INSERT INTO `sys_menu` VALUES (19, 18, '查询', '', 'sys:org:list', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (20, 18, '新增', '', 'sys:org:save', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (21, 18, '修改', '', 'sys:org:update,sys:org:info', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (22, 18, '删除', '', 'sys:org:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (23, 1, '角色管理', 'sys/role/index', '', 0, 0, 'icon-team', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:06:39');
INSERT INTO `sys_menu` VALUES (24, 23, '查询', '', 'sys:role:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (25, 23, '新增', '', 'sys:role:save,sys:role:menu,sys:org:list', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (26, 23, '修改', '', 'sys:role:update,sys:role:info,sys:role:menu,sys:org:list,sys:user:page', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (27, 23, '删除', '', 'sys:role:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (28, 1, '用户管理', 'sys/user/index', '', 0, 0, 'icon-user', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:06:16');
INSERT INTO `sys_menu` VALUES (29, 28, '查询', '', 'sys:user:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (30, 28, '新增', '', 'sys:user:save,sys:role:list', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (31, 28, '修改', '', 'sys:user:update,sys:user:info,sys:role:list', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (32, 28, '删除', '', 'sys:user:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (33, 0, '应用管理', '', '', 0, 0, 'icon-appstore', 18, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:08:19');
INSERT INTO `sys_menu` VALUES (34, 1, '附件管理', 'sys/attachment/index', NULL, 0, 0, 'icon-folder-fill', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (35, 34, '查看', '', 'sys:attachment:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (36, 34, '上传', '', 'sys:attachment:save', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (37, 34, '删除', '', 'sys:attachment:delete', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (38, 0, '日志管理', '', '', 0, 0, 'icon-filedone', 19, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:08:14');
INSERT INTO `sys_menu` VALUES (39, 38, '登录日志', 'sys/log/login', 'sys:log:login', 0, 0, 'icon-solution', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 11:01:26');
INSERT INTO `sys_menu` VALUES (40, 33, '消息管理', '', '', 0, 0, 'icon-message', 2, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_menu` VALUES (41, 40, '短信日志', 'message/sms/log/index', 'sms:log', 0, 0, 'icon-detail', 1, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_menu` VALUES (42, 40, '短信平台', 'message/sms/platform/index', NULL, 0, 0, 'icon-whatsapp', 0, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_menu` VALUES (43, 42, '查看', '', 'sms:platform:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_menu` VALUES (44, 42, '新增', '', 'sms:platform:save', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_menu` VALUES (45, 42, '修改', '', 'sms:platform:update,sms:platform:info', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_menu` VALUES (46, 42, '删除', '', 'sms:platform:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:01:47', 10000, '2022-09-27 11:01:47');
INSERT INTO `sys_menu` VALUES (47, 1, '定时任务', 'quartz/schedule/index', NULL, 0, 0, 'icon-reloadtime', 0, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_menu` VALUES (48, 47, '查看', '', 'schedule:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_menu` VALUES (49, 47, '新增', '', 'schedule:save', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_menu` VALUES (50, 47, '修改', '', 'schedule:update,schedule:info', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_menu` VALUES (51, 47, '删除', '', 'schedule:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_menu` VALUES (52, 47, '立即运行', '', 'schedule:run', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_menu` VALUES (53, 47, '日志', '', 'schedule:log', 1, 0, '', 4, 0, 0, 10000, '2022-09-27 11:02:02', 10000, '2022-09-27 11:02:02');
INSERT INTO `sys_menu` VALUES (54, 0, '全局管理', '', '', 0, 0, 'icon-wallet', 20, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2023-01-12 10:29:04');
INSERT INTO `sys_menu` VALUES (55, 54, '数据项目管理', 'global-manage/project/index', '', 0, 0, 'icon-detail', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-10-09 12:41:16');
INSERT INTO `sys_menu` VALUES (56, 54, '数仓分层展示', 'global-manage/layer/index', '', 0, 0, 'icon-table1', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-09-27 17:12:21');
INSERT INTO `sys_menu` VALUES (57, 55, '查看', '', 'data-integrate:project:page', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 20:46:51', 10000, '2022-09-27 20:46:51');
INSERT INTO `sys_menu` VALUES (58, 55, '新增', '', 'data-integrate:project:save', 1, 0, '', 1, 0, 0, 10000, '2022-09-27 20:46:51', 10000, '2022-09-27 20:46:51');
INSERT INTO `sys_menu` VALUES (59, 55, '修改', '', 'data-integrate:project:update,data-integrate:project:info', 1, 0, '', 2, 0, 0, 10000, '2022-09-27 20:46:51', 10000, '2022-09-27 20:46:51');
INSERT INTO `sys_menu` VALUES (60, 55, '删除', '', 'data-integrate:project:delete', 1, 0, '', 3, 0, 0, 10000, '2022-09-27 20:46:51', 10000, '2022-09-27 20:46:51');
INSERT INTO `sys_menu` VALUES (61, 55, '项目成员', '', 'data-integrate:project:users', 1, 0, '', 0, 0, 0, 10000, '2022-09-27 21:28:39', 10000, '2022-09-27 21:28:39');
INSERT INTO `sys_menu` VALUES (62, 55, '添加成员', '', 'data-integrate:project:adduser', 1, 0, '', 4, 0, 0, 10000, '2022-10-07 12:00:15', 10000, '2022-10-07 12:00:25');
INSERT INTO `sys_menu` VALUES (63, 56, '查看', '', 'data-integrate:layer:page', 1, 0, '', 0, 0, 0, 10000, '2022-10-08 16:55:11', 10000, '2022-10-08 16:55:11');
INSERT INTO `sys_menu` VALUES (66, 56, '修改', '', 'data-integrate:layer:update,data-integrate:layer:info', 1, 0, '', 1, 0, 0, 10000, '2022-10-08 17:30:36', 10000, '2022-10-08 17:30:36');
INSERT INTO `sys_menu` VALUES (67, 0, '数据集成', '', '', 0, 0, 'icon-control', 1, 0, 0, 10000, '2022-10-09 12:40:06', 10000, '2022-10-09 12:40:06');
INSERT INTO `sys_menu` VALUES (68, 67, '数据库管理', 'data-integrate/database/index', '', 0, 0, 'icon-insertrowright', 0, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-10-09 12:49:18');
INSERT INTO `sys_menu` VALUES (69, 67, '文件管理', 'data-integrate/file-category/index', '', 0, 0, 'icon-layout', 1, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-10-28 16:50:34');
INSERT INTO `sys_menu` VALUES (70, 67, '数据接入', 'data-integrate/access/index', '', 0, 0, 'icon-rotate-right', 2, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-10-09 12:51:30');
INSERT INTO `sys_menu` VALUES (71, 67, '贴源数据', 'data-integrate/ods/index', '', 0, 0, 'icon-border', 3, 0, 0, 10000, '2022-09-27 11:01:26', 10000, '2022-10-09 12:53:18');
INSERT INTO `sys_menu` VALUES (72, 0, '数据开发', '', '', 0, 0, 'icon-Function', 2, 0, 0, 10000, '2022-10-09 12:56:37', 10000, '2022-10-09 12:56:37');
INSERT INTO `sys_menu` VALUES (74, 72, '数据建模', '', '', 0, 0, 'icon-calculator-fill', 0, 0, 1, 10000, '2022-10-09 13:02:24', 10000, '2023-04-07 10:06:18');
INSERT INTO `sys_menu` VALUES (75, 74, '维度表', 'data-development/dim/index', '', 0, 0, 'icon-file-markdown', 0, 0, 1, 10000, '2022-10-09 13:02:24', 10000, '2023-04-07 10:06:09');
INSERT INTO `sys_menu` VALUES (76, 74, '明细表', 'data-development/dwd/index', '', 0, 0, 'icon-folder-fill', 1, 0, 1, 10000, '2022-10-09 13:02:24', 10000, '2023-04-07 10:06:12');
INSERT INTO `sys_menu` VALUES (77, 74, '汇总表', 'data-development/dws/index', '', 0, 0, 'icon-folder-open-fill', 2, 0, 1, 10000, '2022-10-09 13:02:24', 10000, '2023-04-07 10:06:15');
INSERT INTO `sys_menu` VALUES (78, 72, '数据生产', 'data-development/production/index', '', 0, 0, 'icon-Console-SQL', 1, 0, 0, 10000, '2022-10-09 13:02:24', 10000, '2023-01-03 21:32:34');
INSERT INTO `sys_menu` VALUES (80, 68, '查看', '', 'data-integrate:database:page', 1, 0, '', 0, 0, 0, 10000, '2022-10-09 17:36:31', 10000, '2022-10-09 17:36:31');
INSERT INTO `sys_menu` VALUES (81, 68, '新增', '', 'data-integrate:database:save', 1, 0, '', 1, 0, 0, 10000, '2022-10-09 17:36:56', 10000, '2022-10-09 17:38:02');
INSERT INTO `sys_menu` VALUES (82, 68, '修改', '', 'data-integrate:database:info,data-integrate:database:update', 1, 0, '', 2, 0, 0, 10000, '2022-10-09 17:37:29', 10000, '2022-10-09 17:38:10');
INSERT INTO `sys_menu` VALUES (83, 68, '删除', '', 'data-integrate:database:delete', 1, 0, '', 3, 0, 0, 10000, '2022-10-09 17:37:54', 10000, '2022-10-09 17:37:54');
INSERT INTO `sys_menu` VALUES (84, 70, '新增', '', 'data-integrate:access:save', 1, 0, '', 1, 0, 0, 10000, '2022-10-24 22:09:49', 10000, '2022-10-24 22:10:06');
INSERT INTO `sys_menu` VALUES (85, 70, '查看', '', 'data-integrate:access:page', 1, 0, '', 0, 0, 0, 10000, '2022-10-24 22:09:49', 10000, '2022-10-24 22:10:38');
INSERT INTO `sys_menu` VALUES (86, 70, '修改', '', 'data-integrate:access:update,data-integrate:access:info', 1, 0, '', 2, 0, 0, 10000, '2022-10-24 22:09:49', 10000, '2022-10-24 22:12:14');
INSERT INTO `sys_menu` VALUES (87, 70, '删除', '', 'data-integrate:access:delete', 1, 0, '', 3, 0, 0, 10000, '2022-10-24 22:09:49', 10000, '2022-10-24 22:12:19');
INSERT INTO `sys_menu` VALUES (88, 70, '发布', '', 'data-integrate:access:release', 1, 0, '', 5, 0, 0, 10000, '2022-10-27 14:32:34', 10000, '2022-10-27 14:32:34');
INSERT INTO `sys_menu` VALUES (89, 70, '取消发布', '', 'data-integrate:access:cancle', 1, 0, '', 6, 0, 0, 10000, '2022-10-27 14:33:06', 10000, '2022-10-27 14:33:06');
INSERT INTO `sys_menu` VALUES (90, 70, '手动执行', '', 'data-integrate:access:selfhandler', 1, 0, '', 7, 0, 0, 10000, '2022-10-27 22:13:07', 10000, '2022-10-27 22:13:07');
INSERT INTO `sys_menu` VALUES (91, 0, '数据治理', '', '', 0, 0, 'icon-insertrowbelow', 3, 0, 0, 10000, '2022-10-29 12:59:30', 10000, '2023-01-20 13:38:57');
INSERT INTO `sys_menu` VALUES (92, 91, '元数据', '', '', 0, 0, 'icon-file-exception', 0, 0, 0, 10000, '2022-10-29 13:01:36', 10000, '2023-01-20 13:38:53');
INSERT INTO `sys_menu` VALUES (93, 92, '元模型', 'data-governance/metamodel/index', '', 0, 0, 'icon-database', 0, 0, 0, 10000, '2022-10-29 13:05:35', 10000, '2023-03-28 11:35:56');
INSERT INTO `sys_menu` VALUES (94, 92, '元数据采集', 'data-governance/metadata-collect/index', '', 0, 0, 'icon-right-square', 1, 0, 0, 10000, '2022-10-29 13:05:35', 10000, '2023-01-20 13:38:45');
INSERT INTO `sys_menu` VALUES (97, 92, '元数据管理', 'data-governance/metadata-manage/index', '', 0, 0, 'icon-reconciliation', 2, 0, 0, 10000, '2022-10-29 13:05:35', 10000, '2023-01-20 13:38:42');
INSERT INTO `sys_menu` VALUES (98, 91, '数据血缘', 'data-governance/data-blood/index', '', 0, 0, 'icon-deleterow', 1, 0, 0, 10000, '2022-10-29 13:13:23', 10000, '2023-01-20 13:38:38');
INSERT INTO `sys_menu` VALUES (99, 0, '数据资产', '', '', 0, 0, 'icon-codelibrary-fill', 5, 0, 0, 10000, '2022-10-29 13:48:15', 10000, '2023-05-28 10:39:59');
INSERT INTO `sys_menu` VALUES (100, 99, '资源管理', 'data-assets/resource-manage/index', '', 0, 0, 'icon-minus-square-fill', 0, 0, 0, 10000, '2022-10-29 13:48:53', 10000, '2023-01-20 13:39:02');
INSERT INTO `sys_menu` VALUES (101, 99, '资产总览', 'data-assets/resource-overview/index', '', 0, 0, 'icon-aim', 1, 0, 0, 10000, '2022-10-29 13:50:30', 10000, '2023-01-20 13:39:06');
INSERT INTO `sys_menu` VALUES (102, 0, '数据服务', '', '', 0, 0, 'icon-transaction', 4, 0, 0, 10000, '2022-10-29 13:52:16', 10000, '2023-05-28 10:39:53');
INSERT INTO `sys_menu` VALUES (103, 102, 'API 目录', 'data-service/api-group/index', '', 0, 0, 'icon-filesearch', 0, 0, 0, 10000, '2022-10-29 13:57:03', 10000, '2023-02-16 14:48:41');
INSERT INTO `sys_menu` VALUES (104, 102, '数据可视化', 'data-service/data-visualization/index', '', 0, 0, 'icon-areachart', 1, 0, 1, 10000, '2022-10-29 13:57:03', 10000, '2023-02-16 14:48:35');
INSERT INTO `sys_menu` VALUES (105, 0, '数据集市', '', '', 0, 0, 'icon-reconciliation', 6, 0, 0, 10000, '2022-10-29 13:57:03', 10000, '2023-01-20 13:39:39');
INSERT INTO `sys_menu` VALUES (106, 105, '资源目录', 'data-market/resource/index', '', 0, 0, 'icon-sever', 0, 0, 0, 10000, '2022-10-29 13:57:03', 10000, '2023-01-20 13:39:27');
INSERT INTO `sys_menu` VALUES (107, 105, 'API目录', 'data-market/api/index', '', 0, 0, 'icon-container', 1, 0, 0, 10000, '2022-10-29 13:57:03', 10000, '2023-01-20 13:39:30');
INSERT INTO `sys_menu` VALUES (108, 105, '我的申请', 'data-market/my-apply/index', '', 0, 0, 'icon-user', 2, 0, 0, 10000, '2022-10-29 13:57:03', 10000, '2023-01-20 13:39:33');
INSERT INTO `sys_menu` VALUES (109, 105, '服务审批', 'data-market/service-check/index', '', 0, 0, 'icon-book', 3, 0, 0, 10000, '2022-10-29 13:57:03', 10000, '2023-01-20 13:39:36');
INSERT INTO `sys_menu` VALUES (110, 69, '分组新增', '', 'data-integrate:fileCategory:save', 1, 0, '', 0, 0, 0, 10000, '2022-11-14 15:17:40', 10000, '2022-11-14 15:17:55');
INSERT INTO `sys_menu` VALUES (111, 69, '分组编辑', '', 'data-integrate:fileCategory:update', 1, 0, '', 1, 0, 0, 10000, '2022-11-14 15:17:40', 10000, '2022-11-14 15:18:20');
INSERT INTO `sys_menu` VALUES (112, 69, '分组删除', '', 'data-integrate:fileCategory:delete', 1, 0, '', 2, 0, 0, 10000, '2022-11-14 15:17:40', 10000, '2022-11-14 15:18:44');
INSERT INTO `sys_menu` VALUES (113, 69, '分页查询', '', 'data-integrate:file:page', 2, 0, '', 3, 0, 0, 10000, '2022-11-18 14:22:42', 10000, '2022-11-18 14:23:04');
INSERT INTO `sys_menu` VALUES (114, 69, '新增', '', 'data-integrate:file:save', 1, 0, '', 4, 0, 0, 10000, '2022-11-18 14:22:42', 10000, '2022-11-18 14:25:48');
INSERT INTO `sys_menu` VALUES (115, 69, '修改', '', 'data-integrate:file:info,data-integrate:file:update', 1, 0, '', 5, 0, 0, 10000, '2022-11-18 14:22:42', 10000, '2022-11-18 14:26:27');
INSERT INTO `sys_menu` VALUES (116, 69, '删除', '', 'data-integrate:file:delete', 1, 0, '', 6, 0, 0, 10000, '2022-11-18 14:22:42', 10000, '2022-11-18 14:27:04');
INSERT INTO `sys_menu` VALUES (122, 143, 'Flink 集群实例', 'data-development/cluster/index', '', 0, 0, 'icon-appstore-fill', 0, 0, 0, 10000, '2022-12-03 11:21:39', 10000, '2023-01-18 13:53:44');
INSERT INTO `sys_menu` VALUES (123, 122, '查询', '', 'data-development:cluster:page', 2, 0, '', 0, 0, 0, 10000, '2022-12-03 11:22:35', 10000, '2022-12-03 11:22:35');
INSERT INTO `sys_menu` VALUES (124, 122, '添加', '', 'data-development:cluster:save', 1, 0, '', 1, 0, 0, 10000, '2022-12-03 11:23:09', 10000, '2022-12-03 11:23:09');
INSERT INTO `sys_menu` VALUES (125, 122, '修改', '', 'data-development:cluster:info,data-development:cluster:update', 1, 0, '', 2, 0, 0, 10000, '2022-12-03 11:24:47', 10000, '2022-12-03 11:24:47');
INSERT INTO `sys_menu` VALUES (126, 122, '删除', '', 'data-development:cluster:delete', 1, 0, '', 3, 0, 0, 10000, '2022-12-03 11:25:10', 10000, '2022-12-03 11:25:10');
INSERT INTO `sys_menu` VALUES (127, 143, 'Hadoop 集群配置', 'data-development/cluster-configuration/index', '', 0, 0, 'icon-insertrowabove', 1, 0, 0, 10000, '2022-12-21 20:39:34', 10000, '2023-01-18 13:53:50');
INSERT INTO `sys_menu` VALUES (128, 127, '查询', '', 'data-development:cluster-configuration:page', 1, 0, '', 0, 0, 0, 10000, '2022-12-21 20:42:02', 10000, '2022-12-21 20:42:02');
INSERT INTO `sys_menu` VALUES (129, 127, '添加', '', 'data-development:cluster-configuration:save', 1, 0, '', 1, 0, 0, 10000, '2022-12-21 20:42:39', 10000, '2022-12-21 20:42:39');
INSERT INTO `sys_menu` VALUES (130, 127, '修改', '', 'data-development:cluster-configuration:update,data-development:cluster-configuration:info', 1, 0, '', 2, 0, 0, 10000, '2022-12-21 20:43:11', 10000, '2022-12-21 20:43:11');
INSERT INTO `sys_menu` VALUES (131, 127, '删除', '', 'data-development:cluster-configuration:delete', 1, 0, '', 3, 0, 0, 10000, '2022-12-21 20:43:35', 10000, '2022-12-21 20:43:35');
INSERT INTO `sys_menu` VALUES (132, 72, '配置中心', 'data-development/sys-config/index', '', 0, 0, 'icon-project', 7, 0, 0, 10000, '2022-12-28 17:45:56', 10000, '2023-01-14 19:13:59');
INSERT INTO `sys_menu` VALUES (133, 72, '运维中心', 'data-development/task-history/index', '', 0, 0, 'icon-send', 4, 0, 0, 10000, '2023-01-03 21:30:58', 10000, '2023-01-14 19:13:11');
INSERT INTO `sys_menu` VALUES (135, 142, '调度管理', 'data-development/schedule/index', '', 0, 0, 'icon-calendar-check', 0, 0, 0, 10000, '2023-01-14 19:11:46', 10000, '2023-01-18 13:52:27');
INSERT INTO `sys_menu` VALUES (136, 135, '查询', '', 'data-development:schedule:page', 2, 0, '', 0, 0, 0, 10000, '2023-01-14 19:17:04', 10000, '2023-01-14 19:17:04');
INSERT INTO `sys_menu` VALUES (137, 135, '新增', '', 'data-development:schedule:save', 1, 0, '', 1, 0, 0, 10000, '2023-01-14 19:17:28', 10000, '2023-01-14 19:17:28');
INSERT INTO `sys_menu` VALUES (138, 135, '编辑', '', 'data-development:schedule:info,data-development:schedule:update', 1, 0, '', 2, 0, 0, 10000, '2023-01-14 19:17:54', 10000, '2023-01-14 19:17:54');
INSERT INTO `sys_menu` VALUES (139, 135, '删除', '', 'data-development:schedule:delete', 1, 0, '', 3, 0, 0, 10000, '2023-01-14 19:18:13', 10000, '2023-01-14 19:18:13');
INSERT INTO `sys_menu` VALUES (141, 135, '执行', '', 'data-development:schedule:run', 1, 0, '', 4, 0, 0, 10000, '2023-01-17 17:05:56', 10000, '2023-01-17 17:06:26');
INSERT INTO `sys_menu` VALUES (142, 72, '调度中心', '', '', 0, 0, 'icon-calendar', 3, 0, 0, 10000, '2023-01-18 13:49:14', 10000, '2023-01-18 13:51:10');
INSERT INTO `sys_menu` VALUES (143, 72, '资源中心', '', '', 0, 0, 'icon-Partition', 6, 0, 0, 10000, '2023-01-18 13:52:46', 10000, '2023-01-18 13:53:37');
INSERT INTO `sys_menu` VALUES (144, 142, '调度记录', 'data-development/schedule-record/index', '', 0, 0, 'icon-insertrowabove', 1, 0, 0, 10000, '2023-01-18 15:59:03', 10000, '2023-01-18 15:59:22');
INSERT INTO `sys_menu` VALUES (145, 144, '查询', '', 'data-development:schedule:record:page', 2, 0, '', 0, 0, 0, 10000, '2023-01-18 16:00:04', 10000, '2023-01-18 16:00:04');
INSERT INTO `sys_menu` VALUES (146, 144, '删除', '', 'data-development:schedule:record:delete', 1, 0, '', 1, 0, 0, 10000, '2023-01-18 16:00:30', 10000, '2023-01-18 16:00:30');
INSERT INTO `sys_menu` VALUES (147, 135, '发布', '', 'data-development:schedule:release', 1, 0, '', 5, 0, 0, 10000, '2023-01-19 21:45:38', 10000, '2023-01-19 21:46:34');
INSERT INTO `sys_menu` VALUES (148, 135, '取消发布', '', 'data-development:schedule:cancle', 1, 0, '', 6, 0, 0, 10000, '2023-01-19 21:47:00', 10000, '2023-01-19 21:47:00');
INSERT INTO `sys_menu` VALUES (149, 103, '修改', '', 'data-service:api-group:info,data-service:api-group:update', 2, 0, '', 1, 0, 0, 10000, '2023-01-30 11:41:31', 10000, '2023-02-06 16:04:35');
INSERT INTO `sys_menu` VALUES (150, 103, '删除', '', 'data-service:api-group:delete', 2, 0, '', 3, 0, 0, 10000, '2023-01-30 11:42:01', 10000, '2023-02-06 16:04:50');
INSERT INTO `sys_menu` VALUES (151, 103, '添加', '', 'data-service:api-group:save', 2, 0, '', 2, 0, 0, 10000, '2023-01-30 11:43:09', 10000, '2023-01-30 11:43:09');
INSERT INTO `sys_menu` VALUES (152, 103, '查看API', '', 'data-service:api-config:page', 2, 0, '', 0, 0, 0, 10000, '2023-02-06 16:04:24', 10000, '2023-02-06 16:11:16');
INSERT INTO `sys_menu` VALUES (153, 103, '新增API', '', 'data-service:api-config:save', 1, 0, '', 0, 0, 0, 10000, '2023-02-06 16:12:02', 10000, '2023-02-06 16:12:02');
INSERT INTO `sys_menu` VALUES (154, 103, '修改API', '', 'data-service:api-config:update,data-service:api-config:info', 1, 0, '', 0, 0, 0, 10000, '2023-02-06 16:12:33', 10000, '2023-02-06 16:12:33');
INSERT INTO `sys_menu` VALUES (155, 103, '删除API', '', 'data-service:api-config:delete', 1, 0, '', 0, 0, 0, 10000, '2023-02-06 16:12:58', 10000, '2023-02-14 09:56:38');
INSERT INTO `sys_menu` VALUES (156, 103, '上线', '', 'data-service:api-config:online', 1, 0, '', 0, 0, 0, 10000, '2023-02-15 11:15:52', 10000, '2023-02-15 11:16:23');
INSERT INTO `sys_menu` VALUES (157, 103, '下线', '', 'data-service:api-config:offline', 1, 0, '', 0, 0, 0, 10000, '2023-02-15 11:16:37', 10000, '2023-02-15 11:16:37');
INSERT INTO `sys_menu` VALUES (158, 102, 'API 权限', 'data-service/app/index', '', 0, 0, 'icon-propertysafety', 1, 0, 0, 10000, '2023-02-16 14:48:26', 10000, '2023-02-16 14:56:47');
INSERT INTO `sys_menu` VALUES (159, 158, '查询', '', 'data-service:app:page', 2, 0, '', 0, 0, 0, 10000, '2023-02-16 14:50:15', 10000, '2023-02-16 14:50:15');
INSERT INTO `sys_menu` VALUES (160, 158, '保存', '', 'data-service:app:save', 1, 0, '', 1, 0, 0, 10000, '2023-02-16 14:50:39', 10000, '2023-02-16 14:50:39');
INSERT INTO `sys_menu` VALUES (161, 158, '更新', '', 'data-service:app:update,data-service:app:info', 1, 0, '', 2, 0, 0, 10000, '2023-02-16 14:51:10', 10000, '2023-02-16 14:51:10');
INSERT INTO `sys_menu` VALUES (162, 158, '删除', '', 'data-service:app:delete', 1, 0, '', 3, 0, 0, 10000, '2023-02-16 14:51:28', 10000, '2023-02-16 14:51:39');
INSERT INTO `sys_menu` VALUES (163, 158, '授权', '', 'data-service:app:auth', 1, 0, '', 4, 0, 0, 10000, '2023-02-17 11:37:39', 10000, '2023-02-17 11:37:39');
INSERT INTO `sys_menu` VALUES (164, 158, '取消授权', '', 'data-service:app:cancel-auth', 1, 0, '', 5, 0, 0, 10000, '2023-02-20 14:11:42', 10000, '2023-02-20 14:13:31');
INSERT INTO `sys_menu` VALUES (165, 102, 'API 日志', 'data-service/log/index', '', 0, 0, 'icon-detail', 2, 0, 0, 10000, '2023-02-22 14:47:37', 10000, '2023-02-22 14:48:31');
INSERT INTO `sys_menu` VALUES (166, 165, '查询', '', 'data-service:log:page', 2, 0, '', 0, 0, 0, 10000, '2023-02-22 14:49:07', 10000, '2023-02-22 14:49:07');
INSERT INTO `sys_menu` VALUES (167, 165, '删除', '', 'data-service:log:delete', 1, 0, '', 1, 0, 0, 10000, '2023-02-22 14:49:25', 10000, '2023-02-22 14:49:25');
INSERT INTO `sys_menu` VALUES (168, 94, '查询', '', 'data-governance:metadata-collect:page', 2, 0, '', 0, 0, 0, 10000, '2023-04-03 10:39:26', 10000, '2023-04-03 10:39:26');
INSERT INTO `sys_menu` VALUES (169, 94, '编辑', '', 'data-governance:metadata-collect:info,data-governance:metadata-collect:update', 1, 0, '', 1, 0, 0, 10000, '2023-04-03 10:40:06', 10000, '2023-04-03 10:40:06');
INSERT INTO `sys_menu` VALUES (170, 94, '保存', '', 'data-governance:metadata-collect:save', 1, 0, '', 2, 0, 0, 10000, '2023-04-03 10:40:25', 10000, '2023-04-03 10:40:42');
INSERT INTO `sys_menu` VALUES (171, 94, '删除', '', 'data-governance:metadata-collect:delete', 1, 0, '', 3, 0, 0, 10000, '2023-04-03 10:41:05', 10000, '2023-04-03 10:41:05');
INSERT INTO `sys_menu` VALUES (172, 94, '发布', '', 'data-governance:metadata-collect:release', 1, 0, '', 4, 0, 0, 10000, '2023-04-05 12:21:56', 10000, '2023-04-05 12:21:56');
INSERT INTO `sys_menu` VALUES (173, 94, '取消发布', '', 'data-governance:metadata-collect:cancel', 1, 0, '', 5, 0, 0, 10000, '2023-04-05 12:22:19', 10000, '2023-04-05 12:22:19');
INSERT INTO `sys_menu` VALUES (174, 94, '执行', '', 'data-governance:metadata-collect:hand-run', 1, 0, '', 6, 0, 0, 10000, '2023-04-06 09:59:53', 10000, '2023-04-06 09:59:53');
INSERT INTO `sys_menu` VALUES (175, 91, '数据标准', '', '', 0, 0, 'icon-calculator', 2, 0, 0, 10000, '2023-05-08 09:39:12', 10000, '2023-05-08 09:39:25');
INSERT INTO `sys_menu` VALUES (176, 175, '标准管理', 'data-governance/data-standard/index', '', 0, 0, 'icon-wallet', 0, 0, 0, 10000, '2023-05-08 09:41:37', 10000, '2023-05-08 09:42:17');
INSERT INTO `sys_menu` VALUES (177, 91, '数据质量', '', '', 0, 0, 'icon-creditcard', 3, 0, 0, 10000, '2023-05-28 09:46:54', 10000, '2023-05-28 09:46:54');
INSERT INTO `sys_menu` VALUES (178, 177, '质量规则', 'data-governance/quality-rule/index', '', 0, 0, 'icon-USB-fill', 0, 0, 0, 10000, '2023-05-28 09:47:52', 10000, '2023-05-28 09:47:52');
INSERT INTO `sys_menu` VALUES (179, 177, '规则配置', 'data-governance/quality-rule/rule-category', '', 0, 0, 'icon-formatpainter-fill', 1, 0, 0, 10000, '2023-05-29 10:40:34', 10000, '2023-05-29 10:40:56');
INSERT INTO `sys_menu` VALUES (180, 177, '质量任务', 'data-governance/quality-task/index', '', 0, 0, 'icon-database-fill', 2, 0, 0, 10000, '2023-06-24 21:43:46', 10000, '2023-06-24 21:44:14');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `deleted` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识  0：正常   1：已删除',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 184 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 54, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (2, 1, 55, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (3, 1, 57, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (4, 1, 61, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (5, 1, 58, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (6, 1, 59, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (7, 1, 60, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (8, 1, 62, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (9, 1, 56, 0, 1, 10000, '2022-10-08 11:16:41', 10000, '2022-10-08 11:16:41');
INSERT INTO `sys_role_menu` VALUES (10, 1, 63, 0, 1, 10000, '2022-10-17 13:49:02', 10000, '2022-10-17 13:49:02');
INSERT INTO `sys_role_menu` VALUES (11, 2, 1, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (12, 2, 2, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (13, 2, 67, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (14, 2, 68, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (15, 2, 80, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (16, 2, 81, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (17, 2, 82, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (18, 2, 83, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (19, 2, 69, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (20, 2, 110, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (21, 2, 111, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (22, 2, 112, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (23, 2, 113, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (24, 2, 114, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (25, 2, 115, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (26, 2, 116, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (27, 2, 70, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (28, 2, 85, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (29, 2, 84, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (30, 2, 86, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (31, 2, 87, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (32, 2, 88, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (33, 2, 89, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (34, 2, 90, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (35, 2, 71, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (36, 2, 72, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (37, 2, 73, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (38, 2, 74, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (39, 2, 75, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (40, 2, 76, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (41, 2, 77, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (42, 2, 78, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (43, 2, 79, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (44, 2, 91, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (45, 2, 92, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (46, 2, 93, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (47, 2, 94, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (48, 2, 97, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (49, 2, 98, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (50, 2, 99, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (51, 2, 100, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (52, 2, 101, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (53, 2, 102, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (54, 2, 103, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (55, 2, 104, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (56, 2, 105, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (57, 2, 106, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (58, 2, 107, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (59, 2, 108, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (60, 2, 109, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (61, 2, 54, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (62, 2, 55, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (63, 2, 57, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (64, 2, 61, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (65, 2, 58, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (66, 2, 59, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (67, 2, 60, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (68, 2, 62, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (69, 2, 56, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (70, 2, 63, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (71, 2, 66, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (72, 2, 33, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (73, 2, 40, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (74, 2, 42, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (75, 2, 43, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (76, 2, 44, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (77, 2, 45, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (78, 2, 46, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (79, 2, 41, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (80, 2, 38, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (81, 2, 39, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (82, 2, 3, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (83, 2, 28, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (84, 2, 29, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (85, 2, 30, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (86, 2, 31, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (87, 2, 32, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (88, 2, 18, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (89, 2, 19, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (90, 2, 20, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (91, 2, 21, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (92, 2, 22, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (93, 2, 13, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (94, 2, 14, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (95, 2, 15, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (96, 2, 16, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (97, 2, 17, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (98, 2, 23, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (99, 2, 24, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (100, 2, 25, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (101, 2, 26, 0, 1, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (102, 2, 27, 0, 0, 10000, '2022-11-25 20:01:22', 10000, '2022-11-25 20:01:22');
INSERT INTO `sys_role_menu` VALUES (103, 3, 54, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (104, 3, 55, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (105, 3, 56, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (106, 3, 1, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (107, 3, 2, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (108, 3, 67, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (109, 3, 68, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (110, 3, 80, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (111, 3, 81, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (112, 3, 82, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (113, 3, 83, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (114, 3, 69, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (115, 3, 110, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (116, 3, 111, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (117, 3, 112, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (118, 3, 113, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (119, 3, 114, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (120, 3, 115, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (121, 3, 116, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (122, 3, 70, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (123, 3, 85, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (124, 3, 84, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (125, 3, 86, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (126, 3, 87, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (127, 3, 88, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (128, 3, 89, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (129, 3, 90, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (130, 3, 71, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (131, 3, 72, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (132, 3, 73, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (133, 3, 74, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (134, 3, 75, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (135, 3, 76, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (136, 3, 77, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (137, 3, 78, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (138, 3, 79, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (139, 3, 91, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (140, 3, 92, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (141, 3, 93, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (142, 3, 94, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (143, 3, 97, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (144, 3, 98, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (145, 3, 99, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (146, 3, 100, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (147, 3, 101, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (148, 3, 102, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (149, 3, 103, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (150, 3, 104, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (151, 3, 105, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (152, 3, 106, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (153, 3, 107, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (154, 3, 108, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (155, 3, 109, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (156, 3, 57, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (157, 3, 61, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (158, 3, 62, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (159, 3, 63, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (160, 3, 3, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (161, 3, 28, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (162, 3, 29, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (163, 3, 30, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (164, 3, 31, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (165, 3, 32, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (166, 3, 18, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (167, 3, 19, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (168, 3, 20, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (169, 3, 21, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (170, 3, 22, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (171, 3, 13, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (172, 3, 14, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (173, 3, 15, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (174, 3, 16, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (175, 3, 17, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (176, 3, 23, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (177, 3, 24, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (178, 3, 25, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (179, 3, 26, 0, 1, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (180, 3, 27, 0, 0, 10000, '2022-11-25 20:10:26', 10000, '2022-11-25 20:10:26');
INSERT INTO `sys_role_menu` VALUES (181, 3, 26, 0, 0, 10000, '2022-11-26 12:07:15', 10000, '2022-11-26 12:07:15');
INSERT INTO `sys_role_menu` VALUES (182, 3, 58, 0, 0, 10000, '2022-11-26 12:10:00', 10000, '2022-11-26 12:10:00');
INSERT INTO `sys_role_menu` VALUES (183, 3, 59, 0, 0, 10000, '2022-11-26 12:10:00', 10000, '2022-11-26 12:10:00');
INSERT INTO `sys_role_menu` VALUES (184, 3, 60, 0, 0, 10000, '2022-11-26 12:10:00', 10000, '2022-11-26 12:10:00');

SET FOREIGN_KEY_CHECKS = 1;

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
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `sys_dict_data` VALUES (139, 40, 'POSTGRESQL', '3', '', 3, 0, 0, 10000, '2023-06-13 17:12:18', 10000, '2023-06-13 17:12:18');
INSERT INTO `sys_dict_data` VALUES (140, 12, 'DORIS', '16', '', 16, 0, 0, 10000, '2023-06-19 17:46:51', 10000, '2023-06-19 17:46:51');
INSERT INTO `sys_dict_data` VALUES (141, 40, 'DORIS', '16', '', 16, 0, 0, 10000, '2023-06-23 10:24:52', 10000, '2023-06-23 10:24:52');
INSERT INTO `sys_dict_data` VALUES (142, 40, 'GREENPLUM', '6', '', 6, 0, 0, 10000, '2023-06-23 10:25:53', 10000, '2023-06-23 10:25:53');

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
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型' ROW_FORMAT = DYNAMIC;

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

SET FOREIGN_KEY_CHECKS = 1;
