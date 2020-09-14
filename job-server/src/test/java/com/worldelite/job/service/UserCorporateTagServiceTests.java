package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.form.UserCorporateTagForm;
import com.worldelite.job.mapper.UserCorporateTagMapper;
import com.worldelite.job.vo.UserCorporateTagVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class UserCorporateTagServiceTests {
    private static final Long TEST_JOB_APPLY_ID = (long) 123456667;
    private static final String TEST_TAG_1 = "test tag 1";
    private static final String TEST_TAG_2 = "test tag 2";
    private static final int TEST_NUM_TAGS = 2;

    @Autowired
    private UserCorporateTagService userCorporateTagService;

    @Autowired
    private UserCorporateTagMapper userCorporateTagMapper;

    @Test
    public void testSaveCommentAndRetrieveByUserId_shouldBeIdentical() {
        //saving user comments.
        final UserCorporateTagForm form1 = new UserCorporateTagForm();
        final UserCorporateTagForm form2 = new UserCorporateTagForm();
        form1.setJobApplyId(TEST_JOB_APPLY_ID);
        form1.setTagName(TEST_TAG_1);

        form2.setJobApplyId(TEST_JOB_APPLY_ID);
        form2.setTagName(TEST_TAG_2);

        userCorporateTagService.saveTag(form1);
        userCorporateTagService.saveTag(form2);

        //retrieving user comments, expecting 2 total comments.
        final List<UserCorporateTagVo> tags = userCorporateTagService.getTagsByJobApplyId(TEST_JOB_APPLY_ID);
        System.out.println(tags);
        Assert.isTrue(tags.size() == TEST_NUM_TAGS);

        //delete ALL user comments created in this test.
        userCorporateTagService.delTag(tags.get(0).getId());
        userCorporateTagService.delTag(tags.get(1).getId());

        //retrieving user comments, expecting 0 total comment.
        final List<UserCorporateTagVo> tagsAfterDeletion = userCorporateTagService.getTagsByJobApplyId(TEST_JOB_APPLY_ID);
        Assert.isTrue(0 == tagsAfterDeletion.size());
    }
}
