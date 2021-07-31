package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminLoginParams;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author: 陈玉婷
 * @create: 2021-07-28 18:15
 **/
@RestController
@Api("登录退出管理")
public class LoginController {

    @Autowired
    IAdminService adminService;

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public RespBean login(@RequestBody AdminLoginParams adminLoginParams, HttpServletRequest request) {
         return adminService.login(adminLoginParams.getUsername(), adminLoginParams.getPassword(), adminLoginParams.getCode(), request);
    }

    @ApiOperation(value = "获取用户对象")
    @GetMapping("/admin/info")
    public Admin adminInfo(Principal principal) {
        if (null == principal) {
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        // 不能泄露密码！
        admin.setPassword(null);
        return admin;
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出登录")
    public RespBean logout() {
        return RespBean.success("退出登录成功！");
    }
}
