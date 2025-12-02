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

-- 插入一些测试数据
INSERT INTO `t_species` (`scientific_name`, `common_name`, `conservation_status`, `diet_type`) VALUES
                                                                                                   ('Ailuropoda melanoleuca', '大熊猫', 'VU', '草食'),
                                                                                                   ('Panthera tigris altaica', '东北虎', 'EN', '肉食');

INSERT INTO `t_animal` (`name`, `chip_code`, `species_id`, `gender`, `birth_date`, `status`) VALUES
                                                                                                 ('萌兰', 'CHIP001', 1, 1, '2015-07-04', 1),
                                                                                                 ('花花', 'CHIP002', 1, 0, '2020-07-04', 1);