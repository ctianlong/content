package com.netease.homework.content;

import com.netease.homework.content.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentApplicationTests {

    @Autowired
    private UserMapper userMapper;

	@Test
	public void contextLoads() {
        System.out.println(userMapper.getByUsername("seller"));
	}



}

