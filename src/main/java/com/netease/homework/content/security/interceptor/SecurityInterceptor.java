package com.netease.homework.content.security.interceptor;

import com.netease.homework.content.config.constant.Role;
import com.netease.homework.content.entity.User;
import com.netease.homework.content.security.SecurityMetadataSource;
import com.netease.homework.content.web.util.SessionUtils;
import com.netease.snailreader.common.util.HttpUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.EnumSet;

/**
 * @Description 认证拦截器，检查用户是否登录，用户鉴权（判断是否有访问当前请求的权限）
 * @Auther ctl
 * @Date 2018/8/1
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    private final SecurityMetadataSource securityMetadataSource;

    public SecurityInterceptor(SecurityMetadataSource source) {
        this.securityMetadataSource = source;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断该请求是否匿名可访问
        if (securityMetadataSource.anoymousAccessAllow(request)) {
            return true;
        }
        // 若未登录，拒绝访问
        if (!SessionUtils.isAuthenticated()) {
            if (HttpUtil.isAjaxRequest(request)) { // 若请求为ajax，直接返回401
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                // 页面请求，发起登录，保存之前访问的url，用于登录成功后跳转
                SessionUtils.saveFullRequestUrl();
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return false;
        }
        // 已登录，且该请求为只需登录即可访问的请求
        if (securityMetadataSource.loginAccessAllow(request)) {
            return true;
        }
        // 其它请求需要具体判断所需角色
        EnumSet<Role> requiredRoles = securityMetadataSource.getRequiredRoles(request);
        if (requiredRoles == null) {
            return true;
        }
        if (!SessionUtils.ifCurrentPrincipalHasRole(requiredRoles)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

}
