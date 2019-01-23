package com.netease.homework.content.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Description 虚拟 HttpServletRequest，只是用请求路径字符串和 HTTP 方法来反向构建 HttpServletRequest，
 * 并不是真的装饰器模式来做代码增强，参考 spring security
 * @Auther ctl
 * @Date 2019/1/22
 */
public class DummyHttpServletRequest extends HttpServletRequestWrapper {
    private static final HttpServletRequest UNSUPPORTED_REQUEST = (HttpServletRequest) Proxy.newProxyInstance(
            DummyHttpServletRequest.class.getClassLoader(),
            new Class[]{HttpServletRequest.class},
            new UnsupportedOperationExceptionInvocationHandler());

    private String contextPath = "";
    private String servletPath;
    private String pathInfo;
    private String queryString;
    private String method;
    private String requestURI;

    public DummyHttpServletRequest() {
        super(UNSUPPORTED_REQUEST);
    }

    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }
}

final class UnsupportedOperationExceptionInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        throw new UnsupportedOperationException(method + " is not supported");
    }
}
