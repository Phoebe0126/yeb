package com.xxxx.server.controller;


import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {
    @Autowired
    IMenuService menuService;

    @ApiOperation(value = "根据id查询用户菜单")
    @GetMapping("/menu")
    public RespBean menu() {
        return menuService.getMenusById();
    }
}
