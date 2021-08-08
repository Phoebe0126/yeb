package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 根据用户id查询菜单列表
     * @param adminId
     * @return
     */
    List<Menu> getMenusById(Integer adminId);

    /**
     * 查询带有角色的菜单
     * @return
     */
    List<Menu> getMenusWithRoles();

    /**
     * 查询所有菜单，包括子菜单
     * @return
     */
    List<Menu> getAllMenus();
}
