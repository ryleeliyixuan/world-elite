package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.form.JobDescExampleForm;
import com.worldelite.job.mapper.JobDescExampleMapper;
import com.worldelite.job.vo.JobDescExampleVo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class JobDescExampleTests {
    private static final Long TEST_CATEGORY_ID = (long)123456667;
    private static final String TEST_EXAMPLE_1 = "test example 1";
    private static final String TEST_EXAMPLE_2 = "test example 2";
    private static final int TEST_NUM_EXAMPLES = 2;

    @Autowired
    private JobDescExampleService jobDescExampleService;

    @Autowired
    private JobDescExampleMapper jobDescExampleMapper;

    @Test
    public void testSaveExampleAndRetrieveByCategoryId_shouldBeIdentical() {
        //saving examples
        final JobDescExampleForm form1 = new JobDescExampleForm();
        final JobDescExampleForm form2 = new JobDescExampleForm();
        form1.setDescription(TEST_EXAMPLE_1);
        form1.setCategoryId(TEST_CATEGORY_ID);
        form2.setDescription(TEST_EXAMPLE_2);
        form2.setCategoryId(TEST_CATEGORY_ID);

        jobDescExampleService.saveExample(form1);
        jobDescExampleService.saveExample(form2);

        //retrieving examples, expecting 2 total examples.
        final JobDescExampleVo examples = jobDescExampleService.getExamplesByCategoryId(TEST_CATEGORY_ID);
        //Assert.assertTrue(examples.size() == 1);

        //delete ALL examples created in this test.
        jobDescExampleService.delExample(examples.getId());
        jobDescExampleService.delExample(examples.getId());

        //retrieving examples, expecting 0 total comment.
        final JobDescExampleVo examplesAfterDeletion = jobDescExampleService.getExamplesByCategoryId(TEST_CATEGORY_ID);
        Assert.assertTrue(examplesAfterDeletion == null);
    }
}
