package com.worldelite.job.vo;

import com.worldelite.job.entity.School;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class SchoolVo implements VoConvertable<SchoolVo, School>{

    private Integer id;
    private String name;
    private String siteLink;
    private CountryVo country;

    @Override
    public SchoolVo asVo(School school) {
        setId(school.getId());
        setName(school.getName());
        setSiteLink(school.getSiteLink());
        return this;
    }
}
