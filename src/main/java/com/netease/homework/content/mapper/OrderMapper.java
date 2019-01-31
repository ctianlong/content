package com.netease.homework.content.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/28
 */
public interface OrderMapper {

    List<Long> getContentIdsByUserId(@Param("userId") Long userId);
}
