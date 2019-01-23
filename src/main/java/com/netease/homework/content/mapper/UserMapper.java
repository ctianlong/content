package com.netease.homework.content.mapper;

import com.netease.homework.content.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/15
 */
public interface UserMapper {

    User getByUsername(@Param("username") String username);

}
