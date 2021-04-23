package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangzq80@gmail.com
 * @date 2021-04-23
 */

@RestController
public class DemoController {

    @Autowired
    MessageService messageService;

    int i = 0;
    @RequestMapping("/")
    public String hello(){
        messageService.send(i);
        return "hello:"+i++;
    }
}
