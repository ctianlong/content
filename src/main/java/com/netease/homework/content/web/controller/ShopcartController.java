package com.netease.homework.content.web.controller;

import com.netease.homework.content.entity.Order;
import com.netease.homework.content.entity.Shopcart;
import com.netease.homework.content.mapper.ContentMapper;
import com.netease.homework.content.mapper.OrderMapper;
import com.netease.homework.content.mapper.ShopcartMapper;
import com.netease.homework.content.web.util.JsonResponse;
import com.netease.homework.content.web.util.ResultCode;
import com.netease.homework.content.web.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/2/1
 */
@RestController
@RequestMapping("/api/shopcart")
public class ShopcartController {

    private final ShopcartMapper shopcartMapper;
    private final OrderMapper orderMapper;
    private final ContentMapper contentMapper;

    @Autowired
    public ShopcartController(ShopcartMapper shopcartMapper, OrderMapper orderMapper, ContentMapper contentMapper) {
        this.shopcartMapper = shopcartMapper;
        this.orderMapper = orderMapper;
        this.contentMapper = contentMapper;
    }

    @PostMapping("/loginuser/add")
    public JsonResponse addShopcartForLoginUser(Long id, Long amount) {
        JsonResponse r = new JsonResponse();
        if (amount < 1) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("购买数量必须大于0");
        }
        if (contentMapper.countById(id) == 0) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("商品不存在");
        }
        Long uid = SessionUtils.getCurrentPrincipalId();
        Order o = orderMapper.getByUserIdAndContentId(uid, id);
        if (o != null) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("您已购买该商品，无法添加到购物车");
        }
        Shopcart s = new Shopcart();
        s.setContentId(id);
        s.setAmount(amount);
        Assert.notNull(uid, "add to shopcart, uid must not be null");
        s.setUserId(uid);
        long t = System.currentTimeMillis();
        s.setCreateTime(t);
        s.setUpdateTime(t);
        // 下述操作可以分成先查再插入或更新
        int num = shopcartMapper.addOrUpdate(s);
        if (num != 1 && num != 2) {
            return r.setCode(ResultCode.ERROR_UNKNOWN).setError("加入购物车失败");
        }
        return r.setSuccessful();
    }
}
