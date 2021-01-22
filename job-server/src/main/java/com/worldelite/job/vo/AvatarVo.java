package com.worldelite.job.vo;

import com.worldelite.job.entity.Avatar;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author Xiang Chao
 **/
@Data
public class AvatarVo implements VoConvertable<AvatarVo, Avatar> {

    private Integer id;

    /**
     * 头像url链接
     */
    private String avatarUrl;

    /**
     * 头像类型
     */
    private Byte type;

    /**
     * 删除标记, 0:未删除;1:已删除
     */
    private Byte delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public AvatarVo asVo(Avatar avatar) {
        if (avatar == null) return null;
        BeanUtils.copyProperties(avatar, this);
        return this;
    }
}
