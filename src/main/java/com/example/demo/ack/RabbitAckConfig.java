package com.example.demo.ack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * @author yangzq80@gmail.com
 * @date 2021-04-26
 */
public class RabbitAckConfig {
    private static Logger logger = LoggerFactory.getLogger(RabbitAckConfig.class);

    /**
     * Queue 可以有4个参数
     *      1.队列名
     *      2.durable       持久化消息队列 ,rabbitmq重启的时候不需要创建新的队列 默认true
     *      3.auto-delete   表示消息队列没有在使用时将被自动删除 默认是false
     *      4.exclusive     表示该消息队列是否只在当前connection生效,默认是false
     */
    @Bean
    public Queue createAckQueue() {
        return new Queue(RabbitAckConstant.QUEUQ,true);
    }
}
