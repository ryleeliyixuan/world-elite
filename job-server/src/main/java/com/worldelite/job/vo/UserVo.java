package com.worldelite.job.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.worldelite.job.entity.User;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

import java.util.Date;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserVo implements VoConvertable<UserVo, User>{

    private Long id; //用于后端
    private String userId; //字符串类型，用于前端
    private String name;
    private String avatar;
    private Byte gender;
    private Byte type;
    private Byte status;
    private String token;
    private String phoneCode;
    private String phone;
    private String email;
    @JSONField(format = "yyyy-MM-dd")
    private Date createTime;
    private Byte subscribeFlag;

    @Override
    public UserVo asVo(User user) {
        setId(user.getId());
        setUserId(String.valueOf(user.getId()));
        setName(user.getName());
        setEmail(user.getEmail());
        setPhoneCode(user.getPhoneCode());
        if(user.getPhone() != null && user.getPhone() != 0){
            setPhone(String.valueOf(user.getPhone()));
        }
        setAvatar(AppUtils.absOssUrl(user.getAvatar()));
        setType(user.getType());
        setGender(user.getGender());
        setStatus(user.getStatus());
        setCreateTime(user.getCreateTime());
        setSubscribeFlag(user.getSubscribeFlag());
        return this;
    }
}
