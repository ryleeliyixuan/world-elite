package com.worldelite.job.doc;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

/**
 * Api Doc生成
 **/
public class GenerateApiDoc {
    public static void main(String[] args) {
        DocsConfig docsConfig = new DocsConfig();
        docsConfig.setProjectPath("./");
        docsConfig.setDocsPath("./api-docs");
        Docs.buildHtmlDocs(docsConfig);
    }
}
