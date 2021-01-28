package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.ConfigCarousel;
import com.worldelite.job.entity.Recommend;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.ConfigCarouselForm;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.mapper.ConfigCarouselMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.ConfigCarouselVo;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页轮播图服务类
 */
@Slf4j
@Service
public class ConfigCarouselService extends BaseService{

    @Autowired
    private ConfigCarouselMapper configCarouselMapper;

    /**
     * 添加轮播图
     * @param configCarouselForm
     */
    public void add(ConfigCarouselForm configCarouselForm) {
        ConfigCarousel configCarousel = new ConfigCarousel();
        Integer maxPosition = configCarouselMapper.selectMaxPosition(configCarousel);
        BeanUtil.copyProperties(configCarouselForm, configCarousel);
        if(maxPosition == null){
            maxPosition = 0;
        }
        configCarousel.setPosition(maxPosition+1);
        configCarouselMapper.insertSelective(configCarousel);
    }

    /**
     *
     * @param pageForm
     * @return
     */
    public PageResult<ConfigCarouselVo> list(PageForm pageForm){
        AppUtils.setPage(pageForm);
        ConfigCarousel options = new ConfigCarousel();
        Page<ConfigCarousel> page = (Page<ConfigCarousel>) configCarouselMapper.selectAndList(options);
        PageResult<ConfigCarouselVo> pageResult = new PageResult<>(page);
        List<ConfigCarouselVo> list = new ArrayList<>();
        for(ConfigCarousel configCarousel:page){
            ConfigCarouselVo configCarouselVo = new ConfigCarouselVo().asVo(configCarousel);
            list.add(configCarouselVo);
        }
        pageResult.setList(list);
        return pageResult;
    }

    /**
     * 删除轮播图
     * @param id
     */
    public void delete(Integer id){
        configCarouselMapper.deleteByPrimaryKey(id);
    }

    /**
     * 上升
     * @param id
     */
    public void moveUp(Integer id){
        ConfigCarousel configCarousel1 = configCarouselMapper.selectByPrimaryKey(id);
        if(configCarousel1 == null){
            throw new ServiceException(ApiCode.INVALID_PARAM);
        }
        ConfigCarousel options = new ConfigCarousel();
        options.setPosition(configCarousel1.getPosition());
        ConfigCarousel configCarousel2 = configCarouselMapper.selectPrePosition(options);
        if(configCarousel2 != null){
            swapPosition(configCarousel1,configCarousel2);
        }
    }

    /**
     * 下降
     * @param id
     */
    public void moveDown(Integer id){
        ConfigCarousel configCarousel1 = configCarouselMapper.selectByPrimaryKey(id);
        if(configCarousel1 == null){
            throw new ServiceException(ApiCode.INVALID_PARAM);
        }
        ConfigCarousel options = new ConfigCarousel();
        options.setPosition(configCarousel1.getPosition());
        ConfigCarousel configCarousel2 = configCarouselMapper.selectNextPosition(options);
        if(configCarousel2 != null){
            swapPosition(configCarousel1,configCarousel2);
        }
    }

    /**
     * 交换两个轮播图位置
     * @param configCarousel1
     * @param configCarousel2
     */
    private void swapPosition(ConfigCarousel configCarousel1,ConfigCarousel configCarousel2){
        Integer tempPosition = configCarousel1.getPosition();
        configCarousel1.setPosition(configCarousel2.getPosition());
        configCarousel2.setPosition(tempPosition);
        configCarouselMapper.updateByPrimaryKeySelective(configCarousel1);
        configCarouselMapper.updateByPrimaryKeySelective(configCarousel2);
    }
}
