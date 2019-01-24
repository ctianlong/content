package com.netease.homework.content.web.controller;

import com.netease.homework.content.web.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/24
 */
@Controller
public class ViewController {

    @GetMapping("/login")
    public String loginPage() {
        if (SessionUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

}
