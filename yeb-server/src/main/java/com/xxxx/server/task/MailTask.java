package com.xxxx.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.MailConstants;
import com.xxxx.server.pojo.MailLog;
import com.xxxx.server.service.IEmployeeService;
import com.xxxx.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: 陈玉婷
 * @create: 2021-08-08 17:58
 **/
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 10秒执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask() {
        // 查询正在发送的消息
        QueryWrapper<MailLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", MailConstants.DELIVERING)
                    .lt("tryTime", LocalDateTime.now());

        List<MailLog> list = mailLogService.list(queryWrapper);

        // 遍历消息列表
        list.forEach((mailLog -> {
            // 判断重试次数是否满足条件，超过3次则设置为发送失败
            if (MailConstants.MAX_TRY_COUNT <= mailLog.getCount()) {
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", MailConstants.FIALURE).eq("msgId", mailLog.getMsgId()));
                return;
            }
            mailLogService.update(
                    new UpdateWrapper<MailLog>()
                            .set("count", mailLog.getCount() + 1)
                            .set("tryTime", LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                            .set("updateTime", LocalDateTime.now())
            );
            Employee employee = employeeService.getAllEmployees(mailLog.getEid()).get(0);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME
            , employee, new CorrelationData(mailLog.getMsgId()));
        }));
    }
}
