package com.worldelite.job.service.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.worldelite.job.constants.*;
import com.worldelite.job.entity.Download;
import com.worldelite.job.form.*;
import com.worldelite.job.service.*;
import com.worldelite.job.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class EasyExcelExportService implements IExportExcelService {

    //最大 excel 记录
    @Value("${export.excel.max.count}")
    private Long excelMaxCount;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private JobService jobService;

    @Autowired
    private RegistrationService registrationService;

    @Override
    public String exportUserList(Long userId, UserListForm listForm) {
        final String excelFileName = listForm.genExportExcelName();
        final File excelFile = fileService.getFile(excelFileName);
        if (excelFile.exists()) {
            return excelFileName;
        }
        PageResult<UserVo> pageResult;
        List<UserExcel> excelData = new ArrayList<>();

        Download download = new Download();
        download.setUrl(excelFileName);
        download.setUserId(userId);
        String taskName = "";
        if (listForm.getType() == UserType.COMPANY.value) {
            taskName += "企业用户列表";
        } else {
            taskName += "用户列表";
        }
        taskName = taskName + "_" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
        download.setName(taskName);
        downloadService.saveDownload(download);

        long exportRecordCount = 0;

        do {
            pageResult = userService.getUserList(listForm);
            for (UserVo userVo : pageResult.getList()) {
                excelData.add(toUserExcel(userVo));
                exportRecordCount++;
            }
            listForm.setPage(listForm.getPage() + 1);
            // 更新进度
            download.setProgress(Math.min(excelData.size() * 100 / pageResult.getTotal(), 99));
            downloadService.saveDownload(download);
        } while (pageResult.getHasMore() && exportRecordCount < excelMaxCount);

        EasyExcel.write(fileService.getFile(excelFileName), UserExcel.class).sheet().doWrite(excelData);

        download.setProgress(100);
        downloadService.saveDownload(download);

        return excelFileName;
    }

    @Override
    public String exportCompanyList(Long userId, CompanyListForm listForm) {
        return null;
    }

    @Override
    public String exportResumeList(Long userId, ResumeListForm listForm) {
        final String excelFileName = listForm.genExportExcelName();
        final File excelFile = fileService.getFile(excelFileName);
        if (excelFile.exists()) {
            return excelFileName;
        }

        PageResult<ResumeVo> pageResult;
        List<ResumeExcel> excelData = new ArrayList<>();

        Download download = new Download();
        download.setUrl(excelFileName);
        download.setUserId(userId);
        String taskName = "简历列表_" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
        download.setName(taskName);
        downloadService.saveDownload(download);

        long exportRecordCount = 0;

        do {
            pageResult = null;
            for (ResumeVo resumeVo : pageResult.getList()) {
                excelData.add(toResumeExcel(resumeVo));
                exportRecordCount++;
            }
            listForm.setPage(listForm.getPage() + 1);
            // 更新进度
            download.setProgress(Math.min(excelData.size() * 100 / pageResult.getTotal(), 99));
            downloadService.saveDownload(download);
        } while (pageResult.getHasMore() && exportRecordCount < excelMaxCount);

        EasyExcel.write(fileService.getFile(excelFileName), ResumeExcel.class).sheet().doWrite(excelData);

        download.setProgress(100);
        downloadService.saveDownload(download);

        return excelFileName;
    }

    @Override
    public String exportJobList(Long userId, JobListForm listForm) {
        final String excelFileName = listForm.genExportExcelName();

        final File excelFile = fileService.getFile(excelFileName);
        if (excelFile.exists()) {
            return excelFileName;
        }

        PageResult<JobVo> pageResult;
        List<JobExcel> excelData = new ArrayList<>();

        Download download = new Download();
        download.setUrl(excelFileName);
        download.setUserId(userId);
        String taskName = "职位列表_" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
        download.setName(taskName);
        downloadService.saveDownload(download);

        long exportRecordCount = 0;

        do {
            pageResult = jobService.getJobList(listForm);
            for (JobVo jobVo : pageResult.getList()) {
                excelData.add(toJobExcel(jobVo));
                exportRecordCount++;
            }
            listForm.setPage(listForm.getPage() + 1);
            // 更新进度
            download.setProgress(Math.min(excelData.size() * 100 / pageResult.getTotal(), 99));
            downloadService.saveDownload(download);
        } while (pageResult.getHasMore() && exportRecordCount < excelMaxCount);

        EasyExcel.write(fileService.getFile(excelFileName), JobExcel.class).sheet().doWrite(excelData);

        download.setProgress(100);
        downloadService.saveDownload(download);

        return excelFileName;
    }

    @Override
    public String exportRegistrationList(Long userId, RegistrationListForm listForm) {
        final String excelFileName = listForm.genExportExcelName();
        final File excelFile = fileService.getFile(excelFileName);
        if (excelFile.exists()) {
            return excelFileName;
        }

        PageResult<RegistrationVo> pageResult;
        List<RegistrationExcel> excelData = new ArrayList<>();

        Download download = new Download();
        download.setUrl(excelFileName);
        download.setUserId(userId);
        String taskName = "报名列表_" + DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
        download.setName(taskName);
        downloadService.saveDownload(download);

        long exportRecordCount = 0;

        do {
            pageResult = registrationService.getRegistrationList(listForm);
            for (RegistrationVo registrationVo : pageResult.getList()) {
                excelData.add(toRegistrationExcel(registrationVo));
                exportRecordCount++;
            }
            listForm.setPage(listForm.getPage() + 1);
            // 更新进度
            download.setProgress(Math.min(excelData.size() * 100 / pageResult.getTotal(), 99));
            downloadService.saveDownload(download);
        } while (pageResult.getHasMore() && exportRecordCount < excelMaxCount);

        EasyExcel.write(fileService.getFile(excelFileName), RegistrationExcel.class).sheet().doWrite(excelData);
        download.setProgress(100);
        downloadService.saveDownload(download);
        return excelFileName;
    }

    private RegistrationExcel toRegistrationExcel(RegistrationVo registrationVo) {
        RegistrationExcel registrationExcel = new RegistrationExcel();
        BeanUtil.copyProperties(registrationVo,registrationExcel);
        registrationExcel.setNumber("");
        registrationExcel.setEducation("");
        registrationExcel.setStatus("");
        return registrationExcel;
    }

    private UserExcel toUserExcel(UserVo user) {
        UserExcel userExcel = new UserExcel();
        BeanUtil.copyProperties(user, userExcel);
        userExcel.setGender(user.getGender() == Gender.MALE.value ? "男" : "女");
        userExcel.setPhone(String.valueOf(user.getPhone()));
        userExcel.setCreateTime(DateUtil.format(user.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
        userExcel.setSubscribeFlag(user.getSubscribeFlag() == Bool.TRUE? "是": "否");
        if (user.getStatus() == UserStatus.NORMAL.value) {
            userExcel.setStatus("正常");
        } else if (user.getStatus() == UserStatus.BLACK.value) {
            userExcel.setStatus("禁用");
        } else {
            userExcel.setStatus("未激活");
        }
        return userExcel;
    }

    private ResumeExcel toResumeExcel(ResumeVo resumeVo){
        ResumeExcel resumeExcel = new ResumeExcel();
        BeanUtil.copyProperties(resumeVo, resumeExcel);
        resumeExcel.setGender(resumeVo.getGender() == Gender.MALE.value ? "男" : "女");
        if(resumeVo.getMaxResumeEdu() != null){
            ResumeEduVo resumeEduVo = resumeVo.getMaxResumeEdu();
            resumeExcel.setSchool(resumeEduVo.getSchoolName());
            resumeExcel.setMajor(resumeEduVo.getMajorName());
            if(resumeEduVo.getDegree() != null){
                resumeExcel.setDegree(resumeEduVo.getDegree().getName());
            }
            resumeExcel.setGpa(String.valueOf(resumeEduVo.getGpa()));
        }
        if(resumeVo.getReturnTime() != null){
            resumeExcel.setReturnTime(DateUtil.format(resumeVo.getReturnTime(), DatePattern.NORM_DATE_PATTERN));
        }
        if(resumeVo.getGraduateTime() != null){
            resumeExcel.setGraduateTime(DateUtil.format(resumeVo.getGraduateTime(), DatePattern.NORM_DATE_PATTERN));
        }
        return resumeExcel;
    }

    private JobExcel toJobExcel(JobVo jobVo){
        JobExcel jobExcel = new JobExcel();
        BeanUtil.copyProperties(jobVo, jobExcel);
        if(jobVo.getJobType() != null) {
            jobExcel.setJobType(jobVo.getJobType().getName());
        }
        if(jobVo.getCategory() != null){
            jobExcel.setCategory(jobVo.getCategory().getName());
        }
        if(jobVo.getCompanyUser() != null){
            jobExcel.setCreator(jobVo.getCompanyUser().getName());
            if(jobVo.getCompanyUser().getCompany() != null){
                jobExcel.setCompany(jobVo.getCompanyUser().getCompany().getFullName());
            }
        }
        if(jobVo.getSalary() != null && jobVo.getSalary().getName() != null){
            String salary = jobVo.getSalary().getName();
            if(jobVo.getSalaryMonths() != null){
                salary += ("x" + jobVo.getSalaryMonths());
            }
            jobExcel.setSalary(salary);
        }
        if(jobVo.getMinDegree() != null){
            jobExcel.setDegree(jobVo.getMinDegree().getName());
        }
        if(jobVo.getCity() != null){
            jobExcel.setCity(jobVo.getCity().getName());
        }
        return jobExcel;
    }
}
