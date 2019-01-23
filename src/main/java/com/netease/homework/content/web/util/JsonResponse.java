package com.netease.homework.content.web.util;

public class JsonResponse {

    private boolean successful;

    private String error;

    private int code;

    private Object data;

    /**
     * 某些接口期望直接返回html片段，可以通过该参数设置
     *
//     * @see FreeMarkerTemplateRenderer#processTemplate(String,
//     *      com.netease.mag.reader.official.util.adapter.ValueSupplier)
     */
    private String html;

    public boolean isSuccessful() {
        return successful;
    }

    public JsonResponse setSuccessful() {
        this.successful = true;
        this.code = ResultCode.SUCCESS;
        return this;
    }

    public static JsonResponse instance() {
        return new JsonResponse().setSuccessful();
    }

    public String getError() {
        return error;
    }

    public JsonResponse setError(String error) {
        this.error = error;
        return this;
    }

    public int getCode() {
        return code;
    }

    public JsonResponse setCode(int code) {
        this.code = code;
        this.successful = code == ResultCode.SUCCESS;
        return this;
    }

    public Object getData() {
        return data;
    }

    public JsonResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public String getHtml() {
        return html;
    }

    public JsonResponse setHtml(String html) {
        this.html = html;
        return this;
    }

    public JsonResponse switchBetween(int code, Object data, String error) {
        this.data = data;
        this.code = code;
        this.successful = code == ResultCode.SUCCESS;
        if (!successful) {
            this.error = error;
        }
        return this;
    }
}

