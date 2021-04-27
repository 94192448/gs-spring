package com.example.demo.ack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author yangzq80@gmail.com
 * @date 2021-04-26
 */
@Service
public class AckSender implements  RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnCallback {

    private static Logger logger = LoggerFactory.getLogger(AckSender.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    int i;
    public void send() {
        //设置回调对象
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
        //构建回调返回的数据
        CorrelationData correlationData = new CorrelationData("CorrelationData-"+i);
        i++;

        String content = "Content= " + new Date() + ", content= msg-" + i;
        this.rabbitTemplate.convertAndSend(RabbitAckConstant.QUEUQ,(Object) content,correlationData);
        logger.info("Confirm Send ok --- "+content);
    }

    /**
     * 消息回调确认方法
     * 如果消息没有到exchange,则confirm回调,ack=false
     * 如果消息到达exchange,则confirm回调,ack=true
     * @param
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean isSendSuccess, String s) {
        //logger.info("confirm--message:回调消息ID为: " + correlationData.getId());
        if (isSendSuccess) {
            //logger.info("confirm--message:消息发送成功"+correlationData.getId());
        } else {
            logger.info("confirm--message:消息发送失败" + s);
        }
    }

    /**
     * exchange到queue成功,则不回调return
     * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("return--message:" + new String(message.getBody()) + ",replyCode:" + replyCode
                + ",replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
    }
}
