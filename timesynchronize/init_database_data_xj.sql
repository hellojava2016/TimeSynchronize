-- 角色表数据初始化 --
insert into `role` (`role_id`, `role_name`, `description`) values(1,'超级管理员','系统超级管理员');
insert into `role` (`role_id`, `role_name`, `description`) values(2,'管理员','系统管理员');
insert into `role` (`role_id`, `role_name`, `description`) values(3,'数据统计分析员','监管机构数据统计分析员');
insert into `role` (`role_id`, `role_name`, `description`) values(4,'指标查看员','监管机构风险指标查看');
insert into `role` (`role_id`, `role_name`, `description`) values(5,'配置信息查看员','监管机构配置信息查看');
insert into `role` (`role_id`, `role_name`, `description`) values(6,'银监局管理员','银监局管理员');
insert into `role` (`role_id`, `role_name`, `description`) values(7,'数据报送管理员','本银行报送管理和数据报送员创建');
insert into `role` (`role_id`, `role_name`, `description`) values(8,'数据报送员','本银行风险指标报送和配置报送');

-- 权限表数据初始化--
-- --用户权限--
insert into permission (permission_id,permission_name,module_name,description) values(1,'user:view','用户管理','查看用户');
insert into permission (permission_id,permission_name,module_name,description) values(2,'user:modify','用户管理','修改用户');
insert into permission (permission_id,permission_name,module_name,description) values(3,'user:add','用户管理','新增用户');
insert into permission (permission_id,permission_name,module_name,description) values(4,'user:disable','用户管理','停用用户');
insert into permission (permission_id,permission_name,module_name,description) values(5,'user:modifyrole','用户管理','修改用户角色');
insert into permission (permission_id,permission_name,module_name,description) values(6,'user:resetpassword','用户管理','重置密码');
-- --角色权限--
insert into permission (permission_id,permission_name,module_name,description) values(31,'role:view','角色管理','查看角色');
insert into permission (permission_id,permission_name,module_name,description) values(32,'role:add','角色管理','新增角色');
insert into permission (permission_id,permission_name,module_name,description) values(33,'role:modify','角色管理','修改角色');
insert into permission (permission_id,permission_name,module_name,description) values(34,'role:delete','角色管理','删除角色');

-- --区域权限 --
insert into permission (permission_id,permission_name,module_name,description) values(41,'area:view','区域管理','查看区域');
insert into permission (permission_id,permission_name,module_name,description) values(42,'area:add','区域管理','新增区域');
insert into permission (permission_id,permission_name,module_name,description) values(43,'area:modify','区域管理','修改区域');
insert into permission (permission_id,permission_name,module_name,description) values(44,'area:delete','区域管理','删除区域');

-- 组织机构--
insert into permission (permission_id,permission_name,module_name,description) values(51,'org:view','组织机构管理','查看组织机构');
insert into permission (permission_id,permission_name,module_name,description) values(52,'org:add','组织机构管理','新增组织机构');
insert into permission (permission_id,permission_name,module_name,description) values(53,'org:modify','组织机构管理','修改组织机构');
insert into permission (permission_id,permission_name,module_name,description) values(54,'org:delete','组织机构管理','删除组织机构');
insert into permission (permission_id,permission_name,module_name,description) values(55,'org:changekey','组织机构管理','更换组织机构签名');

insert into permission (permission_id,permission_name,module_name,description) values(61,'system:riskcategory','系统配置管理','指标参数配置');

insert into permission (permission_id,permission_name,module_name,description) values(71,'system:append:auth','系统配置管理','授权补报');
insert into permission (permission_id,permission_name,module_name,description) values(72,'system:append:delete','系统配置管理','删除补报');

insert into permission (permission_id,permission_name,module_name,description) values(81,'system:backupdb','系统配置管理','数据备份');
insert into permission (permission_id,permission_name,module_name,description) values(82,'system:globalparameter','系统配置管理','全局参数配置');
insert into permission (permission_id,permission_name,module_name,description) values(83,'system:warninginfo','系统配置管理','风险预警');

insert into permission (permission_id,permission_name,module_name,description) values(90,'message:manage','公告管理','发送公告');
insert into permission (permission_id,permission_name,module_name,description) values(91,'message:view','公告管理','查看公告');

insert into permission (permission_id,permission_name,module_name,description) values(101,'risk:view','指标报送管理','查看指标');
insert into permission (permission_id,permission_name,module_name,description) values(102,'risk:export','指标报送管理','导出指标');
insert into permission (permission_id,permission_name,module_name,description) values(103,'risk:add','指标报送管理','上报指标');
insert into permission (permission_id,permission_name,module_name,description) values(104,'risk:import','指标报送管理','导入指标');
insert into permission (permission_id,permission_name,module_name,description) values(106,'risk:delete','指标报送管理','删除指标');

insert into permission (permission_id,permission_name,module_name,description) values(201,'asset:view','配置管理','查看配置');
insert into permission (permission_id,permission_name,module_name,description) values(202,'asset:export','配置管理','导出配置');
insert into permission (permission_id,permission_name,module_name,description) values(203,'asset:add','配置管理','新增配置');
insert into permission (permission_id,permission_name,module_name,description) values(204,'asset:modify','配置管理','修改配置');
insert into permission (permission_id,permission_name,module_name,description) values(205,'asset:delete','配置管理','删除配置');

insert into permission (permission_id,permission_name,module_name,description) values(301,'risk:analyse','风险分析管理','风险分析查看');

-- 系统超级管理员--
insert into role_permissions(role_id,permission_id) values(1,1);
insert into role_permissions(role_id,permission_id) values(1,2);
insert into role_permissions(role_id,permission_id) values(1,3);
insert into role_permissions(role_id,permission_id) values(1,4);
insert into role_permissions(role_id,permission_id) values(1,5);
insert into role_permissions(role_id,permission_id) values(1,6);
insert into role_permissions(role_id,permission_id) values(1,31);
insert into role_permissions(role_id,permission_id) values(1,32);
insert into role_permissions(role_id,permission_id) values(1,33);
insert into role_permissions(role_id,permission_id) values(1,34);
insert into role_permissions(role_id,permission_id) values(1,41);
insert into role_permissions(role_id,permission_id) values(1,42);
insert into role_permissions(role_id,permission_id) values(1,43);
insert into role_permissions(role_id,permission_id) values(1,44);
insert into role_permissions(role_id,permission_id) values(1,51);
insert into role_permissions(role_id,permission_id) values(1,52);
insert into role_permissions(role_id,permission_id) values(1,53);
insert into role_permissions(role_id,permission_id) values(1,54);
insert into role_permissions(role_id,permission_id) values(1,55);
insert into role_permissions(role_id,permission_id) values(1,61);
insert into role_permissions(role_id,permission_id) values(1,71);
insert into role_permissions(role_id,permission_id) values(1,72);
insert into role_permissions(role_id,permission_id) values(1,81);
insert into role_permissions(role_id,permission_id) values(1,82);
insert into role_permissions(role_id,permission_id) values(1,83);
insert into role_permissions(role_id,permission_id) values(1,90);
insert into role_permissions(role_id,permission_id) values(1,101);
insert into role_permissions(role_id,permission_id) values(1,102);
insert into role_permissions(role_id,permission_id) values(1,106);
insert into role_permissions(role_id,permission_id) values(1,201);
insert into role_permissions(role_id,permission_id) values(1,202);
insert into role_permissions(role_id,permission_id) values(1,204);
insert into role_permissions(role_id,permission_id) values(1,205);
insert into role_permissions(role_id,permission_id) values(1,301);

-- 管理员--
insert into role_permissions(role_id,permission_id) values(2,1);
insert into role_permissions(role_id,permission_id) values(2,2);
insert into role_permissions(role_id,permission_id) values(2,3);
insert into role_permissions(role_id,permission_id) values(2,4);
insert into role_permissions(role_id,permission_id) values(2,5);
insert into role_permissions(role_id,permission_id) values(2,6);
insert into role_permissions(role_id,permission_id) values(2,31);
insert into role_permissions(role_id,permission_id) values(2,32);
insert into role_permissions(role_id,permission_id) values(2,33);
insert into role_permissions(role_id,permission_id) values(2,34);
insert into role_permissions(role_id,permission_id) values(2,41);
insert into role_permissions(role_id,permission_id) values(2,42);
insert into role_permissions(role_id,permission_id) values(2,43);
insert into role_permissions(role_id,permission_id) values(2,44);
insert into role_permissions(role_id,permission_id) values(2,51);
insert into role_permissions(role_id,permission_id) values(2,52);
insert into role_permissions(role_id,permission_id) values(2,53);
insert into role_permissions(role_id,permission_id) values(2,54);
insert into role_permissions(role_id,permission_id) values(2,55);
insert into role_permissions(role_id,permission_id) values(2,61);
insert into role_permissions(role_id,permission_id) values(2,71);
insert into role_permissions(role_id,permission_id) values(2,72);
insert into role_permissions(role_id,permission_id) values(2,81);
insert into role_permissions(role_id,permission_id) values(2,82);
insert into role_permissions(role_id,permission_id) values(2,83);
insert into role_permissions(role_id,permission_id) values(2,90);
insert into role_permissions(role_id,permission_id) values(2,101);
insert into role_permissions(role_id,permission_id) values(2,102);
insert into role_permissions(role_id,permission_id) values(2,106);
insert into role_permissions(role_id,permission_id) values(2,201);
insert into role_permissions(role_id,permission_id) values(2,202);
insert into role_permissions(role_id,permission_id) values(2,204);
insert into role_permissions(role_id,permission_id) values(2,205);
insert into role_permissions(role_id,permission_id) values(2,301);
-- 数据统计分析员--
insert into role_permissions(role_id,permission_id) values(3,1);
insert into role_permissions(role_id,permission_id) values(3,301);
-- 指标查看员--
insert into role_permissions(role_id,permission_id) values(4,1);
insert into role_permissions(role_id,permission_id) values(4,71);
insert into role_permissions(role_id,permission_id) values(4,72);
insert into role_permissions(role_id,permission_id) values(4,101);
insert into role_permissions(role_id,permission_id) values(4,102);
insert into role_permissions(role_id,permission_id) values(4,106);
-- 信息配置查看员--
insert into role_permissions(role_id,permission_id) values(5,1);
insert into role_permissions(role_id,permission_id) values(5,201);
insert into role_permissions(role_id,permission_id) values(5,202);
-- 银监局管理员--
insert into role_permissions(role_id,permission_id) values(6,1);
insert into role_permissions(role_id,permission_id) values(6,2);
insert into role_permissions(role_id,permission_id) values(6,3);
insert into role_permissions(role_id,permission_id) values(6,4);
insert into role_permissions(role_id,permission_id) values(6,5);
insert into role_permissions(role_id,permission_id) values(6,6);
insert into role_permissions(role_id,permission_id) values(6,41);
insert into role_permissions(role_id,permission_id) values(6,42);
insert into role_permissions(role_id,permission_id) values(6,43);
insert into role_permissions(role_id,permission_id) values(6,44);
insert into role_permissions(role_id,permission_id) values(6,51);
insert into role_permissions(role_id,permission_id) values(6,52);
insert into role_permissions(role_id,permission_id) values(6,53);
insert into role_permissions(role_id,permission_id) values(6,54);
insert into role_permissions(role_id,permission_id) values(6,55);
insert into role_permissions(role_id,permission_id) values(6,71);
insert into role_permissions(role_id,permission_id) values(6,72);
insert into role_permissions(role_id,permission_id) values(6,90);
insert into role_permissions(role_id,permission_id) values(6,91);
insert into role_permissions(role_id,permission_id) values(6,101);
insert into role_permissions(role_id,permission_id) values(6,102);
insert into role_permissions(role_id,permission_id) values(6,106);
insert into role_permissions(role_id,permission_id) values(6,201);
insert into role_permissions(role_id,permission_id) values(6,202);
insert into role_permissions(role_id,permission_id) values(6,204);
insert into role_permissions(role_id,permission_id) values(6,205);
insert into role_permissions(role_id,permission_id) values(6,301);
-- 数据报送管理员--
insert into role_permissions(role_id,permission_id) values(7,1);
insert into role_permissions(role_id,permission_id) values(7,2);
insert into role_permissions(role_id,permission_id) values(7,3);
insert into role_permissions(role_id,permission_id) values(7,4);
insert into role_permissions(role_id,permission_id) values(7,5);
insert into role_permissions(role_id,permission_id) values(7,6);
insert into role_permissions(role_id,permission_id) values(7,91);
insert into role_permissions(role_id,permission_id) values(7,101);
insert into role_permissions(role_id,permission_id) values(7,102);
insert into role_permissions(role_id,permission_id) values(7,103);
insert into role_permissions(role_id,permission_id) values(7,104);
insert into role_permissions(role_id,permission_id) values(7,201);
insert into role_permissions(role_id,permission_id) values(7,202);
insert into role_permissions(role_id,permission_id) values(7,203);
insert into role_permissions(role_id,permission_id) values(7,204);
-- 数据报送员--
insert into role_permissions(role_id,permission_id) values(8,1);
insert into role_permissions(role_id,permission_id) values(8,101);
insert into role_permissions(role_id,permission_id) values(8,102);
insert into role_permissions(role_id,permission_id) values(8,103);
insert into role_permissions(role_id,permission_id) values(8,104);
insert into role_permissions(role_id,permission_id) values(8,201);
insert into role_permissions(role_id,permission_id) values(8,202);
insert into role_permissions(role_id,permission_id) values(8,203);
insert into role_permissions(role_id,permission_id) values(8,204);

-- 区域数据--
INSERT INTO area_city(`area_id`, `parent_area_id`, `aree_code`, `name`) VALUES (1,NULL,'01','中国');
INSERT INTO area_city(`area_id`, `parent_area_id`, `aree_code`, `name`) VALUES (2,1,'0101','北京市');
INSERT INTO area_city(`area_id`, `parent_area_id`, `aree_code`, `name`) VALUES (3,1,'0102','天津市');



INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (1, 0, 2, 3, 0, 100, 80, '1', '系统可用率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (3, 1, 2, 3, 0, 100, 80, '1101', '核心业务系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (4, 1, 2, 3, 0, 100, 80, '1102', '综合前置系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (5, 1, 2, 3, 0, 100, 80, '1103', '信用卡系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (6, 1, 2, 3, 0, 100, 80, '1104', '网上银行系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (7, 1, 2, 3, 0, 100, 80, '1105', '电话银行系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (8, 1, 2, 3, 0, 100, 80, '1106', '手机银行系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (9, 1, 2, 3, 0, 100, 80, '1107', '大额实时支付前置系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (10, 1, 2, 3, 0, 100, 80, '1108', '小额批量支付前置系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (11, 1, 2, 3, 0, 100, 80, '1109', '第三方存管系统可用率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (22, 0, 1, 3, 0, 100, 80, '2', '系统交易成功率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (23, 1, 1, 3, 0, 100, 80, '2001', '核心业务系统交易成功率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (24, 1, 1, 3, 0, 100, 80, '2001001', '账务类交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (25, 1, 1, 3, 0, 100, 80, '2001002', '非账务类交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (26, 1, 1, 3, 0, 100, 80, '2002', '综合前置系统交易成功率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (27, 1, 1, 3, 0, 100, 80, '2002001', '大额实时支付渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (28, 1, 1, 3, 0, 100, 80, '2002002', '小额批量支付渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (29, 1, 1, 3, 0, 100, 80, '2002003', 'ATM渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (30, 1, 1, 3, 0, 100, 80, '2002004', 'POS渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (31, 1, 1, 3, 0, 100, 80, '2002005', '第三方存管渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (32, 1, 1, 3, 0, 100, 80, '2003', '信用卡系统交易成功率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (33, 1, 1, 3, 0, 100, 80, '2003001', '电话银行渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (34, 1, 1, 3, 0, 100, 80, '2003002', '手机银行渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (35, 1, 1, 3, 0, 100, 80, '2003003', 'ATM渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (36, 1, 1, 3, 0, 100, 80, '2003004', 'POS渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (37, 1, 1, 3, 0, 100, 80, '2004', '网上银行系统交易成功率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (38, 1, 1, 3, 0, 100, 80, '2004001', '个人网银交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (39, 1, 1, 3, 0, 100, 80, '2004002', '企业网银交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (40, 1, 1, 3, 0, 100, 80, '2004003', '银联渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (41, 1, 1, 3, 0, 100, 80, '2004004', '超级网银渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (42, 1, 1, 3, 0, 100, 80, '2004005', '大额实时支付渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (43, 1, 1, 3, 0, 100, 80, '2004006', '小额批量支付渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (44, 1, 1, 3, 0, 100, 80, '2004007', '第三方支付渠道交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (45, 1, 1, 3, 0, 100, 80, '2005', '电话银行系统交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (46, 1, 1, 3, 0, 100, 80, '2006', '手机银行交易成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (47, 0, 3, 3, 0, 100, 80, '3', '投产变更成功率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (48, 1, 3, 3, 0, 100, 80, '3001', '投产变更成功率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (49, 0, 3, 3, 0, 100, 80, '4', '假冒网站查封率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (50, 1, 3, 3, 0, 100, 80, '4001', '假冒网站查封率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (51, 0, 2, 3, 0, 20, -20, '5', '外部攻击变化率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (52, 1, 2, 3, 0, 20, -20, '5001', '外部攻击变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (53, 0, 2, 3, 0, 1000, 0, '6001', '信息科技风险事件数量', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (55, 0, 2, 3, 0, 180, 0, '6001001', '信息系统故障造成服务中断次数', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (56, 1, 2, 3, 0, 10, 0, '6001001001', '核心业务系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (57, 1, 2, 3, 0, 10, 0, '6001001002', '综合前置系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (58, 1, 2, 3, 0, 10, 0, '6001001003', '信用卡系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (59, 1, 2, 3, 0, 10, 0, '6001001004', '网上银行系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (60, 1, 2, 3, 0, 10, 0, '6001001005', '电话银行系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (61, 1, 2, 3, 0, 10, 0, '6001001006', '手机银行系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (62, 1, 2, 3, 0, 10, 0, '6001001007', '大额实时支付系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (63, 1, 2, 3, 0, 10, 0, '6001001008', '小额批量支付系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (64, 1, 2, 3, 0, 10, 0, '6001001009', 'ATM前置系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (65, 1, 2, 3, 0, 10, 0, '6001001010', 'POS前置系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (66, 1, 2, 3, 0, 10, 0, '6001001011', '柜面系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (67, 1, 2, 3, 0, 10, 0, '6001001012', '信贷系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (68, 1, 2, 3, 0, 10, 0, '6001001013', '个贷系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (69, 1, 2, 3, 0, 10, 0, '6001001014', '基金系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (70, 1, 2, 3, 0, 10, 0, '6001001015', '债券系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (71, 1, 2, 3, 0, 10, 0, '6001001016', '第三方存管系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (72, 1, 2, 3, 0, 10, 0, '6001001017', '第三方支付系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (73, 1, 2, 3, 0, 10, 0, '6001001018', '国际结算系统服务中断次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (74, 1, 2, 3, 0, 10, 0, '6001002', '违规操作事件次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (75, 1, 2, 3, 0, 10, 0, '6001003', '病毒爆发事件次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (76, 1, 2, 3, 0, 10, 0, '6001004', '自然灾害事件次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (77, 1, 2, 3, 0, 10, 0, '6001005', '网络中断事件次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (78, 1, 2, 3, 0, 10, 0, '6001006', '基础设施中断事件次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (79, 1, 2, 3, 0, 10, 0, '6001007', '其他安全事件次数', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (80, 0, 1, 3, 0, 20, -20, '7', '主要电子渠道交易变化率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (81, 1, 1, 3, 0, 20, -20, '7001', '网上银行交易变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (82, 1, 1, 3, 0, 20, -20, '7002', '电话银行交易变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (83, 1, 1, 3, 0, 20, -20, '7003', '手机银行交易变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (84, 1, 1, 3, 0, 20, -20, '7004', '大额实时支付交易变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (85, 1, 1, 3, 0, 20, -20, '7005', '小额批量支付交易变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (86, 1, 1, 3, 0, 20, -20, '7006', 'ATM交易变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (87, 1, 1, 3, 0, 20, -20, '7007', 'POS交易变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (88, 0, 3, 3, 0, 20, -20, '8', '主要电子渠道活跃用户变化率', 0, 0, 1, 1);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (89, 1, 3, 3, 0, 20, -20, '8001', '网上银行活跃用户变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (90, 1, 3, 3, 0, 20, -20, '8002', '电话银行活跃用户变化率', 0, 0, 1, 0);
INSERT INTO `risk_category` (`risk_id`, `allow_report`, `cycle`, `end_day`, `end_hour`, `max_value`, `min_value`, `risk_code`, `risk_name`, `start_day`, `start_hour`, `is_use_threshold_value`, `has_leaf`) VALUES (91, 1, 3, 3, 0, 20, -20, '8003', '手机银行活跃用户变化率', 0, 0, 1, 0);

-- 组织机构--
INSERT INTO `organization`(org_id,area_id,parent_id,name,org_no,category,secret_key,address,contacts_name,contacts_mail,contacts_cellphone,contacts_phone,moneyCount,canControl) VALUES (1, 1, null,'中国银行业监督管理委员会', '001',1,'01037148031781818771407166668888','北京市西城区金融大街甲15号', null, null, null, '010-66279113',1,0);
-- 用户--
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('1', '1', 'admin','admin','21232f297a57a5a743894a0e4a801fc3',null,null,null, '2014-06-13 00:13:10', '01', 0, 1);
-- 用户角色--
INSERT INTO `user_roles`(user_id,role_id) VALUES ('1', '1');

insert into backupdb(db_id,ip,port,username,password,daily,taskstatus,checknum) VALUES(1,'127.0.0.1',3306,'root','',1,false,1);
insert into global_parameter(id,systemavailable,systemtransaction,operationchanges,fakesiteattachment,continueDecline,continueDeclineEnable,deviateAverage,deviateAverageEnable) VALUES(1,80,80,0,0,3,true,20,true);


INSERT INTO `organization`(org_id,area_id,parent_id,name,org_no,category,secret_key,address,contacts_name,contacts_mail,contacts_cellphone,contacts_phone,moneyCount,canControl) VALUES (2, 2, null,'北京银行', 'BJBK',5,'01071193387580474171410454334001',null, null, null, null, null,100000000,1);
INSERT INTO `organization`(org_id,area_id,parent_id,name,org_no,category,secret_key,address,contacts_name,contacts_mail,contacts_cellphone,contacts_phone,moneyCount,canControl) VALUES (3, 3, null,'渤海银行', 'BHBK',4,'01071193387580474171410454123875',null, null, null, null, null,100000000,1);
INSERT INTO `organization`(org_id,area_id,parent_id,name,org_no,category,secret_key,address,contacts_name,contacts_mail,contacts_cellphone,contacts_phone,moneyCount,canControl) VALUES (4, 2, null,'北京银监局', 'BJYJ',1,'01060414651279043571413100161125',null, null, null, null, null,100000000,0);
INSERT INTO `organization`(org_id,area_id,parent_id,name,org_no,category,secret_key,address,contacts_name,contacts_mail,contacts_cellphone,contacts_phone,moneyCount,canControl) VALUES (5, 2, null,'北京农商银行', 'BJNS',6,'01071193387580474171410454334002',null, null, null, null, null,100000000,1);
-- 用户--
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('2', '2', 'bjbk','北京银行报送员','0489f2cf05494b4bc95a0f97b987a3de',null,null,null, '2014-06-13 00:13:10', '0101', 0, 1);
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('3', '3', 'bhbk','渤海银行报送员','3b82b178707474b94274ca42a4a86b5f',null,null,null, '2014-06-13 00:13:10', '0102', 0, 1);
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('4', '4', 'bj','北京银监局管理员','39b9df3a0fb3356d11a63e22260e96ab',null,null,null, '2014-06-13 00:13:10', '0101', 0, 1);
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('5', '5', 'bjns','北京农商银行报送员','1d84103f0ccb91e58d8665c07c5ed07b',null,null,null, '2014-06-13 00:13:10', '0101', 0, 1);
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('6', '2', 'bjbkadmin','北京银行管理员','85b0467831892e8e610d0d87096aede9',null,null,null, '2014-06-13 00:13:10', '0101', 0, 1);
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('7', '4', 'bjrisk','北京银监局指标查看员','ad31098c8ed6093068ef7664de3e701d',null,null,null, '2014-06-13 00:13:10', '0101', 0, 1);
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('8', '4', 'bjasset','北京银监局配置信息查看员','addc57d2f3efafeae392bff3e043f278',null,null,null, '2014-06-13 00:13:10', '0101', 0, 1);
INSERT INTO `users`(user_id,org_id,username,realname,passwd,cellphone,telephone,email,create_time,area_code,status,is_online) VALUES ('9', '4', 'bjreport','北京银监局数据统计分析员','c4d6dd354b3f058dd904b3335b20f299',null,null,null, '2014-06-13 00:13:10', '0101', 0, 1);
-- 用户角色--
INSERT INTO `user_roles`(user_id,role_id) VALUES ('2', '8');
INSERT INTO `user_roles`(user_id,role_id) VALUES ('3', '8');
INSERT INTO `user_roles`(user_id,role_id) VALUES ('4', '6');
INSERT INTO `user_roles`(user_id,role_id) VALUES ('5', '8');
INSERT INTO `user_roles`(user_id,role_id) VALUES ('6', '7');
INSERT INTO `user_roles`(user_id,role_id) VALUES ('7', '4');
INSERT INTO `user_roles`(user_id,role_id) VALUES ('8', '5');
INSERT INTO `user_roles`(user_id,role_id) VALUES ('9', '3');

