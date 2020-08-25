package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yangzq80@gmail.com
 * @date 2020-08-25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisClusterTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void getValue(){
        ValueOperations<String,String> operations=redisTemplate.opsForValue();
        operations.set("key1","aaa");
        System.out.println(operations.get("key1"));
    }
}
