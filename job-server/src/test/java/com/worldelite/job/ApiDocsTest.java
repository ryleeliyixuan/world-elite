package com.worldelite.job;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import org.junit.jupiter.api.Test;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class ApiDocsTest {

    @Test
    public void genApiDocs(){
        DocsConfig docsConfig = new DocsConfig();
        docsConfig.setProjectPath("/Users/yeguozhong/Desktop/gitLibrary/wolrd-elite/job-server");
        docsConfig.setDocsPath("/Users/yeguozhong/Desktop/gitLibrary/wolrd-elite/job-server/api-docs");
        Docs.buildHtmlDocs(docsConfig);
    }

}
