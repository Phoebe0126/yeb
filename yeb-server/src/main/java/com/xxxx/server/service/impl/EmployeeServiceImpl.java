package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.EmployeeMapper;
import com.xxxx.server.mapper.MailLogMapper;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.IEmployeeService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MailLogMapper mailLogMapper;


    @Override
    // 查询员工（分页）
    public RespPageBean getEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
        Page<Employee> page = new Page<>(currentPage, size);
        // 查询数据库
        IPage<Employee> employeeByPage = employeeMapper.getEmployeeByPage(page, employee, beginDateScope);

        RespPageBean respPageBean = new RespPageBean(employeeByPage.getTotal(), employeeByPage.getRecords());

        return respPageBean;
    }

    @Override
    // 获取要添加员工的新工号
    public RespBean getMaxWorkId() {
        QueryWrapper<Employee> employeeSelectMaps = new QueryWrapper<Employee>().select("max(workID)");
        List<Map<String, Object>> maps = employeeMapper.selectMaps(employeeSelectMaps);
        Integer maxWorkId  = Integer.parseInt(maps.get(0).get("max(workID)").toString()) + 1;
        return RespBean.success("获取工号成功", String.format("%08d", maxWorkId));
    }

    @Override
    // 添加员工
    public RespBean addEmp(Employee employee) {
        // 需要计算合同期限
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        // 计算还剩多少天
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        // 保留两位小数
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days / 360.00)));
        if (1==employeeMapper.insert(employee)) {
            // 发送消息
            Employee e = employeeMapper.getAllEmployees(employee.getId()).get(0);
            MailLog mailLog = new MailLog();
            String msgId = UUID.randomUUID().toString();

            // 消息入库
            mailLog.setMsgId(msgId);
            mailLog.setEid(e.getId());
            mailLog.setCount(0);
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setStatus(MailConstants.DELIVERING);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());

            mailLogMapper.insert(mailLog);

            // 发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,
                    MailConstants.MAIL_ROUTING_KEY_NAME, e, new CorrelationData(msgId));
            return RespBean.success("添加员工成功！");
        }
        return RespBean.error("添加员工失败！");
    }

    @Override
    public List<Employee> getAllEmployees(Integer id) {
        return employeeMapper.getAllEmployees(id);
    }

    @Override
    // 获取所有员工账套
    public RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size) {
        Page<Employee> page = new Page<>(currentPage, size);
        IPage<Employee> employeePages = employeeMapper.getEmployeeWithSalary(page);
        return new RespPageBean(employeePages.getTotal(), employeePages.getRecords());
    }


}
