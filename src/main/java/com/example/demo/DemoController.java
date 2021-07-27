package com.example.demo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yangzq80@gmail.com
 * @date 2021-07-27
 */
@RestController
public class DemoController {

    AtomicInteger c1 = new AtomicInteger(0);

    @RequestMapping("/")
    public String hello() {
        return "hello world";
    }

    @RequestMapping("/cost/{cost}")
    public String cost(@PathVariable long cost) throws InterruptedException {

        if (cost != 0) {

            Thread.sleep(cost);
        }
        return c1.addAndGet(1) + " cost:" + cost;
    }

}
