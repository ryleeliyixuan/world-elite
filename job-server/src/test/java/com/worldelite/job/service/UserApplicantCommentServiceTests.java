package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.form.UserApplicantCommentForm;
import com.worldelite.job.mapper.UserApplicantCommentMapper;
import com.worldelite.job.vo.UserApplicantCommentVo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @author 王星瀚
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class UserApplicantCommentServiceTests {
    private static final Long TEST_USER_ID = (long)123456667;
    private static final Long TEST_COMAPNY_ID = (long)123456668;
    private static final String TEST_COMMENT_1 = "test comment 1";
    private static final String TEST_COMMENT_2 = "test comment 2";
    private static final int TEST_NUM_COMMENTS = 2;

    @Autowired
    private UserApplicantCommentService userApplicantCommentService;

    @Autowired
    private UserApplicantCommentMapper userApplicantCommentMapper;

    @Test
    public void testSaveCommentAndRetrieveByUserId_shouldBeIdentical() {
        //saving user comments.
        final UserApplicantCommentForm form1 = new UserApplicantCommentForm();
        final UserApplicantCommentForm form2 = new UserApplicantCommentForm();
        form1.setComment(TEST_COMMENT_1);
        form1.setUserApplicantId(TEST_USER_ID);
        form1.setCompanyId(TEST_COMAPNY_ID);
        form2.setComment(TEST_COMMENT_2);
        form2.setUserApplicantId(TEST_USER_ID);
        form2.setCompanyId(TEST_COMAPNY_ID);

        userApplicantCommentService.saveComment(form1);
        userApplicantCommentService.saveComment(form2);

        //retrieving user comments, expecting 2 total comments.
        final List<UserApplicantCommentVo> comments = userApplicantCommentService.getCommentsByUserIdAndCompanyId(TEST_USER_ID, TEST_COMAPNY_ID);
        Assert.assertTrue(comments.size() == TEST_NUM_COMMENTS);

        //delete ALL user comments created in this test.
        userApplicantCommentService.deleteCommentById(comments.get(0).getId());
        userApplicantCommentService.deleteCommentById(comments.get(1).getId());

        //retrieving user comments, expecting 0 total comment.
        final List<UserApplicantCommentVo> commentsAfterDeletion = userApplicantCommentService.getCommentsByUserIdAndCompanyId(TEST_USER_ID, TEST_COMAPNY_ID);
        Assert.assertTrue(commentsAfterDeletion.size() == 0);
    }
}
