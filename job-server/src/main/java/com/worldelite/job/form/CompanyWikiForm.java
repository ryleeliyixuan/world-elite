package com.worldelite.job.form;

import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyWikiForm {
    private Long companyId; //公司ID
    private String summary; //摘要
    private String content; //百科详情
    private String video; //公司视频
    private String address; //公司地址
    private String banner; //公司图片
}
