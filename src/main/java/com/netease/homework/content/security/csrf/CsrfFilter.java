package com.netease.homework.content.security.csrf;

import com.netease.homework.content.security.util.matcher.RequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @Description 仿照spring security的 csrfFilter 源码
 * @Auther ctl
 * @Date 2018/8/2
 */
public class CsrfFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(CsrfFilter.class);

    public static final RequestMatcher DEFAULT_CSRF_MATCHER = new DefaultRequiresCsrfMatcher();

    private RequestMatcher requireCsrfMatcher = DEFAULT_CSRF_MATCHER;

    private final CsrfTokenManager csrfTokenManager;

    public CsrfFilter(CsrfTokenManager csrfTokenManager) {
        this.csrfTokenManager = csrfTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setAttribute(HttpServletResponse.class.getName(), response);
        CsrfToken csrfToken = this.csrfTokenManager.loadToken(request);
        final boolean missingToken = csrfToken == null;
        if (missingToken) {
            csrfToken = this.csrfTokenManager.generateToken(request);
            this.csrfTokenManager.saveToken(csrfToken, request, response);
        }
        request.setAttribute(CsrfToken.class.getName(), csrfToken);
        request.setAttribute(csrfToken.getParameterName(), csrfToken);

        if (!this.requireCsrfMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String actualToken = request.getHeader(csrfToken.getHeaderName());
        if (actualToken == null) {
            actualToken = request.getParameter(csrfToken.getParameterName());
        }
        if (!csrfToken.getToken().equals(actualToken)) {
            if (missingToken) {
               response.sendError(HttpServletResponse.SC_FORBIDDEN, "missing csrfToken");
            }
            else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "invalid csrfToken");
            }
            return;
        }
        filterChain.doFilter(request, response);
    }


    public void setRequireCsrfMatcher(RequestMatcher requireCsrfMatcher) {
        this.requireCsrfMatcher = requireCsrfMatcher;
    }

    /**
     * 对CSRF，根据请求方法匹配
     */
    private static final class DefaultRequiresCsrfMatcher implements RequestMatcher {

        private final HashSet<String> allowedMethods = new HashSet<>(
                Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

        @Override
        public boolean matches(HttpServletRequest request) {
            return !this.allowedMethods.contains(request.getMethod());
        }

    }
}
