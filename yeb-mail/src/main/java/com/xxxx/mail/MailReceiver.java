package com.xxxx.mail;

import com.rabbitmq.client.Channel;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * @author: 陈玉婷
 * @create: 2021-08-08 14:13
 **/
@Component
public class MailReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    RedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {

        // 获得发送过来的员工对象
        Employee employee = (Employee) message.getPayload();

        // 获得消息序号，为了后续的手动确认
        MessageHeaders headers = message.getHeaders();
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);

        // 获得msgId
        String msgId = (String) headers.getOrDefault("spring_returned_message_correlation", null);

        // 用redis缓存存储msgId
        HashOperations hashOperations = redisTemplate.opsForHash();

        try {
            // 判断是否确认过
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                LOGGER.error("消息已经被确认过========>{}", msgId);
                channel.basicAck(tag, false);
                return;
            }

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(msg);
            // 设置发件人
            messageHelper.setFrom(mailProperties.getUsername());
            // 收件人
            messageHelper.setTo(employee.getEmail());
            // 设置主题
            messageHelper.setSubject("入职欢迎邮件");
            // 设置发送日期
            messageHelper.setSentDate(new Date());

            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("posName", employee.getPosition().getName());
            context.setVariable("joblevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            // 利用模板引擎
            String mail = templateEngine.process("mail", context);
            // 第二个参数记得设置为true
            messageHelper.setText(mail, true);
            javaMailSender.send(msg);

            // 消息存入redis，避免二次确认
            hashOperations.put("mail_log", msgId, "OK");
            /**
             * 手动确认消息
             * tag: 消息序号
             * multiple: 是否确认多条
             */
            channel.basicAck(tag, false);

            LOGGER.info("消息发送成功=========>{}", msgId);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                channel.basicAck(tag, false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            LOGGER.error("发送邮件失败===========>{}", e.getMessage());
        }
    }
}
