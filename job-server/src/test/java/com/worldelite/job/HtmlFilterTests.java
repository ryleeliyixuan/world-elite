package com.worldelite.job;

import cn.hutool.http.HTMLFilter;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class HtmlFilterTests {

    @Test
    public void test_htmlFilter(){
        String htmlContent = HttpUtil.downloadString("https://www.baidu.com", StandardCharsets.UTF_8);
        System.out.println(filter(htmlContent));
    }

    private String filter(String htmlText){

        HashMap vAllowed = new HashMap<>();

        final ArrayList<String> no_atts = new ArrayList<>();
        vAllowed.put("b", no_atts);
        vAllowed.put("strong", no_atts);
        vAllowed.put("i", no_atts);
        vAllowed.put("em", no_atts);
        vAllowed.put("p", no_atts);
        vAllowed.put("div", no_atts);
        vAllowed.put("ul", no_atts);
        vAllowed.put("ol", no_atts);
        vAllowed.put("li", no_atts);

        String[] vSelfClosingTags = new String[]{"img"};
        String[] vNeedClosingTags = new String[]{"a", "b", "strong", "i", "em"};
        String[] vDisallowed = new String[]{"script","style"};
        String[] vAllowedProtocols = new String[]{"http", "mailto", "https"}; // no ftp.
        String[] vProtocolAtts = new String[]{"src", "href"};
        String[] vRemoveBlanks = new String[]{"a", "b", "strong", "i", "em"};
        String[] vAllowedEntities = new String[]{"amp", "gt", "lt", "quot"};


        Map<String, Object> confMap = new HashMap<>();
        confMap.put("vAllowed", vAllowed);
        confMap.put("vSelfClosingTags", vSelfClosingTags);
        confMap.put("vNeedClosingTags", vNeedClosingTags);
        confMap.put("vDisallowed", vDisallowed);
        confMap.put("vAllowedProtocols", vAllowedProtocols);
        confMap.put("vProtocolAtts", vProtocolAtts);
        confMap.put("vRemoveBlanks", vRemoveBlanks);
        confMap.put("vAllowedEntities", vAllowedEntities);

        Whitelist whitelist = new Whitelist();
        whitelist.addTags("div","p","ul","ol","li","b","strong","i","em");

        return Jsoup.clean(htmlText, whitelist);
    }
}
