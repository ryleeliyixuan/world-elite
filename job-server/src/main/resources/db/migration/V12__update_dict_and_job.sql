-- 字典表添加职位其他要求数据
INSERT INTO t_dict (id,name,`type`,value) VALUES (1039,'有留学经验优先',27,'1');

-- 职位表去掉address字段，改用职位地址表的address字段
ALTER TABLE t_job DROP COLUMN address;