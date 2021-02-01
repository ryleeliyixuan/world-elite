package com.worldelite.job.form;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class ActivityListForm extends PageForm {
    private Byte form; //活动形式, 线上0, 线下1
    private String cityIds; //城市ID
    private Long userId; //用户id|公司id
    private Long companyId;//公司id, 用于查询公司发布的全部活动. 当userId不为空的时候有效
    private Integer timeId; //时间id-搜索专用
    private String publisherType; //活动发布者类型.个人:1 企业:2 留学生专区:3 管理账户:100(保留,暂时没用到)
    private String status; //活动状态
    private String keyword; //搜索关键词
    private String sortField; //排序字段

    public String[] getCityIds() {
        return StringUtils.isNotBlank(cityIds) ? cityIds.split(",") : null;
    }

    public void setCityIds(String[] cityIds) {
        if (StringUtils.isNoneBlank(cityIds))
            this.cityIds = String.join(",", cityIds);
    }

    public String[] getStatus() {
        return StringUtils.isNotBlank(status) ? status.split(",") : null;
    }

    public void setStatus(String[] status) {
        if (StringUtils.isNoneBlank(status))
            this.status = String.join(",", status);
    }
}
