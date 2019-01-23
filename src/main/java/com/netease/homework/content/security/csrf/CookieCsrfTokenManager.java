package com.netease.homework.content.security.csrf;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @Description 仿照spring security的 CookieCsrfTokenRepository 源码
 * @Auther ctl
 * @Date 2018/8/2
 */
public class CookieCsrfTokenManager implements CsrfTokenManager {

    public static final String DEFAULT_CSRF_COOKIE_NAME = "XSRF-TOKEN";

    public static final String DEFAULT_CSRF_PARAMETER_NAME = "_csrf";

    public static final String DEFAULT_CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    private String parameterName = DEFAULT_CSRF_PARAMETER_NAME;

    private String headerName = DEFAULT_CSRF_HEADER_NAME;

    private String cookieName = DEFAULT_CSRF_COOKIE_NAME;

    private final Method setHttpOnlyMethod;

    private boolean cookieHttpOnly;

    private String cookiePath;

    public CookieCsrfTokenManager() {
        this.setHttpOnlyMethod = ReflectionUtils.findMethod(Cookie.class, "setHttpOnly", boolean.class);
        if (this.setHttpOnlyMethod != null) {
            this.cookieHttpOnly = true;
        }
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new CsrfToken(this.headerName, this.parameterName, createNewToken());
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        String tokenValue = token == null ? "" : token.getToken();
        Cookie cookie = new Cookie(this.cookieName, tokenValue);
        cookie.setSecure(request.isSecure());
        if (this.cookiePath != null && !this.cookiePath.isEmpty()) {
            cookie.setPath(this.cookiePath);
        } else {
            cookie.setPath(this.getRequestContext(request));
        }
        if (token == null) {
            cookie.setMaxAge(0);
        }
        else {
            cookie.setMaxAge(-1);
        }
        if (cookieHttpOnly && setHttpOnlyMethod != null) {
            ReflectionUtils.invokeMethod(setHttpOnlyMethod, cookie, Boolean.TRUE);
        }

        response.addCookie(cookie);
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, this.cookieName);
        if (cookie == null) {
            return null;
        }
        String token = cookie.getValue();
        if (token == null || token.trim().equals("")) {
            return null;
        }
        return new CsrfToken(this.headerName, this.parameterName, token);
    }

    private String createNewToken() {
        return UUID.randomUUID().toString();
    }

    private String getRequestContext(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public void setCookieHttpOnly(boolean cookieHttpOnly) {
        if (cookieHttpOnly && setHttpOnlyMethod == null) {
            throw new IllegalArgumentException("Cookie will not be marked as HttpOnly because you are using a version of Servlet less than 3.0. NOTE: The Cookie#setHttpOnly(boolean) was introduced in Servlet 3.0.");
        }
        this.cookieHttpOnly = cookieHttpOnly;
    }

    public static CookieCsrfTokenManager withHttpOnlyFalse() {
        CookieCsrfTokenManager result = new CookieCsrfTokenManager();
        result.setCookieHttpOnly(false);
        return result;
    }

    public void setCookiePath(String path) {
        this.cookiePath = path;
    }

    public String getCookiePath() {
        return this.cookiePath;
    }


}
