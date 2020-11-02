package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.CommentType;
import com.worldelite.job.entity.CompanyComment;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyCommentForm;
import com.worldelite.job.form.CompanyCommentListForm;
import com.worldelite.job.form.CompanyReportForm;
import com.worldelite.job.mapper.CompanyCommentMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyCommentVo;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业评论服务类
 */
@Slf4j
@Service
public class CompanyCommentService extends BaseService{

    @Autowired
    private CompanyCommentMapper companyCommentMapper;

    @Autowired
    private CompanyPostService companyPostService;

    @Autowired
    private CompanyScoreService companyScoreService;

    @Autowired
    private CompanyLikeService companyLikeService;

    @Autowired
    private CompanyReportService companyReportService;

    @Autowired
    private UserApplicantService userApplicantService;

    /**
     * 保存评论
     * @param companyCommentForm 评论表单
     * @return 评论
     */
    @Transactional
    public CompanyComment save(CompanyCommentForm companyCommentForm){
        //如果不存在对应评论数据，新建空评论
        CompanyComment companyComment = null;
        if(companyCommentForm.getId() != null){
            companyComment = getById(companyCommentForm.getId());
        }
        if(companyComment == null){
            //添加评论
            companyComment = newPost();
            //保存基本数据
            BeanUtil.copyProperties(companyCommentForm,companyComment,"id");
            //设置被评论人
            setToIdAndOwnerIds(companyComment);
            //只需分两层，所以改变所属对象
            changeOwnerId(companyComment);
            //更新热度
            hotCalc(companyComment);
            //更新父级热度
            ownerHotCalc(companyComment);
        }else{
            //修改评论
            //保存基本数据
            BeanUtil.copyProperties(companyCommentForm,companyComment,"id");
            //更新热度
            hotCalc(companyComment);
        }
        return companyComment;
    }

    /**
     * 删除评论
     * @param commentId
     */
    @Transactional
    public void delete(Long commentId){
        //先复制一份要删除的数据
        CompanyComment comment = getById(commentId);
        //删除评论
        companyCommentMapper.deleteByPrimaryKey(commentId);
        //删除点赞数据
        companyLikeService.deleteByOwnerId(commentId);
        //删除举报数据
        companyReportService.deleteByOwnerId(commentId);
        //删除子评论
        deleteByOwnerId(commentId);
        //更新父级热度
        ownerHotCalc(comment);
    }

    /**
     * 点赞
     * @param commentId
     * @return
     */
    @Transactional
    public CompanyCommentVo like(Long commentId){
        CompanyComment comment = getById(commentId);
        companyLikeService.changLike(commentId);
        hotCalc(comment);
        CompanyCommentVo commentVo = new CompanyCommentVo();
        commentVo.setLikes(comment.getLikes());
        commentVo.setLike(companyLikeService.hasLike(commentId));
        return commentVo;
    }

    /**
     * 举报
     * @param companyReportForm
     * @return
     */
    @Transactional
    public CompanyCommentVo report(CompanyReportForm companyReportForm){
        CompanyComment comment = getById(companyReportForm.getOwnerId());
        companyReportService.save(companyReportForm);
        hotCalc(comment);
        CompanyCommentVo commentVo = new CompanyCommentVo();
        commentVo.setReports(comment.getReports());
        commentVo.setReport(companyReportService.getReportVo(companyReportForm.getOwnerId()));
        return commentVo;
    }

    /**
     * 评论
     * @param companyCommentForm
     * @return
     */
    @Transactional
    public CompanyCommentVo comment(CompanyCommentForm companyCommentForm){
        CompanyComment comment = getById(companyCommentForm.getOwnerId());
        companyCommentForm.setType(CommentType.COMMENT.value);
        CompanyComment companyComment = save(companyCommentForm);
        hotCalc(comment);
        ownerHotCalc(comment);
        return getCommentVo(companyComment);
    }

    /**
     * 查询
     * @param listForm
     * @return
     */
    public PageResult<CompanyCommentVo> listVo(CompanyCommentListForm listForm){
        CompanyComment companyComment = new CompanyComment();
        BeanUtil.copyProperties(listForm,companyComment);
        AppUtils.setPage(listForm);
        Page<CompanyComment> companyCommentPage = (Page<CompanyComment>) companyCommentMapper.selectAndList(companyComment);
        PageResult<CompanyCommentVo> pageResult = new PageResult<>(companyCommentPage);
        List<CompanyCommentVo> commentVoList = new ArrayList<>(companyCommentPage.size());
        for(CompanyComment comment:companyCommentPage){
            commentVoList.add(getCommentVo(comment));
        }
        pageResult.setList(commentVoList);
        return pageResult;
    }

    public Integer getCommentCount(Long ownerId){
        Integer commentCount = companyCommentMapper.countByOwnerId(ownerId);
        return commentCount!=null?commentCount:0;
    }

    /**
     * 获取评论列表
     * @param listForm 搜索表单
     * @return 评论列表
     */
    public PageResult<CompanyComment> list(CompanyCommentListForm listForm){
        CompanyComment companyComment = new CompanyComment();
        BeanUtil.copyProperties(listForm,companyComment);
        AppUtils.setPage(listForm);
        Page<CompanyComment> companyCommentPage = (Page<CompanyComment>) companyCommentMapper.selectAndList(companyComment);
        PageResult<CompanyComment> pageResult = new PageResult<>(companyCommentPage);
        pageResult.setList(companyCommentPage);
        return pageResult;
    }

    /**
     * 删除指定对象的所有评论
     * 不维护评论数，调用此方法后owner需要重新计算评论数
     * @param ownerId 所属对象ID
     */
    public void deleteByOwnerId(Long ownerId){
        companyCommentMapper.deleteByOwnerId(ownerId);
    }

    /**
     * 通过评论ID获取评论数据
     * @param commentId 评论ID
     * @return 评论数据
     */
    public CompanyComment getById(Long commentId){
        CompanyComment companyComment = companyCommentMapper.selectByPrimaryKey(commentId);
        if(companyComment==null){
            throw new ServiceException(message("api.error.data.comment"));
        }
        return companyComment;
    }

    public CompanyCommentVo getCommentVo(CompanyComment companyComment){
        CompanyCommentVo commentVo = new CompanyCommentVo().asVo(companyComment);
        commentVo.setFromUser(userApplicantService.getUserInfo(companyComment.getFromId()));
        commentVo.setToUser(userApplicantService.getUserInfo(companyComment.getToId()));
        //登录用户显示点赞和评论信息
        if(curUser() != null) {
            commentVo.setLike(companyLikeService.hasLike(companyComment.getId()));
            commentVo.setReport(companyReportService.getReportVo(companyComment.getId()));
        }
        return commentVo;
    }


    /**
     * 新建评论
     * @return 新评论
     */
    private CompanyComment newPost(){
        CompanyComment companyComment = new CompanyComment();
        companyComment.setId(AppUtils.nextId());
        companyComment.setFromId(curUser().getId());
        companyComment.setLikes(0);
        companyComment.setComments(0);
        companyComment.setReports(0);
        companyComment.setHots(0);
        companyComment.setOwnerIds("");
        companyCommentMapper.insertSelective(companyComment);
        return companyComment;
    }

    /**
     * 计算评论热度
     * @param companyComment 评论
     */
    private void hotCalc(CompanyComment companyComment){
        int hot = 0;
        Long commentId = companyComment.getId();
        Integer likeCount = companyLikeService.getLikeCount(commentId);
        Integer reportCount = companyReportService.getReportCount(commentId);
        Integer commentCount = getCommentCount(commentId);
        hot += likeCount+reportCount+commentCount;
        companyComment.setLikes(likeCount);
        companyComment.setReports(reportCount);
        companyComment.setComments(commentCount);
        companyComment.setHots(hot);
        companyCommentMapper.updateByPrimaryKeySelective(companyComment);
    }

    /**
     * 更新父级热度
     * @param companyComment
     */
    private void ownerHotCalc(CompanyComment companyComment){
        if(companyComment.getType()==null || companyComment.getOwnerId()==null){
            throw new ServiceException(message("api.error.data.comment"));
        }
        Long ownerId = companyComment.getOwnerId();
        switch (CommentType.valueOf(companyComment.getType())){
            case POST:
                companyPostService.hotCalc(ownerId);
                return;
            case SCORE:
                companyScoreService.hotCalc(ownerId);
                return;
            case COMMENT:
                CompanyComment comment = getById(ownerId);
                hotCalc(comment);
                ownerHotCalc(comment);
                return;
        }
    }

    /**
     * 添加被评论人和所属对象集
     * @param companyComment 评论
     */
    private void setToIdAndOwnerIds(CompanyComment companyComment){
        if(companyComment.getType()==null || companyComment.getOwnerId()==null){
            throw new ServiceException(message("api.error.data.comment"));
        }
        Long ownerId = companyComment.getOwnerId();
        switch (CommentType.valueOf(companyComment.getType())){
            case POST:
                CompanyPost post = companyPostService.getById(ownerId);
                companyComment.setToId(post.getFromId());
                companyComment.setOwnerIds(String.valueOf(ownerId));
                return;
            case SCORE:
                CompanyScore score = companyScoreService.getById(ownerId);
                companyComment.setToId(score.getFromId());
                companyComment.setOwnerIds(String.valueOf(ownerId));
                return;
            case COMMENT:
                CompanyComment comment = getById(ownerId);
                companyComment.setToId(comment.getFromId());
                companyComment.setOwnerIds(comment.getOwnerIds()+","+String.valueOf(ownerId));
                return;
            default:
                throw new ServiceException(message("api.error.data.comment"));
        }
    }

    /**
     * 获取实际所属ID
     * 这里是为了评论只分两层
     * 如果希望评论无限级分层
     * 则不需要调用此方法，直接使用ownerId即可
     * @param companyComment 本次评论
     */
    private void changeOwnerId(CompanyComment companyComment){
        if(companyComment.getType()==null || companyComment.getOwnerId()==null){
            throw new ServiceException(message("api.error.data.comment"));
        }
        Byte type = companyComment.getType();
        //对于评论的对象也是评论的，如果不是一级评论，则将本次评论的对象设为一级评论
        if(CommentType.valueOf(type)==CommentType.COMMENT){
            CompanyComment comment = getById(companyComment.getOwnerId());
            if(CommentType.valueOf(comment.getType())==CommentType.COMMENT){
                companyComment.setOwnerId(comment.getOwnerId());
                companyComment.setOwnerIds(comment.getOwnerIds());
            }
        }
    }
}
