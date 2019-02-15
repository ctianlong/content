package com.netease.homework.content.mapper;

import com.netease.homework.content.entity.Content;
import com.netease.snailreader.common.component.intercept.InterceptableComponent;
import com.netease.snailreader.common.component.intercept.merge.annotation.MergeMap;
import com.netease.snailreader.common.component.intercept.prevalid.annotation.NotEmpty;
import com.netease.snailreader.common.component.intercept.split.annotation.SplitParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/28
 */
public interface ContentMapper extends InterceptableComponent {

    List<Content> listForAnonymous();

    List<Content> listBySellerId(@Param("userId") Long userId);

    int deleteById(@Param("id") Long id);

    int deleteByIdLogic(@Param("id") Long id);

    Content getBaseinfoById(@Param("id") Long id);

    Content getFullinfoById(@Param("id") Long id);

    int countById(@Param("id") Long id);

    int save(Content c);

    int update(Content c);

    @MergeMap
    @MapKey("id")
    Map<Long, Content> listByContentIds(@NotEmpty @SplitParam @Param("ids") Collection<Long> ids);
}
