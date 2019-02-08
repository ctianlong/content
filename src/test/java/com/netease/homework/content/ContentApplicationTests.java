package com.netease.homework.content;

import com.netease.homework.content.entity.Content;
import com.netease.homework.content.mapper.ContentMapper;
import com.netease.homework.content.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        Map<Long, Content> cMap = contentMapper.listByContentIds(ids);
        System.out.println(cMap);
    }





}

