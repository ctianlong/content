package com.netease.homework.content.security.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description csrfToken管理，可以基于cookie或session
 * @Auther ctl
 * @Date 2018/8/2
 */
public interface CsrfTokenManager {

    CsrfToken generateToken(HttpServletRequest request);

    void saveToken(CsrfToken token, HttpServletRequest request,
                   HttpServletResponse response);

    CsrfToken loadToken(HttpServletRequest request);
}
