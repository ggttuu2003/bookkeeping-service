-- 创建数据库
CREATE DATABASE IF NOT EXISTS smart_bookkeeping DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE smart_bookkeeping;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `gender` tinyint(1) DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `open_id` varchar(100) DEFAULT NULL COMMENT '微信OpenID',
    `union_id` varchar(100) DEFAULT NULL COMMENT '微信UnionID',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_open_id` (`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户角色表
CREATE TABLE IF NOT EXISTS `user_role` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` varchar(50) NOT NULL COMMENT '角色名称',
    `code` varchar(50) NOT NULL COMMENT '角色编码',
    `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role_relation` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 账本表
CREATE TABLE IF NOT EXISTS `account_book` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '账本ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `name` varchar(50) NOT NULL COMMENT '账本名称',
    `icon` varchar(255) DEFAULT NULL COMMENT '账本图标',
    `description` varchar(255) DEFAULT NULL COMMENT '账本描述',
    `default_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认账本：0-否，1-是',
    `budget` decimal(12,2) DEFAULT 0.00 COMMENT '账本预算',
    `currency` varchar(10) DEFAULT 'CNY' COMMENT '货币类型',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账本表';

-- 账本成员表
CREATE TABLE IF NOT EXISTS `account_book_member` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `book_id` bigint(20) NOT NULL COMMENT '账本ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `role` tinyint(1) NOT NULL DEFAULT 0 COMMENT '角色：0-普通成员，1-管理员，2-创建者',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_book_user` (`book_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账本成员表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID，NULL表示系统预设分类',
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
    `type` tinyint(1) NOT NULL COMMENT '类型：1-收入，2-支出',
    `parent_id` bigint(20) DEFAULT 0 COMMENT '父分类ID，0表示一级分类',
    `sort` int(11) DEFAULT 0 COMMENT '排序',
    `system_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否系统预设：0-否，1-是',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 标签表
CREATE TABLE IF NOT EXISTS `tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `name` varchar(50) NOT NULL COMMENT '标签名称',
    `color` varchar(20) DEFAULT NULL COMMENT '标签颜色',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

-- 账目记录表
CREATE TABLE IF NOT EXISTS `transaction` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易ID',
    `book_id` bigint(20) NOT NULL COMMENT '账本ID',
    `user_id` bigint(20) NOT NULL COMMENT '记录用户ID',
    `category_id` bigint(20) NOT NULL COMMENT '分类ID',
    `amount` decimal(12,2) NOT NULL COMMENT '金额',
    `type` tinyint(1) NOT NULL COMMENT '类型：1-收入，2-支出，3-转账',
    `transaction_time` datetime NOT NULL COMMENT '交易时间',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `location` varchar(255) DEFAULT NULL COMMENT '位置',
    `image_urls` text DEFAULT NULL COMMENT '图片URL，多个以逗号分隔',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_transaction_time` (`transaction_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账目记录表';

-- 交易标签关联表
CREATE TABLE IF NOT EXISTS `transaction_tag` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `transaction_id` bigint(20) NOT NULL COMMENT '交易ID',
    `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_transaction_tag` (`transaction_id`, `tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易标签关联表';

-- 预算表
CREATE TABLE IF NOT EXISTS `budget` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预算ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `book_id` bigint(20) NOT NULL COMMENT '账本ID',
    `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID，NULL表示总预算',
    `amount` decimal(12,2) NOT NULL COMMENT '预算金额',
    `period_type` tinyint(1) NOT NULL COMMENT '周期类型：1-日，2-周，3-月，4-年',
    `start_date` date NOT NULL COMMENT '开始日期',
    `end_date` date DEFAULT NULL COMMENT '结束日期',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预算表';

-- AI预测记录表
CREATE TABLE IF NOT EXISTS `ai_prediction` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预测ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID',
    `book_id` bigint(20) NOT NULL COMMENT '账本ID',
    `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID，NULL表示总预测',
    `prediction_date` date NOT NULL COMMENT '预测日期',
    `predicted_amount` decimal(12,2) NOT NULL COMMENT '预测金额',
    `confidence` decimal(5,2) DEFAULT NULL COMMENT '置信度',
    `actual_amount` decimal(12,2) DEFAULT NULL COMMENT '实际金额',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_prediction_date` (`prediction_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI预测记录表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `system_config` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` varchar(100) NOT NULL COMMENT '配置键',
    `config_value` text NOT NULL COMMENT '配置值',
    `description` varchar(255) DEFAULT NULL COMMENT '描述',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 初始化角色数据
INSERT INTO `user_role` (`name`, `code`, `description`) VALUES 
('管理员', 'ROLE_ADMIN', '系统管理员'),
('普通用户', 'ROLE_USER', '普通用户');

-- 初始化系统预设分类
INSERT INTO `category` (`name`, `icon`, `type`, `parent_id`, `sort`, `system_flag`) VALUES 
-- 收入分类
('工资', 'salary', 1, 0, 1, 1),
('奖金', 'bonus', 1, 0, 2, 1),
('投资收益', 'investment', 1, 0, 3, 1),
('兼职收入', 'part_time', 1, 0, 4, 1),
('礼金', 'gift', 1, 0, 5, 1),
('其他收入', 'other_income', 1, 0, 6, 1),

-- 支出分类
('餐饮', 'food', 2, 0, 1, 1),
('购物', 'shopping', 2, 0, 2, 1),
('交通', 'transport', 2, 0, 3, 1),
('住房', 'house', 2, 0, 4, 1),
('娱乐', 'entertainment', 2, 0, 5, 1),
('医疗', 'medical', 2, 0, 6, 1),
('教育', 'education', 2, 0, 7, 1),
('旅行', 'travel', 2, 0, 8, 1),
('数码', 'digital', 2, 0, 9, 1),
('服饰', 'clothes', 2, 0, 10, 1),
('美容', 'beauty', 2, 0, 11, 1),
('健身', 'fitness', 2, 0, 12, 1),
('宠物', 'pet', 2, 0, 13, 1),
('社交', 'social', 2, 0, 14, 1),
('礼物', 'gift_expense', 2, 0, 15, 1),
('其他支出', 'other_expense', 2, 0, 16, 1);

-- 初始化系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `description`) VALUES 
('SYSTEM_NAME', '智能记账小程序', '系统名称'),
('SYSTEM_VERSION', '1.0.0', '系统版本'),
('AI_PREDICTION_ENABLED', 'true', 'AI预测功能是否启用'),
('OCR_RECOGNITION_ENABLED', 'true', 'OCR识别功能是否启用'),
('DEFAULT_CURRENCY', 'CNY', '默认货币');