package com.worldelite.job.api;

import com.worldelite.job.entity.SearchJobHistory;
import com.worldelite.job.form.SearchHistoryForm;
import com.worldelite.job.service.SearchJobHistoryService;
import com.worldelite.job.vo.ApiResult;
import com.worldelite.job.vo.SearchJobHistoryVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @Author twz
 * @Date 2021-01-14
 * @Desc TODO
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/history")
public class SearchJobHistoryApi {

    private SearchJobHistoryService searchHistoryService;

    @PostMapping("save")
    public ApiResult save(@RequestBody SearchHistoryForm searchHistoryForm) {
        searchHistoryService.saveSearchJobHistory(searchHistoryForm);
        return ApiResult.ok();
    }


    @GetMapping("get-job-history")
    public ApiResult<List<SearchJobHistoryVo>> getSearchJobHistory() {
        List<SearchJobHistoryVo> searchJobHistoryVos = searchHistoryService.findJobHistory();
        return ApiResult.ok(searchJobHistoryVos);
    }

}
