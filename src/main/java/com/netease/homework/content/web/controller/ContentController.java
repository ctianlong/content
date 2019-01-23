package com.netease.homework.content.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/22
 */
@RestController
public class ContentController {

    @GetMapping("/test")
    public String index() {
        return "hello";
    }

}
