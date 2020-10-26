package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.constants.CommentType;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.entity.CompanyComment;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.CompanyScore;
import com.worldelite.job.entity.User;
import com.worldelite.job.form.*;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 公司评论测试类
 */
//@Transactional
@SpringBootTest(classes = JobApplication.class)
public class CompanyCommentServiceTest {

    @Autowired
    private CompanyPostService companyPostService;

    @Autowired
    private CompanyScoreService companyScoreService;

    @Autowired
    private CompanyCommentService companyCommentService;

    /**
     * 测试对帖子发表评论
     */
    @Test
    public void testSavePostComment(){
        //生成帖子测试数据
        CompanyPost companyPost = newPost();
        //生成评论测试数据
        CompanyComment companyComment1 = newComment(companyPost.getId(), CommentType.POST.value);
        CompanyComment companyComment2 = newComment(companyPost.getId(), CommentType.POST.value);
        CompanyComment companyComment3 = newComment(companyPost.getId(), CommentType.POST.value);
        //获取帖子的评论数据
        CompanyCommentListForm listForm = new CompanyCommentListForm();
        listForm.setOwnerId(companyPost.getId());
        PageResult<CompanyComment> companyCommentPage = companyCommentService.list(listForm);
        //评论数据与预期比较
        Assert.notEmpty(companyCommentPage.getList(),"评论数据为空");
        Assert.isTrue(companyCommentPage.getTotal()==3,"评论数据不正确");
        //帖子的评论数应该为3
        companyPost = companyPostService.getById(companyPost.getId());
        Assert.isTrue(companyPost.getComments()==3,"帖子评论数不正确");
        //检查评论关联是否正常
        for(CompanyComment comment:companyCommentPage.getList()){
            Assert.isTrue(comment.getOwnerId().equals(companyPost.getId()),"评论所属对象ID不正确");
            Assert.isTrue(comment.getToId().equals(companyPost.getFromId()),"被评论人ID不正确");
        }
    }

    /**
     * 测试对评分发表评论
     */
    @Test
    public void testSaveScoreComment(){
        //生成评分测试数据
        CompanyScore companyScore = newScore();
        //生成评论数据
        CompanyComment companyComment1 = newComment(companyScore.getId(), CommentType.SCORE.value);
        CompanyComment companyComment2 = newComment(companyScore.getId(), CommentType.SCORE.value);
        CompanyComment companyComment3 = newComment(companyScore.getId(), CommentType.SCORE.value);
        //获取评分的评论数据
        CompanyCommentListForm listForm = new CompanyCommentListForm();
        listForm.setOwnerId(companyScore.getId());
        PageResult<CompanyComment> companyCommentPage = companyCommentService.list(listForm);
        //断言查询结果
        Assert.notEmpty(companyCommentPage.getList(),"评分数据为空");
        Assert.isTrue(companyCommentPage.getTotal()==3,"评分数据不正确");
        //评分的评论数应该为3
        companyScore = companyScoreService.getById(companyScore.getId());
        Assert.isTrue(companyScore.getComments()==3,"帖子评分数不正确");
        //检查评论关联是否正常
        for(CompanyComment comment:companyCommentPage.getList()){
            Assert.isTrue(comment.getOwnerId().equals(companyScore.getId()),"评论所属对象ID不正确");
            Assert.isTrue(comment.getToId().equals(companyScore.getFromId()),"被评论人ID不正确");
        }
    }

    /**
     * 测试对已有评论发表评论
     */
    @Test
    public void testSaveCommentComment(){
        //添加帖子
        CompanyPost companyPost = newPost();
        //添加帖子的评论
        CompanyComment companyComment = newComment(companyPost.getId(), CommentType.POST.value);
        //添加评论的评论
        CompanyComment comment1 = newComment(companyComment.getId(), CommentType.COMMENT.value);
        CompanyComment comment2 = newComment(comment1.getId(), CommentType.COMMENT.value);
        //帖子的评论评论数应该为2
        companyComment = companyCommentService.getById(companyComment.getId());
        Assert.isTrue(companyComment.getComments()==2,"帖子评论的评论数不正确");
        //帖子的评论数应该为3
        companyPost = companyPostService.getById(companyPost.getId());
        Assert.isTrue(companyPost.getComments()==3,"帖子的评论数不正确");
    }

    /**
     * 测试删除评论
     */
    @Test
    public void testDeleteComment(){
        //添加帖子
        CompanyPost companyPost = newPost();
        //添加帖子的评论
        CompanyComment companyComment = newComment(companyPost.getId(), CommentType.POST.value);
        //添加评论的评论
        CompanyComment comment1 = newComment(companyComment.getId(), CommentType.COMMENT.value);
        CompanyComment comment2 = newComment(comment1.getId(), CommentType.COMMENT.value);
        //删除子评论
        companyCommentService.deleteById(comment1.getId());
        //评论数应该为1
        companyComment = companyCommentService.getById(companyComment.getId());
        Assert.isTrue(companyComment.getComments()==1,"帖子评论的评论数不正确");

    }

    /**
     * 新增帖子
     * @return 帖子
     */
    private CompanyPost newPost(){
        //模拟用户登录
        User user = new User();
        user.setId(AppUtils.nextId());
        AppUtils.request().setAttribute(AttrKeys.LOGIN_USER,new UserVo().asVo(user));
        //新增帖子
        CompanyPostForm companyPostForm = new CompanyPostForm();
        companyPostForm.setCliqueId(0);
        companyPostForm.setCompanyId(AppUtils.nextId());
        companyPostForm.setTitle("测试帖子");
        companyPostForm.setContent("测试测试<a>测试</a>");
        companyPostForm.setTags(new String[]{"abc","def"});
        return companyPostService.save(companyPostForm);
    }

    /**
     * 新评分
     * @return 评分
     */
    private CompanyScore newScore(){
        //模拟用户登录
        User user = new User();
        user.setId(AppUtils.nextId());
        AppUtils.request().setAttribute(AttrKeys.LOGIN_USER,new UserVo().asVo(user));
        CompanyScoreForm companyScoreForm = new CompanyScoreForm();
        companyScoreForm.setCompanyId(AppUtils.nextId());
        companyScoreForm.setScore(100);
        companyScoreForm.setContent("测试测试测试");
        companyScoreForm.setAnonymous((byte)0);
        return companyScoreService.save(companyScoreForm);
    }

    /**
     * 新增评论
     * @return 评论
     */
    private CompanyComment newComment(Long ownerId,Byte type){
        //模拟用户
        User user = new User();
        user.setId(AppUtils.nextId());
        AppUtils.request().setAttribute(AttrKeys.LOGIN_USER,new UserVo().asVo(user));
        //新增评论
        CompanyCommentForm companyCommentForm = new CompanyCommentForm();
        companyCommentForm.setOwnerId(ownerId);
        companyCommentForm.setType(type);
        companyCommentForm.setContent("测试测试<a>测试</a>");
        return companyCommentService.save(companyCommentForm);
    }
}
