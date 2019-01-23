package com.netease.homework.content.security.thymeleaf.dialect.processor;

import com.netease.homework.content.web.util.SessionUtils;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;

/**
 * @Description 扩展thymeleaf属性，添加当前用户对象属性
 * @Auther ctl
 * @Date 2018/8/1
 */
public class AuthenticationAttrProcessor extends AbstractTextChildModifierAttrProcessor {

    private static final int ATTR_PRECEDENCE = 1300;
    private static final String ATTR_NAME = "authentication";

    public AuthenticationAttrProcessor() {
        super(ATTR_NAME);
    }

    @Override
    protected String getText(Arguments arguments, Element element, String attributeName) {
        final String attributeValue = element.getAttributeValue(attributeName);
        if (attributeValue == null || attributeValue.trim().equals("")) {
            return null;
        }
        Object property = SessionUtils.getCurrentPrincipalProperty(attributeValue);
        if (property == null) {
            return null;
        }
        return property.toString();
    }

    @Override
    public int getPrecedence() {
        return ATTR_PRECEDENCE;
    }
}
