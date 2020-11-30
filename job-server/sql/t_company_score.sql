ALTER TABLE db_worldelite.t_company_score ADD recommend TINYINT(4) DEFAULT 0 NOT NULL;
ALTER TABLE db_worldelite.t_company_score CHANGE recommend recommend TINYINT(4) DEFAULT 0 NOT NULL AFTER content;
