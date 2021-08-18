package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * @author: 陈玉婷
 * @create: 2021-08-18 16:55
 **/
@RestController
public class AdminInfoController {

    @Autowired
    IAdminService adminService;

    @ApiOperation(value = "修改用户信息")
    @PutMapping("/admin/info")
    public RespBean updateAdminInfo(@RequestBody Admin admin, Authentication authentication) {
        if (adminService.updateById(admin)) {
            // 需要重新设置spring security的信息
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin.getUsername(), null, authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return RespBean.success("修改用户信息成功！");
        }
        return RespBean.error("修改用户信息失败!");
    }

    @ApiOperation(value = "修改用户密码")
    @PutMapping("/admin/pass")
    public RespBean updatePassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updatePassword(oldPass, pass, adminId);
    }
}
