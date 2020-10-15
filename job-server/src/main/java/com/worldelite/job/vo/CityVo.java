package com.worldelite.job.vo;

import cn.hutool.core.bean.BeanUtil;
import com.worldelite.job.entity.City;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 城市视图对象
 */
@Data
public class CityVo  implements VoConvertable<CityVo, City>{

    private Integer id; //城市ID
    private Integer parentId; //父级ID
    private String name; //名称
    private String code; //行政区划代码
    private String phoneCode; //固定电话区号
    private String zipCode; //邮编
    private String shortName; //简称
    private String pinyin; //拼音
    private String py; //简拼
    private String firstChar; //首字母
    private String english; //英文
    private String lng; //经度
    private String lat; //纬度
    private Byte type; //城市类型
    private Date createTime; //创建时间
    private Date updateTime; //更新时间

    @Ignore
    private List<CityVo> children;

    @Override
    public CityVo asVo(City city) {
        BeanUtil.copyProperties(city,this);
        return this;
    }
}