package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.Admin;
import com.worldelite.job.entity.User;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

import java.util.Date;

/**
 * @author 邓集阶
 */
@Data
public class AdminVo implements VoConvertable<AdminVo, Admin>{

    private Long id; //用于后端
    private String userId; //字符串ID，用于前端
    private String name; //名字
    private String avatar; //头像
    private Byte gender; //性别
    private Byte type; //用户类型
    private Byte status; //状态
    private String token; //登录凭证
    private String phoneCode; //电话区号
    private String phone; //电话号码
    private String email; //邮箱
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime; //创建时间
    private Byte subscribeFlag; //订阅标志

    @Override
    public AdminVo asVo(Admin admin) {
        setId(admin.getId());
        setUserId(String.valueOf(admin.getId()));
        setName(admin.getName());
        setEmail(admin.getEmail());
        setPhoneCode(admin.getPhoneCode());
        if(admin.getPhone() != null && admin.getPhone() != 0){
            setPhone(String.valueOf(admin.getPhone()));
        }
        setAvatar(AppUtils.absOssUrl(admin.getAvatar()));
        setType(admin.getType());
        setGender(admin.getGender());
        setStatus(admin.getStatus());
        setCreateTime(admin.getCreateTime());
        setSubscribeFlag(admin.getSubscribeFlag());
        return this;
    }
}
