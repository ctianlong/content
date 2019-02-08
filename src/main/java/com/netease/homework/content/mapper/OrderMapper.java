package com.netease.homework.content.mapper;

import com.netease.homework.content.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/28
 */
public interface OrderMapper {

    List<Long> getContentIdsByUserId(@Param("userId") Long userId);

    Order getByUserIdAndContentId(@Param("userId") Long userId, @Param("id") Long id);

    List<Order> listByUserId(@Param("userId") Long uid);
}
