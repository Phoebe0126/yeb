package com.xxxx.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IMenuRoleService;
import com.xxxx.server.service.IMenuService;
import com.xxxx.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组管理
 * @author: gouzi
 * @create: 2021-08-02 13:48
 **/
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {

    @Autowired
    IRoleService roleService;

    @Autowired
    IMenuService menuService;

    @Autowired
    IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role) {
        // 规范角色名称
        if (!role.getName().startsWith("ROLE_")) {
            role.setName("ROLE_" + role.getName());
        }
        if (roleService.save(role)) {
            return RespBean.success("添加角色成功！");
        }
        return RespBean.error("添加角色失败！");
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/role/{id}")
    public RespBean deleteRole(@PathVariable Integer id) {
        if (roleService.removeById(id)) {
            return RespBean.success("删除角色成功！");
        }
        return RespBean.error("删除角色失败!");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询菜单id列表")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMenusByRole(@PathVariable Integer rid) {
        QueryWrapper<MenuRole> menuRoleQueryWrapper = new QueryWrapper<>();
        menuRoleQueryWrapper.eq("rid", rid);
        return menuRoleService.list(menuRoleQueryWrapper)
                .stream()
                .map(MenuRole::getMid)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色信息")
    @PutMapping("/")
    public RespBean updateRole(Integer rid, Integer[] mids) {
        return roleService.updateRole(rid, mids);
    }
}
