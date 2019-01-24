package com.netease.homework.content.web.controller;

import com.netease.homework.content.entity.User;
import com.netease.homework.content.mapper.UserMapper;
import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.ResultCode;
import com.netease.homework.content.web.util.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/23
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {

    private final UserMapper userMapper;

    @Autowired
    public CommonController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/user/login")
    public JsonResponse login(String username, String passwordMd5) {
        JsonResponse response = new JsonResponse();
        if (SessionUtils.isAuthenticated()) {
            return response.setCode(ResultCode.LOGIN.AUTHENTICATION_FAILED).setError("用户已登录");
        }
        if (StringUtils.isAnyBlank(username, passwordMd5)) {
            return response.setCode(ResultCode.ERROR_BAD_PARAMETER).setError("请求参数错误");
        }
        User user = userMapper.getByUsername(username);
        if (user == null || !passwordMd5.equals(user.getPasswordMd5())) {
            return response.setCode(ResultCode.LOGIN.AUTHENTICATION_FAILED).setError("用户名或密码错误");
        }
        user.setPasswordMd5(null);
        SessionUtils.login(user);
        String preUrl = SessionUtils.getLastFullRequestUrl(true);
        if (preUrl == null) {
            preUrl = SessionUtils.getContextPath() + "/";
        }
        Map<String, Object> data = new HashMap<>();
        data.put("redirectUrl", preUrl);
        return response.setSuccessful().setData(data);
    }

    @PostMapping("/user/logout")
    public JsonResponse logout() {
        SessionUtils.logout();
        return JsonResponse.instance();
    }


}
