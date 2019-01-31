package com.netease.homework.content.mapper;

import com.netease.homework.content.entity.Content;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/28
 */
public interface ContentMapper {

    List<Content> listForAnonymous();


    List<Content> listBySellerId(@Param("userId") Long userId);

    int deleteById(@Param("id") Long id);

    Content getBaseinfoById(@Param("id") Long id);

    Content getFullinfoById(@Param("id") Long id);
}
