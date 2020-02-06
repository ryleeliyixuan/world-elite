package com.worldelite.job.vo;

import com.worldelite.job.entity.User;
import com.worldelite.job.util.AppUtils;
import lombok.Data;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
public class UserVo implements VoConvertable<UserVo, User>{

    private Long id;
    private String name;
    private String avatar;
    private Byte type;
    private Byte status;
    private String token;
    private String phoneCode;
    private String phone;
    private String email;

    @Override
    public UserVo asVo(User user) {
        setId(user.getId());
        setName(user.getName());
        setAvatar(AppUtils.absOssUrl(user.getAvatar()));
        setType(user.getType());
        setStatus(user.getStatus());
        return this;
    }
}
