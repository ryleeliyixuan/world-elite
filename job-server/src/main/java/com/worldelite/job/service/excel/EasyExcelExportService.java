package com.worldelite.job.service.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.worldelite.job.constants.Gender;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.constants.UserType;
import com.worldelite.job.entity.Download;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.User;
import com.worldelite.job.form.*;
import com.worldelite.job.service.DownloadService;
import com.worldelite.job.service.FileService;
import com.worldelite.job.service.ResumeService;
import com.worldelite.job.service.UserService;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeEduVo;
import com.worldelite.job.vo.ResumeVo;
import com.worldelite.job.vo.UserVo;
import org.apache.commons.codec.digest.DigestUtils;
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
    private ResumeService resumeService;

    @Autowired
    private FileService fileService;

    @Autowired
    private DownloadService downloadService;

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
            pageResult = resumeService.getResumeList(listForm);
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
        return null;
    }

    private UserExcel toUserExcel(UserVo user) {
        UserExcel userExcel = new UserExcel();
        BeanUtil.copyProperties(user, userExcel);
        userExcel.setGender(user.getGender() == Gender.MALE.value ? "男" : "女");
        userExcel.setPhone(String.valueOf(user.getPhone()));
        userExcel.setCreateTime(DateUtil.format(user.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
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
            resumeExcel.setDegree(resumeEduVo.getDegree().getName());
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
}
