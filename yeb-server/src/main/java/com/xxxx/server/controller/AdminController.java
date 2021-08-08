package com.xxxx.server.controller;


import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    @Autowired
    IRoleService roleService;

    @ApiOperation(value = "查询所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(@RequestParam String keywords) {
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation(value = "更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin) {
        if (adminService.updateById(admin)) {
            return RespBean.success("更新操作员成功！");
        }
        return RespBean.error("更新操作员失败！");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id) {
        if (adminService.removeById(id)) {
            return RespBean.success("删除操作员成功！");
        }
        return RespBean.error("删除操作员失败！");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.list();
    }


    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/role")
    public RespBean updateAdminWithRoles(Integer adminId, Integer[] rids) {
        return adminService.updateAdminWithRoles(adminId, rids);
    }
}
