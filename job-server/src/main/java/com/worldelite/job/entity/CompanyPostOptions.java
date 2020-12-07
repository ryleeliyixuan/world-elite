package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
public class CompanyPostOptions extends CompanyPost {
    private Long[] postIds;
    private String[] fromNames; //发帖人名
    private String[] companyNames; //公司名
    private String keyword; //关键词
    private Date beginTime; //开始日期
    private Date endTime; //结束日期

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

    public String getPostIds(){
        if(postIds==null) return null;
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<postIds.length;i++){
            if(i==postIds.length-1){
                buffer.append(postIds[i]);
                break;
            }
            buffer.append(postIds[i]+",");
        }
        return buffer.toString();
    }
}