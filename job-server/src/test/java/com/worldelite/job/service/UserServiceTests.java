package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.DictType;
import com.worldelite.job.constants.UserStatus;
import com.worldelite.job.entity.Country;
import com.worldelite.job.entity.Dict;
import com.worldelite.job.mapper.CountryMapper;
import com.worldelite.job.mapper.DictMapper;
import com.worldelite.job.vo.JobCategoryVo;
import com.worldelite.job.vo.UserVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class UserServiceTests {

    @Autowired
    private UserService userService;


    @Autowired
    private JobCategoryService jobCategoryService;

    String tagStringHtml = "<span class=\"content\">\n" +
            "                                <a href=\"/gongsi/_zzz_iy100001/\" class=\"\">电子商务</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100002/\" class=\"\">游戏</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100003/\" class=\"\">媒体</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100004/\" class=\"\">广告营销</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100005/\" class=\"\">数据服务</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100006/\" class=\"\">医疗健康</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100007/\" class=\"\">生活服务</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100008/\" class=\"\">O2O</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100009/\" class=\"\">旅游</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100010/\" class=\"\">分类信息</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100011/\" class=\"\">音乐/视频/阅读</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100012/\" class=\"\">在线教育</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100013/\" class=\"\">社交网络</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100014/\" class=\"\">人力资源服务</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100015/\" class=\"\">企业服务</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100016/\" class=\"\">信息安全</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100018/\" class=\"\">智能硬件</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100019/\" class=\"\">移动互联网</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100020/\" class=\"\">互联网</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100021/\" class=\"\">计算机软件</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100024/\" class=\"\">通信/网络设备</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100101/\" class=\"\">广告/公关/会展</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100206/\" class=\"\">互联网金融</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100502/\" class=\"\">物流/仓储</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100504/\" class=\"\">贸易/进出口</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100601/\" class=\"\">咨询</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100702/\" class=\"\">工程施工</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy100801/\" class=\"\">汽车生产</a>\n" +
            "                                <a href=\"/gongsi/_zzz_iy101304/\" class=\"\">其他行业</a>\n" +
            "                    </span>";

    @Autowired
    private DictMapper dictMapper;

    private int tagValue = 1;

    @Test
    public void test_getCategoryTree(){
        Document document = Jsoup.parse(tagStringHtml);
        document.getElementsByTag("a").forEach(element -> {
            String tag  = element.text();
            Dict dict = new Dict();
            dict.setName(tag);
            dict.setType(DictType.INDUSTRY.value);
            dict.setValue(String.valueOf(tagValue));
            dictMapper.insertSelective(dict);
            tagValue++;
        });
    }
}
