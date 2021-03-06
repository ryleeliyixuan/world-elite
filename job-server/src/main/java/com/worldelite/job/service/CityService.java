package com.worldelite.job.service;

import com.alibaba.fastjson.JSON;
import com.worldelite.job.context.RedisKeys;
import com.worldelite.job.entity.City;
import com.worldelite.job.mapper.CityMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CityVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 城市数据服务类
 */
@Slf4j
@Service
public class CityService {

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取所有城市数据树形数据
     *
     * @return 城市树
     */
    public List<CityVo> getCityTree(List<City> cityList) {
        if (cityList == null || cityList.size() == 0) return null;
        //从数据库取出所有数据
        //将城市数据保存到Map以便生成树形结构时能直接取到父类
        Map<Integer, CityVo> cityMap = new HashMap<>();
        for (City city : cityList) {
            cityMap.put(city.getId(), new CityVo().asVo(city));
        }
        //生成城市树列表
        List<CityVo> rootList = new ArrayList<>();
        for (Integer cityId : cityMap.keySet()) {
            CityVo cityVo = cityMap.get(cityId);
            if (cityVo.getParentId() == null || cityVo.getParentId() == 0) {
                //防止数据异常，再将父节点ID统一设为0
                cityVo.setParentId(0);
                rootList.add(cityVo);
            } else {
                //添加子节点
                CityVo parentCity = cityMap.get(cityVo.getParentId());
                if (parentCity.getChildren() == null) {
                    parentCity.setChildren(new ArrayList<>());
                }
                parentCity.getChildren().add(cityVo);
            }
        }
        return rootList;
    }

    /**
     * 从缓存获取全部城市树
     *
     * @return 城市树
     */
    public List<CityVo> getCityList() {
        //从Redis中取缓存数据
        final String cityTreeJson = stringRedisTemplate.opsForValue().get(RedisKeys.CITY_TREE);
        if (StringUtils.isNotEmpty(cityTreeJson)) {
            return JSON.parseArray(cityTreeJson, CityVo.class);
        }

        final List<City> cities = cityMapper.selectAndList(new City());
        List<CityVo> cityTree = getCityTree(cities);
        //没有缓存则新建缓存
        if (CollectionUtils.isNotEmpty(cityTree)) {
            stringRedisTemplate.opsForValue().set(RedisKeys.CITY_TREE, JSON.toJSONString(cityTree), 10, TimeUnit.MINUTES);
        }
        return cityTree;
    }

    /**
     * 用父级城市id获取下级城市, 如使用河南省id获取下属市的城市列表
     *
     * @param parentId 父级城市id
     * @return 城市列表
     */
    public List<CityVo> getChildCityList(Integer parentId) {
        //从Redis中取缓存数据
        final String cityTreeJson = stringRedisTemplate.opsForValue().get(RedisKeys.CITY_TREE + "_" + parentId);
        if (StringUtils.isNotEmpty(cityTreeJson)) {
            return JSON.parseArray(cityTreeJson, CityVo.class);
        }

        //由于获取的只有一层, 不需要生成树
        final List<City> cities = cityMapper.selectByParentId(parentId);
        final List<CityVo> cityVoList = AppUtils.asVoList(cities, CityVo.class);
        //没有缓存则新建缓存
        if (CollectionUtils.isNotEmpty(cityVoList)) {
            stringRedisTemplate.opsForValue().set(RedisKeys.CITY_TREE + "_" + parentId, JSON.toJSONString(cityVoList), 10, TimeUnit.MINUTES);
        }
        return cityVoList;
    }

    /**
     * 根据ID搜索城市
     *
     * @param cityId 城市ID
     * @return 城市
     */
    public City getById(Integer cityId) {
        return cityMapper.selectByPrimaryKey(cityId);
    }

    /**
     * 根据ID搜索城市
     *
     * @param cityId 城市ID
     * @return 城市
     */
    public CityVo getCityVo(Integer cityId) {
        City city = getById(cityId);
        return new CityVo().asVo(city);
    }

    public List<Integer> getCityId(String[] cityNames) {
        List<Integer> ids = new ArrayList<>();
        for (String cityName : cityNames) {
            ids.addAll(cityMapper.selectIdByNameWithFuzzy(cityName));
        }
        return ids.stream().distinct().collect(Collectors.toList());
    }

    public List<CityVo> getCitiesByName(String name) {
        City options = new City();
        options.setName(name);
        List<City> list = cityMapper.selectAndList(options);
        List<CityVo> listVo = new ArrayList<>();
        for (City city : list) {
            listVo.add(new CityVo().asVo(city));
        }
        return listVo;
    }

    /**
     * 根据城市名获取城市,到市一级
     *
     * @param name
     * @return
     */
    public List<CityVo> getToCityLevelByName(String name) {
        List<City> list = cityMapper.selectAndListToCityLevel(name);
        return AppUtils.asVoList(list, CityVo.class);
    }


}
