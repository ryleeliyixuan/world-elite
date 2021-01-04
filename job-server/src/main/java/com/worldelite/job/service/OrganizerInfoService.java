package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.OrganizerInfo;
import com.worldelite.job.form.OrganizerInfoForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.OrganizerInfoMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.OrganizerInfoVo;
import com.worldelite.job.vo.PageResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class OrganizerInfoService extends BaseService {
    private final OrganizerInfoMapper organizerInfoMapper;

    public PageResult<OrganizerInfoVo> getOrganizerInfoList(OrganizerInfoForm organizerInfoForm, PageForm pageForm) {
        OrganizerInfo options = new OrganizerInfo();
        BeanUtil.copyProperties(organizerInfoForm, options);
        AppUtils.setPage(pageForm);
        Page<OrganizerInfo> organizerInfoPage = (Page<OrganizerInfo>) organizerInfoMapper.selectAndList(options);
        PageResult<OrganizerInfoVo> pageResult = new PageResult<>(organizerInfoPage);
        pageResult.setList(AppUtils.asVoList(organizerInfoPage, OrganizerInfoVo.class));
        return pageResult;
    }

    public OrganizerInfoVo getOrganizerInfo(Integer id) {
        final OrganizerInfo OrganizerInfo = organizerInfoMapper.selectByPrimaryKey(id);
        if (OrganizerInfo == null) return null;

        return new OrganizerInfoVo().asVo(OrganizerInfo);
    }

    public Boolean addOrganizerInfo(OrganizerInfoForm organizerInfoForm) {
        OrganizerInfo organizerInfo = new OrganizerInfo();
        BeanUtil.copyProperties(organizerInfoForm, organizerInfo);

        return organizerInfoMapper.insertSelective(organizerInfo) == 1;
    }

    public Boolean updateOrganizerInfo(OrganizerInfoForm organizerInfoForm) {
        OrganizerInfo organizerInfo = new OrganizerInfo();
        BeanUtil.copyProperties(organizerInfoForm, organizerInfo);

        return organizerInfoMapper.updateByPrimaryKeySelective(organizerInfo) == 1;
    }

    public Boolean delOrganizerInfo(Integer id) {
        return organizerInfoMapper.deleteByPrimaryKey(id) == 1;
    }
}
