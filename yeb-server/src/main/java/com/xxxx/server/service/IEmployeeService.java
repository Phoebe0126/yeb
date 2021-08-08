package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 查询员工（分页）
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    RespPageBean getEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * 获取工号
     * @return
     */
    RespBean getMaxWorkId();

    /**
     * 添加员工
     * @return
     */
    RespBean addEmp(Employee employee);

    /**
     *  查询所有员工
     * @param id
     * @return
     */
    List<Employee> getAllEmployees(Integer id);
}
