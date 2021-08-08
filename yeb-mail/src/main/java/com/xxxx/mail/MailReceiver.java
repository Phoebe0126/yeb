package com.xxxx.mail;

import com.xxxx.server.pojo.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

    @RabbitListener(queues = "mail.welcome")
    public void handler(Employee employee) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(msg);
        try {
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

        } catch (MessagingException e) {
            e.printStackTrace();
            LOGGER.error("发送邮件失败！", e.getMessage());
        }
    }
}
