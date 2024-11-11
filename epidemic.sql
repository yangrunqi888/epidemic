/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : epidemic

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 12/10/2024 21:23:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for antigen
-- ----------------------------
DROP TABLE IF EXISTS `antigen`;
CREATE TABLE `antigen`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `collect_time` date NOT NULL COMMENT '采集日期',
  `result` int(0) NULL DEFAULT NULL COMMENT '0阳性，1阴性，2未完成',
  `picture_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '抗原结果照片路径',
  PRIMARY KEY (`id_number`, `collect_time`) USING BTREE,
  CONSTRAINT `fk_antigen_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of antigen
-- ----------------------------
INSERT INTO `antigen` VALUES ('320211200107044531', '2023-03-02', 1, '93f3c546-5079-4bc3-8111-c29683c4eb5d.jpg');
INSERT INTO `antigen` VALUES ('320211200107044531', '2023-03-04', 1, '1068d024-bf11-4cf6-a9cd-ae0b6af8b2cb.jpg');
INSERT INTO `antigen` VALUES ('320211200107044531', '2023-04-19', 1, '77fa3c77-ad6a-43cf-86ee-534bcd9780de.jpg');
INSERT INTO `antigen` VALUES ('666666666666666666', '2023-02-28', 1, '8d076d37-4742-42de-9f5e-f7424f79876c.jpg');
INSERT INTO `antigen` VALUES ('666666666666666666', '2023-03-02', 0, 'b6ad2425-558b-415c-9f81-4e40a5b979a6.jpg');
INSERT INTO `antigen` VALUES ('666666666666666666', '2023-03-07', 1, '2585b15b-6e65-47ee-b94d-907308267400.jpg');

-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority`  (
  `user_type` int(0) NULL DEFAULT NULL,
  `menu_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of authority
-- ----------------------------
INSERT INTO `authority` VALUES (1, '100');
INSERT INTO `authority` VALUES (1, '101');
INSERT INTO `authority` VALUES (1, '102');
INSERT INTO `authority` VALUES (1, '103');
INSERT INTO `authority` VALUES (1, '105');
INSERT INTO `authority` VALUES (1, '106');
INSERT INTO `authority` VALUES (1, '108');
INSERT INTO `authority` VALUES (1, '109');
INSERT INTO `authority` VALUES (1, '110');
INSERT INTO `authority` VALUES (1, '112');
INSERT INTO `authority` VALUES (1, '113');
INSERT INTO `authority` VALUES (2, '100');
INSERT INTO `authority` VALUES (2, '101');
INSERT INTO `authority` VALUES (2, '102');
INSERT INTO `authority` VALUES (2, '108');
INSERT INTO `authority` VALUES (2, '106');
INSERT INTO `authority` VALUES (2, '105');
INSERT INTO `authority` VALUES (2, '109');
INSERT INTO `authority` VALUES (2, '110');
INSERT INTO `authority` VALUES (2, '112');
INSERT INTO `authority` VALUES (2, '113');
INSERT INTO `authority` VALUES (3, '100');
INSERT INTO `authority` VALUES (3, '101');
INSERT INTO `authority` VALUES (3, '103');
INSERT INTO `authority` VALUES (3, '105');
INSERT INTO `authority` VALUES (3, '106');
INSERT INTO `authority` VALUES (3, '108');
INSERT INTO `authority` VALUES (3, '109');
INSERT INTO `authority` VALUES (3, '110');
INSERT INTO `authority` VALUES (3, '112');
INSERT INTO `authority` VALUES (3, '113');
INSERT INTO `authority` VALUES (3, '102');
INSERT INTO `authority` VALUES (0, '100');
INSERT INTO `authority` VALUES (0, '101');
INSERT INTO `authority` VALUES (0, '103');
INSERT INTO `authority` VALUES (0, '105');
INSERT INTO `authority` VALUES (0, '106');
INSERT INTO `authority` VALUES (0, '108');
INSERT INTO `authority` VALUES (0, '109');
INSERT INTO `authority` VALUES (0, '110');
INSERT INTO `authority` VALUES (0, '112');
INSERT INTO `authority` VALUES (0, '113');
INSERT INTO `authority` VALUES (0, '2');
INSERT INTO `authority` VALUES (0, '4');
INSERT INTO `authority` VALUES (0, '3');
INSERT INTO `authority` VALUES (0, '102');
INSERT INTO `authority` VALUES (4, '100');
INSERT INTO `authority` VALUES (5, '100');
INSERT INTO `authority` VALUES (4, '112');
INSERT INTO `authority` VALUES (4, '113');
INSERT INTO `authority` VALUES (5, '112');
INSERT INTO `authority` VALUES (5, '113');
INSERT INTO `authority` VALUES (6, '100');
INSERT INTO `authority` VALUES (6, '201');
INSERT INTO `authority` VALUES (6, '202');
INSERT INTO `authority` VALUES (6, '203');
INSERT INTO `authority` VALUES (6, '204');
INSERT INTO `authority` VALUES (6, '205');

-- ----------------------------
-- Table structure for back_home
-- ----------------------------
DROP TABLE IF EXISTS `back_home`;
CREATE TABLE `back_home`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `current_location` char(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前所在地',
  `back_date` date NOT NULL COMMENT '计划返乡时间',
  `health_code_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '健康码截图路径',
  `travel_code_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '行程码截图路径',
  `state` int(0) NULL DEFAULT 2 COMMENT '审核状态，0未通过，1已通过，2未审核',
  `employer_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人工号',
  `demand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返乡要求',
  PRIMARY KEY (`id_number`, `back_date`) USING BTREE,
  INDEX `fk_back_home_grid_staff_1`(`employer_number`) USING BTREE,
  CONSTRAINT `fk_back_home_grid_staff_1` FOREIGN KEY (`employer_number`) REFERENCES `staff` (`employee_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_back_home_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of back_home
-- ----------------------------
INSERT INTO `back_home` VALUES ('320211200107044531', '江苏省南京市', '2023-03-06', 'e57f346b-fa23-4dbf-9009-9ebad8bc6373.jpg', '20621303-2a2a-4550-ad1e-08476e37ee92.jpg', 0, 'admin', 'ky');
INSERT INTO `back_home` VALUES ('320211200107044531', '江苏省南京市', '2023-03-08', 'ded8baa9-aaf5-4e3f-ae9a-1e6783021e32.jpg', 'a5b4d1bc-514d-4789-8f21-8482033846d5.jpg', 0, 'admin', '6');
INSERT INTO `back_home` VALUES ('320211200107044531', '河北省石家庄市', '2023-04-06', '6f717bfe-5eb3-40d1-a758-bb8b3a6d4d65.jpg', '153fc819-9f45-422f-b97c-25a965788279.jpg', 1, '001', '6');
INSERT INTO `back_home` VALUES ('320211200107044531', '江苏省南京市', '2023-04-23', '5423d892-87bb-41cb-a5d7-8c90a24e0939.jpg', 'ad21fbf7-2273-463e-bae4-67fbe51aad4f.jpg', 1, '003', '具体要求。。。。');
INSERT INTO `back_home` VALUES ('320211200107044531', '江苏省南京市', '2023-04-26', '147581ec-be68-4402-9208-b89e75383591.jpg', '2d76c780-2828-4312-8861-ceb25394ad2c.jpg', 1, '001', '通过');
INSERT INTO `back_home` VALUES ('320211200107044531', '江西省新余市', '2023-04-27', '4cf80436-6691-425d-a2fd-0a3180d5e4b1.jpg', 'f5e08e74-fc91-48d5-ac84-e8f9d382ae79.jpg', 1, '003', '123');
INSERT INTO `back_home` VALUES ('320211200107044531', '山西省大同市', '2023-04-28', '244c776c-7a80-4584-b4e5-b2289f41288e.jpg', '5b8ae17e-70d9-4586-8e90-52050eb62f7e.jpg', 1, 'admin', '123213');
INSERT INTO `back_home` VALUES ('320211200107044531', '浙江省杭州市', '2024-09-13', 'ae74b19f-4f00-4a4e-8a76-b27228240828.jpg', '66db903e-2849-4105-959e-a0218b641b57.jpg', 1, '001', 'yo。。。。');
INSERT INTO `back_home` VALUES ('320211200107044531', '内蒙古自治区包头市', '2024-09-18', 'bea5d8ca-9da9-408f-81bf-e8f14bca8f1a.jpg', '6b0e23f7-d620-43cb-99ce-37594aafc908.jpg', 1, '001', '1');
INSERT INTO `back_home` VALUES ('666666666666666666', '黑龙江省伊春市', '2023-03-07', '394421a7-3f5f-4e4a-90cc-0dc90b0317b7.jpg', 'a9e09d72-cd98-4f70-9732-f689385fd052.jpg', 2, NULL, NULL);
INSERT INTO `back_home` VALUES ('666666666666666666', '辽宁省丹东市', '2023-03-09', '7855518d-a3a5-40c5-baf9-fd1f48b2ad8d.jpg', 'f94a5c7f-8ace-4fac-9cf3-fd3f0f1e9aa0.jpg', 2, NULL, NULL);

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `building_number` int(0) NOT NULL COMMENT '楼栋号',
  `per_floor_room_number` int(0) NULL DEFAULT NULL COMMENT '每层户数',
  `floor_number` int(0) NULL DEFAULT NULL COMMENT '楼层数',
  `header` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼栋长',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `grid_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属网格号',
  `is_del` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`building_number`) USING BTREE,
  INDEX `fk_building_grid_1`(`grid_number`) USING BTREE,
  INDEX `fk_building_resident_1`(`header`) USING BTREE,
  CONSTRAINT `fk_building_grid_1` FOREIGN KEY (`grid_number`) REFERENCES `grid` (`grid_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_building_resident_1` FOREIGN KEY (`header`) REFERENCES `resident` (`id_number`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of building
-- ----------------------------
INSERT INTO `building` VALUES (1, 2, 6, '320212200102060512', '', '1', 0);
INSERT INTO `building` VALUES (39, 2, 6, '320211200107044531', NULL, '2', 0);
INSERT INTO `building` VALUES (106, 4, 30, '322222222222222222', NULL, '1', 0);

-- ----------------------------
-- Table structure for complaint
-- ----------------------------
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint`  (
  `id` char(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` int(0) NULL DEFAULT NULL COMMENT '0建议，1投诉',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `complaint_time` datetime(0) NULL DEFAULT NULL COMMENT '投诉时间',
  `complaint_id` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理人身份证，为空表示匿名',
  `employee_id` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理人工号',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '处理结果',
  `state` int(0) NULL DEFAULT 0 COMMENT '0未处理，1已处理',
  `comment` double NULL DEFAULT 0 COMMENT '用户评价，1-5星',
  `handle_time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  `anonymous` int(0) NULL DEFAULT NULL COMMENT '是否匿名',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_complaint_staff_1`(`employee_id`) USING BTREE,
  CONSTRAINT `fk_complaint_staff_1` FOREIGN KEY (`employee_id`) REFERENCES `staff` (`employee_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of complaint
-- ----------------------------
INSERT INTO `complaint` VALUES ('1629338554082328578', 1, '关于小区疫情期间表现的投诉', '您好，我所居住的小区在疫情期间，小区进出口的消毒措施严重不足。小区没有专门的消毒人员，也没有专门的消毒设备，只有保安不定期的用水管冲洗地面。请物业公司立即加强消毒措施，确保小区业主的生命安全。', '2023-02-25 12:32:10', '320211200107044531', '001', '尊敬的业主，感谢您对小区疫情防控工作的关注和支持。我们已经向小区保安下发了专门的消毒剂和设备，并进行了专业的培训，保安将在每天的不同时间段进行消毒。同时，我们也会加强对进出小区人员的登记和测量体温，确保小区业主的生命安全。再次感谢您对物业工作的关注和支持。', 1, 0, '2023-02-25 14:24:11', 1);
INSERT INTO `complaint` VALUES ('1632252777506627586', 0, '12321', '3123123123', '2023-03-05 13:32:43', '320211200107044531', 'admin', '12312321', 1, 4, '2023-03-05 14:48:05', 1);
INSERT INTO `complaint` VALUES ('1632252814374559746', 1, '123', '21312321', '2023-03-05 13:32:51', '320211200107044531', NULL, NULL, 0, 0, NULL, 0);
INSERT INTO `complaint` VALUES ('1632397057332154370', 0, '213123', '23123', '2023-03-05 23:06:02', '666666666666666666', NULL, NULL, 0, 0, NULL, 1);
INSERT INTO `complaint` VALUES ('1649696939713613826', 0, '123', '21312', '2023-04-22 16:49:35', '320211200107044531', '003', '32131231', 1, 3, '2023-04-22 16:50:12', 0);
INSERT INTO `complaint` VALUES ('1650735548793188354', 1, '标题', '内容', '2023-04-25 13:36:39', '320211200107044531', '001', '输入处理结果', 1, 4, '2023-04-25 13:37:17', 1);
INSERT INTO `complaint` VALUES ('1651039711452278785', 0, '213', '312', '2023-04-26 09:45:17', '320211200107044531', '003', '3213', 1, 3, '2023-04-26 09:45:44', 1);
INSERT INTO `complaint` VALUES ('1651148143618547714', 0, '123213', '321312', '2023-04-26 16:56:09', '320211200107044531', 'admin', '3213213', 1, 3, '2023-04-26 16:56:18', 1);

-- ----------------------------
-- Table structure for go_out
-- ----------------------------
DROP TABLE IF EXISTS `go_out`;
CREATE TABLE `go_out`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `out_time` datetime(0) NOT NULL COMMENT '出门时间',
  `back_time` datetime(0) NULL DEFAULT NULL COMMENT '返回时间',
  `out_total_time` double NULL DEFAULT NULL COMMENT '外出时长（分钟）',
  `reason` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事由',
  `out_guard_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出小区批准人',
  `back_guard_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入小区批准人',
  `state` int(0) NULL DEFAULT 0 COMMENT '0是外出中，1是已返回',
  PRIMARY KEY (`id_number`, `out_time`) USING BTREE,
  INDEX `fk_out_guard_1`(`out_guard_number`) USING BTREE,
  INDEX `fk_out_guard_2`(`back_guard_number`) USING BTREE,
  CONSTRAINT `fk_out_guard_1` FOREIGN KEY (`out_guard_number`) REFERENCES `guard` (`guard_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_out_guard_2` FOREIGN KEY (`back_guard_number`) REFERENCES `guard` (`guard_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_out_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of go_out
-- ----------------------------
INSERT INTO `go_out` VALUES ('320211200107044531', '2023-01-23 21:40:00', '2023-01-23 22:40:18', 60.3, '采购物资', 'G001', 'G002', 1);
INSERT INTO `go_out` VALUES ('320211200107044531', '2023-04-26 16:49:00', '2023-04-26 16:49:05', 0.07775, '131', NULL, NULL, 1);
INSERT INTO `go_out` VALUES ('322222222222222222', '2023-04-22 16:39:18', '2023-04-22 16:39:32', 0.22656666666666667, '采购物资', NULL, NULL, 1);

-- ----------------------------
-- Table structure for grid
-- ----------------------------
DROP TABLE IF EXISTS `grid`;
CREATE TABLE `grid`  (
  `grid_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网格编号',
  `area` int(0) NULL DEFAULT NULL COMMENT '网格面积',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网格范围描述',
  `leadership` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网格长工号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`grid_number`) USING BTREE,
  INDEX `fk_grid_grid_staff_1`(`leadership`) USING BTREE,
  CONSTRAINT `fk_grid_grid_staff_1` FOREIGN KEY (`leadership`) REFERENCES `staff` (`employee_number`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grid
-- ----------------------------
INSERT INTO `grid` VALUES ('1', 20, '1-20号', '001', NULL);
INSERT INTO `grid` VALUES ('2', 30, '21-40号', '001', NULL);

-- ----------------------------
-- Table structure for grid_manage
-- ----------------------------
DROP TABLE IF EXISTS `grid_manage`;
CREATE TABLE `grid_manage`  (
  `grid_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网格号',
  `employee_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工作人员编号',
  PRIMARY KEY (`grid_number`, `employee_number`) USING BTREE,
  INDEX `fk_grid_manage_grid_staff_1`(`employee_number`) USING BTREE,
  CONSTRAINT `fk_grid_manage_grid_1` FOREIGN KEY (`grid_number`) REFERENCES `grid` (`grid_number`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_grid_manage_grid_staff_1` FOREIGN KEY (`employee_number`) REFERENCES `staff` (`employee_number`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grid_manage
-- ----------------------------
INSERT INTO `grid_manage` VALUES ('1', '001');
INSERT INTO `grid_manage` VALUES ('2', '001');
INSERT INTO `grid_manage` VALUES ('1', '003');
INSERT INTO `grid_manage` VALUES ('2', '003');

-- ----------------------------
-- Table structure for guard
-- ----------------------------
DROP TABLE IF EXISTS `guard`;
CREATE TABLE `guard`  (
  `guard_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网格员工号',
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `name` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `gender` int(0) NULL DEFAULT NULL COMMENT '性别，0为男，1为女',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `politics_status` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `education` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文化程度',
  `position` int(0) NULL DEFAULT NULL COMMENT '4保安队长，5保安',
  `guard_start` date NULL DEFAULT NULL COMMENT '任职时间',
  `password` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `prohibit_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(0) NULL DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`guard_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guard
-- ----------------------------
INSERT INTO `guard` VALUES ('G001', '123456786543223456', '葛弧', 1, '2003-12-30', '群众', NULL, 4, '2023-02-27', 'f379eaf3c831b04de153469d1bec345e', NULL, NULL, '747c586e-9df6-4951-bf8d-600aaf920809.jpg', NULL, 0);
INSERT INTO `guard` VALUES ('G002', '123123123123456789', '张三', 0, '2014-07-16', '', '小学', 5, '2021-06-08', 'f379eaf3c831b04de153469d1bec345e', '', '', 'default.jpg', NULL, 0);
INSERT INTO `guard` VALUES ('G003', '212121212121212121', '杨润琦', 0, '2023-03-01', '', '小学', 5, '2023-03-02', 'f379eaf3c831b04de153469d1bec345e', '', '', 'D:/yiqing/prohibit/guard/default.jpg', NULL, 0);

-- ----------------------------
-- Table structure for live
-- ----------------------------
DROP TABLE IF EXISTS `live`;
CREATE TABLE `live`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `building_number` int(0) NULL DEFAULT NULL COMMENT '楼栋号',
  `room_number` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房间号',
  `state` int(0) NULL DEFAULT NULL COMMENT '0为户主，1为租借人员',
  `is_del` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id_number`) USING BTREE,
  INDEX `fk_live_building_1`(`building_number`) USING BTREE,
  CONSTRAINT `fk_live_building_1` FOREIGN KEY (`building_number`) REFERENCES `building` (`building_number`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_live_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of live
-- ----------------------------
INSERT INTO `live` VALUES ('320211200107044531', 39, '601', 0, 0);
INSERT INTO `live` VALUES ('320212200102060512', 1, '602', 1, 0);
INSERT INTO `live` VALUES ('322222222222222222', 106, '602', 1, 0);
INSERT INTO `live` VALUES ('450921200010041625', 1, '602', 1, 1);
INSERT INTO `live` VALUES ('666666666666666666', 1, '601', 1, 1);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menu_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点名字',
  `icon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `label` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由',
  `url` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `parent_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父节点id',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', NULL, 's-tools', '高级管理', NULL, NULL, NULL);
INSERT INTO `menu` VALUES ('100', 'home', 's-home', '首页', '/home', 'home.vue', NULL);
INSERT INTO `menu` VALUES ('101', 'quarantine', 'user-solid', '隔离人员管理', '/quarantine', 'quarantine.vue', NULL);
INSERT INTO `menu` VALUES ('102', 'backHome', 's-claim', '返乡审批', '/backHome', 'backHome.vue', NULL);
INSERT INTO `menu` VALUES ('103', 'complaint', 's-release\r\n', '投诉与建议处理', '/complaint', 'complaint.vue', NULL);
INSERT INTO `menu` VALUES ('104', NULL, 's-order', '基本信息管理', '', NULL, NULL);
INSERT INTO `menu` VALUES ('105', 'resident', 'user-solid', '居民信息', '/resident', 'resident.vue', '104');
INSERT INTO `menu` VALUES ('106', 'building', 'office-building', '楼栋地址', '/building', 'building.vue', '104');
INSERT INTO `menu` VALUES ('107', '', 's-opportunity', '居民健康信息管理', NULL, NULL, NULL);
INSERT INTO `menu` VALUES ('108', 'nucleicAcid', 'edit', '核酸信息', '/nucleicAcid', 'nucleicAcid.vue', '107');
INSERT INTO `menu` VALUES ('109', 'antigen', 'edit', '抗原信息', '/antigen', 'antigen.vue', '107');
INSERT INTO `menu` VALUES ('110', 'temperature', 'edit', '体温信息', '/temperature', 'temperature.vue', '107');
INSERT INTO `menu` VALUES ('111', NULL, 's-promotion', '出入管理', NULL, NULL, '');
INSERT INTO `menu` VALUES ('112', 'goOut', 'edit', '住户外出', '/goOut', 'goOut.vue', '111');
INSERT INTO `menu` VALUES ('113', 'visit', 'edit', '访客登记', '/visit', 'visit.vue', '111');
INSERT INTO `menu` VALUES ('2', 'gridManage', 'location', '网格划分', '/gridManage', 'gridManage.vue', '1');
INSERT INTO `menu` VALUES ('200', NULL, 's-claim', '信息上报', NULL, NULL, NULL);
INSERT INTO `menu` VALUES ('201', 'userAcid', 'edit', '核酸上报', '/userAcid', 'userAcid.vue', '200');
INSERT INTO `menu` VALUES ('202', 'userantigen', 'edit', '抗原上报', '/userantigen', 'userantigen.vue', '200');
INSERT INTO `menu` VALUES ('203', 'userTemperature', 'edit', '体温上报', '/userTemperature', 'userTemperature.vue', '200');
INSERT INTO `menu` VALUES ('204', 'userBackHome', 's-ticket', '返乡报备', '/userBackHome', 'userBackHome.vue', NULL);
INSERT INTO `menu` VALUES ('205', 'userComplaint', 's-order', '投诉与建议', '/userComplaint', 'userComplaint.vue', NULL);
INSERT INTO `menu` VALUES ('3', 'staffManage', 'user-solid', '网格员管理', '/staffManage', 'staffManage.vue', '1');
INSERT INTO `menu` VALUES ('4', 'guardManage', 'user-solid', '保安管理', '/guardManage', 'guardManage.vue', '1');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint(0) NOT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `announce_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `employee_id` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布人工号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_notice_staff_1`(`employee_id`) USING BTREE,
  CONSTRAINT `fk_notice_staff_1` FOREIGN KEY (`employee_id`) REFERENCES `staff` (`employee_number`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1649392263092867073, '关于小区物业将进一步加强小区的防疫措施的通知', '<p style=\"text-align: start;\"> &nbsp; &nbsp; &nbsp; 尊敬的业主：为了做好新型冠状病毒感染的肺炎疫情防控工作，小区物业将进一步加强小区的防疫措施，特此通知：</p><p style=\"text-align: start;\"> &nbsp; &nbsp;1. 所有进出小区的业主和访客，必须佩戴口罩并测量体温，符合相关条件方可进入小区。</p><p style=\"text-align: start;\"> &nbsp; &nbsp;2. 每日定期消毒公共区域，包括楼道、电梯、门禁、垃圾桶等公共设施。</p><p style=\"text-align: start;\"> &nbsp; &nbsp;3. 居民应自觉避免聚集，不参加聚会、聚餐等活动，不随意外出，做好自我防护。</p>', '2023-04-21 20:38:54', 'admin');
INSERT INTO `notice` VALUES (1649394757617422337, '关于近期新型冠状病毒感染的最新情况', '<p style=\"text-align: start;\"> &nbsp; &nbsp;近期新型冠状病毒感染的肺炎疫情在全国范围内持续蔓延，小区物业将加强小区疫情防控工作，特此通知：</p><ol><li style=\"text-align: start;\"><strong> &nbsp; &nbsp;严格执行进出小区人员登记、测量体温等疫情防控措施，保障小区居民的生命安全。</strong></li><li style=\"text-align: start;\"><strong> &nbsp; &nbsp;要求小区内的商铺、餐饮等场所落实疫情防控措施，确保居民生活区域的安全。</strong></li><li style=\"text-align: start;\"><strong> &nbsp; &nbsp;加强宣传教育，提高业主和居民的疫情防控意识。</strong></li><li style=\"text-align: start;\"><strong> &nbsp; &nbsp;最近本市疫情通报 尊敬的业主：近期，本市出现了新型冠状病毒感染的肺炎疫情，请各位业主做好自我防护，遵守疫情防控措施，特此通知：</strong></li><li style=\"text-align: start;\"><strong> &nbsp; &nbsp;请尽量减少外出，如必须外出，请佩戴口罩、勤洗手，尽量避免人群聚集。</strong></li><li style=\"text-align: start;\"><strong> &nbsp; &nbsp;如出现发热、咳嗽等症状，请及时就医，并告知就诊医院近期的旅行史、接触史等信息。</strong></li><li style=\"text-align: start;\"><strong> &nbsp; &nbsp;如有疑似症状，请及时向小区物业报告，以便小区采取相应的防控措施。</strong></li></ol>', '2023-04-21 20:48:49', 'admin');
INSERT INTO `notice` VALUES (1649693739614851074, '我是标题', '<p><span style=\"color: rgb(83, 29, 171);\"><u><strong>这里</strong></u></span><span style=\"color: rgb(64, 169, 255);\"><em><strong>是</strong></em></span><span style=\"color: rgb(83, 29, 171); background-color: rgb(196, 29, 127);\"><strong>内容</strong></span></p>', '2023-04-22 16:36:52', 'admin');
INSERT INTO `notice` VALUES (1651145294230708225, '123', '<p><span style=\"color: rgb(231, 95, 51);\">12313</span></p>', '2023-04-26 16:44:50', 'admin');
INSERT INTO `notice` VALUES (1651145940640063489, '13213', '<ul><li><strong>12331</strong></li></ul>', '2023-04-26 16:47:24', 'admin');

-- ----------------------------
-- Table structure for nucleic_acid
-- ----------------------------
DROP TABLE IF EXISTS `nucleic_acid`;
CREATE TABLE `nucleic_acid`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `collect_time` date NOT NULL COMMENT '采集日期',
  `type` int(0) NULL DEFAULT NULL COMMENT '0返乡核酸，1隔离核酸，2通勤人员核酸，3其他',
  `result` int(0) NULL DEFAULT 2 COMMENT '0阳性，1阴性，2未完成',
  `picture_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '核酸结果截图路径',
  PRIMARY KEY (`id_number`, `collect_time`) USING BTREE,
  CONSTRAINT `fk_nucleic_acid_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nucleic_acid
-- ----------------------------
INSERT INTO `nucleic_acid` VALUES ('320211200107044531', '2023-02-20', 0, 1, '015f48c6-9801-4076-8876-7949047190c7.jpg');
INSERT INTO `nucleic_acid` VALUES ('320211200107044531', '2023-02-21', 1, 1, '8239524b-5354-4f41-bf10-0a73cc1ce787.jpg');
INSERT INTO `nucleic_acid` VALUES ('320211200107044531', '2023-03-06', 1, 1, '16575cfe-e8b3-40e2-a521-5a8383e155df.jpg');
INSERT INTO `nucleic_acid` VALUES ('320212200102060512', '2023-02-26', 1, 2, NULL);

-- ----------------------------
-- Table structure for quarantine
-- ----------------------------
DROP TABLE IF EXISTS `quarantine`;
CREATE TABLE `quarantine`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '隔离人员身份证号',
  `type` int(0) NULL DEFAULT NULL COMMENT '隔离类型，0居家隔离，1集中隔离',
  `start_time` date NOT NULL COMMENT '隔离开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '隔离结束时间',
  PRIMARY KEY (`id_number`, `start_time`) USING BTREE,
  CONSTRAINT `fk_quarantine_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of quarantine
-- ----------------------------
INSERT INTO `quarantine` VALUES ('320211200107044531', 0, '2023-04-25', '2023-04-29');
INSERT INTO `quarantine` VALUES ('320211200107044531', 0, '2023-04-26', '2023-05-31');
INSERT INTO `quarantine` VALUES ('320212200102060512', 1, '2023-02-25', '2023-02-27');

-- ----------------------------
-- Table structure for resident
-- ----------------------------
DROP TABLE IF EXISTS `resident`;
CREATE TABLE `resident`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号码',
  `name` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '0为男，1为女',
  `birth_day` date NULL DEFAULT NULL COMMENT '出生日期',
  `nation` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '民族',
  `politics_status` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `job` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职业',
  `native_city` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '籍贯（市）',
  `native_province` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '籍贯（省）',
  `permanent_resident_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '户籍地址',
  `phone` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `password` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录密码',
  `state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '0为正常，1为通勤人员，2为黄码，3为红码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `prohibit_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(0) NULL DEFAULT NULL COMMENT '逻辑删除标志',
  PRIMARY KEY (`id_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resident
-- ----------------------------
INSERT INTO `resident` VALUES ('320211200107044531', '杨润琦', '0', '2001-07-04', '汉族', '共青团员', '学生', '无锡市', '江苏省', '江苏省无锡市梁溪区中联新村39-601', '13812503155', 'f379eaf3c831b04de153469d1bec345e', '0', '250244033@qq.com', '17569b41-3376-4c0c-9e26-91b846b2ba3f.jpg', NULL, 0);
INSERT INTO `resident` VALUES ('320212200102060512', '葛东琦', '0', '2001-02-04', '汉族', '共青团员', '无业游民', '盐城市', '江苏省', '芦庄六区', '13101963880', 'f379eaf3c831b04de153469d1bec345e', '0', '2660356674@qq.com', 'default.jpg', NULL, 0);
INSERT INTO `resident` VALUES ('322222222222222222', '陶文浩', '0', '2000-01-01', '汉族', '民革党员', '工程师', '无锡市', '江苏省', '扬名花园', '139061706010', 'f379eaf3c831b04de153469d1bec345e', '0', '916726099@qq.com', 'default.jpg', NULL, 0);
INSERT INTO `resident` VALUES ('450921200010041625', '黄燕', '1', '2000-10-02', '汉族', '共青团员', '服务员', '玉林市', '广西壮族自治区', '江苏省无锡市锡山区', '15852518962', 'f379eaf3c831b04de153469d1bec345e', '0', '2356863886@qq.com', 'default.jpg', NULL, 1);
INSERT INTO `resident` VALUES ('666666666666666666', '蔡徐坤', '0', '2023-01-11', '汉族', '群众', '篮球家', '东城区', '北京市', '北京朝阳派出所', '110', 'f379eaf3c831b04de153469d1bec345e', '0', '110@qq.com', 'default.jpg', NULL, 1);

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`  (
  `employee_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网格员工号',
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `name` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `gender` int(0) NULL DEFAULT NULL COMMENT '性别，0为男，1为女',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `politics_status` char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `education` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文化程度',
  `position` int(0) NULL DEFAULT NULL COMMENT '0管理员，1网格员，2网格长，3片长',
  `employment_start` date NULL DEFAULT NULL COMMENT '任职时间',
  `password` char(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `prohibit_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_del` int(0) NULL DEFAULT NULL COMMENT '逻辑删除',
  PRIMARY KEY (`employee_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES ('001', '123312322232345678', '刘燕秋', 1, '1980-02-27', NULL, '', 1, '2023-01-23', 'f379eaf3c831b04de153469d1bec345e', NULL, NULL, 'db03295a-725c-4443-a279-a354e9a0d070.jpg', NULL, 0);
INSERT INTO `staff` VALUES ('002', '123312345333456782', '上官蕾', 1, '1981-03-27', NULL, NULL, 3, NULL, 'f379eaf3c831b04de153469d1bec345e', NULL, NULL, '39be091e-ca62-440f-80fd-f82b992125e9.jpg', NULL, 0);
INSERT INTO `staff` VALUES ('003', '123312345676545678', '李四', 0, '2023-03-01', '', '', 1, '2023-03-06', 'f379eaf3c831b04de153469d1bec345e', '', '', 'default.jpg', NULL, 0);
INSERT INTO `staff` VALUES ('admin', NULL, 'admin', 0, '1976-02-27', NULL, NULL, 0, NULL, 'f379eaf3c831b04de153469d1bec345e', NULL, NULL, 'b574d960-586c-417e-a723-459ba00e01fd.jpg', NULL, 0);

-- ----------------------------
-- Table structure for temperature
-- ----------------------------
DROP TABLE IF EXISTS `temperature`;
CREATE TABLE `temperature`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号',
  `collect_time` datetime(0) NOT NULL COMMENT '测量时间',
  `result` double NULL DEFAULT NULL COMMENT '温度',
  PRIMARY KEY (`id_number`, `collect_time`) USING BTREE,
  CONSTRAINT `fk_temperature_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of temperature
-- ----------------------------
INSERT INTO `temperature` VALUES ('320211200107044531', '2023-03-04 00:00:00', 36.9);
INSERT INTO `temperature` VALUES ('320211200107044531', '2023-03-04 21:02:05', 37);
INSERT INTO `temperature` VALUES ('320211200107044531', '2023-04-26 13:00:00', 36.8);

-- ----------------------------
-- Table structure for visit
-- ----------------------------
DROP TABLE IF EXISTS `visit`;
CREATE TABLE `visit`  (
  `visit_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '访问者身份证号',
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '被访问者身份证号',
  `entry_time` datetime(0) NOT NULL COMMENT '进入时间',
  `leave_time` datetime(0) NULL DEFAULT NULL COMMENT '离开时间',
  `reason` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事由',
  `in_guard_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '进入小区批准人',
  `out_guard_number` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出小区批准人',
  `visit_total_time` double NULL DEFAULT NULL COMMENT '访问总时长（分钟）',
  `state` int(0) NULL DEFAULT 0 COMMENT '0为未离开，1为已离开',
  PRIMARY KEY (`entry_time`, `visit_number`) USING BTREE,
  INDEX `fk_visitt_guard_1`(`in_guard_number`) USING BTREE,
  INDEX `fk_visitt_resident_1`(`id_number`) USING BTREE,
  INDEX `fk_visitt_visitor_1`(`visit_number`) USING BTREE,
  CONSTRAINT `fk_visitt_guard_1` FOREIGN KEY (`in_guard_number`) REFERENCES `guard` (`guard_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_visitt_resident_1` FOREIGN KEY (`id_number`) REFERENCES `resident` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_visitt_visitor_1` FOREIGN KEY (`visit_number`) REFERENCES `visitor` (`id_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of visit
-- ----------------------------
INSERT INTO `visit` VALUES ('320211200107044531', '320211200107044531', '2023-01-24 12:12:37', '2023-03-03 15:56:44', '探亲', 'G001', 'G001', 54944.11526666667, 1);
INSERT INTO `visit` VALUES ('320211200107044531', '320211200107044531', '2023-01-24 12:13:34', '2023-03-04 21:25:55', '探亲', 'G001', NULL, 56712.354816666666, 1);
INSERT INTO `visit` VALUES ('320211200107044531', '320211200107044531', '2023-01-24 12:17:25', '2023-04-26 07:10:48', '探亲', 'G001', 'G001', 132173.38096666668, 1);
INSERT INTO `visit` VALUES ('123345678981234567', '320212200102060512', '2023-03-03 16:37:04', '2023-03-03 17:15:44', '一十', NULL, NULL, 38.661683333333336, 1);
INSERT INTO `visit` VALUES ('123123123123123123', '322222222222222222', '2023-03-04 21:31:48', '2023-03-04 21:36:33', '12321', NULL, NULL, 4.74395, 1);
INSERT INTO `visit` VALUES ('320211200107044531', '320212200102060512', '2023-04-26 07:10:35', '2023-04-26 09:43:55', '1', 'G001', 'G001', 153.33408333333333, 1);
INSERT INTO `visit` VALUES ('320211200107044531', '320211200107044531', '2023-04-26 09:44:31', '2023-04-26 09:44:40', '31', 'G001', 'G001', 0.14415, 1);
INSERT INTO `visit` VALUES ('320211200107044531', '322222222222222222', '2023-04-26 16:49:38', '2023-04-26 16:49:50', '213213213', NULL, NULL, 0.20146666666666666, 1);

-- ----------------------------
-- Table structure for visitor
-- ----------------------------
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE `visitor`  (
  `id_number` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证号码',
  `name` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `gender` int(0) NULL DEFAULT NULL COMMENT '0为男，1为女',
  `phone` char(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of visitor
-- ----------------------------
INSERT INTO `visitor` VALUES ('123123123123123123', '213', 1, '123213213213');
INSERT INTO `visitor` VALUES ('123345678981234567', '王五', 1, '123123123123');
INSERT INTO `visitor` VALUES ('320211200107044531', '杨润琦2', 0, '1312312');

SET FOREIGN_KEY_CHECKS = 1;
