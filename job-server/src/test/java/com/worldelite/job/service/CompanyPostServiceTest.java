package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.worldelite.job.JobApplication;
import com.worldelite.job.context.AttrKeys;
import com.worldelite.job.entity.CompanyPost;
import com.worldelite.job.entity.User;
import com.worldelite.job.form.CompanyPostForm;
import com.worldelite.job.form.CompanyPostListForm;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公司帖子测试类
 */
@Transactional
@SpringBootTest(classes = JobApplication.class)
public class CompanyPostServiceTest {

    @Autowired
    private CompanyPostService companyPostService;

    /**
     * 保存和修改帖子测试
     */
    @Test
    public void testSave(){
        //模拟用户
        User user = new User();
        user.setId(0L);
        AppUtils.request().setAttribute(AttrKeys.LOGIN_USER,new UserVo().asVo(user));
        //新增帖子
        CompanyPostForm companyPostForm = new CompanyPostForm();
        companyPostForm.setCliqueId(0);
        companyPostForm.setCompanyId(0L);
        companyPostForm.setTitle("测试帖子");
        companyPostForm.setContent("测试测试<a>测试</a>");
        companyPostForm.setTags(new String[]{"abc","def"});
        CompanyPost companyPost = companyPostService.save(companyPostForm);
        //获取帖子
        companyPost = companyPostService.getById(companyPost.getId());
        Assert.notNull(companyPost,"帖子数据不存在");
        Assert.isTrue("测试帖子".equals(companyPost.getTitle()),"帖子数据错误");
        //修改帖子
        BeanUtil.copyProperties(companyPost,companyPostForm);
        companyPostForm.setTitle("修改后帖子");
        companyPostService.save(companyPostForm);
        //获取修改后的帖子
        companyPost = companyPostService.getById(companyPost.getId());
        Assert.notNull(companyPost,"帖子数据不存在");
        Assert.isTrue("修改后帖子".equals(companyPost.getTitle()),"帖子数据错误");
    }

    /**
     * 搜索测试
     */
    @Test
    public void testList(){
        //新增帖子
        CompanyPost companyPost = newPost();
        //搜索
        CompanyPostListForm listForm = new CompanyPostListForm();
        listForm.setTitle("测试");
        PageResult<CompanyPost> pageResult = companyPostService.list(listForm);
        Assert.notNull(pageResult,"帖子数据不存在");
        Assert.isTrue(pageResult.getTotal()==1,"搜索结果不对");
    }

    /**
     * 新增帖子
     * @return 帖子
     */
    private CompanyPost newPost(){
        //模拟用户
        User user = new User();
        user.setId(0L);
        AppUtils.request().setAttribute(AttrKeys.LOGIN_USER,new UserVo().asVo(user));
        //新增帖子
        CompanyPostForm companyPostForm = new CompanyPostForm();
        companyPostForm.setCliqueId(0);
        companyPostForm.setCompanyId(0L);
        companyPostForm.setTitle("测试帖子");
        companyPostForm.setContent("测试测试<a>测试</a>");
        companyPostForm.setTags(new String[]{"abc","def"});
        return companyPostService.save(companyPostForm);
    }
}
