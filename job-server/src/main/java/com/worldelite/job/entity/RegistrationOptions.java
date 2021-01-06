package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动报名信息数据库查询类
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class RegistrationOptions extends Registration {

    private String keywords; //关键词
    private Integer[] statusList; //状态列表

    public String getStatusList(){
        if(statusList==null) return null;
        StringBuffer statusListStr = new StringBuffer();
        for(int i=0;i<statusList.length;i++){
            statusListStr.append(statusList[i]);
            if(i<statusList.length-1){
                statusListStr.append(",");
            }
        }
        return statusListStr.toString();
    }

}