package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeVo;
import com.worldelite.job.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest(classes = JobApplication.class)
public class ResumeApplicantServiceTest {

    @Autowired
    private ResumeApplicantService resumeApplicantService;

    /**
     * 测试获取默认简历
     */
    @Test
    public void testGetDefaultResume(){
        Long userId = 1255431171070513152L;
        ResumeVo resumeVo = resumeApplicantService.getDefaultOrCreate();
        System.out.println(resumeVo.getUserId());
    }

    /**
     * 测试根据筛选条件获取用户简历
     */
    @Test
    public void testGetResumeList(){
        //什么筛选条件都不传，获取所有数据
        PageResult pageResult = resumeApplicantService.getResumeList(new ResumeListForm());
        System.out.println("简历份数："+pageResult.getList().size());
    }

    /**
     * 测试获取简历详情
     */
    @Test
    public void testGetResumeDetail(){
        Long resumeId = 1308653216016195584L;
        ResumeVo resumeVo = resumeApplicantService.getResumeDetail(resumeId);
        Assert.isTrue("董文嘉".equals(resumeVo.getName()),"简历获取错误");
    }

    /**
     * 保存简历基础信息测试
     */
    @Test
    public void testSaveBasic(){
        Long resumeId = 1308653216016195584L;
        Long userId = 1255431171070513152L;
        UserVo userVo = new UserVo();
        userVo.setUserId(String.valueOf(userId));
        AppUtils.request().setAttribute(AttrKeys.LOGIN_USER,userVo);
        ResumeForm resumeForm = new ResumeForm();
        resumeForm.setId(resumeId);
        resumeForm.setIntroduction("测试");
        ResumeVo resumeVo = resumeApplicantService.saveBasic(resumeForm);
        Assert.isTrue("测试".equals(resumeVo.getIntroduction()),"自我介绍信息错误");
    }

}
