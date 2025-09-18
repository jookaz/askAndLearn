
# 创建课程以及章节的表结构以及插入随机数据
CREATE TABLE `course` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `teacher_id` BIGINT NOT NULL,
    `course_name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';
CREATE TABLE `chapter` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `course_id` BIGINT NOT NULL COMMENT '逻辑外键（对应course.id）',
    `chapter_name` VARCHAR(255) NOT NULL,
    `chapter_order` INT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='章节表';
INSERT INTO `course` (`teacher_id`, `course_name`, `description`) VALUES
(1001, '数据库系统', '本科数据库课程'),
(1002, '数据结构与算法', '计算机专业核心课程'),
(1001, '操作系统原理', '操作系统原理与设计'),
(1003, '人工智能基础', 'AI入门课程'),
(1002, '计算机网络', '网络通信与协议');
-- 课程ID 1（数据库系统）的章节
INSERT INTO `chapter` (`course_id`, `chapter_name`, `chapter_order`) VALUES
(1, '数据库基础', 1),
(1, '事务管理', 2),
(1, '索引优化', 3);

-- 课程ID 2（数据结构与算法）的章节
INSERT INTO `chapter` (`course_id`, `chapter_name`, `chapter_order`) VALUES
(2, '线性表', 1),
(2, '栈与队列', 2),
(2, '树与图', 3),
(2, '排序算法', 4);

-- 课程ID 3（操作系统原理）的章节
INSERT INTO `chapter` (`course_id`, `chapter_name`, `chapter_order`) VALUES
(3, '进程管理', 1),
(3, '内存管理', 2);

-- 课程ID 4（人工智能基础）的章节
INSERT INTO `chapter` (`course_id`, `chapter_name`, `chapter_order`) VALUES
(4, '机器学习基础', 1),
(4, '神经网络', 2);

-- 课程ID 5（计算机网络）的章节
INSERT INTO `chapter` (`course_id`, `chapter_name`, `chapter_order`) VALUES
(5, 'TCP/IP协议', 1),
(5, '网络安全', 2);
# 创建学生以及老师的表数据
