package com.netease.homework.content.security.csrf;

import java.io.Serializable;

/**
 * @Description csrfToken信息
 * @Auther ctl
 * @Date 2018/8/2
 */
public class CsrfToken implements Serializable {

    private static final long serialVersionUID = 303290237352662081L;

    private final String token;

    private final String parameterName;

    private final String headerName;

    public CsrfToken(String headerName, String parameterName, String token) {
        this.headerName = headerName;
        this.parameterName = parameterName;
        this.token = token;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public String getToken() {
        return this.token;
    }

}
