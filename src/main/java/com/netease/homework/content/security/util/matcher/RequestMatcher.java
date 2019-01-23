package com.netease.homework.content.security.util.matcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 请求匹配策略
 * @Auther ctl
 * @Date 2018/8/2
 */
public interface RequestMatcher {

    boolean matches(HttpServletRequest request);

}
