package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.Avatar;
import com.worldelite.job.form.AvatarForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.AvatarMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.AvatarVo;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Xiang Chao
 **/
@Service
@AllArgsConstructor
public class AvatarService {
    private final AvatarMapper avatarMapper;

    public PageResult<AvatarVo> getAvatarList(AvatarForm avatarForm, PageForm pageForm) {
        Avatar avatar = new Avatar();
        BeanUtil.copyProperties(avatarForm, avatar);

        AppUtils.setPage(pageForm);
        Page<Avatar> avatars = (Page<Avatar>) avatarMapper.selectAndList(avatar);
        PageResult<AvatarVo> pageResult = new PageResult<>(avatars);
        pageResult.setList(AppUtils.asVoList(avatars, AvatarVo.class));
        return pageResult;
    }

    public void addAvatar(AvatarForm form) {
        final int insert = avatarMapper.insertSelective(formToEntity(form));
        if (insert != 1)
            throw new RuntimeException();
    }

    public void updateAvatar(AvatarForm form) {
        final int insert = avatarMapper.updateByPrimaryKeySelective(formToEntity(form));
        if (insert != 1)
            throw new RuntimeException();
    }

    public void delAvatar(String id) {
        final int insert = avatarMapper.deleteByPrimaryKey(Integer.valueOf(id));
        if (insert != 1)
            throw new RuntimeException();
    }

    private Avatar formToEntity(AvatarForm form) {
        Avatar avatar = new Avatar();

        avatar.setId(StringUtils.isNotBlank(form.getId()) ? Integer.parseInt(form.getId()) : null);

        if (StringUtils.isNotBlank(form.getAvatarUrl()))
            avatar.setAvatarUrl(form.getAvatarUrl());

        if (form.getType() != null)
            avatar.setType(form.getType());

        avatar.setCreateTime(new Date());
        return avatar;
    }
}
