package com.worldelite.job.service;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.School;
import com.worldelite.job.mapper.SchoolMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.SchoolVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class SchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

    /**
     * 根据名称模糊搜索
     *
     * @param name
     * @return
     */
    public List<SchoolVo> searchByName(String name){
        PageHelper.startPage(1, 10);
        List<School> schoolList = schoolMapper.selectLikeName(name);
        return AppUtils.asVoList(schoolList, SchoolVo.class);
    }

}
