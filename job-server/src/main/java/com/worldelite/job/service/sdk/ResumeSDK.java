package com.worldelite.job.service.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.worldelite.job.constants.Gender;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.AttachmentParserMapper;
import com.worldelite.job.service.BaseService;
import com.worldelite.job.util.FileDownloadUtil;
import com.worldelite.job.vo.ResumeEduVo;
import com.worldelite.job.vo.ResumeVo;
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
import java.util.ArrayList;
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
    public JSONObject parse(String url) {
    	// 设置头字段
        HttpPost httpPost = new HttpPost(api);
        httpPost.addHeader("content-type", "application/json");
        String resCont = "";

        try {
            String data = getBase64String(url);
            System.out.println(url);
            // 设置内容信息
            JSONObject json = new JSONObject();
            json.put("uid", uid);            // 用户id
            json.put("pwd", pwd);            // 用户密码
            json.put("file_name", url);    // 文件名
            json.put("file_cont", data);    // 经base64编码过的文件内容
            json.put("need_avatar",1); //是否需要头像
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
            AttachmentParser attachmentParser = new AttachmentParser();
            attachmentParser.setEmail(result.getString("email"));
            attachmentParser.setAttachContent(result.toString());
            attachmentParserMapper.insertSelective(attachmentParser);
            return result;
        }
    }

    /**
     * 解析简历基础信息
     * @param result
     * @return
     */
    public ResumeForm getResume(JSONObject result){
        ResumeForm resume = new ResumeForm();
        resume.setName(result.getString("name"));
        resume.setBirth(getDate(result.getString("birthday")));
        resume.setGender(getGender(result.getString("gender")));
        resume.setGraduateTime(getDate(result.getString("grad_time")));
        resume.setCurPlace(result.getString("living_address_norm"));
        resume.setIntroduction(result.getString("cont_my_desc"));
        return resume;
    }

    /**
     * 解析出教育信息
     * @param result
     * @return
     */
    public List<ResumeEduForm> getResumeEdu(JSONObject result){
        JSONArray eduList = result.getJSONArray("education_objs");
        List<ResumeEduForm> resumeEduList = new ArrayList<>();
        for(int i=0;i<eduList.size();i++){
            ResumeEduForm resumeEdu = new ResumeEduForm();
            JSONObject edu = eduList.getJSONObject(i);
            resumeEdu.setStartTime(getDate(edu.getString("start_date")));
            resumeEdu.setFinishTime(getDate(edu.getString("end_date")));
            resumeEdu.setSchoolName(edu.getString("edu_college"));
            resumeEdu.setMajorName(edu.getString("edu_major"));
            //Todo 解析degree
            resumeEdu.setDegreeId(2);
            try{
                resumeEdu.setGpa(Double.parseDouble(edu.getString("edu_gpa")));
            }catch (Exception e){

            }
            resumeEduList.add(resumeEdu);
        }
        return resumeEduList;
    }

    public List<ResumeLanguageForm> getResumeLanguage(JSONObject result){
        // to do
        return null;
    }

    public List<ResumeCertificateForm> getResumeCertificate(JSONObject result){
        // to do
        return null;
    }

    /**
     * 解析工作经验
     * @param result
     * @return
     */
    public List<ResumeExpForm> getResumeExperience(JSONObject result){
        JSONArray experienceList = result.getJSONArray("job_exp_objs");
        List<ResumeExpForm> resumeExpFormList = new ArrayList<>();
        for(int i=0;i<experienceList.size();i++){
            ResumeExpForm resumeExpForm = new ResumeExpForm();
            JSONObject experience = experienceList.getJSONObject(i);
            resumeExpForm.setStartTime(getDate(experience.getString("start_date")));
            resumeExpForm.setFinishTime(getDate(experience.getString("end_date")));
            resumeExpForm.setCompany(experience.getString("job_cpy"));
            resumeExpForm.setDepart(experience.getString("job_dept"));
            resumeExpForm.setPost(experience.getString("job_position"));
            resumeExpForm.setDescription(experience.getString("job_content"));
            resumeExpFormList.add(resumeExpForm);
        }
        return resumeExpFormList;
    }

    /**
     * 解析实践经验
     * 对应ResumeSDK解析结果中的项目经历
     * @param result
     * @return
     */
    public List<ResumePracticeForm> getResumePractice(JSONObject result){
        List<ResumePracticeForm> resumePracticeFormList = new ArrayList<>();
        JSONArray practiceList = result.getJSONArray("proj_exp_objs");
        for(int i=0;i<practiceList.size();i++){
            JSONObject practice = practiceList.getJSONObject(i);
            ResumePracticeForm resumePracticeForm = new ResumePracticeForm();
            resumePracticeForm.setStartTime(getDate(practice.getString("start_date")));
            resumePracticeForm.setFinishTime(getDate(practice.getString("end_date")));
            resumePracticeForm.setTitle(practice.getString("proj_name"));
            resumePracticeForm.setPost(practice.getString("proj_position"));
            resumePracticeForm.setDescription(practice.getString("proj_content"));
            resumePracticeFormList.add(resumePracticeForm);
        }
        return resumePracticeFormList;
    }

    /**
     * 解析技能标签
     * @param result
     * @return
     */
    public ResumeSkillForm getResumeSkill(JSONObject result){
        List<String> tagList = new ArrayList<>();
        JSONArray skillList = result.getJSONArray("skills_objs");
        ResumeSkillForm resumeSkillForm = new ResumeSkillForm();
        for(int i=0;i<skillList.size();i++){
            JSONObject skill = skillList.getJSONObject(i);
            tagList.add(skill.getString("skills_name"));
        }
        resumeSkillForm.setTagList(tagList.toArray(new String[tagList.size()]));
        return resumeSkillForm;
    }

    /**
     * 解析社交主页
     * ResumeSDK只提供了一条博客地址的解析
     * @param result
     * @return
     */
    public ResumeLinkForm getResumeLink(JSONObject result){
        ResumeLinkForm resumeLinkForm = new ResumeLinkForm();
        resumeLinkForm.setName("博客/主页");
        resumeLinkForm.setLink(result.getString("blog"));
        return resumeLinkForm;
    }

    /**
     * 从OSS上读取文件
     * 并转码base64字符串
     * @param url OSS上的文件路径
     * @return
     * @throws IOException
     */
    public String getBase64String(String url) throws IOException {
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
    public Date getDate(String date){
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
    public Byte getGender(String gender){
        if("男".equals(gender) || "male".equals(gender)){
            return Gender.MALE.value;
        }
        if("女".equals(gender) || "female".equals(gender)){
            return Gender.FEMALE.value;
        }
        return 0;
    }
}
