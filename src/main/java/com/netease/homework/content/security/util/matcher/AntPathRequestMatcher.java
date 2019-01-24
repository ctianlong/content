package com.netease.homework.content.security.util.matcher;

import com.netease.homework.content.web.util.HttpUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Description 参考spring security源码
 * @Auther ctl
 * @Date 2019/1/19
 */
public final class AntPathRequestMatcher implements RequestMatcher {
    private static final String MATCH_ALL = "/**";

    private final Matcher matcher;
    private final String pattern;
    private final HttpMethod httpMethod;
    private final boolean caseSensitive;

    public AntPathRequestMatcher(String pattern) {
        this(pattern, null);
    }

    public AntPathRequestMatcher(String pattern, String httpMethod) {
        this(pattern, httpMethod, true);
    }

    public AntPathRequestMatcher(String pattern, String httpMethod, boolean caseSensitive) {
        Assert.hasText(pattern, "Pattern cannot be null or empty");
        this.caseSensitive = caseSensitive;

        if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
            pattern = MATCH_ALL;
            this.matcher = null;
        } else {
            boolean tmp = pattern.indexOf('?') == -1 && pattern.indexOf('{') == -1 && pattern.indexOf('}') == -1;
            if (tmp && pattern.indexOf('*') == -1) {
                // 完全相等精确匹配
                this.matcher = new ExactMatcher(
                        pattern.endsWith("/") ? pattern.substring(0, pattern.length() - 1) : pattern,
                        caseSensitive);
            } else if (tmp && pattern.endsWith(MATCH_ALL)
                    && pattern.indexOf('*') == pattern.length() - 2) {
                this.matcher = new SubpathMatcher(
                        pattern.substring(0, pattern.length() - 3), caseSensitive);
            } else {
                this.matcher = new SpringAntMatcher(pattern, caseSensitive);
            }
        }

        this.pattern = pattern;
        this.httpMethod = HttpUtils.parseHttpMethod(httpMethod);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return (this.httpMethod == null || this.httpMethod == HttpUtils.parseHttpMethod(request.getMethod()))
                && (this.pattern.equals(MATCH_ALL) || this.matcher.matches(HttpUtils.getRequestPath(request)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AntPathRequestMatcher that = (AntPathRequestMatcher) o;
        return caseSensitive == that.caseSensitive &&
                Objects.equals(pattern, that.pattern) &&
                httpMethod == that.httpMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, httpMethod, caseSensitive);
    }

    private interface Matcher {
        boolean matches(String path);
    }

    /**
     * 使用Spring提供的AntPathMatcher进行实现
     */
    private static class SpringAntMatcher implements Matcher {
        private final AntPathMatcher antPathMatcher;
        private final String pattern;

        private SpringAntMatcher(String pattern, boolean caseSensitive) {
            this.pattern = pattern;
            this.antPathMatcher = new AntPathMatcher();
            antPathMatcher.setCaseSensitive(caseSensitive);
            antPathMatcher.setTrimTokens(false);
        }

        @Override
        public boolean matches(String path) {
            return antPathMatcher.match(pattern, path);
        }
    }


    /**
     * 对于/**结尾的匹配模式，使用以下优化的匹配器，而不使用Spring提供的AntPathMatcher
     */
    private static class SubpathMatcher implements Matcher {
        private final String subpath;
        private final int length;
        private final boolean caseSensitive;

        private SubpathMatcher(String subpath, boolean caseSensitive) {
            this.subpath = caseSensitive ? subpath : subpath.toLowerCase();
            this.length = subpath.length();
            this.caseSensitive = caseSensitive;
        }

        @Override
        public boolean matches(String path) {
            if (!caseSensitive) {
                path = path.toLowerCase();
            }
            return path.startsWith(subpath) && (path.length() == length || path.charAt(length) == '/');
        }
    }

    /**
     * 路径完全相同匹配
     */
    private static class ExactMatcher implements Matcher {
        private final String exactPath;
        private final String exactPathWithSlash;
        private final boolean caseSensitive;

        private ExactMatcher(String exactPath, boolean caseSensitive) {
            this.exactPath = caseSensitive ? exactPath : exactPath.toLowerCase();
            this.exactPathWithSlash = this.exactPath + "/";
            this.caseSensitive = caseSensitive;
        }

        @Override
        public boolean matches(String path) {
            if (!caseSensitive) {
                path = path.toLowerCase();
            }
            return path.equals(exactPath) || path.equals(exactPathWithSlash);
        }
    }

}
