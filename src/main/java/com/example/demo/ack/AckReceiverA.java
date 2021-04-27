package com.example.demo.ack;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author yangzq80@gmail.com
 * @date 2021-04-26
 */
@Component
@RabbitListener(queues = RabbitAckConstant.QUEUQ)
public class AckReceiverA {
    private static Logger logger = LoggerFactory.getLogger(AckReceiverA.class);
    @RabbitHandler
    public void process(String str, Channel channel, Message message)  {


        logger.info("ReceiverA : " + str +", receive date:"+ new Date());

        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            logger.info("消息消费A成功！");
        } catch (Exception e) {
            logger.error("消息消费A失败:"+ e.getMessage(),e);
            //丢弃这条消息
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
