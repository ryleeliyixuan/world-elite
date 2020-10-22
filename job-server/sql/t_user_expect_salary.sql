ALTER TABLE `db_worldelite`.`t_user_expect_salary`
DROP COLUMN `min_value`,
DROP COLUMN `max_value`,
ADD COLUMN `salary_id` int(0) NOT NULL AFTER `user_id`;