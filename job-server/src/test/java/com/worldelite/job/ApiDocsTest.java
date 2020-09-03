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
        docsConfig.setProjectPath("./");
        docsConfig.setDocsPath("./api-docs");
        Docs.buildHtmlDocs(docsConfig);
    }

}
