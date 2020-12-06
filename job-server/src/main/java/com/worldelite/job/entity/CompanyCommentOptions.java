package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
public class CompanyCommentOptions extends CompanyComment {
    private Long[] commentIds;
    private String title; //帖子标题
    private String[] toNames;//被评论人名
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
    public String getToNames(){
        if(toNames==null) return null;
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<toNames.length;i++){
            if(i==toNames.length-1){
                buffer.append(toNames[i]);
                break;
            }
            buffer.append(toNames[i]+"|");
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
    public String getCommentIds(){
        if(commentIds==null) return null;
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<commentIds.length;i++){
            if(i==commentIds.length-1){
                buffer.append(commentIds[i]);
                break;
            }
            buffer.append(commentIds[i]+",");
        }
        return buffer.toString();
    }
}