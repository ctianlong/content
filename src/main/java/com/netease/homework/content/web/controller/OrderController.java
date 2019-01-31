package com.netease.homework.content.web.controller;

import com.netease.homework.content.entity.User;
import com.netease.homework.content.mapper.OrderMapper;
import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/28
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @GetMapping("/loginuser/content/ids")
    public JsonResponse getContentIdsForLoginUser() {
        JsonResponse response = new JsonResponse();
        List<Long> ids = orderMapper.getContentIdsByUserId(SessionUtils.getCurrentPrincipalId());
        return response.setSuccessful().setData(ids);
    }

}
