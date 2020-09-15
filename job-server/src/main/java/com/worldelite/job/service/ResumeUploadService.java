package com.worldelite.job.service;

import com.worldelite.job.constants.ConfigType;
import com.worldelite.job.util.ResumeUtils;
import com.worldelite.job.service.read.ResumeFileRead;
import com.worldelite.job.service.read.ResumeFileReadFactory;
import com.worldelite.job.service.IEmailService;
import com.worldelite.job.form.EmailForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 熊文剑
 */
@Slf4j
@Service
public class ResumeUploadService {

    @Autowired
    private ResumeFileReadFactory resumeFileReadFactory;

    @Autowired
    private ResumeUtils resumeUtils;

    @Autowired
    private ConfigService configService;

    @Autowired
    private IEmailService emailService;
    /**
     * 读取简历内容
     *
     * @param urls 简历url
     * @return
     */
    @Async
    public List<String> getResumesDetail(String[] urls) {
        List<String> resumes = new ArrayList<>();
        for (String url: urls) {
            try {
                //region 读取附件简历内容
                String suffix = url.substring(url.lastIndexOf(".") + 1);
                ResumeFileRead fileRead = resumeFileReadFactory.getFileRead(suffix);
                String resume = fileRead.read(new URL(url));
                if (StringUtils.isBlank(resume)) continue;
                resumes.add(resume);
            } catch (Exception e) {
                log.error("上传简历时异常", e);
            }
        }
        return resumes;
    }

    /**
     * 获取邮箱
     *
     * @param resumes 简历
     * @return
     */
    public List<String> getResumesEmail(List<String> resumes) {
        List<String> emails = new ArrayList<>();
        for (String resume: resumes) {
            emails.add(resumeUtils.getEmail(resume));
        }
        return emails;
    }

    /**
     * 获取电话号码
     *
     * @param resumes 简历
     * @return
     */
    public List<String> getResumesPhone(List<String> resumes) {
        List<String> phones = new ArrayList<>();
        for (String resume: resumes) {
            phones.add(resumeUtils.getPhone(resume));
        }
        return phones;
    }

    /**
     * 发送邀请注册邮件到用户邮箱
     *
     * @param emails 邮件
     * @return
     */
    public void sendInviteLink(List<String> emails) {
        for (String email: emails) {
            final String inviteLink = ""; //需要生成邀请链接
            EmailForm emailForm = configService.getEmailForm(ConfigType.EMAIL_INVITE_LINK);
            emailForm.setAddress(email);
            emailForm.setSubject("【WorldElite】欢迎成为我们的用户");
            emailForm.setEmailBody(emailForm.getEmailBody().replace("${INVITE_LINK}", inviteLink));
            emailService.sendEmail(emailForm);
        }
    }
}
