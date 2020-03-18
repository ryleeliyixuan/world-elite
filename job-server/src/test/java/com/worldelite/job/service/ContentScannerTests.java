package com.worldelite.job.service;

import com.worldelite.job.JobApplication;
import com.worldelite.job.entity.ScanResult;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author yeguozhong yedaxia.github.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobApplication.class)
@WebAppConfiguration
public class ContentScannerTests {

    @Autowired
    private IContentScanner contentScanner;

    @Test
    public void test_scanText() throws Exception {
        ScanResult scanResult = contentScanner.scanText("ni234324sdfios@@#R#DFD新疆独立iuouiusdfsd");
        Assert.assertEquals(scanResult.getCode(), ScanResult.CODE_BlOCK);
        scanResult = contentScanner.scanText("一对一导师、五险一金、餐费补贴、交通便利");
        Assert.assertEquals(scanResult.getCode(), ScanResult.CODE_PASS);
        scanResult = contentScanner.scanText("负责游戏平台核心服务的研发，为亿级用户提供优质顺畅的服务和体验；本科及以上学历，热爱互联网技术，精通至少一门编程语言，包括但不仅限于：Java、C、C++、PHP、 Python、Go；");
        Assert.assertEquals(scanResult.getCode(), ScanResult.CODE_PASS);
    }
}
