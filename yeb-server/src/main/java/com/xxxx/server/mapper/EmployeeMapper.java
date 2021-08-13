package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxxx.server.pojo.Employee;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 查询员工表（分页）
     * @param page
     * @param employee
     * @param beginDateScope
     */
    IPage<Employee> getEmployeeByPage(Page<Employee> page, Employee employee, LocalDate[] beginDateScope);

    /**
     * 查询所有员工
     * @param id
     * @return
     */
    List<Employee> getAllEmployees(Integer id);

    IPage<Employee> getEmployeeWithSalary(Page<Employee> page);
}
