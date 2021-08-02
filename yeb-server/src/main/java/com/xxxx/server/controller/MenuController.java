package com.xxxx.server.controller;


import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/system")
public class MenuController {
    @Autowired
    IMenuService menuService;

    @ApiOperation(value = "根据id查询用户菜单")
    @GetMapping("/menu")
    public List<Menu> menu() {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (null == admin) {
            throw new AccessDeniedException("尚未登录，请登录！");
        }
        return menuService.getMenusById(admin.getId());
    }
}
