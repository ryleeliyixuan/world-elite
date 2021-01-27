INSERT INTO t_city (`id`, `parent_id`, `name`, `code`, `phone_code`, `zip_code`, `short_name`, `pinyin`, `py`, `first_char`, `english`, `lng`, `lat`, `type`) VALUES (1, 0, '国内', '1', '', '', '国内', 'guo nei', 'GN', 'G', 'Domestic', '', '', 0);
INSERT INTO t_city (`id`, `parent_id`, `name`, `code`, `phone_code`, `zip_code`, `short_name`, `pinyin`, `py`, `first_char`, `english`, `lng`, `lat`, `type`) VALUES (2, 0, '国外', '2', '', '', '国外', 'guo wai', 'GW', 'G', 'Foreign', '', '', 0);

update t_city set parent_id = 1 where type = 1 and parent_id = 0;
update t_city set parent_id = 2 where type = 2 and parent_id = 0;

alter table t_city drop column type;

delete from t_city where id in (999992, 999993);

INSERT INTO t_city (`id`, `parent_id`, `name`, `code`, `phone_code`, `zip_code`, `short_name`, `pinyin`, `py`, `first_char`, `english`, `lng`, `lat`) VALUES (3, 0, '线上', '3', '', '', '线上', 'xian shang', 'XS', 'X', 'Online', '', '');

-- 更新系统中现有数据
update t_activity set city_id = 3 where city_id in (999992, 999993);
update t_company_address set city_id = 3 where city_id in (999992, 999993);
update t_job set city_id = 3 where city_id in (999992, 999993);
update t_search_job_history_info set city_id = 3 where city_id in (999992, 999993);
update t_user_expect_place set city_id = 3 where city_id in (999992, 999993);
