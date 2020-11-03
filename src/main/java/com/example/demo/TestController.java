package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangzq80@gmail.com
 * @date 2020-09-02
 */
@RestController
@Slf4j
public class TestController {
    @GetMapping("/hello")
    public String hello(String id) {
        log.info("Hello..."+id);
        return "Backend hello"+id;
    }
}
