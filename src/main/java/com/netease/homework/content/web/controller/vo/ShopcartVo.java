package com.netease.homework.content.web.controller.vo;

import com.netease.homework.content.config.constant.ImgType;
import com.netease.homework.content.entity.Shopcart;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/2/15
 */
public class ShopcartVo extends Shopcart {
    private static final long serialVersionUID = 3264887754511990842L;

    private String title;
    private double price;
    private String imgUrl;
    private ImgType imgType;
    private int status;

    public ShopcartVo() {}

    public ShopcartVo(Shopcart sc) {
        setId(sc.getId());
        setAmount(sc.getAmount());
        setContentId(sc.getContentId());
        setUserId(sc.getUserId());
        setCreateTime(sc.getCreateTime());
        setUpdateTime(sc.getUpdateTime());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
