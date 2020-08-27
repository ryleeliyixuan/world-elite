package com.worldelite.job.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ResumeUtils {

    /**
     * 返回文本中的邮箱
     * @param resume
     */
    public static String getEmail(String resume) {
        try {
            Pattern p = Pattern.compile("[a-zA-Z0-9_]{3,20}@[a-zA-Z0-9]{2,15}[.][a-zA-Z0-9]{1,6}");
            Matcher m = p.matcher(resume);
            while (m.find()) {
                return m.group();
            }
        } catch (Exception e) {
            log.error("提取邮箱时出现异常", e);
        }
        return null;
    }

    /**
     * 返回文本中的电话号码
     * @param resume
     */
    public static String getPhone(String resume) {
        List<Pattern> psInChina = new ArrayList<>();
        List<Pattern> psInUS = new ArrayList<>();
        psInChina.add(Pattern.compile("[1][3,4,5,7,8][0-9]{9}")); // ex: 13197155331
        psInChina.add(Pattern.compile("[1][3,4,5,7,8][0-9][-][0-9]{4}[-][0-9]{4}")); // ex: 131-9715-5331
        psInChina.add(Pattern.compile("[1][3,4,5,7,8][0-9]\\s[0-9]{4}\\s[0-9]{4}")); // ex: 131 9715 5331
        psInUS.add(Pattern.compile("[0-9]{10}")); // ex: 2134770213
        psInUS.add(Pattern.compile("[(][0-9]{3}[)][0-9]{7}")); // ex: (213)4770213
        psInUS.add(Pattern.compile("[(][0-9]{3}[)][0-9]{4}[-][0-9]{4}")); // ex: (213)477-0213
        psInUS.add(Pattern.compile("[0-9]{3}[-][0-9]{4}[-][0-9]{4}")); // ex: 213-477-0213
        try {
            for (Pattern p: psInChina) {
                Matcher m = p.matcher(resume);
                while (m.find()) {
                    return m.group();
                }
            }
            for (Pattern p: psInUS) {
                Matcher m = p.matcher(resume);
                while (m.find()) {
                    return m.group();
                }
            }
        } catch (Exception e) {
            log.error("提取电话号码时出现异常", e);
        }
        return null;
    }
}