package com.xxxx.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.RespPageBean;
import com.xxxx.server.pojo.Salary;
import com.xxxx.server.service.IEmployeeService;
import com.xxxx.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: gouzi
 * @create: 2021-08-13 17:48
 **/
@RestController
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    ISalaryService salaryService;

    @Autowired
    IEmployeeService employeeService;

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries() {
        return salaryService.list();
    }

    @ApiOperation(value = "获取员工账套")
    @GetMapping("/")
    public RespPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return employeeService.getEmployeeWithSalary(currentPage, size);
    }

    @ApiOperation(value = "修改员工账套")
    @PutMapping("/")
    public RespBean updateEmployeeSalaryType(@RequestParam Integer eid, @RequestParam Integer sid) {
        UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<Employee>().set("salaryId", sid).eq("id", eid);
        if (employeeService.update(updateWrapper)) {
            return RespBean.success("修改员工账套成功！");
        }
        return RespBean.error("修改员工账套失败！");

    }
}
