package com.netease.homework.content.security.csrf;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description 用于在页面表单上form中添加隐藏_csrf参数，仿照spring security，适用于Thymeleaf
 * https://www.cnblogs.com/zh94/p/8214149.html
 * @Auther ctl
 * @Date 2018/8/3
 */
public class CsrfRequestDataValueProcessor implements RequestDataValueProcessor {

//    private Pattern DISABLE_CSRF_TOKEN_PATTERN = Pattern.compile("(?i)^(GET|HEAD|TRACE|OPTIONS)$");
    private Pattern DISABLE_CSRF_TOKEN_PATTERN = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$", Pattern.CASE_INSENSITIVE);

    private String DISABLE_CSRF_TOKEN_ATTR = "DISABLE_CSRF_TOKEN_ATTR";

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {
        if (httpMethod != null && DISABLE_CSRF_TOKEN_PATTERN.matcher(httpMethod).matches()) {
            request.setAttribute(DISABLE_CSRF_TOKEN_ATTR, Boolean.TRUE);
        }
        else {
            request.removeAttribute(DISABLE_CSRF_TOKEN_ATTR);
        }
        return action;
    }

    @Override
    public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
        return value;
    }

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        if (Boolean.TRUE.equals(request.getAttribute(DISABLE_CSRF_TOKEN_ATTR))) {
            request.removeAttribute(DISABLE_CSRF_TOKEN_ATTR);
            return Collections.emptyMap();
        }

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            return Collections.emptyMap();
        }
        Map<String, String> hiddenFields = new HashMap<>(1, 1.0f);
        hiddenFields.put(token.getParameterName(), token.getToken());
        return hiddenFields;
    }

    @Override
    public String processUrl(HttpServletRequest request, String url) {
        return url;
    }
}
