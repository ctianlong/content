package com.netease.homework.content.security;

import com.netease.homework.content.config.constant.Role;
import com.netease.homework.content.security.util.matcher.AntPathRequestMatcher;
import com.netease.homework.content.security.util.matcher.RequestMatcher;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Description 存储访问控制元数据，以后考虑将3个url列表合在一起
 * @Auther ctl
 * @Date 2019/1/18
 */
public class SecurityMetadataSource {

    /**
     * key为需要鉴权的url匹配器，value为访问该url需要的角色集合（用户拥有其中一个角色即可访问）
     */
    private final Map<RequestMatcher, EnumSet<Role>> requireAnyRoleUrl = new LinkedHashMap<>();
    /**
     * 匿名即可访问的url
     */
    private final List<RequestMatcher> anonymousUrl = new ArrayList<>();
    /**
     * 登录即可访问的url
     */
    private final List<RequestMatcher> onlyRequireLoginUrl = new ArrayList<>();

    public EnumSet<Role> getRequiredRoles(HttpServletRequest request) {
        for(Map.Entry<RequestMatcher, EnumSet<Role>> entry: requireAnyRoleUrl.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean anoymousAccessAllow(HttpServletRequest request) {
        for (RequestMatcher matcher: anonymousUrl) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public boolean loginAccessAllow(HttpServletRequest request) {
        for (RequestMatcher matcher: onlyRequireLoginUrl) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public AuthorizedUrl antMatchers(String... antPatterns) {
        return new AuthorizedUrl(RequestMatchers.antMatchers(antPatterns));
    }

    public AuthorizedUrl antMatchers(HttpMethod method, String... antPatterns) {
        return new AuthorizedUrl(RequestMatchers.antMatchers(method, antPatterns));
    }

    public AuthorizedUrl antMatchers(HttpMethod method) {
        return new AuthorizedUrl(RequestMatchers.antMatchers(method, "/**"));
    }

    public class AuthorizedUrl {
        private List<? extends RequestMatcher> requestMatchers;
        private boolean not;

        private AuthorizedUrl(List<? extends RequestMatcher> requestMatchers) {
            this.requestMatchers = requestMatchers;
        }

        protected List<? extends RequestMatcher> getRequestMatchers() {
            return this.requestMatchers;
        }

        public AuthorizedUrl not() {
            this.not = true;
            return this;
        }

        public SecurityMetadataSource hasRole(Role role) {
            return hasAnyRole(role);
        }

        public SecurityMetadataSource hasAnyRole(Role... roles) {
            EnumSet<Role> needRoles = EnumSet.noneOf(Role.class);
            for (Role r: roles) {
                needRoles.add(r);
            }
            for (RequestMatcher matcher: requestMatchers) {
                requireAnyRoleUrl.put(matcher, needRoles);
            }
            return SecurityMetadataSource.this;
        }

        public SecurityMetadataSource anonymous() {
            anonymousUrl.addAll(requestMatchers);
            return SecurityMetadataSource.this;
        }

        public SecurityMetadataSource authenticated() {
            onlyRequireLoginUrl.addAll(requestMatchers);
            return SecurityMetadataSource.this;
        }

    }

    private static final class RequestMatchers {

        public static List<RequestMatcher> antMatchers(HttpMethod httpMethod,
                                                       String... antPatterns) {
            String method = httpMethod == null ? null : httpMethod.toString();
            List<RequestMatcher> matchers = new ArrayList<>();
            for (String pattern : antPatterns) {
                matchers.add(new AntPathRequestMatcher(pattern, method));
            }
            return matchers;
        }

        public static List<RequestMatcher> antMatchers(String... antPatterns) {
            return antMatchers(null, antPatterns);
        }

//        public static List<RequestMatcher> regexMatchers(HttpMethod httpMethod,
//                                                         String... regexPatterns) {
//            String method = httpMethod == null ? null : httpMethod.toString();
//            List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();
//            for (String pattern : regexPatterns) {
//                matchers.add(new RegexRequestMatcher(pattern, method));
//            }
//            return matchers;
//        }
//
//        public static List<RequestMatcher> regexMatchers(String... regexPatterns) {
//            return regexMatchers(null, regexPatterns);
//        }

        private RequestMatchers() {
        }
    }


}
