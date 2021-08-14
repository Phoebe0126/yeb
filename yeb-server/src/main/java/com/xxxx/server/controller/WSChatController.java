package com.xxxx.server.controller;

import com.xxxx.server.pojo.Admin;
import com.xxxx.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: gouzi
 * @create: 2021-08-14 17:01
 **/
@RestController
public class WSChatController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "按照关键词获取所有用户")
    @GetMapping("/chat/admin")
    public List<Admin> getAllAdmins(String keywords) {
        return adminService.getAllAdmins(keywords);
    }
}
