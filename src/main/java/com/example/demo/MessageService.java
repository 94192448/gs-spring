package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author yangzq80@gmail.com
 * @date 2021-04-23
 */
@Service
@Slf4j
public class MessageService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(int i){
        rabbitTemplate.convertAndSend("myqueue","test msg: no exchange "+i);
//        rabbitTemplate.convertAndSend("payment","myqueue","test msg:"+i);
    }

    @RabbitListener(queues = "myqueue")
    void rec(String m){
        log.info(m);
    }

    @Bean
    public Queue myQueue() {
        return new Queue("myqueue");
    }
}
