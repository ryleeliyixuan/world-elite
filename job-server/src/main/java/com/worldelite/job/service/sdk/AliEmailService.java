package com.worldelite.job.service.sdk;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.worldelite.job.context.config.AliConfig;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.EmailForm;
import com.worldelite.job.service.IEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 阿里云邮件发送
 *
 * @author yeguozhong yedaxia.github.com
 */
@Component
@Slf4j
public class AliEmailService implements IEmailService {

    @Autowired
    private AliConfig aliConfig;

    @Value("${aliyun.email.account}")
    private String EMAIL_ACCOUNT;

    @Value("${aliyun.email.alias}")
    private String EMAIL_ALIAS;

    @Override
    public void sendEmail(EmailForm emailForm) {
        IAcsClient acsClient = initAcsClient();
        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setAccountName(EMAIL_ACCOUNT);
        request.setFromAlias(EMAIL_ALIAS);
        request.setAddressType(1);
        request.setReplyToAddress(true);
        request.setToAddress(emailForm.getAddress());
        request.setSubject(emailForm.getSubject());
        request.setHtmlBody(emailForm.getEmailBody());

        //SDK 采用的是http协议的发信方式, 默认是GET方法，有一定的长度限制。
        //若textBody、htmlBody或content的大小不确定，建议采用POST方式提交，避免出现uri is not valid异常
        request.setMethod(MethodType.POST);

        try{
            SingleSendMailResponse httpResponse = acsClient.getAcsResponse(request);
        }catch (Exception ex){
            log.error("send mail fail : " +  emailForm.getAddress(), ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    private IAcsClient initAcsClient(){
        IClientProfile profile = DefaultProfile.getProfile(aliConfig.getRegion(), aliConfig.getAccessKeyId(), aliConfig.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }
}
