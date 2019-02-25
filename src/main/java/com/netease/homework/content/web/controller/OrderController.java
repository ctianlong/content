package com.netease.homework.content.web.controller;

import com.netease.homework.content.entity.Content;
import com.netease.homework.content.entity.Order;
import com.netease.homework.content.entity.Shopcart;
import com.netease.homework.content.mapper.ContentMapper;
import com.netease.homework.content.mapper.OrderMapper;
import com.netease.homework.content.mapper.ShopcartMapper;
import com.netease.homework.content.web.controller.vo.OrderVo;
import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.ResultCode;
import com.netease.homework.content.web.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/28
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderMapper orderMapper;
    private final ContentMapper contentMapper;
    private final ShopcartMapper shopcartMapper;

    @Autowired
    public OrderController(OrderMapper orderMapper, ContentMapper contentMapper, ShopcartMapper shopcartMapper) {
        this.orderMapper = orderMapper;
        this.contentMapper = contentMapper;
        this.shopcartMapper = shopcartMapper;
    }

    @GetMapping("/loginuser/content/ids")
    public JsonResponse getContentIdsForLoginUser() {
        JsonResponse r = new JsonResponse();
        List<Long> ids = orderMapper.getContentIdsByUserId(SessionUtils.getCurrentPrincipalId());
        return r.setSuccessful().setData(ids);
    }

    @GetMapping("/loginuser/content/{cid}")
    public JsonResponse getContentForLoginUser(@PathVariable("cid") Long cid) {
        JsonResponse r = new JsonResponse();
        Long uid = SessionUtils.getCurrentPrincipalId();
        Order o = orderMapper.getByUserIdAndContentId(uid, cid);
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
        Map<Long, Content> contents = contentMapper.listByContentIds(cids, true);
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

    @Transactional
    @PostMapping("/shopcart/settle")
    public JsonResponse settleAccount(@RequestBody List<Shopcart> scs) {
        JsonResponse r = new JsonResponse();
        Long uid = SessionUtils.getCurrentPrincipalId();
        Assert.notNull(uid, "settleAccount, uid must not be null");
        Set<Long> cidsToBuy = new HashSet<>();
        for (Shopcart s: scs) {
            if (cidsToBuy.contains(s.getContentId()) || s.getAmount() < 1) {
                return r.setCode(ResultCode.ERROR_BAD_PARAMETER).setError("商品条目重复或购买数量有误");
            }
            cidsToBuy.add(s.getContentId());
        }
        // 检查购物车中是否存在该用户已经购买过的商品，已购买商品不允许再买
        List<Long> cidsBought = orderMapper.getContentIdsByUserId(uid);
        Set<Long> cidsNotAllowed = new HashSet<>();
        for (Long cid : cidsBought) {
            if (cidsToBuy.contains(cid)) {
                cidsNotAllowed.add(cid);
            }
        }
        if (!cidsNotAllowed.isEmpty()) {
            Map<Long, Content> contentMap = contentMapper.listByContentIds(cidsNotAllowed, true);
            StringBuilder sb = new StringBuilder("以下商品已购买，无法再次购买：");
            for (Long cid: cidsNotAllowed) {
                Content c = contentMap.get(cid);
                if (c == null) {
                    LOGGER.error("uid {}, contentId {} not exist in database include deleted", uid, cid);
                    return r.setCode(ResultCode.ERROR_UNKNOWN).setError("请求出错，请稍后再试");
                }
                sb.append("<br/>").append(c.getTitle());
            }
            return r.setCode(ResultCode.ERROR_ACTION_REPEATED).setError(sb.toString());
        }
        // 检查所购商品是否均未删除，同时组合商品信息
        Map<Long, Content> contentMap = contentMapper.listByContentIds(cidsToBuy, true);
        boolean hasDeleted = false;
        StringBuilder sb = new StringBuilder("以下商品已下架，无法购买：");
        List<Order> ordersToBuy = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (Shopcart s: scs) {
            Content c = contentMap.get(s.getContentId());
            if (c == null) {
                LOGGER.error("uid {}, contentId {} not exist in database include deleted", uid, s.getContentId());
                return r.setCode(ResultCode.ERROR_UNKNOWN).setError("请求出错，请稍后再试");
            }
            if (c.getStatus() == -1) {
                hasDeleted = true;
                sb.append("<br/>").append(c.getTitle());
            }
            if (!hasDeleted) {
                Order o = new Order();
                o.setContentId(s.getContentId());
                o.setTradeAmount(s.getAmount());
                o.setTradePrice(c.getPrice());
                o.setUserId(uid);
                o.setTradeTime(time);
                ordersToBuy.add(o);
            }
        }
        if (hasDeleted) {
            return r.setCode(ResultCode.ERROR_BAD_PARAMETER).setError(sb.toString());
        }
        // 结算，购买商品插入买家订单表
        orderMapper.insertBatch(ordersToBuy);
        // 更新所购买商品的已售出数量
        contentMapper.updateAmountBatch(scs);
        // 删除用户购物车内记录
        shopcartMapper.deleteByUid(uid);
        return r.setSuccessful();
    }

}
