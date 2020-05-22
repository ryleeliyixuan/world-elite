package com.worldelite.job.vo;

import com.worldelite.job.entity.School;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class SchoolVo implements VoConvertable<SchoolVo, School>{

    private Integer id; //学校ID
    private String name; //学校名
    private String siteLink; //主页
    private CountryVo country; //所在国家

    @Override
    public SchoolVo asVo(School school) {
        setId(school.getId());
        setName(school.getName());
        setSiteLink(school.getSiteLink());
        return this;
    }
}
