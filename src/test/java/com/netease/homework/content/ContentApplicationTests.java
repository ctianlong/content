package com.netease.homework.content;

import com.netease.homework.content.entity.Content;
import com.netease.homework.content.entity.Shopcart;
import com.netease.homework.content.mapper.ContentMapper;
import com.netease.homework.content.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContentMapper contentMapper;

	@Test
	public void contextLoads() {
        System.out.println(userMapper.getByUsername("seller"));
	}

	@Test
	public void testSplitParam() {
        Set<Long> ids = new HashSet<>();
        Map<Long, Content> cMap = contentMapper.listByContentIds(ids, true);
        System.out.println(cMap);
    }

    @Test
    public void testUpdateBatch() {
        List<Shopcart> s = new ArrayList<>();
        Shopcart sc = new Shopcart();
        sc.setContentId(2);
        sc.setAmount(5);
        Shopcart sc2 = new Shopcart();
        sc2.setContentId(6);
        sc2.setAmount(10);
        s.add(sc);
        s.add(sc2);
        int i = contentMapper.updateAmountBatch(s);
        System.out.println(i);
    }





}

