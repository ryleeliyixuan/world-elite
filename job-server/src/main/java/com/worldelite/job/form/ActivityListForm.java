package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ActivityListForm extends PageForm {
    /**
     * 活动形式, 线上0, 线下1
     */
    @NotNull
    private Integer activityForm; //活动形式, 线上0, 线下1
    private String cityIds; //城市ID,线上国内=999992; 线上国外=999993
    private Long userId; //用户id|公司id
    private Integer timeId; //时间id-搜索专用
    private String publisherType; //活动发布者类型.个人:1 企业:2 留学生专区:3 管理账户:100(保留,暂时没用到)
    private Byte status; //活动状态
    private String keyword; //搜索关键词
    private String sortField; //排序字段

    public String[] getCityIds() {
        return StringUtils.isNotBlank(cityIds) ? cityIds.split(",") : null;
    }

    public void setCityIds(String[] cityIds) {
        if (StringUtils.isNoneBlank(cityIds))
            this.cityIds = String.join(",", cityIds);
    }
}
