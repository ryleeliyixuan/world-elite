package com.worldelite.job.vo;

import com.github.pagehelper.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 *
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class PageResult<T> {

    private Integer total;
    private Integer pageCount;
    private Integer currentPage;
    private Integer pageSize;
    private List<T> list;
    private Boolean hasMore;

    public PageResult(){}

    public PageResult(Page result){
        this.total = (int)result.getTotal();
        this.currentPage = result.getPageNum();
        this.pageSize = result.getPageSize();
        calPageCountAndHasMore(result);
    }

    public void calPageCountAndHasMore(List list){
        if(pageSize != null && pageSize != 0 && list != null){
            setPageCount(total%pageSize == 0 ? total/pageSize : total/pageSize + 1);
            setHasMore(list.size() + (this.currentPage - 1) * this.pageSize < this.total);
        }
    }

    public static PageResult emptyResult(){
        PageResult pageResult = new PageResult();
        pageResult.setTotal(0);
        pageResult.setCurrentPage(0);
        pageResult.setHasMore(false);
        pageResult.setPageSize(0);
        pageResult.setPageCount(0);
        pageResult.setList(Collections.EMPTY_LIST);
        return pageResult;
    }
}
