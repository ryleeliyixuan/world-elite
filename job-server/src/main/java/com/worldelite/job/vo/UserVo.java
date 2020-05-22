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
