package com.worldelite.job.service;

import com.github.pagehelper.Page;
import com.worldelite.job.constants.*;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.AdditionIndustryMapper;
import com.worldelite.job.mapper.JobApplyMapper;
import com.worldelite.job.mapper.JobMapper;
import com.worldelite.job.mq.JobMessage;
import com.worldelite.job.mq.MessageTaskHandler;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import net.sf.cglib.transform.impl.AddInitTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户自定义行业领域关键词
 * @author 邓集阶
 */
@Service
public class AdditionIndustryService extends BaseService {

    @Autowired
    private AdditionIndustryMapper additionIndustryMapper;

    /**
     * 通过ID查询关键词
     * @param id 行业领域关键词ID
     * @return
     */
    public AdditionIndustry findById(Integer id){
        return additionIndustryMapper.selectByPrimaryKey(id);
    }

    /**
     * 通过关键词名和创建者ID查询关键词列表
     * @param creatorId 创建者ID
     * @param name 关键词名称
     * @return
     */
    public String[] listByName(Long creatorId,String name){
        List<AdditionIndustry> additionIndustryList = listByCreatorIdAndName(creatorId,name);
        String[] names = new String[additionIndustryList.size()];
        for(int i=0;i<additionIndustryList.size();i++){
            names[i] = additionIndustryList.get(i).getName();
        }
        return names;
    }

    /**
     * 根据关键词创建者ID查询关键词列表
     * @param creatorId 创建者ID
     * @return
     */
    public List<JobIndustryVo> listByCreatorId(Long creatorId){
        List<AdditionIndustry> additionIndustryList = listByCreatorIdAndName(creatorId,null);
        List<JobIndustryVo> jobIndustryVoList = new ArrayList<>();
        for(AdditionIndustry additionIndustryTemp : additionIndustryList){
            jobIndustryVoList.add(toJobIndustryVo(additionIndustryTemp));
        }
        return jobIndustryVoList;
    }

    public List<AdditionIndustry> listByCreatorIdAndName(Long creatorId,String name){
        AdditionIndustry additionIndustry = new AdditionIndustry();
        additionIndustry.setName(name);
        additionIndustry.setCreatorId(creatorId);
        return additionIndustryMapper.selectByCreatorId(additionIndustry);
    }

    /**
     * 保存行业领域关键词
     * @param additionIndustry 行业领域关键词对象
     */
    public JobIndustryVo save(AdditionIndustry additionIndustry){
        additionIndustryMapper.insertSelective(additionIndustry);
        return toJobIndustryVo(additionIndustry);
    }

    /**
     * 删除行业领域关键词
     * @param id 行业领域关键词ID
     */
    public void delete(Integer id){
        additionIndustryMapper.deleteByPrimaryKey(id);
    }

    private JobIndustryVo toJobIndustryVo(AdditionIndustry additionIndustry){
        JobIndustryVo jobIndustryVo = new JobIndustryVo();
        jobIndustryVo.setId(additionIndustry.getId().longValue());
        jobIndustryVo.setName(additionIndustry.getName());
        jobIndustryVo.setCreateTime(additionIndustry.getCreateTime());
        return jobIndustryVo;
    }
}
