package com.netease.homework.content.security.thymeleaf.dialect;

import com.netease.homework.content.security.thymeleaf.dialect.processor.AuthenticationAttrProcessor;
import com.netease.homework.content.security.thymeleaf.dialect.processor.AuthorizeRoleAttrProcessor;
import com.netease.homework.content.security.thymeleaf.dialect.processor.AuthorizeUrlAttrProcessor;
import com.netease.homework.content.web.util.SessionUtils;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description 自定义thymeleaf标签，登录、认证相关
 * @Auther ctl
 * @Date 2018/8/1
 */
public class SecurityDialect extends AbstractDialect implements IExpressionEnhancingDialect {

    private static final String DEFAULT_PREFIX = "isec";

    public static final String AUTHENTICATION_EXPRESSION_OBJECT_NAME = "authentication";

    @Override
    public String getPrefix() {
        return DEFAULT_PREFIX;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        final Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new AuthenticationAttrProcessor());
        processors.add(new AuthorizeRoleAttrProcessor());
        processors.add(new AuthorizeUrlAttrProcessor());
        return processors;
    }


    @Override
    public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
        final Map<String,Object> objects = new HashMap<>(1, 1.0f);
        objects.put(AUTHENTICATION_EXPRESSION_OBJECT_NAME, SessionUtils.getCurrentPrincipal());
        return objects;
    }
}
