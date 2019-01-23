package com.netease.homework.content.security.thymeleaf.dialect.processor;

import com.netease.homework.content.web.util.SessionUtils;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractConditionalVisibilityAttrProcessor;

/**
 * @Description 扩展thymeleaf标签，用于页面元素的可见性显示，当前登录用户包含指定角色才显示
 * @Auther ctl
 * @Date 2018/8/1
 */
public class AuthorizeRoleAttrProcessor extends AbstractConditionalVisibilityAttrProcessor {

    private static final int ATTR_PRECEDENCE = 300;
    private static final String ATTR_NAME = "authorize-role";

    public AuthorizeRoleAttrProcessor() {
        super(ATTR_NAME);
    }

    @Override
    protected boolean isVisible(Arguments arguments, Element element, String attributeName) {
        final String roleStr = element.getAttributeValue(attributeName);
        return roleStr != null && !roleStr.trim().equals("") && SessionUtils.ifCurrentPrincipalInRole(roleStr);
    }

    @Override
    public int getPrecedence() {
        return ATTR_PRECEDENCE;
    }
}
