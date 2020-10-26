package com.worldelite.job.vo;

import com.worldelite.job.entity.*;
import lombok.Data;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class CompanyWikiVo {
    private CompanyVo company; //公司信息
    private String summary; //摘要
    private String content; //内容
    private String video; //公司视频
    private CityVo city; //所在城市
    private List<CompanyEmployee> employeeList; //雇员数量
    private List<CompanyProduct> productList; //产品列表
    private CompanyMarket market; //市值信息
    private List<CompanyHistory> historyList; //发展路径
    private List<CompanyStructure> structure; //公司架构
}
