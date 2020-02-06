package com.worldelite.job.form;

import lombok.Data;

/**
 * 分页表单
 *
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class PageForm {

    private Integer page;
    private Integer limit;
    private String sort;

    public PageForm(){
        this(1);
    }

    private PageForm(Integer page){
        this(page, null);
    }

    private PageForm(Integer page, Integer pageSize) {
        this.page = (page == null || page < 1)? 1 :page;
        this.limit = (pageSize == null || pageSize < 0 || pageSize > 1000) ? 10: pageSize;
    }

    public static PageForm pageOf(Integer page){
        return new PageForm(page);
    }

    public static PageForm pageOf(Integer page, Integer pageSize) {
        return new PageForm(page, pageSize);
    }
}
