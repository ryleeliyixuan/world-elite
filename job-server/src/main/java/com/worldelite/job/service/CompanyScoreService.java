package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.CommentType;
import com.worldelite.job.entity.CompanyComment;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.CompanyScoreMapper;
import com.worldelite.job.mapper.CompanyScoreMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyCommentVo;
import com.worldelite.job.vo.CompanyScoreVo;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.UserApplicantVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业评分服务类
 */
@Slf4j
@Service
public class CompanyScoreService extends BaseService{

    @Autowired
    private CompanyScoreMapper companyScoreMapper;

    @Autowired
    private CompanyLikeService companyLikeService;

    @Autowired
    private CompanyReportService companyReportService;

    @Autowired
    private CompanyCommentService companyCommentService;

    @Autowired
    private UserApplicantService userApplicantService;

    /**
     * 保存评分
     * @param companyScoreForm 评分表单
     * @return 评分
     */
    @Transactional
    public CompanyScore save(CompanyScoreForm companyScoreForm){
        //如果不存在对应评分数据，新建空评分
        CompanyScore companyScore = null;
        if(companyScoreForm.getId() != null){
            companyScore = getById(companyScoreForm.getId());
        }
        if(companyScore == null){
            companyScore = newScore(companyScoreForm.getCompanyId());
        }
        //保存基本数据
        BeanUtil.copyProperties(companyScoreForm,companyScore,"id");
        //更新数据
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
        //计算热度
        hotCalc(companyScore);
        return companyScore;
    }

    /**
     * 删除评分
     * @param scoreId
     */
    @Transactional
    public void delete(Long scoreId){
        //删除评分
        companyScoreMapper.deleteByPrimaryKey(scoreId);
        //删除点赞
        companyLikeService.deleteByOwnerId(scoreId);
        //删除举报
        companyReportService.deleteByOwnerId(scoreId);
        //删除评论
        companyCommentService.deleteByOwnerId(scoreId);
    }

    /**
     * 点赞
     * @param scoreId
     */
    @Transactional
    public CompanyScoreVo like(Long scoreId){
        //评分存在才能点赞
        CompanyScore companyScore = getById(scoreId);
        //点赞
        companyLikeService.changLike(scoreId);
        //更新点赞数
        hotCalc(companyScore);
        CompanyScoreVo companyScoreVo = new CompanyScoreVo();
        companyScoreVo.setLike(companyLikeService.hasLike(scoreId));
        companyScoreVo.setLikes(companyScore.getLikes());
        return companyScoreVo;
    }

    /**
     * 举报
     * @param companyReportForm
     */
    @Transactional
    public CompanyScoreVo report(CompanyReportForm companyReportForm){
        CompanyScore companyScore = getById(companyReportForm.getOwnerId());
        companyReportService.save(companyReportForm);
        hotCalc(companyScore);
        CompanyScoreVo companyScoreVo = new CompanyScoreVo();
        companyScoreVo.setReport(companyReportService.getReportVo(companyReportForm.getOwnerId()));
        companyScoreVo.setReports(companyScore.getReports());
        return companyScoreVo;
    }

    /**
     * 评论
     * @param companyCommentForm
     */
    @Transactional
    public CompanyCommentVo comment(CompanyCommentForm companyCommentForm){
        CompanyScore companyScore = getById(companyCommentForm.getOwnerId());
        companyCommentForm.setType(CommentType.SCORE.value);
        CompanyComment companyComment = companyCommentService.save(companyCommentForm);
        hotCalc(companyScore);
        return companyCommentService.getCommentVo(companyComment);
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
     * 评分视图对象
     * @param listForm
     * @return
     */
    public PageResult<CompanyScoreVo> listVo(CompanyScoreListForm listForm){
        PageResult<CompanyScore> companyScorePageResult = list(listForm);
        List<CompanyScore> postList = companyScorePageResult.getList();
        PageResult<CompanyScoreVo> pageResult = new PageResult<>();
        BeanUtil.copyProperties(companyScorePageResult,pageResult,"list");
        List<CompanyScoreVo> postVoList = new ArrayList<>(postList.size());
        for(CompanyScore companyScore:postList){
            postVoList.add(getScoreVo(companyScore));
        }
        pageResult.setList(postVoList);
        return pageResult;
    }

    public CompanyScoreVo getScoreVo(CompanyScore companyScore){
        CompanyScoreVo companyScoreVo = new CompanyScoreVo().asVo(companyScore);
        if(companyScore.getAnonymous()==0){
            companyScoreVo.setFromUser(userApplicantService.getUserInfo(companyScore.getFromId()));
        }else{
            UserApplicantVo userVo = new UserApplicantVo();
            userVo.setName("匿名用户");
            userVo.setAvatar("");
            companyScoreVo.setFromUser(userVo);
        }
        companyScoreVo.setLike(companyLikeService.hasLike(companyScore.getId()));
        companyScoreVo.setReport(companyReportService.getReportVo(companyScore.getId()));
        return companyScoreVo;
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
    private CompanyScore newScore(Long companyId){
        CompanyScore companyScore = new CompanyScore();
        companyScore.setId(AppUtils.nextId());
        companyScore.setFromId(curUser().getId());
        companyScore.setCompanyId(companyId);
        companyScore.setAnonymous((byte) 0);
        companyScore.setLikes(0);
        companyScore.setComments(0);
        companyScore.setReports(0);
        companyScore.setHots(0);
        companyScoreMapper.insertSelective(companyScore);
        return companyScore;
    }

    private void hotCalc(CompanyScore companyScore){
        int hot = 0;
        Long scoreId = companyScore.getId();
        Integer likeCount = companyLikeService.getLikeCount(scoreId);
        Integer reportCount = companyReportService.getReportCount(scoreId);
        Integer commentCount = companyCommentService.getCommentCount(scoreId);
        hot += likeCount+reportCount+commentCount;
        companyScore.setLikes(likeCount);
        companyScore.setReports(reportCount);
        companyScore.setComments(commentCount);
        companyScore.setHots(hot);
        companyScoreMapper.updateByPrimaryKeySelective(companyScore);
    }

    public void hotCalc(Long scoreId){
        CompanyScore companyScore = getById(scoreId);
        hotCalc(companyScore);
    }
}
