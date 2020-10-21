package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyScoreForm;
import com.worldelite.job.form.CompanyScoreListForm;
import com.worldelite.job.mapper.CompanyScoreMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业评分服务类
 */
@Slf4j
@Service
public class CompanyScoreService extends BaseService{

    @Autowired
    private CompanyScoreMapper companyScoreMapper;

    /**
     * 保存评分
     * @param companyScoreForm 评分表单
     * @return 评分
     */
    public CompanyScore save(CompanyScoreForm companyScoreForm){
        //如果不存在对应评分数据，新建空评分
        CompanyScore companyScore = null;
        if(companyScoreForm.getId() != null){
            companyScore = getById(companyScoreForm.getId());
        }
        if(companyScore == null){
            companyScore = newPost(companyScoreForm.getCompanyId());
        }
        //保存基本数据
        BeanUtil.copyProperties(companyScoreForm,companyScore,"id");
        //计算热度
        companyScore.setHots(hotCalc(companyScore));
        //更新数据
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
        return companyScore;
    }

    /**
     * 获取评分列表
     * @param listForm 搜索表单
     * @return 评分列表
     */
    public PageResult<CompanyScore> list(CompanyScoreListForm listForm){
        CompanyScore companyScore = new CompanyScore();
        BeanUtil.copyProperties(listForm,companyScore);
        AppUtils.setPage(listForm);
        Page<CompanyScore> companyScorePage = (Page<CompanyScore>) companyScoreMapper.selectAndList(companyScore);
        PageResult<CompanyScore> pageResult = new PageResult<>(companyScorePage);
        List<CompanyScore> companyScoreList = (List<CompanyScore>) companyScorePage;
        pageResult.setList(companyScoreList);
        return pageResult;
    }

    /**
     * 点赞
     * @param scoreId 评分ID
     * @return 评分
     */
    public CompanyScore likesAdd(Long scoreId){
        CompanyScore companyScore = getById(scoreId);
        Integer likes = companyScore.getLikes();
        if(likes==null){
            likes = 0;
        }else{
            likes++;
        }
        companyScore.setLikes(likes);
        hotCalc(companyScore);
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
        return companyScore;
    }

    /**
     * 取消点赞
     * @param scoreId 评分ID
     * @return 评分
     */
    public CompanyScore likesSub(Long scoreId){
        CompanyScore companyScore = getById(scoreId);
        Integer likes = companyScore.getLikes();
        if(likes==null || likes==0){
            likes = 0;
        }else{
            likes--;
        }
        companyScore.setLikes(likes);
        hotCalc(companyScore);
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
        return companyScore;
    }

    /**
     * 评论
     * @param scoreId 评分ID
     * @return 评分
     */
    public CompanyScore commentsAdd(Long scoreId){
        CompanyScore companyScore = getById(scoreId);
        Integer comments = companyScore.getComments();
        if(comments==null){
            comments = 0;
        }else{
            comments++;
        }
        companyScore.setComments(comments);
        hotCalc(companyScore);
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
        return companyScore;
    }

    /**
     * 取消评论
     * @param scoreId 评分ID
     * @return 评分
     */
    public CompanyScore commentsSub(Long scoreId){
        CompanyScore companyScore = getById(scoreId);
        Integer comments = companyScore.getComments();
        if(comments==null || comments==0){
            comments = 0;
        }else{
            comments--;
        }
        companyScore.setComments(comments);
        hotCalc(companyScore);
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
        return companyScore;
    }

    /**
     * 举报
     * 举报不能取消，只能修改举报理由
     * @param scoreId 评分ID
     * @return 评分
     */
    public CompanyScore reportsAdd(Long scoreId){
        CompanyScore companyScore = getById(scoreId);
        Integer reports = companyScore.getReports();
        if(reports==null){
            reports = 0;
        }else{
            reports++;
        }
        companyScore.setReports(reports);
        hotCalc(companyScore);
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
        return companyScore;
    }


    /**
     * 通过评分ID获取评分数据
     * @param scoreId 评分ID
     * @return 评分数据
     */
    public CompanyScore getById(Long scoreId){
        CompanyScore companyScore = companyScoreMapper.selectByPrimaryKey(scoreId);
        if(companyScore==null){
            throw new ServiceException(message("api.error.data.score"));
        }
        return companyScore;
    }

    /**
     * 新建评分
     * @param companyId 公司ID
     * @return 新评分
     */
    private CompanyScore newPost(Long companyId){
        CompanyScore companyScore = new CompanyScore();
        companyScore.setId(AppUtils.nextId());
        companyScore.setFromId(curUser().getId());
        companyScore.setCompanyId(companyId);
        companyScore.setScore(0);
        companyScore.setAnonymous((byte) 0);
        companyScore.setLikes(0);
        companyScore.setComments(0);
        companyScore.setReports(0);
        companyScore.setHots(0);
        companyScoreMapper.insertSelective(companyScore);
        return companyScore;
    }

    /**
     * 计算评分热度
     * @param companyScore 评分
     * @return 热度
     */
    private int hotCalc(CompanyScore companyScore){
        int hot = 0;
        if(companyScore.getHots() != null){
            hot = companyScore.getHots();
        }
        if(companyScore.getLikes() != null){
            hot += companyScore.getLikes();
        }
        if(companyScore.getComments() != null){
            hot += companyScore.getComments();
        }
        return hot;
    }

}
