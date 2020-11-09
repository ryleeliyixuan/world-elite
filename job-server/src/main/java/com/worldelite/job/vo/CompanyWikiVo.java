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
    private String address; //公司地址
    private String banner; //图片地址

    private WikiModuleVo wikiModule; //模块启用情况

    private List<CompanyEmployeeVo> employeeList; //雇员数量
    private List<CompanyProductVo> productList; //产品列表
    private CompanyMarketVo market; //市值信息
    private List<CompanyHistoryVo> historyList; //发展路径
    private List<CompanyStructureVo> structure; //公司架构
    private List<CompanySalaryVo> salaryList; //薪资待遇
    private List<CompanyRecruitVo> recruitList; //招聘进度
    private List<CompanyEnvironmentVo> environmentList; //工作环境
    private List<CompanyDepartmentVo> department; //部门
    private List<CompanyHonorVo> honorList; //企业荣誉
    private List<CompanyPostVo> postList; //推荐帖子
    private List<JobVo> jobList; //推荐职位

    private Integer favoriteCount; //订阅量
    private Integer score; //公司评分
}
