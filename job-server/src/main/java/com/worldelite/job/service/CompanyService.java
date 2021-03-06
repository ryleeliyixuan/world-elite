package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.*;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.*;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class CompanyService extends BaseService{

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCorporateMapper userCorporateMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private CompanyAddressService companyAddressService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyWikiService companyWikiService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CompanyNameSearchService companyNameSearchService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "luceneIndexCmdFanoutExchange")
    private FanoutExchange exchange;

    @Resource(name = "luceneIndexCmdQueue")
    private Queue queue;
    /**
     * 搜索公司
     *
     * @return
     */
    public PageResult<CompanyVo> search(CompanyListForm companyListForm) {
        CompanyOptions options = new CompanyOptions();
        options.setFullName(companyListForm.getName());
        AppUtils.setPage(companyListForm);
        Page<Company> companyPage = (Page<Company>) companyMapper.selectAndList(options);
        PageResult<CompanyVo> pageResult = new PageResult<>(companyPage);
        pageResult.setList(AppUtils.asVoList(companyPage, CompanyVo.class));
        return pageResult;
    }

    /**
     * 搜索公司百科
     * @param listForm
     * @return
     */
    public PageResult<CompanyVo> searchCompanyWiki(CompanyWikiListForm listForm){
        AppUtils.setPage(listForm);
        Page<Company> companyPage = (Page<Company>) companyMapper.searchWikiByName(listForm.getKeyword());
        PageResult<CompanyVo> pageResult = new PageResult<>(companyPage);
        List<CompanyVo> companyVoList = new ArrayList<>(companyPage.size());
        for(Company company: companyPage){
            CompanyVo companyVo = toCompanyVo(company);
            companyVo.setWikiSummary(companyWikiService.getCompanyWikiSummary(company.getId()));
            companyVoList.add(companyVo);
        }
        pageResult.setList(companyVoList);
        return pageResult;
    }

    /**
     * 通过关键字和行业ID搜索公司百科
     * @param listForm
     * @return
     */
    public PageResult<CompanyVo> searchCompanyWikiWithIndustry(CompanyWikiListForm listForm){
        AppUtils.setPage(listForm);
        CompanyOptions companyOptions = new CompanyOptions();
        companyOptions.setName(listForm.getKeyword());
        companyOptions.setIndustryId(listForm.getIndustryId());
        Page<Company> companyPage = (Page<Company>) companyMapper.searchWiki(companyOptions);
        PageResult<CompanyVo> pageResult = new PageResult<>(companyPage);
        List<CompanyVo> companyVoList = new ArrayList<>(companyPage.size());
        for(Company company: companyPage){
            CompanyVo companyVo = toCompanyVo(company);
            companyVo.setWikiSummary(companyWikiService.getCompanyWikiSummary(company.getId()));
            companyVoList.add(companyVo);
        }
        pageResult.setList(companyVoList);
        return pageResult;
    }

    /**
     * 获取公司列表
     *
     * @param listForm
     * @return
     */
    public PageResult<CompanyVo> getCompanyList(CompanyListForm listForm){
        CompanyOptions options = new CompanyOptions();
        BeanUtil.copyProperties(listForm, options);
        AppUtils.setPage(listForm);
        Page<Company> companyPage = (Page<Company>) companyMapper.selectAndList(options);
        PageResult<CompanyVo> pageResult = new PageResult<>(companyPage);
        List<CompanyVo> companyVoList = new ArrayList<>(companyPage.size());
        for(Company company: companyPage){
            companyVoList.add(toCompanyVo(company));
        }
        pageResult.setList(companyVoList);
        return pageResult;
    }

    /**
     * 获取公司用户信息
     *
     * @param userId
     * @return
     */
    public CompanyUserVo getCompanyUser(Long userId) {
        CompanyUser options = new CompanyUser();
        options.setUserId(userId);
        List<CompanyUser> companyUserList = companyUserMapper.selectAndList(options);
        if (CollectionUtils.isEmpty(companyUserList)) {
            return null;
        }
        CompanyUser companyUser = companyUserList.get(0);
        CompanyUserVo companyUserVo = new CompanyUserVo();
        companyUserVo.asVo(userCorporateMapper.selectByPrimaryKey(userId));
        companyUserVo.setDepart(companyUser.getDepart());
        companyUserVo.setPost(companyUser.getPost());
        CompanyVo companyVo = getSimpleCompanyInfo(companyUser.getCompanyId());
        if(companyVo == null){
            return null;
        }
        companyUserVo.setCompany(companyVo);
        return companyUserVo;
    }

    /**
     * 获取公司首页数据
     *
     * @param companyId
     * @return
     */
    public CompanyVo getCompanyHomeData(Long companyId){
        CompanyVo companyVo = getCompanyInfo(companyId);
        if(companyVo == null){
            return null;
        }
        JobListForm jobListForm = new JobListForm();
        jobListForm.setCompanyId(companyId);
        jobListForm.setStatus(JobStatus.PUBLISH.value);
        jobListForm.setPage(1);
        jobListForm.setLimit(3);
        jobListForm.setSort("-id");
        companyVo.setJobList(jobService.getJobList(jobListForm).getList());
        companyVo.setAddressList(companyAddressService.getCompanyAddressList(companyId));
        CompanyWikiVo companyWikiVo = companyWikiService.getCompanyWiki(companyId);
        companyVo.setCompanyWiki(companyWikiVo != null ? companyWikiVo.getContent(): "");
        return companyVo;
    }

    /**
     * 获取当前用户的公司信息
     *
     * @return
     */
    public CompanyVo getMyCompanyInfo(){
        CompanyUser companyUser = companyUserMapper.selectByUserId(curUser().getId());
        if(companyUser == null){
            return null;
        }
        CompanyVo companyVo = getCompanyInfo(companyUser.getCompanyId());
        companyVo.setAddressList(companyAddressService.getCompanyAddressList(Long.valueOf(companyVo.getId())));
        companyVo.setCompleteProgress(AppUtils.calCompleteProgress(companyVo));
        return companyVo;
    }

    /**
     * 获取公司信息
     *
     * @param id
     * @return
     */
    public CompanyVo getCompanyInfo(Long id){
        Company company = companyMapper.selectByPrimaryKey(id);
        if (company == null) return null;
        CompanyVo companyVo = toCompanyVo(company);
        companyVo.setAddressList(companyAddressService.getCompanyAddressList(Long.valueOf(companyVo.getId())));
        companyVo.setCompleteProgress(AppUtils.calCompleteProgress(companyVo));
        companyVo.setWikiSummary(companyWikiService.getCompanyWikiSummary(id));
        return companyVo;
    }

    /**
     * 获取公司信息：适合列表
     *
     * @param companyId
     * @return
     */
    public CompanyVo getSimpleCompanyInfo(Long companyId) {
        Company company = companyMapper.selectSimpleById(companyId);
        if (company == null) return null;
        return toCompanyVo(company);
    }

    /**
     * 保存公司信息
     *
     * @param companyForm
     */
    public Long saveCompany(CompanyForm companyForm){
        checkCompanyUser(companyForm.getId());
        Company company = companyMapper.selectByPrimaryKey(companyForm.getId());
        if(company == null){
            company = new Company();
        }
        company.setName(companyForm.getName());
        company.setFullName(companyForm.getFullName());
        company.setHomepage(companyForm.getHomepage());
        company.setStageId(companyForm.getStageId());
        company.setScaleId(companyForm.getScaleId());
        company.setIndustryId(companyForm.getIndustryId());
        company.setPropertyId(companyForm.getPropertyId());
        if(StringUtils.isNotEmpty(companyForm.getLogo())){
            company.setLogo(AppUtils.getOssKey(companyForm.getLogo()));
        }
        if(StringUtils.isNotEmpty(companyForm.getIntroduction())){
            company.setIntroduction(companyForm.getIntroduction());
        }
        if(StringUtils.isNotEmpty(companyForm.getSynopsis())){
            company.setSynopsis(companyForm.getSynopsis());
        }
        if(company.getId() == null){
            company.setId(AppUtils.nextId());
            companyMapper.insertSelective(company);
            //同时创建一个空的百科
            CompanyWikiForm companyWikiForm = new CompanyWikiForm();
            companyWikiForm.setCompanyId(company.getId());
            companyWikiService.saveCompanyWiki(companyWikiForm);
        }else{
            company.setUpdateTime(new Date());
            companyMapper.updateByPrimaryKeySelective(company);
        }

        companyNameSearchService.createOrRefreshCompanyNameIndex();

        //MQ广播索引更新指令
        LuceneIndexCmdDto indexCmdDto = new LuceneIndexCmdDto(null, OperationType.CREATE_OR_UPDATE, BusinessType.COMPANY, queue.getActualName());
        rabbitTemplate.convertAndSend(exchange.getName(), StrUtil.EMPTY, indexCmdDto);
        log.info("Lucene index synchronize command message [saveCompany] {}",indexCmdDto.toString());

        return company.getId();
    }

    /**
     * 删除公司
     * @param companyId
     */
    public void delCompany(Long companyId){
        Company company = companyMapper.selectSimpleById(companyId);
        if(company != null){
            company.setDelFlag(Bool.TRUE);
            companyMapper.updateByPrimaryKeySelective(company);

            companyNameSearchService.createOrRefreshCompanyNameIndex();
            //MQ广播索引更新指令
            LuceneIndexCmdDto indexCmdDto = new LuceneIndexCmdDto(null, OperationType.DELETE, BusinessType.COMPANY, queue.getActualName());
            rabbitTemplate.convertAndSend(exchange.getName(), StrUtil.EMPTY, indexCmdDto);
            log.info("Lucene index synchronize command message [delCompany] {}",indexCmdDto.toString());
        }
    }

    /**
     * 检查当前用户是否拥有公司信息的修改权限
     *
     * @param companyId
     */
    public void checkCompanyUser(Long companyId) {
        // 管理员
        if(curUser().getType() == UserType.ADMIN.value){
            return;
        }
        CompanyUser options = new CompanyUser();
        options.setUserId(curUser().getId());
        options.setCompanyId(companyId);
        List<CompanyUser> companyUserList = companyUserMapper.selectAndList(options);
        if (CollectionUtils.isEmpty(companyUserList)) {
            throw new ServiceException(ApiCode.PERMISSION_DENIED);
        }
    }

    private CompanyVo toCompanyVo(Company company){
        CompanyVo companyVo = new CompanyVo().asVo(company);
        companyVo.setScale(dictService.getById(company.getScaleId()));
        companyVo.setStage(dictService.getById(company.getStageId()));
        companyVo.setIndustry(dictService.getById(company.getIndustryId()));
        companyVo.setProperty(dictService.getById(company.getPropertyId()));
        companyVo.setAddressList(companyAddressService.getCompanyAddressList(company.getId()));
        if(curUser() != null){
            Boolean favorite = favoriteService.checkUserFavorite(company.getId(), FavoriteType.COMPANY);
            companyVo.setFavoriteFlag(favorite? Bool.TRUE: Bool.FALSE);
        }
        companyVo.setFavoriteNumber(favoriteService.getWikiFavoriteCount(company.getId()));
        return companyVo;
    }
}
