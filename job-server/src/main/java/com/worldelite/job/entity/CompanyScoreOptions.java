package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@Slf4j
@EqualsAndHashCode(callSuper=false)
public class CompanyScoreOptions extends CompanyScore {
    private Long[] scoreIds;
    private Integer[] scores; //评分列表
    private String[] fromNames; //发帖人名
    private String[] companyNames; //公司名
    private String keyword; //关键词
    private Date beginTime; //开始日期
    private Date endTime; //结束日期

    public String getScoreIds(){
        if(scoreIds==null) return null;
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<scoreIds.length;i++){
            if(i==scoreIds.length-1){
                buffer.append(scoreIds[i]);
                break;
            }
            buffer.append(scoreIds[i]+",");
        }
        return buffer.toString();
    }

    public String getFromNames(){
        if(fromNames==null) return null;
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<fromNames.length;i++){
            if(i==fromNames.length-1){
                buffer.append(fromNames[i]);
                break;
            }
            buffer.append(fromNames[i]+"|");
        }
        return buffer.toString();
    }

    public String getCompanyNames(){
        if(companyNames==null) return null;
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<companyNames.length;i++){
            if(i==companyNames.length-1){
                buffer.append(companyNames[i]);
                break;
            }
            buffer.append(companyNames[i]+"|");
        }
        return buffer.toString();
    }

    public String getScores(){
        if(scores==null) return null;
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<scores.length;i++){
            if(i==scores.length-1){
                buffer.append(scores[i]);
                break;
            }
            buffer.append(scores[i]+",");
        }
        return buffer.toString();
    }
}