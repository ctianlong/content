package com.netease.homework.content.web.util;

import com.netease.homework.content.config.constant.Role;
import com.netease.homework.content.entity.User;
import com.netease.homework.content.security.interceptor.SecurityInterceptor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * @Description 用户会话工具（登录、权限相关）
 * @Auther ctl
 * @Date 2018/8/1
 */
public class SessionUtils {

    /**
     * 登录的session key
     */
    public static final String USER_SESSION_KEY = "SESSION_USER";
    /**
     * 记录上一次请求完整url的session key
     */
    public static final String LAST_URL_SESSION_KEY = "LAST_FULL_REQUEST_URL";

    public static void login(Object principal) {
        getSession(true).setAttribute(USER_SESSION_KEY, principal);
    }

    public static void logout() {
        HttpSession session = getSession(false);
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
//            session.invalidate();
        }
    }

    public static User getCurrentPrincipal() {
        HttpSession session = getSession(false);
        return session != null ? (User) session.getAttribute(USER_SESSION_KEY) : null;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isAuthenticated() {
        HttpSession session = getSession(false);
        return session != null && session.getAttribute(USER_SESSION_KEY) != null;
    }

    /**
     * 获取当前用户的角色
     * @return
     */
    public static Role getCurrentPrincipalRole() {
        User principal = getCurrentPrincipal();
        return principal == null ? null : principal.getRole();
    }

    /**
     * 判断当前用户是否拥有指定角色
     * @param role
     * @return
     */
    public static boolean ifCurrentPrincipalHasRole(Role role) {
        Role currentRole = getCurrentPrincipalRole();
        return currentRole != null && currentRole.equals(role);
    }

    public static boolean ifCurrentPrincipalHasRole(int intValue) {
        Role currentRole = getCurrentPrincipalRole();
        return currentRole != null && currentRole.is(intValue);
    }

    public static boolean ifCurrentPrincipalInRole(String displayName) {
        Role currentRole = getCurrentPrincipalRole();
        return currentRole != null && currentRole.is(displayName);
    }

    /**
     * 判断当前用户是否拥有指定角色集合中的至少一个角色
     * @param roles
     * @return
     */
    public static boolean ifCurrentPrincipalHasRole(EnumSet<Role> roles) {
        Role currentRole = getCurrentPrincipalRole();
        if (currentRole == null) {
            return false;
        }
        for (Role role: roles) {
            if (role.equals(currentRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前用户指定名称的属性
     * @param property
     * @return
     */
    public static Object getCurrentPrincipalProperty(String property) {
        User principal = getCurrentPrincipal();
        if (principal == null) {
            return null;
        }
        BeanWrapperImpl wrapper = new BeanWrapperImpl(principal);
        return wrapper.getPropertyValue(property);
    }

    /**
     * 保存当前请求的完整url
     */
    public static void saveFullRequestUrl() {
        HttpServletRequest request = getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(LAST_URL_SESSION_KEY, HttpUtils.getFullRequestUrl(request));
    }

    /**
     * 获取用户上一次访问的完整url
     *
     * @param remove 获取后是否移除
     * @return
     */
    public static String getLastFullRequestUrl(boolean remove) {
        HttpSession session = getSession(false);
        String lastUrl = null;
        if (session != null) {
            lastUrl = (String) session.getAttribute(LAST_URL_SESSION_KEY);
            if (remove && lastUrl != null) {
                session.removeAttribute(LAST_URL_SESSION_KEY);
            }
        }
        return lastUrl;
    }

    public static HttpServletRequest getRequest() {
        // 注意，此处获取的request是未经过multipartResolver的
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

}
