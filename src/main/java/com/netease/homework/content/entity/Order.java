package com.netease.homework.content.entity;

import java.io.Serializable;

/**
 * @Description 用户订单账务
 * @Auther ctl
 * @Date 2019/1/27
 */
public class Order implements Serializable {
    private static final long serialVersionUID = -3774255163808726271L;

    private long id;
    private long contentId;
    private long tradeAmount;
    private long userId;
    private double tradePrice;
    private long tradeTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(long tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(long tradeTime) {
        this.tradeTime = tradeTime;
    }
}
