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

    private List<CompanyEmployeeVo> employeeList; //雇员数量
    private List<CompanyProductVo> productList; //产品列表
    private CompanyMarketVo market; //市值信息
    private List<CompanyHistoryVo> historyList; //发展路径
    private List<CompanyStructureVo> structure; //公司架构

    private Byte cityEnable; //是否启用地址模块
    private Byte employeeEnable; //是否启用雇员数量模块
    private Byte productEnable; //是否启用产品模块
    private Byte marketEnable; //是否启用市值模块
    private Byte historyEnable; //是否启用发展路径模块
    private Byte structureEnable; //是否启用组织架构模块

    private Integer favoriteCount; //订阅量
    private Integer score; //公司评分
}
