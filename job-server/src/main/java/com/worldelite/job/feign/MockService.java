package com.worldelite.job.feign;

import com.worldelite.job.context.config.FeignConfig;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.InterviewerInfoVo;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "mockService", url = "${feign.mock.url}", configuration = FeignConfig.class)
public interface MockService {
    @Headers(value = {"Content-type:application/json;charset=UTF-8"})
    @RequestMapping(method = RequestMethod.GET, value = "/interviewer/{id}")
    @ResponseBody
    ApiResult<InterviewerInfoVo> getInterviewerInfo(@PathVariable("id") String id);
}
