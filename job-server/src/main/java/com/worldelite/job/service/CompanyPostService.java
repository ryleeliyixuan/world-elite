package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.worldelite.job.constants.CommentType;
import com.worldelite.job.entity.*;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.*;
import com.worldelite.job.mapper.CompanyPostMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.*;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 企业帖子服务类
 */
@Slf4j
@Service
public class CompanyPostService extends BaseService{

    @Autowired
    private CompanyPostMapper companyPostMapper;

    @Autowired
    private CompanyLikeService companyLikeService;

    @Autowired
    private CompanyReportService companyReportService;

    @Autowired
    private CompanyCommentService companyCommentService;

    @Autowired
    private UserApplicantService userApplicantService;

    @Autowired
    private CompanyForbiddenService companyForbiddenService;

    @Autowired
    private CompanyService companyService;

    /**
     * 保存帖子
     * @param companyPostForm 帖子表单
     * @return 帖子
     */
    @Transactional
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
        //更新数据
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
        //计算热度
        hotCalc(companyPost);
        return companyPost;
    }

    /**
     * 删除帖子
     * @param postId
     */
    @Transactional
    public void delete(Long postId){
        //删除帖子
        companyPostMapper.deleteByPrimaryKey(postId);
        //删除点赞
        companyLikeService.deleteByOwnerId(postId);
        //删除举报
        companyReportService.deleteByOwnerId(postId);
        //删除评论
        companyCommentService.deleteByOwnerId(postId);
    }

    @Transactional
    public void deleteAll(Long[] postIds){
        //批量删除帖子
        CompanyPostOptions options = new CompanyPostOptions();
        options.setPostIds(postIds);
        companyPostMapper.deleteAll(options);
    }


    /**
     * 点赞
     * @param postId
     */
    @Transactional
    public CompanyPostVo like(Long postId){
        //帖子存在才能点赞
        CompanyPost companyPost = getById(postId);
        //点赞
        companyLikeService.changLike(postId);
        //更新点赞数
        hotCalc(companyPost);
        CompanyPostVo companyPostVo = new CompanyPostVo();
        companyPostVo.setLike(companyLikeService.hasLike(postId));
        companyPostVo.setLikes(companyPost.getLikes());
        return companyPostVo;
    }

    /**
     * 举报
     * @param companyReportForm
     */
    @Transactional
    public CompanyPostVo report(CompanyReportForm companyReportForm){
        CompanyPost companyPost = getById(companyReportForm.getOwnerId());
        companyReportService.save(companyReportForm);
        hotCalc(companyPost);
        CompanyPostVo companyPostVo = new CompanyPostVo();
        companyPostVo.setReport(companyReportService.getReportVo(companyReportForm.getOwnerId()));
        companyPostVo.setReports(companyPost.getReports());
        return companyPostVo;
    }

    /**
     * 评论
     * @param companyCommentForm
     */
    @Transactional
    public CompanyCommentVo comment(CompanyCommentForm companyCommentForm){
        CompanyPost companyPost = getById(companyCommentForm.getOwnerId());
        companyCommentForm.setType(CommentType.POST.value);
        CompanyComment companyComment = companyCommentService.save(companyCommentForm);
        hotCalc(companyPost);
        return companyCommentService.getCommentVo(companyComment);
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
     * 帖子视图对象
     * @param listForm
     * @return
     */
    public PageResult<CompanyPostVo> listVo(CompanyPostListForm listForm){
        PageResult<CompanyPost> companyPostPageResult = list(listForm);
        List<CompanyPost> postList = companyPostPageResult.getList();
        PageResult<CompanyPostVo> pageResult = new PageResult<>();
        BeanUtil.copyProperties(companyPostPageResult,pageResult,"list");
        List<CompanyPostVo> postVoList = new ArrayList<>(postList.size());
        for(CompanyPost companyPost:postList){
            CompanyPostVo vo = getPostVo(companyPost);
            vo.setContent(null);
            postVoList.add(vo);
        }
        pageResult.setList(postVoList);
        return pageResult;
    }

    public CompanyPostVo getPostVo(CompanyPost companyPost){
        CompanyPostVo companyPostVo = new CompanyPostVo().asVo(companyPost);
        companyPostVo.setFromUser(userApplicantService.getUserInfo(companyPost.getFromId()));
        //如果没有设置图片数据
        //从内容中获取第一张图片做为图片数据
        if(StringUtils.isEmpty(companyPost.getImage())){
            String image = getContentImage(companyPost.getContent());
            companyPostVo.setImage(image);
        }
        //企业信息
        CompanyVo company = companyService.getSimpleCompanyInfo(companyPost.getCompanyId());
        companyPostVo.setCompany(company);
        //禁言信息
        boolean isForbidden = companyForbiddenService.isForbidden(companyPost.getFromId());
        if(isForbidden){
            companyPostVo.setForbidden((byte) 1);
        }else{
            companyPostVo.setForbidden((byte) 0);
        }
        //登录后才有点赞和举报
        if(curUser() != null) {
            companyPostVo.setLike(companyLikeService.hasLike(companyPost.getId()));
            companyPostVo.setReport(companyReportService.getReportVo(companyPost.getId()));
        }
        return companyPostVo;
    }

    public CompanyPostVo getPostVo(Long postId){
        CompanyPost companyPost = getById(postId);
        return getPostVo(companyPost);
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

    private void hotCalc(CompanyPost companyPost){
        int hot = 0;
        Long postId = companyPost.getId();
        Integer likeCount = companyLikeService.getLikeCount(postId);
        Integer reportCount = companyReportService.getReportCount(postId);
        Integer commentCount = companyCommentService.getCommentCount(postId);
        hot += likeCount+reportCount+commentCount;
        companyPost.setLikes(likeCount);
        companyPost.setReports(reportCount);
        companyPost.setComments(commentCount);
        companyPost.setHots(hot);
        companyPostMapper.updateByPrimaryKeySelective(companyPost);
    }

    public void hotCalc(Long postId){
        CompanyPost companyPost = getById(postId);
        hotCalc(companyPost);
    }

    /**
     * 从内容中提取第一张图片
     * @param content
     * @return
     */
    public String getContentImage(String content){
        if(content==null) return null;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern pattern = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            // 得到<img />数据
            String img = matcher.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            if(m.find()){
                return m.group(1);
            }
        }
        return null;
    }

    public PageResult<CompanyPostVo> search(CompanyPostListForm listForm){
        AppUtils.setPage(listForm);
        CompanyPostOptions options = new CompanyPostOptions();
        BeanUtil.copyProperties(listForm,options);
        Page<CompanyPost> page = (Page<CompanyPost>) companyPostMapper.search(options);
        PageResult<CompanyPostVo> pageResult = new PageResult<>(page);
        List<CompanyPostVo> voList = new ArrayList<>();
        for(CompanyPost Post:page){
            voList.add(getPostVo(Post));
        }
        pageResult.setList(voList);
        return pageResult;
    }
}
