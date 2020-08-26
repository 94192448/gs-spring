package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangzq80@gmail.com
 * @date 2020-08-26
 */
@RestController
public class TestController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    int i=0;

    @GetMapping("/test")
    public String getValue(){
        ValueOperations<String,String> operations=redisTemplate.opsForValue();
        i+=1;
        operations.set("k","aaa"+i);
        System.out.println(operations.get("k"));

        return "from redis is:"+operations.get("k");
    }
}
