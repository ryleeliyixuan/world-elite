package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.entity.Company;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.CompanyPostForm;
import com.worldelite.job.form.CompanyPostListForm;
import com.worldelite.job.mapper.CompanyPostMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CompanyVo;
import com.worldelite.job.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业帖子服务类
 */
@Slf4j
@Service
public class CompanyPostService extends BaseService{

    @Autowired
    private CompanyPostMapper companyPostMapper;

    /**
     * 保存帖子
     * @param companyPostForm 帖子表单
     * @return 帖子
     */
    public CompanyPost save(CompanyPostForm companyPostForm){
        //如果不存在对应帖子数据，新建空帖子
        CompanyPost companyPost = null;
        if(companyPostForm.getId() != null){
            companyPost = getById(companyPostForm.getId());
        }
        if(companyPost == null){
            companyPost = newPost(companyPostForm.getCompanyId(),companyPostForm.getCliqueId());
        }
        //保存基本数据
        BeanUtil.copyProperties(companyPostForm,companyPost,"id");
        //计算热度
        companyPost.setHots(hotCalc(companyPost));
        //更新数据
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
        return companyPost;
    }

    /**
     * 获取帖子列表
     * @param listForm 搜索表单
     * @return 帖子列表
     */
    public PageResult<CompanyPost> list(CompanyPostListForm listForm){
        CompanyPost companyPost = new CompanyPost();
        BeanUtil.copyProperties(listForm,companyPost);
        AppUtils.setPage(listForm);
        Page<CompanyPost> companyPostPage = (Page<CompanyPost>) companyPostMapper.selectAndList(companyPost);
        PageResult<CompanyPost> pageResult = new PageResult<>(companyPostPage);
        List<CompanyPost> companyPostList = (List<CompanyPost>) companyPostPage;
        pageResult.setList(companyPostList);
        return pageResult;
    }

    /**
     * 点赞
     * @param postId 帖子ID
     * @return 帖子
     */
    public CompanyPost likesAdd(Long postId){
        CompanyPost companyPost = getById(postId);
        Integer likes = companyPost.getLikes();
        if(likes==null){
            likes = 0;
        }else{
            likes++;
        }
        companyPost.setLikes(likes);
        companyPost.setHots(hotCalc(companyPost));
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
        return companyPost;
    }

    /**
     * 取消点赞
     * @param postId 帖子ID
     * @return 帖子
     */
    public CompanyPost likesSub(Long postId){
        CompanyPost companyPost = getById(postId);
        Integer likes = companyPost.getLikes();
        if(likes==null || likes==0){
            likes = 0;
        }else{
            likes--;
        }
        companyPost.setLikes(likes);
        companyPost.setHots(hotCalc(companyPost));
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
        return companyPost;
    }

    /**
     * 评论
     * @param postId 帖子ID
     * @return 帖子
     */
    public CompanyPost commentsAdd(Long postId){
        CompanyPost companyPost = getById(postId);
        Integer comments = companyPost.getComments();
        if(comments==null){
            comments = 0;
        }else{
            comments++;
        }
        companyPost.setComments(comments);
        companyPost.setHots(hotCalc(companyPost));
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
        return companyPost;
    }

    /**
     * 取消评论
     * @param postId 帖子ID
     * @param value 减少的评论数
     * @return 帖子
     */
    public CompanyPost commentsSub(Long postId,Integer value){
        CompanyPost companyPost = getById(postId);
        Integer comments = companyPost.getComments();
        if(comments==null || comments<=value){
            comments = 0;
        }else{
            comments -= value;
        }
        companyPost.setComments(comments);
        companyPost.setHots(hotCalc(companyPost));
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
        return companyPost;
    }

    /**
     * 举报
     * 举报不能取消，只能修改举报理由
     * @param postId 帖子ID
     * @return 帖子
     */
    public CompanyPost reportsAdd(Long postId){
        CompanyPost companyPost = getById(postId);
        Integer reports = companyPost.getReports();
        if(reports==null){
            reports = 0;
        }else{
            reports++;
        }
        companyPost.setReports(reports);
        companyPost.setHots(hotCalc(companyPost));
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
        return companyPost;
    }


    /**
     * 通过帖子ID获取帖子数据
     * @param postId 帖子ID
     * @return 帖子数据
     */
    public CompanyPost getById(Long postId){
        CompanyPost companyPost = companyPostMapper.selectByPrimaryKey(postId);
        if(companyPost==null){
            throw new ServiceException(message("api.error.data.post"));
        }
        return companyPost;
    }

    /**
     * 新建帖子
     * @param companyId 公司ID
     * @param cliqueId 圈子ID
     * @return 新帖子
     */
    private CompanyPost newPost(Long companyId,Integer cliqueId){
        CompanyPost companyPost = new CompanyPost();
        companyPost.setId(AppUtils.nextId());
        companyPost.setFromId(curUser().getId());
        companyPost.setCompanyId(companyId);
        companyPost.setCliqueId(cliqueId);
        companyPost.setTitle("");
        companyPost.setLikes(0);
        companyPost.setComments(0);
        companyPost.setReports(0);
        companyPost.setHots(0);
        companyPostMapper.insertSelective(companyPost);
        return companyPost;
    }

    /**
     * 计算帖子热度
     * @param companyPost 帖子
     * @return 热度
     */
    private int hotCalc(CompanyPost companyPost){
        int hot = 0;
        if(companyPost.getHots() != null){
            hot = companyPost.getHots();
        }
        if(companyPost.getLikes() != null){
            hot += companyPost.getLikes();
        }
        if(companyPost.getComments() != null){
            hot += companyPost.getComments();
        }
        return hot;
    }

}
