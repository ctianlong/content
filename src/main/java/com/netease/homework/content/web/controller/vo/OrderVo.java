package com.netease.homework.content.web.controller.vo;

import com.netease.homework.content.config.constant.ImgType;
import com.netease.homework.content.entity.Order;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/2/7
 */
public class OrderVo extends Order {
    private static final long serialVersionUID = 7175144354683327307L;

    private String title;
    private String imgUrl;
    private ImgType imgType;

    public OrderVo() {}

    public OrderVo(Order order) {
        setId(order.getId());
        setContentId(order.getContentId());
        setTradeAmount(order.getTradeAmount());
        setTradePrice(order.getTradePrice());
        setTradeTime(order.getTradeTime());
        setUserId(order.getUserId());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ImgType getImgType() {
        return imgType;
    }

    public void setImgType(ImgType imgType) {
        this.imgType = imgType;
    }
}
