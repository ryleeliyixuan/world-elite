package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.VerificationStatus;
import com.worldelite.job.entity.RealNameAuth;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.RealNameAuthForm;
import com.worldelite.job.mapper.RealNameAuthMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.RealNameAuthVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 实名认证服务类
 *
 * @author Xiang Chao
 **/
@Slf4j
@Service
@AllArgsConstructor
public class RealNameAuthService extends BaseService {
    private final RealNameAuthMapper realNameAuthMapper;

    public PageResult<RealNameAuthVo> getRealNameAuthList(RealNameAuthForm realNameAuthForm) {
        RealNameAuth options = new RealNameAuth();
        BeanUtil.copyProperties(realNameAuthForm, options);

        AppUtils.setPage(realNameAuthForm);
        Page<RealNameAuth> realNameAuthPage = (Page<RealNameAuth>) realNameAuthMapper.selectAndList(options);
        PageResult<RealNameAuthVo> pageResult = new PageResult<>(realNameAuthPage);
        pageResult.setList(AppUtils.asVoList(realNameAuthPage, RealNameAuthVo.class));
        return pageResult;
    }

    public RealNameAuthVo getRealNameAuth(Long userId) {
        final RealNameAuth realNameAuth = realNameAuthMapper.selectByPrimaryKey(userId);
        if (realNameAuth == null) return null;

        return new RealNameAuthVo().asVo(realNameAuth);
    }

    public Boolean addRealNameAuth(RealNameAuthForm realNameAuthForm) {
        RealNameAuth realNameAuth = new RealNameAuth();
        BeanUtil.copyProperties(realNameAuthForm, realNameAuth);

        if (realNameAuthForm.getUserId() == null)
            realNameAuth.setUserId(curUser().getId());

        realNameAuth.setStatus(VerificationStatus.REVIEWING.value);
        return realNameAuthMapper.insertSelective(realNameAuth) == 1;
    }

    public Boolean updateRealNameAuth(RealNameAuthForm realNameAuthForm) {
        RealNameAuth realNameAuth = new RealNameAuth();
        BeanUtil.copyProperties(realNameAuthForm, realNameAuth);

        if (realNameAuthForm.getUserId() == null)
            realNameAuth.setUserId(curUser().getId());

        return realNameAuthMapper.updateByPrimaryKeySelective(realNameAuth) == 1;
    }

    public Boolean delRealNameAuth(Long userId) {
        return realNameAuthMapper.deleteByPrimaryKey(userId) == 1;
    }

    public Boolean realNameAuthPass(Long userId) {
        if (userId == null)
            throw new ServiceException(message("api.error.invalid.param"));

        RealNameAuth realNameAuth = new RealNameAuth();
        realNameAuth.setUserId(userId);
        realNameAuth.setStatus(VerificationStatus.PASS.value);
        return realNameAuthMapper.updateByPrimaryKeySelective(realNameAuth) == 1;
    }

    public Boolean realNameAuthFailure(Long userId, String reason) {
        if (userId == null)
            throw new ServiceException(message("api.error.invalid.param"));

        RealNameAuth realNameAuth = new RealNameAuth();
        realNameAuth.setUserId(userId);
        realNameAuth.setStatus(VerificationStatus.PASS.value);
        realNameAuth.setReason(reason);
        return realNameAuthMapper.updateByPrimaryKeySelective(realNameAuth) == 1;
    }
}
