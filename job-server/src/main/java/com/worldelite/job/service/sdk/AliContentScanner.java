package com.worldelite.job.service.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.worldelite.job.context.config.AliConfig;
import com.worldelite.job.service.IContentScanner;
import com.worldelite.job.entity.ScanResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class AliContentScanner implements IContentScanner {

    @Autowired
    private AliConfig aliConfig;

    @Override
    public ScanResult scanText(String content) throws Exception{

        DefaultAcsClient acsClient = createAcsClient();
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(aliConfig.getRegion());
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();

        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符
         */
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /**
         * 检测场景，文本垃圾检测传递：antispam
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        textScanRequest.setContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

        // 请务必设置超时时间
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);

        HttpResponse httpResponse = acsClient.doAction(textScanRequest);


        ScanResult scanResult = new ScanResult();

        if (httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getContent(), "UTF-8"));
            if (200 == scrResponse.getInteger("code")) {
                JSONArray taskResults = scrResponse.getJSONArray("data");
                JSONObject taskResult = taskResults.getJSONObject(0);

                if (200 == taskResult.getInteger("code")) {
                    JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                    JSONObject sceneResult =  sceneResults.getJSONObject(0);
                    String suggestion = sceneResult.getString("suggestion");
                    if("pass".equals(suggestion)){
                        scanResult.setCode(ScanResult.CODE_PASS);
                    }else if("block".equals(suggestion)){
                        scanResult.setCode(ScanResult.CODE_BlOCK);
                    }else{
                        scanResult.setCode(ScanResult.CODE_REVIEW);
                    }
                } else {
                    scanResult.setCode(ScanResult.CODE_ERROR);
                    scanResult.setMsg(taskResult.getString("msg"));
                }

            } else {
                scanResult.setCode(ScanResult.CODE_ERROR);
                scanResult.setMsg("detect not success. code:" + scrResponse.getInteger("code"));
            }
        } else {
            scanResult.setCode(ScanResult.CODE_ERROR);
            scanResult.setMsg("response not success. status:" + httpResponse.getStatus());
        }

        return scanResult;
    }

    private DefaultAcsClient createAcsClient() {
        DefaultProfile profile = DefaultProfile.getProfile(aliConfig.getRegion(), aliConfig.getAccessKeyId(), aliConfig.getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }
}
