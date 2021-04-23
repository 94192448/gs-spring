package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-31
 */
@RestController
@RequestMapping("/home")
public class HelloController {

    @GetMapping(value = "/greetings")
    public String greetings() {
        return "Greetings from Spring Boot!";
    }
}
