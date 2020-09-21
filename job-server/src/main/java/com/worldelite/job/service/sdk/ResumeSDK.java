package com.worldelite.job.service.sdk;

import com.alibaba.fastjson.JSONObject;
import com.worldelite.job.constants.Gender;
import com.worldelite.job.entity.AttachmentParser;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeRepository;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.mapper.AttachmentParserMapper;
import com.worldelite.job.service.BaseService;
import com.worldelite.job.util.FileDownloadUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ResumeSDK extends BaseService {


    @Autowired
    private AttachmentParserMapper attachmentParserMapper;

    private String api = "http://www.resumesdk.com/api/parse";
    private String url = "https://oss.myworldelite.com/attachment/1085befd058d44af9f3a0a2c86e49ce2.pdf";	//替换为你的文件名
    private int uid = 2009042;		//替换为你的用户名（int格式）
    private String pwd = "yU7YxS";	//替换为你的密码（String格式）

    /**
     * 解析附件简历内容
     * @param url
     * @throws Exception
     */
    public AttachmentParser parse(String url) {
    	// 设置头字段
        HttpPost httpPost = new HttpPost(api);
        httpPost.addHeader("content-type", "application/json");
        String resCont = "";

        try {
            String data = getBase64String(url);
            // 设置内容信息
            JSONObject json = new JSONObject();
            json.put("uid", uid);            // 用户id
            json.put("pwd", pwd);            // 用户密码
            json.put("file_name", url);    // 文件名
            json.put("file_cont", data);    // 经base64编码过的文件内容
            StringEntity params = new StringEntity(json.toString(), Consts.UTF_8);
            httpPost.setEntity(params);
            // 发送请求
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpPost);
            // 处理返回结果
            resCont = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("调用ResumeSDK接口失败");
        }
        JSONObject res = JSONObject.parseObject(resCont);
        JSONObject status = res.getJSONObject("status");
        if(status.getIntValue("code") != 200) {
        	throw new ServiceException("request failed: code=<" + status.getIntValue("code") + ">, message=<" + status.getString("message") + ">");
        }
        else {
        	JSONObject result = res.getJSONObject("result");
            // 解析内容保存到数据库
            //按邮箱判断，存在就更新，不存在就新增
            //List<AttachmentParser> attachmentParserList = attachmentParserMapper.selectByEmail(result.getString("email"));
            AttachmentParser attachmentParser = new AttachmentParser();
            //if(CollectionUtils.isEmpty(attachmentParserList)){
                attachmentParser.setEmail(result.getString("email"));
                attachmentParser.setAttachContent(result.toString());
                attachmentParserMapper.insertSelective(attachmentParser);
            //}else{
            //    attachmentParser = attachmentParserList.get(0);
            //    attachmentParser.setAttachContent(result.toString());
            //    attachmentParserMapper.updateByPrimaryKeySelective(attachmentParser);
            //}
            return attachmentParser;
        }
    }

    public Resume getResume(JSONObject result) {
        Resume resume = new Resume();
        resume.setName(result.getString("name"));
        resume.setBirth(getDate(result.getString("birthday")));
        resume.setGender(getGender(result.getString("gender")));
        resume.setGraduateTime(getDate(result.getString("grad_time")));
        resume.setCurPlace(result.getString("living_address_norm"));
        resume.setIntroduction(result.getString("cont_my_desc"));
        return resume;
    }

    /**
     * 从OSS上读取文件
     * 并转码base64字符串
     * @param url OSS上的文件路径
     * @return
     * @throws IOException
     */
    private String getBase64String(String url) throws IOException {
        // 读取简历内容
        BufferedInputStream inputStream = new BufferedInputStream(FileDownloadUtil.syncDownload(new URL(url)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        byte[] bytes = out.toByteArray();
        return new String(Base64.encodeBase64(bytes), Consts.UTF_8);
    }

    /**
     * ResumeSDK返回的日期为字符串类型，且可能为2019.10.01或者2019.10两种类型中的一个
     * @param date
     * @return
     */
    private Date getDate(String date){
        if(date==null) return null;
        try {
            if(date.matches("\\d+\\.\\d+\\.\\d+")){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                return dateFormat.parse(date);
            }
            if(date.matches("\\d+\\.\\d+")){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM");
                return dateFormat.parse(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //其它情况视为转换失败，默认返回null
        return null;
    }

    /**
     * 将获取到的性别信息转化为系统性别的枚举类型
     * @param gender
     * @return
     */
    private Byte getGender(String gender){
        if("男".equals(gender) || "male".equals(gender)){
            return Gender.MALE.value;
        }
        if("女".equals(gender) || "female".equals(gender)){
            return Gender.FEMALE.value;
        }
        return 0;
    }
}
