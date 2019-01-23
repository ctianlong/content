package com.netease.homework.content.security.thymeleaf.dialect.processor;

import com.netease.homework.content.config.constant.Role;
import com.netease.homework.content.security.DummyHttpServletRequest;
import com.netease.homework.content.security.SecurityMetadataSource;
import com.netease.homework.content.web.util.SessionUtils;
import com.netease.homework.content.web.util.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.processor.attr.AbstractConditionalVisibilityAttrProcessor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.EnumSet;
import java.util.Map;

/**
 * @Description 扩展thymeleaf标签，用于页面元素的可见性显示，当前登录用户有权访问指定url时才显示
 * @Auther ctl
 * @Date 2018/8/1
 */
public class AuthorizeUrlAttrProcessor extends AbstractConditionalVisibilityAttrProcessor {

    private static final int ATTR_PRECEDENCE = 300;
    private static final String ATTR_NAME = "authorize-url";

    public AuthorizeUrlAttrProcessor() {
        super(ATTR_NAME);
    }

    @Override
    protected boolean isVisible(Arguments arguments, Element element, String attributeName) {
        String attributeValue = element.getAttributeValue(attributeName);
        if (attributeValue == null || attributeValue.trim().equals("")) {
            return false;
        }
        attributeValue = attributeValue.trim();

        int spaceIndex = attributeValue.indexOf(' ');
        String url =
                (spaceIndex < 0 ? attributeValue : attributeValue.substring(spaceIndex + 1)).trim();
        String method =
                (spaceIndex < 0 ? "GET" : attributeValue.substring(0, spaceIndex)).trim();

        IContext context = arguments.getContext();
        if (!(context instanceof IWebContext)) {
            throw new ConfigurationException(
                    "Thymeleaf execution context is not a web context , implementation of " +
                            IWebContext.class.getName());
        }
        IWebContext webContext = (IWebContext) context;

        HttpServletRequest request = webContext.getHttpServletRequest();
        ServletContext servletContext = webContext.getServletContext();
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        Map<String, SecurityMetadataSource> sources = ctx.getBeansOfType(SecurityMetadataSource.class);

        if (sources.size() == 0) {
            throw new TemplateProcessingException(
                    "No visible SecurityMetadataSource instance could be found in the application");
        }

        SecurityMetadataSource source = (SecurityMetadataSource) sources.values().toArray()[0];
        HttpServletRequest dummyRequest = createRequest(request.getContextPath(), url, method);
        EnumSet<Role> requiredRoles = source.getRequiredRoles(dummyRequest);
        return requiredRoles == null || SessionUtils.ifCurrentPrincipalHasRole(requiredRoles);
    }

    @Override
    public int getPrecedence() {
        return ATTR_PRECEDENCE;
    }

    private HttpServletRequest createRequest(String contextPath, String servletPath, String method) {
        DummyHttpServletRequest request = new DummyHttpServletRequest();
        request.setContextPath(contextPath);
        request.setServletPath(servletPath);
        request.setMethod(method);
        request.setRequestURI(contextPath + servletPath);
        return request;
    }

}
