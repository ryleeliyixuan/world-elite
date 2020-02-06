package com.worldelite.job.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.worldelite.job.entity.Country;
import com.worldelite.job.mapper.CountryMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.CountryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class CountryService {

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private DatabaseReader ipDbReader;

    /**
     * 获取所有国家代码
     * @return
     */
    public List<CountryVo> getAll(){
        List<Country> countryList = countryMapper.selectAndList(new Country());
        return AppUtils.asVoList(countryList, CountryVo.class);
    }

    /**
     * 根据ip获取国家代码
     *
     * @param ip
     * @return
     */
    public CountryVo getCountryByIp(String ip){
        try{
            CityResponse response = ipDbReader.city(InetAddress.getByName(ip));
            Country options = new Country();
            options.setCountryCode(response.getCountry().getIsoCode());
            List<Country> countryList = countryMapper.selectAndList(options);
            return CollectionUtils.isNotEmpty(countryList)? new CountryVo().asVo(countryList.get(0)): null;
        }catch ( IOException | GeoIp2Exception ex){
            log.error("getCountryCode Error", ex);
        }
        return null;
    }
}
