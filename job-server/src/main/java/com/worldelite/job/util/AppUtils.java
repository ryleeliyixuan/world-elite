package com.worldelite.job.util;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.worldelite.job.anatation.ResumeScore;
import com.worldelite.job.context.AppContext;
import com.worldelite.job.context.MessageResource;
import com.worldelite.job.context.config.AliConfig;
import com.worldelite.job.context.config.DomainConfig;
import com.worldelite.job.form.PageForm;
import com.worldelite.job.vo.ResumeVo;
import com.worldelite.job.vo.VoConvertable;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
public class AppUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);

    private static final IdWorker ID_WORKER = new IdWorker();

    /**
     * 获取Oss资源路径
     *
     * @param relativeUrl
     * @return
     */
    public static String absOssUrl(String relativeUrl) {
        if (StringUtils.isEmpty(relativeUrl))
            return "";

        if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://")) {
            return relativeUrl;
        }
        if (relativeUrl.startsWith("/")) {
            relativeUrl = relativeUrl.substring(1);
        }
        DomainConfig domainConfig = getBean(DomainConfig.class);
        return domainConfig.getOss() + relativeUrl;
    }

    /**
     * 获取用户端网页链接
     *
     * @param relativeUrl
     * @return
     */
    public static String wholeWebUrl(String relativeUrl) {
        if (StringUtils.isEmpty(relativeUrl))
            return "";

        if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://")) {
            return relativeUrl;
        }
        if (relativeUrl.startsWith("/")) {
            relativeUrl = relativeUrl.substring(1);
        }
        DomainConfig domainConfig = getBean(DomainConfig.class);
        return domainConfig.getWebHost() + relativeUrl;
    }

    /**
     * 获取Oss资源路径
     *
     * @param relativeUrl
     * @return
     */
    public static String absOssInnerUrl(String relativeUrl) {
        if (relativeUrl == null)
            return "";

        DomainConfig domainConfig = getBean(DomainConfig.class);
        if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://")) {
            return relativeUrl.replace(domainConfig.getOss(), domainConfig.getInnerOss());
        }
        if (relativeUrl.startsWith("/")) {
            relativeUrl = relativeUrl.substring(1);
        }
        return domainConfig.getInnerOss() + relativeUrl;
    }

    /**
     * 获取资源的相对路径
     *
     * @param url
     * @return
     */
    public static String getOssKey(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        AliConfig ossConfig = getBean(AliConfig.class);
        if (!url.contains(ossConfig.getEndPoint())) {
            return url;
        }
        return url.substring(url.indexOf(ossConfig.getEndPoint()) + ossConfig.getEndPoint().length() + 1);
    }


    /**
     * 获取 Bean 对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return AppContext.getApplicationContext().getBean(clazz);
    }

    /**
     * 获取当前请求对象
     *
     * @return
     */
    public static HttpServletRequest request() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 消息对象
     *
     * @return
     */
    public static MessageResource message() {
        return getBean(MessageResource.class);
    }

    /**
     * get HttpServletResponse instance
     *
     * @return
     */
    public static HttpServletResponse response() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 把entity列表转成 vo 列表，调用 VoConvertable 接口的 asVo
     *
     * @param entityList
     * @param voClass
     * @param <VO>
     * @return
     */
    public static <VO extends VoConvertable> List<VO> asVoList(List entityList, Class<VO> voClass) {
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }
        List voList = new ArrayList(entityList.size());
        try {
            for (Object e : entityList) {
                VO vo = voClass.newInstance();
                voList.add(vo.asVo(e));
            }
            return voList;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 设置分页
     *
     * @param pageForm
     */
    public static void setPage(PageForm pageForm) {
        Page page = PageHelper.startPage(pageForm.getPage(), pageForm.getLimit());
        if (StringUtils.isNoneEmpty(pageForm.getSort())) {
            if (pageForm.getSort().startsWith("+")) {
                page.setOrderBy(String.format("%s asc", pageForm.getSort().substring(1)));
            } else if (pageForm.getSort().startsWith("-")) {
                page.setOrderBy(String.format("%s desc", pageForm.getSort().substring(1)));
            } else {
                page.setOrderBy(pageForm.getSort());
            }
        }
    }

    /**
     * 计算进度
     *
     * @param object
     * @return
     */
    public static Integer calCompleteProgress(Object object) {
        Class resumeClass = object.getClass();
        double total = 0, current = 0;
        try {
            for (Field field : resumeClass.getDeclaredFields()) {
                ResumeScore resumeScore = field.getAnnotation(ResumeScore.class);
                if (resumeScore != null) {
                    total += resumeScore.value();
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value != null) {
                        if (value instanceof Collection && CollectionUtils.isNotEmpty((Collection) value)) {
                            current += resumeScore.value();
                        } else {
                            current += resumeScore.value();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("cal resume progress error", ex);
        }
        return total == 0 ? 0 : (int) (current / total * 100);
    }

    /**
     * 生成id
     *
     * @return
     */
    public static long nextId() {
        return ID_WORKER.nextId();
    }
}
