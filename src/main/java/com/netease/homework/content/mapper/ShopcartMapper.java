package com.netease.homework.content.mapper;

import com.netease.homework.content.entity.Shopcart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/2/1
 */
public interface ShopcartMapper {

    int addOrUpdate(Shopcart s);

    List<Shopcart> listItemByUserId(@Param("userId") Long userId);

    int deleteByUidAndCid(@Param("uid") Long uid,@Param("cid") Long cid);

    int updateItemAmount(@Param("uid") Long uid, @Param("contentId") long contentId,
                         @Param("updateTime") long updateTime,@Param("amount") long amount);

    int deleteByUid(@Param("uid") Long uid);
}
