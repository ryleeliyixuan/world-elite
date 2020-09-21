package com.worldelite.job.service;

import com.alibaba.fastjson.JSONObject;
import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.AttachmentParser;
import com.worldelite.job.mapper.AttachmentParserMapper;
import com.worldelite.job.service.sdk.ResumeSDK;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 **/
@SpringBootTest(classes = JobApplication.class)
public class ResumeRepositoryServiceTest {

    @Autowired
    private ResumeRepositoryService resumeRepositoryService;

    @Autowired
    private ResumeSDK resumeSDK;

    @Autowired
    private AttachmentParserMapper attachmentParserMapper;

    public ResumeRepositoryServiceTest() {
    }

    @Test
    public void testSaveAttachment(){
        resumeRepositoryService.saveAttachment("https://oss.myworldelite.com/attachment/1085befd058d44af9f3a0a2c86e49ce2.pdf");
    }

    @Test
    public void testGetResume() throws ParseException {
        String json = "{\"college\":\"昆士兰科技大学\",\"resume_parse_time\":\"2020-09-14 11:40:53\",\"cont_job_exp\":\"深圳市腾讯计算机系统有限公司  产品运营实习生\\n2020.2-2020.5  运营社群,分析用户画像,制作PPT和短视频,输出内容吸引群内高中生用户关注,单周报名公益课数\\n200+,IP号引流500+,参与策划以及执行直播活动,打造KOL老师,吸引新用户,共引流约350人次\\n进群,新用户转化率达到30%,单周最高转化率超过50%。\\n北京世纪好未来教育科技有限公司  社群运营实习生  2019.7-2019.9\\n负责完成短期班的标准化课程服务,包括学情沟通,答疑解惑等;负责用户社群的运营和管理,最终达成促\\n转目的;负责课前课后课件的制作以及现场直播 。第一期促转率35%,第二期促转率40%,第三期促转率\\n46%,第四期转化率35%,均在小学年级处于领先地位,为公司带来88.6464w的利润。\\n横渠书院办事处  产品运营实习生\\n2018.7-2018.9\\n负责书院浏览手册的内容挖掘,策划新亮点;结合真实历史,策划线上线下活动,推广关学文化,参与\\n人数超200+人次;整理世界哲学大会和中国哲学大会音频资料约20000+字。\\n东岭集团股份有限公司  东岭璞玉校园记者\\n2018.10-2018.12\\n主导东岭集团HR微信公众号运营,带领三人团队,以校园招聘、就业形势为热点进行文案写作,实习期\\n间与团队共发表12篇文章,共获得阅读量约20,000次,其中以校园为主题的最美辅导员活动以获得\\n5000+点击量而成为当月第一。\\n北京爱益思咨询有限公司  线上运营实习生  2017.7-2017.9\\n添加竞品群,求职群等,进行竞品分析和新公众号粉丝增长。培训活动策划框架,运营的MVP:用最\\n小的成本来完成产品雏形并进行市场检验;个人裂变等等,成功引流1000+的新用户。\\n上海智遇信息科技有限公司  新媒体运营实习生  2016.7-2016.10\\n负责公司校园社群的拉新、留存、促转等工作,共拉新用户100+人,组织过五次社群活动,用户参与度\\n高达80%;策划线上活动,编写活动文案,三个月内,活动总曝光量1000+人。\",\"living_address_norm\":\"中国-北京-北京市-朝阳区\",\"city\":\"北京市\",\"training_objs\":[],\"resume_type\":\"0\",\"cont_job_skill\":\"文案设计 互联网作图工具:创可贴,PPT,PS,美图秀秀,草料二维码生成器\\n用户心理 数据收集分析工具:腾讯问卷,问卷星,金数据,麦客表单\\n运营工具 编辑排版工具:135编辑器 微信社群管理工具:wetool\\n视频编辑工具:PR,优酷iDo\\n语言 英语(流利,习惯原文阅读,CET6),雅思总分6.5(口语7.5)\\n证书 国家创业创新项目结项证书,网易青媒优秀学员,北大创新创业培训班结项证书\\n普通话一级乙等,机动车驾驶证c1\",\"has_oversea_edu\":\"1\",\"job_exp_objs\":[{\"end_date\":\"2020.05\",\"job_nature\":\"实习\",\"job_content\":\"社群,分析用户画像,制作PPT和短视频,输出内容吸引群内高中生用户关注,单周报名公益课数\\n200+,IP号引流500+,参与策划以及执行直播活动,打造KOL老师,吸引新用户,共引流约350人次\\n进群,新用户转化率达到30%,单周最高转化率超过50%。\",\"job_position\":\"产品运营实习生\",\"job_duration\":\"3个月\",\"job_cpy\":\"深圳市腾讯计算机系统有限公司\",\"start_date\":\"2020.02\"},{\"end_date\":\"2019.09\",\"job_nature\":\"实习\",\"job_content\":\"负责完成短期班的标准化课程服务,包括学情沟通,答疑解惑等;负责用户社群的运营和管理,最终达成促\\n转目的;负责课前课后课件的制作以及现场直播 。第一期促转率35%,第二期促转率40%,第三期促转率\\n46%,第四期转化率35%,均在小学年级处于领先地位,为公司带来88.6464w的利润。\",\"job_position\":\"社群运营实习生\",\"job_duration\":\"2个月\",\"job_cpy\":\"北京世纪好未来教育科技有限公司\",\"start_date\":\"2019.07\"},{\"end_date\":\"2018.12\",\"job_content\":\"主导东岭集团HR微信公众号运营,带领三人团队,以校园招聘、就业形势为热点进行文案写作,实习期\\n间与团队共发表12篇文章,共获得阅读量约20,000次,其中以校园为主题的最美辅导员活动以获得\\n5000+点击量而成为当月第一。\",\"job_position\":\"东岭璞玉校园记者\",\"job_duration\":\"2个月\",\"job_cpy\":\"东岭集团股份有限公司\",\"start_date\":\"2018.10\"},{\"end_date\":\"2018.09\",\"job_dept\":\"办事处\",\"job_nature\":\"实习\",\"job_content\":\"负责书院浏览手册的内容挖掘,策划新亮点;结合真实历史,策划线上线下活动,推广关学文化,参与\\n人数超200+人次;整理世界哲学大会和中国哲学大会音频资料约20000+字。\",\"job_position\":\"产品运营实习生\",\"job_duration\":\"2个月\",\"job_cpy\":\"横渠书院\",\"start_date\":\"2018.07\"},{\"end_date\":\"2017.09\",\"job_nature\":\"实习\",\"job_content\":\"添加竞品群,求职群等,进行竞品分析和新公众号粉丝增长。培训活动策划框架,运营的MVP:用最\\n小的成本来完成产品雏形并进行市场检验;个人裂变等等,成功引流1000+的新用户。\",\"job_position\":\"线上运营实习生\",\"job_duration\":\"2个月\",\"job_cpy\":\"北京爱益思咨询有限公司\",\"start_date\":\"2017.07\"},{\"end_date\":\"2016.10\",\"job_nature\":\"实习\",\"job_content\":\"负责公司校园社群的拉新、留存、促转等工作,共拉新用户100+人,组织过五次社群活动,用户参与度\\n高达80%;策划线上活动,编写活动文案,三个月内,活动总曝光量1000+人。\",\"job_position\":\"新媒体运营实习生\",\"job_duration\":\"3个月\",\"job_cpy\":\"上海智遇信息科技有限公司\",\"start_date\":\"2016.07\"}],\"work_company\":\"深圳市腾讯计算机系统有限公司\",\"work_job_nature\":\"实习\",\"major\":\"整合营销传播\",\"proj_exp_objs\":[{\"end_date\":\"2019.11\",\"proj_position\":\"运营营销\",\"proj_content\":\"网易青媒新媒体训练营  优秀学员\\n浏览量破万,并多次被点赞和转发,最终获得优秀学员资质。\",\"proj_name\":\"运营营销\",\"start_date\":\"2019.10\"},{\"end_date\":\"2017.06\",\"proj_content\":\"整体毕业生就业情况进行长期的分析及预测,最终本创新项目取得校级重点资质。\",\"proj_name\":\"主导项目申报\",\"start_date\":\"2016.04\"},{\"end_date\":\"2017.02\",\"proj_content\":\"最终Presentation制作,作为Timer的角色成功把控演讲节奏和时间,获得当次商务策划及运营项\\n目奖项:BusinessPlanRunnerUpBestOveral。\",\"proj_name\":\"合作策划商务模拟运营的草案\",\"start_date\":\"2017.01\"}],\"surname\":\"吴\",\"college_type\":\"4\",\"resume_name\":\"/mnt/apps/resume_web/data/cv_dir/p20200914_114053_u2009042_wpt0_0.pdf\",\"city_norm\":\"中国-北京-北京市-朝阳区\",\"lang_objs\":[],\"email\":\"2757319072@qq.com\",\"raw_text\":\"年龄:22岁  地址:北京市朝阳区\\n吴雨伦\\n电话:15771751259(微信同) 邮箱:2757319072@qq.com\\n教育背景\\n陕西师范大学  本科 哲学\\n2015.9-2019.6\\n昆士兰科技大学  硕士 整合营销传播\\n2019.10-2022.1\\n工作经历\\n深圳市腾讯计算机系统有限公司  产品运营实习生\\n2020.2-2020.5  运营社群,分析用户画像,制作PPT和短视频,输出内容吸引群内高中生用户关注,单周报名公益课数\\n200+,IP号引流500+,参与策划以及执行直播活动,打造KOL老师,吸引新用户,共引流约350人次\\n进群,新用户转化率达到30%,单周最高转化率超过50%。\\n北京世纪好未来教育科技有限公司  社群运营实习生  2019.7-2019.9\\n负责完成短期班的标准化课程服务,包括学情沟通,答疑解惑等;负责用户社群的运营和管理,最终达成促\\n转目的;负责课前课后课件的制作以及现场直播 。第一期促转率35%,第二期促转率40%,第三期促转率\\n46%,第四期转化率35%,均在小学年级处于领先地位,为公司带来88.6464w的利润。\\n横渠书院办事处  产品运营实习生\\n2018.7-2018.9\\n负责书院浏览手册的内容挖掘,策划新亮点;结合真实历史,策划线上线下活动,推广关学文化,参与\\n人数超200+人次;整理世界哲学大会和中国哲学大会音频资料约20000+字。\\n东岭集团股份有限公司  东岭璞玉校园记者\\n2018.10-2018.12\\n主导东岭集团HR微信公众号运营,带领三人团队,以校园招聘、就业形势为热点进行文案写作,实习期\\n间与团队共发表12篇文章,共获得阅读量约20,000次,其中以校园为主题的最美辅导员活动以获得\\n5000+点击量而成为当月第一。\\n北京爱益思咨询有限公司  线上运营实习生  2017.7-2017.9\\n添加竞品群,求职群等,进行竞品分析和新公众号粉丝增长。培训活动策划框架,运营的MVP:用最\\n小的成本来完成产品雏形并进行市场检验;个人裂变等等,成功引流1000+的新用户。\\n上海智遇信息科技有限公司  新媒体运营实习生  2016.7-2016.10\\n负责公司校园社群的拉新、留存、促转等工作,共拉新用户100+人,组织过五次社群活动,用户参与度\\n高达80%;策划线上活动,编写活动文案,三个月内,活动总曝光量1000+人。\\n项目经历\\n网易青媒新媒体训练营  优秀学员\\n2019.10-2019.11\\n学习内容创作,运营营销,数据分析,执行策划等相关课程,产出20+网易新闻,其中有多篇新闻\\n浏览量破万,并多次被点赞和转发,最终获得优秀学员资质。\\n2017.1-2017.2  北京大学软件与微电子学院创新创业培训班  项目核心成员\\n与六人团队共同学习金融工程、BP、Leadership培养等课程,合作策划商务模拟运营的草案。参与\\n最终Presentation制作,作为Timer的角色成功把控演讲节奏和时间,获得当次商务策划及运营项\\n目奖项:BusinessPlanRunnerUpBestOveral。\\n2016.4-2017.6  国家创业创新项目(行政管理本科毕业生就业状况及对策研究)  组长\\n在经费有限的情况下,主导项目申报,以访问的方式收集真实资料,并且采用 EviewsARIMA模型对\\n整体毕业生就业情况进行长期的分析及预测,最终本创新项目取得校级重点资质。\\n技能证书\\n文案设计 互联网作图工具:创可贴,PPT,PS,美图秀秀,草料二维码生成器\\n用户心理 数据收集分析工具:腾讯问卷,问卷星,金数据,麦客表单\\n运营工具 编辑排版工具:135编辑器 微信社群管理工具:wetool\\n视频编辑工具:PR,优酷iDo\\n语言 英语(流利,习惯原文阅读,CET6),雅思总分6.5(口语7.5)\\n证书 国家创业创新项目结项证书,网易青媒优秀学员,北大创新创业培训班结项证书\\n普通话一级乙等,机动车驾驶证c1\",\"qq\":\"2757319072\",\"living_address\":\"北京市朝阳区\",\"cert_objs\":[],\"languages\":\"英语\",\"cont_basic_info\":\"年龄:22岁  地址:北京市朝阳区\\n吴雨伦\\n电话:15771751259(微信同) 邮箱:2757319072@qq.com\",\"work_position\":\"产品运营实习生\",\"degree\":\"硕士\",\"education_objs\":[{\"end_date\":\"2022.01\",\"edu_college_type\":\"4\",\"edu_college\":\"昆士兰科技大学\",\"edu_degree\":\"硕士\",\"edu_major\":\"整合营销传播\",\"edu_degree_norm\":\"硕士研究生\",\"start_date\":\"2019.10\"},{\"end_date\":\"2019.06\",\"edu_college_type\":\"2\",\"edu_college\":\"陕西师范大学\",\"edu_degree\":\"本科\",\"edu_major\":\"哲学\",\"edu_college_rank\":\"72\",\"edu_degree_norm\":\"本科\",\"start_date\":\"2015.09\"}],\"cont_education\":\"陕西师范大学  本科 哲学\\n2015.9-2019.6\\n昆士兰科技大学  硕士 整合营销传播\\n2019.10-2022.1\",\"cont_proj_exp\":\"网易青媒新媒体训练营  优秀学员\\n2019.10-2019.11\\n学习内容创作,运营营销,数据分析,执行策划等相关课程,产出20+网易新闻,其中有多篇新闻\\n浏览量破万,并多次被点赞和转发,最终获得优秀学员资质。\\n2017.1-2017.2  北京大学软件与微电子学院创新创业培训班  项目核心成员\\n与六人团队共同学习金融工程、BP、Leadership培养等课程,合作策划商务模拟运营的草案。参与\\n最终Presentation制作,作为Timer的角色成功把控演讲节奏和时间,获得当次商务策划及运营项\\n目奖项:BusinessPlanRunnerUpBestOveral。\\n2016.4-2017.6  国家创业创新项目(行政管理本科毕业生就业状况及对策研究)  组长\\n在经费有限的情况下,主导项目申报,以访问的方式收集真实资料,并且采用 EviewsARIMA模型对\\n整体毕业生就业情况进行长期的分析及预测,最终本创新项目取得校级重点资质。\",\"resume_integrity\":\"68\",\"skills_objs\":[{\"skills_name\":\"文案\"},{\"skills_name\":\"PPT\"},{\"skills_name\":\"PS\"},{\"skills_name\":\"编辑\"},{\"skills_name\":\"视频编辑\"},{\"skills_name\":\"PR\"},{\"skills_name\":\"英语\"},{\"skills_name\":\"CET6\"},{\"skills_name\":\"普通话一级乙等\"},{\"skills_name\":\"机动车驾驶证\"},{\"skills_name\":\"c1\"},{\"skills_name\":\"产品运营\"},{\"skills_name\":\"策划\"},{\"skills_name\":\"微信公众号运营\"},{\"skills_name\":\"文案写作\"},{\"skills_name\":\"竞品分析\"},{\"skills_name\":\"活动策划\"},{\"skills_name\":\"新媒体运营\"},{\"skills_name\":\"新媒体\"},{\"skills_name\":\"数据分析\"},{\"skills_name\":\"商务\"},{\"skills_name\":\"演讲\"},{\"skills_name\":\"行政管理\"}],\"phone\":\"15771751259\",\"name\":\"吴雨伦\",\"grad_time\":\"2022.01\",\"age\":\"22\"}";
        JSONObject result = JSONObject.parseObject(json);
        resumeSDK.getResume(result);
    }

    @Test
    public void testBuildIndex() {
//        resumeRepositoryService.buildIndex();
//        Thread.currentThread().join();
        AttachmentParser attachmentParser = new AttachmentParser();
        attachmentParser.setEmail("aaaa");
        attachmentParser.setAttachContent("aaa");
        int result = attachmentParserMapper.insertSelective(attachmentParser);
        System.out.println(result);
        System.out.println(attachmentParser.getId());
    }
}
