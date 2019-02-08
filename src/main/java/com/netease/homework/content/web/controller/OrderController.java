package com.netease.homework.content.web.controller;

import com.netease.homework.content.entity.Content;
import com.netease.homework.content.entity.Order;
import com.netease.homework.content.mapper.ContentMapper;
import com.netease.homework.content.mapper.OrderMapper;
import com.netease.homework.content.web.controller.vo.OrderVo;
import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.ResultCode;
import com.netease.homework.content.web.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/28
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderMapper orderMapper;
    private final ContentMapper contentMapper;

    @Autowired
    public OrderController(OrderMapper orderMapper, ContentMapper contentMapper) {
        this.orderMapper = orderMapper;
        this.contentMapper = contentMapper;
    }

    @GetMapping("/loginuser/content/ids")
    public JsonResponse getContentIdsForLoginUser() {
        JsonResponse r = new JsonResponse();
        List<Long> ids = orderMapper.getContentIdsByUserId(SessionUtils.getCurrentPrincipalId());
        return r.setSuccessful().setData(ids);
    }

    @GetMapping("/loginuser/content/{id}")
    public JsonResponse getContentForLoginUser(@PathVariable("id") Long id) {
        JsonResponse r = new JsonResponse();
        Long uid = SessionUtils.getCurrentPrincipalId();
        Order o = orderMapper.getByUserIdAndContentId(uid, id);
        return o == null ? r.setCode(ResultCode.ERROR_UNKNOWN).setError("订单商品不存在")
                : r.setSuccessful().setData(o);
    }

    @GetMapping("/item/list")
    public JsonResponse getOrderItemList() {
        JsonResponse r = new JsonResponse();
        Long uid = SessionUtils.getCurrentPrincipalId();
        List<Order> orders = orderMapper.listByUserId(uid);
        Set<Long> cids = new HashSet<>();
        for (Order o : orders) {
            cids.add(o.getContentId());
        }
        Map<Long, Content> contents = contentMapper.listByContentIds(cids);
        List<OrderVo> list = new ArrayList<>();
        for (Order o : orders) {
            OrderVo ov = new OrderVo(o);
            Content c = contents.get(o.getContentId());
            if (c != null) {
                ov.setImgType(c.getImgType());
                ov.setImgUrl(c.getImgUrl());
                ov.setTitle(c.getTitle());
            }
            list.add(ov);
        }
        return r.setSuccessful().setData(list);
    }

}
