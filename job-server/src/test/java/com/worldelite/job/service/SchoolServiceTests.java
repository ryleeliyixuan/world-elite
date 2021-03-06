package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.form.SchoolListForm;
import com.worldelite.job.vo.SchoolVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class SchoolServiceTests {

    @Autowired
    private SchoolService schoolService;

    @Test
    public void test_takeOffJobs(){
        Integer[] schoolIds = new Integer[]{1,2,3,600340};
        SchoolListForm schoolListForm = new SchoolListForm();
        schoolListForm.setSchoolIds(schoolIds);
        List<SchoolVo> schoolVoList = schoolService.getSchoolListByIds(schoolListForm);
        System.out.println(schoolVoList.size());
    }

}
