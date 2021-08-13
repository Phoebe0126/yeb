package com.xxxx.server.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnsCallback;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.pojo.MailConstants;
import com.xxxx.server.pojo.MailLog;
import com.xxxx.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: 陈玉婷
 * @create: 2021-08-08 17:26
 **/
@Configuration
@Component
public class RabbitMQConfig implements ConfirmCallback, ReturnsCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Bean
    public Queue queue() {
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        // 邮件发送队列到交换机，并匹配到路由key
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }

    @Override
    /**
     * 消息确认回调，确认消息是否到达broker
     * data：消息的唯一表示
     * ack：确认结果
     * cause：失败原因
     */
    public void confirm(CorrelationData data, boolean ack, String cause) {
        assert data != null;
        String msgId = data.getId();
        if(ack) {
            LOGGER.info("{}===================>消息发送成功", msgId);
            // 修改消息状态
            mailLogService.update(new UpdateWrapper<MailLog>().set("status", MailConstants.SUCCESS).eq("msgId", msgId));
        } else {
            LOGGER.error("{}===================>消息发送失败", msgId);
        }
    }

    @Override
    /**
     * 消息发送失败回调，比如router不到queue时的回调
     * returnedMessage: 消息
     */
    public void returnedMessage(ReturnedMessage returnedMessage) {
        LOGGER.error("{}===================>消息发送给queue时失败", returnedMessage.getMessage());
    }
}
