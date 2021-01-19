package com.worldelite.job.service;

import com.worldelite.job.entity.*;
import com.worldelite.job.form.SearchHistoryForm;
import com.worldelite.job.mapper.CityMapper;
import com.worldelite.job.mapper.DictMapper;
import com.worldelite.job.mapper.SearchJobHistoryInfoMapper;
import com.worldelite.job.mapper.SearchJobHistoryMapper;
import com.worldelite.job.vo.SearchJobHistoryVo;
import lombok.AllArgsConstructor;
//import org.omg.CORBA.INTERNAL;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author twz
 * @Date 2021-01-14
 * @Desc TODO
 */
@Service
@AllArgsConstructor
public class SearchJobHistoryService extends BaseService{

    private SearchJobHistoryMapper searchJobHistoryMapper;
    private SearchJobHistoryInfoMapper searchJobHistoryInfoMapper;
    private CityMapper cityMapper;
    private DictMapper dictMapper;

    public void saveSearchJobHistory(SearchHistoryForm searchHistoryForm) {
        if (curUser() == null) return;

        SearchJobHistory searchJobHistory = SearchJobHistory.builder()
                .keyWord(searchHistoryForm.getKeyWord())
                .userId(curUser().getId())
                .filterCount(searchHistoryForm.getFilterCount())
                .build();

        searchJobHistoryMapper.insert(searchJobHistory);
        Long historyId = searchJobHistory.getId();

        SearchJobHistoryInfo searchJobHistoryInfo = SearchJobHistoryInfo.builder()
                .historyId(historyId)
                .build();

        int maxLength = getMaxIdsLength(searchHistoryForm);
        for (int i = 0; i < maxLength; i++) {
            if (i < searchHistoryForm.getCityIds().length)
                searchJobHistoryInfo.setCityId(searchHistoryForm.getCityIds()[i]);

            if (i < searchHistoryForm.getDegreeIds().length)
                searchJobHistoryInfo.setDegreeId(searchHistoryForm.getDegreeIds()[i]);

            if (i < searchHistoryForm.getSalaryIds().length)
                searchJobHistoryInfo.setSalaryId(searchHistoryForm.getSalaryIds()[i]);

            if (i < searchHistoryForm.getIndustryIds().length)
                searchJobHistoryInfo.setIndustryId(searchHistoryForm.getIndustryIds()[i]);

            searchJobHistoryInfoMapper.insert(searchJobHistoryInfo);
        }
    }


    public List<SearchJobHistoryVo> findJobHistory() {
        List<SearchJobHistoryVo> searchJobHistoryVos = new ArrayList<>();

        if (curUser() == null) return searchJobHistoryVos;

        SearchJobHistoryOptions options = SearchJobHistoryOptions.builder()
                .userId(curUser().getId())
                .build();

        List<SearchJobHistory> jobHistories = searchJobHistoryMapper.selectAndList(options);
        if (jobHistories == null || jobHistories.size() <= 0) {
            return searchJobHistoryVos;
        }

        return safeGetHistory(jobHistories, searchJobHistoryVos, options);
    }

    private List<SearchJobHistoryVo> safeGetHistory(List<SearchJobHistory> jobHistories, List<SearchJobHistoryVo> vos, SearchJobHistoryOptions options) {
        int count = 0;

        for (SearchJobHistory jobHistory : jobHistories) {
            if (count > 2) break;
            options.setHistoryId(jobHistory.getId());
            List<SearchJobHistoryInfo> jobHistoryInfos = searchJobHistoryInfoMapper.selectAndList(options);

            List<Integer> cityIds = new ArrayList<>();
            List<Integer> salaryIds = new ArrayList<>();
            List<Integer> degreeIds = new ArrayList<>();
            List<Integer> industryIds = new ArrayList<>();

            List<String> cityValues = new ArrayList<>();
            List<String> salaryValues = new ArrayList<>();
            List<String> degreeValues = new ArrayList<>();
            List<String> industryValues = new ArrayList<>();
            if (jobHistoryInfos != null && jobHistoryInfos.size() > 0) {
                SearchJobHistoryVo vo = SearchJobHistoryVo.builder()
                        .keyWord(jobHistory.getKeyWord())
                        .filterCount(jobHistory.getFilterCount())
                        .build();

                for (SearchJobHistoryInfo jobHistoryInfo : jobHistoryInfos) {
                    cityIds.add(jobHistoryInfo.getCityId());
                    salaryIds.add(jobHistoryInfo.getSalaryId());
                    degreeIds.add(jobHistoryInfo.getDegreeId());
                    industryIds.add(jobHistoryInfo.getIndustryId());

                    Dict dict = new Dict();
                    cityValues.add(Optional.ofNullable(cityMapper.selectByPrimaryKey(jobHistoryInfo.getCityId())).orElse(new City()).getName());
                    salaryValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getSalaryId())).orElse(dict).getName());
                    degreeValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getDegreeId())).orElse(dict).getName());
                    industryValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getIndustryId())).orElse(dict).getName());
                }
                vo.setCityIds(cityIds.stream().distinct().collect(Collectors.toList()));
                vo.setSalaryIds(salaryIds.stream().distinct().collect(Collectors.toList()));
                vo.setDegreeIds(degreeIds.stream().distinct().collect(Collectors.toList()));
                vo.setIndustryIds(industryIds.stream().distinct().collect(Collectors.toList()));

                vo.setCityValues(cityValues.stream().distinct().collect(Collectors.toList()));
                vo.setSalaryValues(salaryValues.stream().distinct().collect(Collectors.toList()));
                vo.setDegreeValues(degreeValues.stream().distinct().collect(Collectors.toList()));
                vo.setIndustryValues(industryValues.stream().distinct().collect(Collectors.toList()));
                vos.add(vo);
                count++;
            }
        }

        return vos;

    }


    private int getMaxIdsLength(SearchHistoryForm searchHistoryForm) {
        List<Integer> lengthList = Arrays.asList(searchHistoryForm.getCityIds().length,
                searchHistoryForm.getDegreeIds().length,
                searchHistoryForm.getSalaryIds().length);
        Collections.sort(lengthList);
        return lengthList.get(lengthList.size() - 1);
    }

}
