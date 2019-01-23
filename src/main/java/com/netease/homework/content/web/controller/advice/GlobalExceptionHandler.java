package com.netease.homework.content.web.controller.advice;

import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 全局异常处理
 * @Auther ctl
 * @Date 2019/1/22
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 文件上传大小超出限制异常
     * @param req
     * @param e
     * @param redirectAttributes
     * @return
     */
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public JsonResponse handleError(HttpServletRequest req, MultipartException e, RedirectAttributes redirectAttributes) {
        return new JsonResponse().setCode(ResultCode.ERROR_BAD_PARAMETER).setError("文件大小不符合要求");
    }
}
