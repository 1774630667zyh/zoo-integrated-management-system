-- 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS zoo_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zoo_db;

-- 1. 物种信息表 (t_species)
CREATE TABLE IF NOT EXISTS `t_species` (
                                           `id` INT AUTO_INCREMENT COMMENT '主键ID',
                                           `scientific_name` VARCHAR(100) NOT NULL COMMENT '学名',
    `common_name` VARCHAR(50) NOT NULL COMMENT '中文俗名',
    `conservation_status` VARCHAR(20) COMMENT 'IUCN保护等级',
    `diet_type` VARCHAR(20) COMMENT '食性',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_scientific_name` (`scientific_name`)
    ) ENGINE=InnoDB COMMENT='物种信息表';

-- 2. 动物基础信息表 (t_animal)
CREATE TABLE IF NOT EXISTS `t_animal` (
                                          `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
                                          `name` VARCHAR(50) COMMENT '动物昵称',
    `global_id` VARCHAR(64) COMMENT '全球唯一标识符',
    `chip_code` VARCHAR(64) COMMENT '芯片代码',
    `species_id` INT NOT NULL COMMENT '所属物种ID',
    `gender` TINYINT NOT NULL DEFAULT 2 COMMENT '1:雄性, 0:雌性, 2:未知',
    `birth_date` DATE COMMENT '出生日期',
    `sire_id` BIGINT COMMENT '父亲ID',
    `dam_id` BIGINT COMMENT '母亲ID',
    `enclosure_id` INT COMMENT '所在场馆ID',
    `status` TINYINT DEFAULT 1 COMMENT '1:在园, 2:借出, 3:死亡, 4:已出售',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_chip_code` (`chip_code`),
    KEY `idx_species` (`species_id`)
    ) ENGINE=InnoDB COMMENT='动物基础信息表';

-- 3. 医疗记录表 (t_medical_record)
CREATE TABLE IF NOT EXISTS `t_medical_record` (
                                                  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
                                                  `animal_id` BIGINT NOT NULL COMMENT '动物ID',
                                                  `vet_id` BIGINT NOT NULL COMMENT '兽医ID',
                                                  `visit_date` DATETIME NOT NULL COMMENT '就诊时间',
                                                  `symptoms` TEXT COMMENT '症状',
                                                  `diagnosis` VARCHAR(255) COMMENT '诊断结果',
    `treatment` TEXT COMMENT '治疗方案',
    `next_visit` DATE COMMENT '复诊日期',
    PRIMARY KEY (`id`),
    KEY `idx_animal` (`animal_id`)
    ) ENGINE=InnoDB COMMENT='医疗记录表';

-- 4. 员工表 (t_staff)
CREATE TABLE IF NOT EXISTS `t_staff` (
                                         `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
                                         `username` VARCHAR(50) NOT NULL COMMENT '工号/用户名',
    `password` VARCHAR(64) NOT NULL COMMENT '加密密码',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `role` VARCHAR(20) NOT NULL COMMENT '角色(admin,vet,feeder)',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `status` TINYINT DEFAULT 1 COMMENT '1:启用, 0:禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
    ) ENGINE=InnoDB COMMENT='员工表';

-- 5. 场馆表 (t_facility)
CREATE TABLE IF NOT EXISTS `t_facility` (
                                            `id` INT AUTO_INCREMENT COMMENT '主键ID',
                                            `name` VARCHAR(50) NOT NULL COMMENT '场馆名称',
    `type` VARCHAR(20) COMMENT '类型(室内,室外)',
    `location` VARCHAR(100) COMMENT '位置',
    `status` TINYINT DEFAULT 1 COMMENT '1:开放, 0:关闭, 2:维护中',
    `capacity` INT COMMENT '容纳人数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB COMMENT='场馆表';

-- 6. 饲料表 (t_food)
CREATE TABLE IF NOT EXISTS `t_food` (
                                        `id` INT AUTO_INCREMENT COMMENT '主键ID',
                                        `name` VARCHAR(50) NOT NULL COMMENT '饲料名称',
    `type` VARCHAR(20) COMMENT '类型',
    `stock` DECIMAL(10,2) DEFAULT 0 COMMENT '库存量',
    `unit` VARCHAR(10) COMMENT '单位',
    `min_threshold` DECIMAL(10,2) DEFAULT 0 COMMENT '预警阈值',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB COMMENT='饲料表';

-- 7. 维修工单表 (t_maintenance)
CREATE TABLE IF NOT EXISTS `t_maintenance` (
                                               `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
                                               `facility_id` INT NOT NULL COMMENT '场馆ID',
                                               `description` VARCHAR(255) COMMENT '故障描述',
    `reporter_id` BIGINT COMMENT '报修人ID',
    `status` TINYINT DEFAULT 0 COMMENT '0:待处理, 1:维修中, 2:已完成',
    `result` VARCHAR(255) COMMENT '处理结果',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
    `resolve_time` DATETIME COMMENT '完成时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB COMMENT='维修工单表';

-- 8. 喂养计划表 (t_feeding_plan)
CREATE TABLE IF NOT EXISTS `t_feeding_plan` (
                                                `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
                                                `animal_id` BIGINT NOT NULL COMMENT '动物ID',
                                                `food_id` INT NOT NULL COMMENT '饲料ID',
                                                `amount` DECIMAL(10,2) COMMENT '投喂量',
    `feeding_time` TIME COMMENT '投喂时间',
    `feeder_id` BIGINT COMMENT '负责人ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB COMMENT='喂养计划表';

-- 9. 票务订单表 (t_ticket_order)
CREATE TABLE IF NOT EXISTS `t_ticket_order` (
                                                `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
    `user_id` BIGINT COMMENT '用户ID(游客可为空)',
    `mobile` VARCHAR(20) NOT NULL COMMENT '联系手机',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT '总金额',
    `pay_status` TINYINT DEFAULT 0 COMMENT '0:未支付, 1:已支付, 2:已退款',
    `visit_date` DATE COMMENT '预约入园日期',
    `ticket_json` JSON COMMENT '购票详情快照',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    PRIMARY KEY (`order_no`),
    KEY `idx_mobile` (`mobile`)
    ) ENGINE=InnoDB COMMENT='票务订单表';

-- 10. 票种信息表 (t_ticket_product)
CREATE TABLE IF NOT EXISTS `t_ticket_product` (
                                                  `id` INT AUTO_INCREMENT COMMENT '主键ID',
                                                  `name` VARCHAR(50) NOT NULL COMMENT '票种名称',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT DEFAULT 1 COMMENT '1:上架, 0:下架',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_name` (`name`)
    ) ENGINE=InnoDB COMMENT='票种信息表';

-- 11. [新增] 游客表 (t_visitor)
CREATE TABLE IF NOT EXISTS `t_visitor` (
                                           `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
                                           `username` VARCHAR(50) NOT NULL COMMENT '用户名/手机号',
    `password` VARCHAR(64) NOT NULL COMMENT '加密密码',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_visitor_username` (`username`)
    ) ENGINE=InnoDB COMMENT='游客表';

-- 12. [新增] 社区评论表 (t_comment)
CREATE TABLE IF NOT EXISTS `t_comment` (
                                           `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
                                           `visitor_id` BIGINT NOT NULL COMMENT '游客ID',
                                           `nickname` VARCHAR(50) COMMENT '冗余游客昵称',
    `type` VARCHAR(20) NOT NULL COMMENT '类型(animal, facility)',
    `target_id` BIGINT NOT NULL COMMENT '关联目标ID',
    `content` VARCHAR(500) NOT NULL COMMENT '评论内容',
    `rating` INT DEFAULT 5 COMMENT '评分(1-5)',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    PRIMARY KEY (`id`),
    KEY `idx_target` (`type`, `target_id`)
    ) ENGINE=InnoDB COMMENT='社区评论表';

-- 插入一些测试数据
INSERT IGNORE INTO `t_species` (`scientific_name`, `common_name`, `conservation_status`, `diet_type`) VALUES
('Ailuropoda melanoleuca', '大熊猫', 'VU', '草食'),
('Panthera tigris altaica', '东北虎', 'EN', '肉食');

INSERT IGNORE INTO `t_animal` (`name`, `chip_code`, `species_id`, `gender`, `birth_date`, `status`) VALUES
('萌兰', 'CHIP001', 1, 1, '2015-07-04', 1),
('花花', 'CHIP002', 1, 0, '2020-07-04', 1);

INSERT IGNORE INTO `t_staff` (`username`, `password`, `real_name`, `role`, `status`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'admin', 1);
-- password is '123456' in MD5

INSERT IGNORE INTO `t_ticket_product` (`name`, `price`, `description`) VALUES
('成人票', 120.00, '适用于18-60岁成年人'),
('儿童票', 60.00, '适用于1.2米-1.5米儿童'),
('学生票', 60.00, '持有效学生证'),
('老年票', 30.00, '60岁以上老人');

-- 插入测试游客 (密码 123456)
INSERT IGNORE INTO `t_visitor` (`username`, `password`, `nickname`) VALUES
('visitor001', 'e10adc3949ba59abbe56e057f20f883e', '爱护动物的小明');