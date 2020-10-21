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
import com.worldelite.job.mapper.CompanyCommentMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 保存评论
     * @param companyCommentForm 评论表单
     * @return 评论
     */
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
            //更新数据
            companyCommentMapper.updateByPrimaryKeySelective(companyComment);
            //更新父级评论数
            ownerCommentsAdd(companyComment);
        }else{
            //修改评论
            //保存基本数据
            BeanUtil.copyProperties(companyCommentForm,companyComment,"id");
            //计算热度
            companyComment.setHots(hotCalc(companyComment));
            //更新数据
            companyCommentMapper.updateByPrimaryKeySelective(companyComment);
        }
        return companyComment;
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
     * 删除指定评论
     * @param commentId 评论ID
     */
    public void deleteById(Long commentId){
        //先计算评论数
        CompanyComment comment = getById(commentId);
        //需要删除的评论数量为子评论数+本身
        Integer value = comment.getComments()+1;
        ownerCommentsSub(comment,value);
        //删除评论
        companyCommentMapper.deleteByPrimaryKey(commentId);
        //删除子评论
        deleteByOwnerId(commentId);
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
     * 点赞
     * @param commentId 评论ID
     * @return 评论
     */
    public CompanyComment likesAdd(Long commentId){
        CompanyComment companyComment = getById(commentId);
        Integer likes = companyComment.getLikes();
        if(likes==null){
            likes = 0;
        }else{
            likes++;
        }
        companyComment.setLikes(likes);
        companyComment.setHots(hotCalc(companyComment));
        companyCommentMapper.updateByPrimaryKeySelective(companyComment);
        return companyComment;
    }

    /**
     * 取消点赞
     * @param commentId 评论ID
     * @return 评论
     */
    public CompanyComment likesSub(Long commentId){
        CompanyComment companyComment = getById(commentId);
        Integer likes = companyComment.getLikes();
        if(likes==null || likes==0){
            likes = 0;
        }else{
            likes--;
        }
        companyComment.setLikes(likes);
        companyComment.setHots(hotCalc(companyComment));
        companyCommentMapper.updateByPrimaryKeySelective(companyComment);
        return companyComment;
    }

    /**
     * 评论
     * @param commentId 评论ID
     * @return 评论
     */
    public CompanyComment commentsAdd(Long commentId){
        CompanyComment companyComment = getById(commentId);
        Integer comments = companyComment.getComments();
        if(comments==null){
            comments = 0;
        }else{
            comments++;
        }
        companyComment.setComments(comments);
        companyComment.setHots(hotCalc(companyComment));
        companyCommentMapper.updateByPrimaryKeySelective(companyComment);
        return companyComment;
    }

    /**
     * 取消评论
     * @param commentId 评论ID
     * @param value 减少的评论数
     * @return 评论
     */
    public CompanyComment commentsSub(Long commentId,Integer value){
        CompanyComment companyComment = getById(commentId);
        Integer comments = companyComment.getComments();
        if(comments==null || comments<=value){
            comments = 0;
        }else{
            comments -= value;
        }
        companyComment.setComments(comments);
        companyComment.setHots(hotCalc(companyComment));
        companyCommentMapper.updateByPrimaryKeySelective(companyComment);
        return companyComment;
    }

    /**
     * 举报
     * 举报不能取消，只能修改举报理由
     * @param commentId 评论ID
     * @return 评论
     */
    public CompanyComment reportsAdd(Long commentId){
        CompanyComment companyComment = getById(commentId);
        Integer reports = companyComment.getReports();
        if(reports==null){
            reports = 0;
        }else{
            reports++;
        }
        companyComment.setReports(reports);
        companyComment.setHots(hotCalc(companyComment));
        companyCommentMapper.updateByPrimaryKeySelective(companyComment);
        return companyComment;
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
     * @return 热度
     */
    private int hotCalc(CompanyComment companyComment){
        int hot = 0;
        if(companyComment.getLikes() != null){
            hot += companyComment.getLikes();
        }
        if(companyComment.getComments() != null){
            hot += companyComment.getComments();
        }
        return hot;
    }

    /**
     * 给所属对象增加评论数
     * @param companyComment 评论
     */
    private void ownerCommentsAdd(CompanyComment companyComment){
        if(companyComment.getType()==null || companyComment.getOwnerId()==null){
            throw new ServiceException(message("api.error.data.comment"));
        }
        Long ownerId = companyComment.getOwnerId();
        switch (CommentType.valueOf(companyComment.getType())){
            case POST:
                companyPostService.commentsAdd(ownerId);
                return;
            case SCORE:
                companyScoreService.commentsAdd(ownerId);
                return;
            case COMMENT:
                //给父评论添加评论数
                commentsAdd(ownerId);
                //递归给父评论所属对象添加评论数
                CompanyComment comment = getById(ownerId);
                ownerCommentsAdd(comment);
                return;
        }
    }

    /**
     * 给所属对象减少评论数
     * @param companyComment 评论
     * @param value 删除的评论数量
     */
    private void ownerCommentsSub(CompanyComment companyComment,Integer value){
        if(companyComment.getType()==null || companyComment.getOwnerId()==null){
            throw new ServiceException(message("api.error.data.comment"));
        }
        Long ownerId = companyComment.getOwnerId();
        switch (CommentType.valueOf(companyComment.getType())){
            case POST:
                companyPostService.commentsSub(ownerId,value);
                return;
            case SCORE:
                companyScoreService.commentsSub(ownerId,value);
                return;
            case COMMENT:
                //给父评论添加评论数
                commentsSub(ownerId,value);
                //递归给父评论所属对象添加评论数
                CompanyComment comment = getById(ownerId);
                ownerCommentsSub(comment,value);
                return;
        }
    }

    /**
     * 添加被评论人和所有所属对象
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
