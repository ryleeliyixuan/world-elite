package com.worldelite.job.service.sdk;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.PolicyConditions;
import com.worldelite.job.constants.OssDir;
import com.worldelite.job.context.config.AliConfig;
import com.worldelite.job.vo.UploadTokenVo;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author yeguozhong yedaxia.github.com
 */
@Component
public class AliOssService {

    @Autowired
    private AliConfig aliOssConfig;

    /**
     * 获取上传图片的token
     *
     * @param fileType
     * @return
     */
    public UploadTokenVo getUploadPicToken(String fileType){
        OSSClient client = createOSSClient();
        final String objectKey = genFileName(OssDir.PIC, fileType);
        final long expireEndTime = System.currentTimeMillis() + aliOssConfig.getUploadTokenExpired() * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0,
                aliOssConfig.getUploadMaxPicSize() * 1024 * 1024);
        final String postPolicy = client.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = StringUtils.getBytesUtf8(postPolicy);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = client.calculatePostSignature(postPolicy);
        UploadTokenVo uploadTokenVo = new UploadTokenVo();
        uploadTokenVo.setOSSAccessKeyId(aliOssConfig.getAccessKeyId());
        uploadTokenVo.setExpire(String.valueOf(expireEndTime/1000));
        uploadTokenVo.setKey(objectKey);
        uploadTokenVo.setPolicy(encodedPolicy);
        uploadTokenVo.setSignature(postSignature);
        uploadTokenVo.setHost(String.format("https://%s.%s", aliOssConfig.getBucketName(), aliOssConfig.getEndPoint()));
        return uploadTokenVo;
    }

    private OSSClient createOSSClient() {
        return new OSSClient(String.format("https://%s", aliOssConfig.getEndPoint()), aliOssConfig.getAccessKeyId(), aliOssConfig.getAccessKeySecret());
    }


    private static String genFileName(OssDir osDir, String fileSuffix) {

        if(fileSuffix.contains(".")){
            fileSuffix = fileSuffix.substring(fileSuffix.lastIndexOf("."));
        }
        return osDir.value + "/"+ UUID.randomUUID().toString().replace("-", "")
                + fileSuffix.toLowerCase();
    }
}
