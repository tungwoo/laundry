-- 创建数据库
CREATE DATABASE IF NOT EXISTS laundry DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE laundry;

-- 创建衣物类型表
CREATE TABLE IF NOT EXISTS `t_clothes_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '衣物类型名称',
  `code` varchar(50) NOT NULL COMMENT '衣物类型编码',
  `price` decimal(10, 2) NOT NULL COMMENT '标准价格',
  `member_price` decimal(10, 2) NOT NULL COMMENT '会员价格',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT 0 COMMENT '排序号',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='衣物类型表';

-- 创建会员表
CREATE TABLE IF NOT EXISTS `t_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `name` varchar(50) DEFAULT NULL COMMENT '会员姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint(1) DEFAULT 0 COMMENT '性别（0-未知，1-男，2-女）',
  `openid` varchar(50) DEFAULT NULL COMMENT '微信用户唯一标识',
  `balance` decimal(10, 2) DEFAULT 0.00 COMMENT '会员卡余额（正常余额）',
  `gift_balance` decimal(10, 2) DEFAULT 0.00 COMMENT '会员卡赠送余额',
  `points` int(11) DEFAULT 0 COMMENT '会员积分',
  `level` tinyint(1) DEFAULT 1 COMMENT '会员等级（1-普通会员，2-银卡会员，3-金卡会员）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_openid` (`openid`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 创建会员账户流水表
CREATE TABLE IF NOT EXISTS `t_member_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `member_name` varchar(50) DEFAULT NULL COMMENT '会员姓名',
  `type` tinyint(1) NOT NULL COMMENT '交易类型（1-充值，2-消费，3-退款，4-赠送）',
  `amount` decimal(10, 2) NOT NULL COMMENT '交易金额',
  `before_balance` decimal(10, 2) NOT NULL COMMENT '交易前余额（正常余额）',
  `after_balance` decimal(10, 2) NOT NULL COMMENT '交易后余额（正常余额）',
  `before_gift_balance` decimal(10, 2) DEFAULT 0.00 COMMENT '交易前赠送余额',
  `after_gift_balance` decimal(10, 2) DEFAULT 0.00 COMMENT '交易后赠送余额',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员账户流水表';

-- 创建订单表
CREATE TABLE IF NOT EXISTS `t_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `member_id` bigint(20) DEFAULT NULL COMMENT '会员ID',
  `member_name` varchar(50) DEFAULT NULL COMMENT '会员姓名',
  `member_phone` varchar(20) NOT NULL COMMENT '会员手机号',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '实付金额',
  `pay_status` tinyint(1) DEFAULT 0 COMMENT '支付状态（0-未支付，1-已支付，2-已退款）',
  `pay_type` tinyint(1) DEFAULT NULL COMMENT '支付方式（1-微信支付，2-现金，3-会员卡）',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `status` tinyint(1) DEFAULT 1 COMMENT '订单状态（1-待清洗，2-清洗中，3-待上架，4-已上架，5-已自取，6-已送上门，7-已取消）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `clothes_count` int(11) DEFAULT 0 COMMENT '衣物件数',
  `receive_time` datetime DEFAULT NULL COMMENT '收衣时间',
  `estimate_time` datetime DEFAULT NULL COMMENT '预计取衣时间',
  `shelf_location` varchar(50) DEFAULT NULL COMMENT '上架位置',
  `shelf_number` varchar(50) DEFAULT NULL COMMENT '上架号',
  `pickup_code` varchar(20) DEFAULT NULL COMMENT '取衣码',
  `pickup_time` datetime DEFAULT NULL COMMENT '取衣时间',
  `delivery_address` varchar(255) DEFAULT NULL COMMENT '配送地址',
  `delivery_photo` varchar(255) DEFAULT NULL COMMENT '配送照片',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_no` (`order_no`) USING BTREE,
  KEY `idx_member_id` (`member_id`) USING BTREE,
  KEY `idx_member_phone` (`member_phone`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 创建订单明细表
CREATE TABLE IF NOT EXISTS `t_order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `clothes_type_id` bigint(20) NOT NULL COMMENT '衣物类型ID',
  `clothes_type_name` varchar(50) NOT NULL COMMENT '衣物类型名称',
  `color` varchar(50) DEFAULT NULL COMMENT '衣物颜色',
  `brand` varchar(50) DEFAULT NULL COMMENT '衣物品牌',
  `material` varchar(50) DEFAULT NULL COMMENT '衣物材质',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `quantity` int(11) DEFAULT 1 COMMENT '数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `photo` varchar(255) DEFAULT NULL COMMENT '衣物照片',
  `flaw_photos` varchar(1000) DEFAULT NULL COMMENT '衣物瑕疵照片，多张用逗号分隔',
  `stain_mark` varchar(255) DEFAULT NULL COMMENT '污渍标记',
  `special_request` varchar(255) DEFAULT NULL COMMENT '特殊要求',
  `is_urgent` tinyint(1) DEFAULT 0 COMMENT '是否加急（0-否，1-是）',
  `is_color_protect` tinyint(1) DEFAULT 0 COMMENT '是否防褪色（0-否，1-是）',
  `is_special` tinyint(1) DEFAULT 0 COMMENT '是否特殊处理（0-否，1-是）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 创建订单状态日志表
CREATE TABLE IF NOT EXISTS `t_order_status_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `before_status` tinyint(1) DEFAULT NULL COMMENT '之前状态',
  `after_status` tinyint(1) NOT NULL COMMENT '之后状态',
  `description` varchar(100) NOT NULL COMMENT '操作描述',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`) USING BTREE,
  KEY `idx_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单状态日志表';

-- 创建管理员表
CREATE TABLE IF NOT EXISTS `t_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role` tinyint(1) DEFAULT 2 COMMENT '角色（1-超级管理员，2-普通管理员）',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除（0-否，1-是）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 插入初始数据
-- 插入超级管理员账号（密码为加密后的 admin123）
INSERT INTO `t_admin` (`username`, `password`, `name`, `role`, `status`, `create_time`, `update_time`, `deleted`)
VALUES ('admin', '0192023a7bbd73250516f069df18b500', '超级管理员', 1, 1, NOW(), NOW(), 0);

-- 插入衣物类型数据
INSERT INTO `t_clothes_type` (`name`, `code`, `price`, `member_price`, `sort`, `status`, `create_time`, `update_time`, `deleted`)
VALUES 
('西装', 'SUIT', 60.00, 48.00, 1, 1, NOW(), NOW(), 0),
('衬衫', 'SHIRT', 20.00, 16.00, 2, 1, NOW(), NOW(), 0),
('裤子', 'PANTS', 25.00, 20.00, 3, 1, NOW(), NOW(), 0),
('外套', 'COAT', 50.00, 40.00, 4, 1, NOW(), NOW(), 0),
('羽绒服', 'DOWN_JACKET', 80.00, 64.00, 5, 1, NOW(), NOW(), 0),
('毛衣', 'SWEATER', 35.00, 28.00, 6, 1, NOW(), NOW(), 0),
('裙子', 'SKIRT', 40.00, 32.00, 7, 1, NOW(), NOW(), 0),
('风衣', 'TRENCH_COAT', 55.00, 44.00, 8, 1, NOW(), NOW(), 0),
('皮衣', 'LEATHER_JACKET', 90.00, 72.00, 9, 1, NOW(), NOW(), 0); 