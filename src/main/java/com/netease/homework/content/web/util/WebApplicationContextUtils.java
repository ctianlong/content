package com.netease.homework.content.web.util;

import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/22
 */
public class WebApplicationContextUtils {

    public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
        WebApplicationContext wac = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (wac == null) {
            final Enumeration<String> attrNames = servletContext.getAttributeNames();
            while (attrNames.hasMoreElements()) {
                final String attrName = attrNames.nextElement();
                final Object attrValue = servletContext.getAttribute(attrName);
                if (attrValue instanceof WebApplicationContext) {
                    if (wac != null) {
                        throw new IllegalStateException("No unique WebApplicationContext found: more than one " +
                                "DispatcherServlet registered with publishContext=true?");
                    }
                    wac = (WebApplicationContext) attrValue;
                }
            }
        }
        if (wac == null) {
            throw new IllegalStateException("No WebApplicationContext found: no ContextLoaderListener registered?");
        }
        return wac;
    }

}
