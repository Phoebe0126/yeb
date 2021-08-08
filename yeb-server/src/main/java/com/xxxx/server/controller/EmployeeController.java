package com.xxxx.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.xxxx.server.pojo.*;
import com.xxxx.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    IEmployeeService employeeService;

    @Autowired
    IPoliticsStatusService politicsStatusService;

    @Autowired
    INationService nationService;

    @Autowired
    IPositionService positionService;

    @Autowired
    IJoblevelService joblevelService;

    @Autowired
    IDepartmentService departmentService;

    @ApiOperation(value = "查询员工（分页）")
    @GetMapping("/")
    public RespPageBean getEmployee(@RequestParam(defaultValue = "1") Integer currentPage,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    Employee employee, LocalDate[] beginDateScope) {

        return employeeService.getEmployee(currentPage, size, employee, beginDateScope);

    }

    @ApiOperation(value = "查询政治面貌")
    @GetMapping("/politicsstatus")
    public List<PoliticsStatus> getPoliticStatus() {
        return politicsStatusService.list();
    }

    @ApiOperation(value = "查询所有民族")
    @GetMapping("/nations")
    public List<Nation> getNations() {
        return nationService.list();
    }

    @ApiOperation(value = "查询所有职位")
    @GetMapping("/positions")
    public List<Position> getPositions() {
        return positionService.list();
    }

    @ApiOperation(value = "查询所有职称")
    @GetMapping("/joblevels")
    public List<Joblevel> getJobLevels() {
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/deps")
    public List<Department> getDeps() {
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value = "获取工号")
    @GetMapping("/maxWorkID")
    public RespBean getMaxWorkId() {
        return employeeService.getMaxWorkId();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody  Employee employee) {
        return employeeService.addEmp(employee);
    }

    @ApiOperation(value = "修改员工")
    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee) {
        if (employeeService.updateById(employee)) {
            return RespBean.success("修改员工成功！");
        }
        return RespBean.error("修改员工失败！");
    }

    @ApiOperation(value = "删除员工")
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id) {
        if (employeeService.removeById(id)) {
            return RespBean.success("删除员工成功！");
        }
        return RespBean.error("删除员工失败！");
    }
    
    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export", produces = "application/octet-stream")
    public void exportData(HttpServletResponse response) {
        List<Employee> list = employeeService.getAllEmployees(null);
        // 创建导出参数
        ExportParams exportParams = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        // 创建workbook
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Employee.class, list);
        ServletOutputStream out = null;
        // 获取输出流
        try {
            out = response.getOutputStream();
            // 流形式
            response.setHeader("content-type", "application/octet-stream");
            // 防止中文乱码
            response.setHeader("content-disposition", "attachment;filename="+ URLEncoder.encode("员工表.xls", "UTF-8"));

            // 导出
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @ApiOperation(value = "导入员工数据")
    @PostMapping("/import")
    public RespBean importData(MultipartFile file) {
        ImportParams importParams = new ImportParams();
        // 去掉标题
        importParams.setTitleRows(1);

        try {
            List<Nation> nationList = nationService.list();
            List<Position> positionList = positionService.list();
            List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
            List<Joblevel> joblevelList = joblevelService.list();
            List<Department> departmentList = departmentService.list();

            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, importParams);

            list.forEach(
                e -> {
                    // 重写hashcode和equals方法，让对象的值用name来代替，不需要重复查询数据库
                    e.setNationId(nationList.get(nationList.indexOf(new Nation(e.getNation().getName()))).getId());
                    e.setPosId(positionList.get(positionList.indexOf(new Position(e.getPosition().getName()))).getId());
                    e.setPoliticId(politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(e.getPoliticsStatus().getName()))).getId());
                    e.setJobLevelId(joblevelList.get(joblevelList.indexOf(new Joblevel(e.getJoblevel().getName()))).getId());
                    e.setDepartmentId(departmentList.get(departmentList.indexOf(new Department(e.getDepartment().getName()))).getId());
                }
            );
            if (employeeService.saveBatch(list)) {
                return RespBean.success("导入员工数据成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入员工数据失败！");
    }



}
