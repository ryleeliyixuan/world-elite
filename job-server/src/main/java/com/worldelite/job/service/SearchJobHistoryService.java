package com.worldelite.job.service;

import com.worldelite.job.entity.*;
import com.worldelite.job.form.SearchHistoryForm;
import com.worldelite.job.mapper.*;
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
    private SkillTagMapper skillTagMapper;

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

            if (i < searchHistoryForm.getExpIds().length)
                searchJobHistoryInfo.setExpId(searchHistoryForm.getExpIds()[i]);

            if (i < searchHistoryForm.getScaleIds().length)
                searchJobHistoryInfo.setScaleId(searchHistoryForm.getScaleIds()[i]);

            if (i < searchHistoryForm.getDefineIds().length)
                searchJobHistoryInfo.setDefineId(searchHistoryForm.getDefineIds()[i]);

            if (i < searchHistoryForm.getJobTypeIds().length)
                searchJobHistoryInfo.setJobTypeId(searchHistoryForm.getJobTypeIds()[i]);

            if (i < searchHistoryForm.getLanIds().length)
                searchJobHistoryInfo.setLanId(searchHistoryForm.getLanIds()[i]);

            if (i < searchHistoryForm.getSpecialIds().length)
                searchJobHistoryInfo.setSpecialId(searchHistoryForm.getSpecialIds()[i]);

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
            List<Integer> expIds = new ArrayList<>();
            List<Integer> scaleIds = new ArrayList<>();
            List<Integer> defineIds = new ArrayList<>();
            List<Integer> jobTypeIds = new ArrayList<>();
            List<Integer> lanIds = new ArrayList<>();
//            List<Integer> specialIds = new ArrayList<>();

            List<String> cityValues = new ArrayList<>();
            List<String> salaryValues = new ArrayList<>();
            List<String> degreeValues = new ArrayList<>();
            List<String> industryValues = new ArrayList<>();
            List<String> expValues = new ArrayList<>();
            List<String> scaleValues = new ArrayList<>();
            List<String> defineValues = new ArrayList<>();
            List<String> jobTypeValues = new ArrayList<>();
            List<String> lanValues = new ArrayList<>();
//            List<String> specialValues = new ArrayList<>();
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
                    expIds.add(jobHistoryInfo.getExpId());
                    scaleIds.add(jobHistoryInfo.getScaleId());
                    defineIds.add(jobHistoryInfo.getDefineId());
                    jobTypeIds.add(jobHistoryInfo.getJobTypeId());
                    lanIds.add(jobHistoryInfo.getLanId());
//                    specialIds.add(jobHistoryInfo.getSpecialId());

                    Dict dict = new Dict();
                    City city = new City();
                    SkillTag skillTag = new SkillTag();
                    dict.setName("不限");
                    city.setName("不限");
                    skillTag.setName("不限");

                    cityValues.add(Optional.ofNullable(cityMapper.selectByPrimaryKey(jobHistoryInfo.getCityId())).orElse(city).getName());
                    salaryValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getSalaryId())).orElse(dict).getName());
                    degreeValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getDegreeId())).orElse(dict).getName());
                    industryValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getIndustryId())).orElse(dict).getName());

                    expValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getExpId())).orElse(dict).getName());
                    scaleValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getScaleId())).orElse(dict).getName());
                    defineValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getDefineId())).orElse(dict).getName());
                    jobTypeValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getJobTypeId())).orElse(dict).getName());

                    lanValues.add(Optional.ofNullable(skillTagMapper.selectByPrimaryKey(jobHistoryInfo.getLanId())).orElse(skillTag).getName());
//                    specialValues.add(Optional.ofNullable(dictMapper.selectByPrimaryKey(jobHistoryInfo.getSpecialId())).orElse(dict).getName());
                }
                vo.setCityIds(cityIds.stream().distinct().collect(Collectors.toList()));
                vo.setSalaryIds(salaryIds.stream().distinct().collect(Collectors.toList()));
                vo.setDegreeIds(degreeIds.stream().distinct().collect(Collectors.toList()));
                vo.setIndustryIds(industryIds.stream().distinct().collect(Collectors.toList()));
                vo.setExpIds(expIds.stream().distinct().collect(Collectors.toList()));
                vo.setScaleIds(scaleIds.stream().distinct().collect(Collectors.toList()));
                vo.setDefineIds(defineIds.stream().distinct().collect(Collectors.toList()));
                vo.setJobTypeIds(jobTypeIds.stream().distinct().collect(Collectors.toList()));
                vo.setLanIds(lanIds.stream().distinct().collect(Collectors.toList()));
//                vo.setSpecialIds(specialIds.stream().distinct().collect(Collectors.toList()));

                vo.setCityValues(cityValues.stream().distinct().collect(Collectors.toList()));
                vo.setSalaryValues(salaryValues.stream().distinct().collect(Collectors.toList()));
                vo.setDegreeValues(degreeValues.stream().distinct().collect(Collectors.toList()));
                vo.setIndustryValues(industryValues.stream().distinct().collect(Collectors.toList()));
                vo.setExpValues(expValues.stream().distinct().collect(Collectors.toList()));
                vo.setScaleValues(scaleValues.stream().distinct().collect(Collectors.toList()));
                vo.setDefineValues(defineValues.stream().distinct().collect(Collectors.toList()));
                vo.setJobTypeValues(jobTypeValues.stream().distinct().collect(Collectors.toList()));
                vo.setLanValues(lanValues.stream().distinct().collect(Collectors.toList()));
//                vo.setSpecialIds(specialValues.stream().distinct().collect(Collectors.toList()));

                vos.add(vo);
                count++;
            }
        }

        return vos;

    }


    private int getMaxIdsLength(SearchHistoryForm searchHistoryForm) {
        List<Integer> lengthList = Arrays.asList(
                searchHistoryForm.getCityIds().length,
                searchHistoryForm.getDegreeIds().length,
                searchHistoryForm.getIndustryIds().length,
                searchHistoryForm.getSalaryIds().length,
                searchHistoryForm.getExpIds().length,
                searchHistoryForm.getScaleIds().length,
                searchHistoryForm.getDefineIds().length,
                searchHistoryForm.getJobTypeIds().length,
                searchHistoryForm.getLanIds().length
//                searchHistoryForm.getSpecialIds().length
        );
        Collections.sort(lengthList);
        return lengthList.get(lengthList.size() - 1);
    }

}
