package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.JobStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.Company;
import com.worldelite.job.entity.CompanyOptions;
import com.worldelite.job.entity.CompanyUser;
import com.worldelite.job.entity.Job;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.CompanyMapper;
import com.worldelite.job.mapper.CompanyUserMapper;
import com.worldelite.job.mapper.CompanyVerificationMapper;
import com.worldelite.job.mapper.UserMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class CompanyService extends BaseService{

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private CompanyAddressService companyAddressService;

    @Autowired
    private JobService jobService;


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
        companyUserVo.asVo(userMapper.selectByPrimaryKey(userId));
        companyUserVo.setCompany(getSimpleCompanyInfo(companyUser.getCompanyId()));
        companyUserVo.setDepart(companyUser.getDepart());
        companyUserVo.setPost(companyUser.getPost());
        return companyUserVo;
    }

    /**
     * 获取用户关联的公司
     * @param userId
     * @return
     */
    public CompanyVo getUserCompany(Long userId){
        CompanyUser options = new CompanyUser();
        options.setUserId(userId);
        List<CompanyUser> companyUserList = companyUserMapper.selectAndList(options);
        if (CollectionUtils.isEmpty(companyUserList)) {
            return null;
        }
        CompanyUser companyUser = companyUserList.get(0);
        return getSimpleCompanyInfo(companyUser.getCompanyId());
    }

    /**
     * 获取公司首页数据
     *
     * @param companyId
     * @return
     */
    public CompanyVo getCompanyHomeData(Long companyId){
        CompanyVo companyVo = getSimpleCompanyInfo(companyId);
        JobListForm jobListForm = new JobListForm();
        jobListForm.setCompanyId(companyId);
        jobListForm.setStatus(JobStatus.PUBLISH.value);
        jobListForm.setPage(1);
        jobListForm.setLimit(3);
        jobListForm.setSort("-pub_time");
        companyVo.setJobList(jobService.getJobList(jobListForm).getList());
        companyVo.setAddressList(companyAddressService.getCompanyAddressList(Long.valueOf(companyVo.getId())));
        return companyVo;
    }

    /**
     * 获取当前用户的公司信息
     *
     * @return
     */
    public CompanyVo getMyCompanyInfo(){
        CompanyVo companyVo = getUserCompany(curUser().getId());
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
        return companyVo;
    }

    /**
     * 获取公司信息：适合列表
     *
     * @param companyId
     * @return
     */
    private CompanyVo getSimpleCompanyInfo(Long companyId) {
        Company company = companyMapper.selectSimpleById(companyId);
        if (company == null) return null;
        return toCompanyVo(company);
    }

    /**
     * 保存公司信息
     *
     * @param companyForm
     */
    public void saveCompany(CompanyForm companyForm){
        checkCompanyUser(companyForm.getId());
        Company company = companyMapper.selectByPrimaryKey(companyForm.getId());
        company.setName(companyForm.getName());
        company.setHomepage(companyForm.getHomepage());
        company.setStageId(companyForm.getStageId());
        company.setScaleId(companyForm.getScaleId());
        company.setIndustryId(companyForm.getIndustryId());
        company.setPropertyId(companyForm.getPropertyId());
        company.setLogo(AppUtils.getOssKey(companyForm.getLogo()));
        company.setUpdateTime(new Date());
        company.setIntroduction(companyForm.getIntroduction());
        companyMapper.updateByPrimaryKeyWithBLOBs(company);
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
        return companyVo;
    }
}
