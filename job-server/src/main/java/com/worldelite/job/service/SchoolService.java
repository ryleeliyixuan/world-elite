package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.entity.School;
import com.worldelite.job.form.SchoolForm;
import com.worldelite.job.form.SchoolListForm;
import com.worldelite.job.mapper.SchoolMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.SchoolVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class SchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private CountryService countryService;

    /**
     * 获取学校列表
     *
     * @param listForm
     * @return
     */
    public PageResult<SchoolVo> getSchoolList(SchoolListForm listForm){
        School options = new School();
        BeanUtils.copyProperties(listForm, options);
        AppUtils.setPage(listForm);
        Page<School> schoolPage = (Page<School>) schoolMapper.selectAndList(options);
        PageResult<SchoolVo> pageResult = new PageResult<>(schoolPage);
        List<SchoolVo> schoolVoList = new ArrayList<>(schoolPage.size());
        for(School school: schoolPage){
            SchoolVo schoolVo = new SchoolVo().asVo(school);
            schoolVo.setCountry(countryService.getById(school.getCountryId()));
            schoolVoList.add(schoolVo);
        }
        pageResult.setList(schoolVoList);
        return pageResult;
    }

    /**
     * 保存学校
     *
     * @param schoolForm
     */
    public void saveSchool(SchoolForm schoolForm){
        School school = null;
        if(schoolForm.getId() != null){
            school = schoolMapper.selectByPrimaryKey(schoolForm.getId());
        }

        if(school == null){
            school = new School();
        }
        BeanUtils.copyProperties(schoolForm, school);

        if(school.getId() == null){
           schoolMapper.insertSelective(school);
        }else{
            schoolMapper.updateByPrimaryKeySelective(school);
        }
    }

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
