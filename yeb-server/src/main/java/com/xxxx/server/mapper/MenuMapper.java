package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户id查询菜单列表
     * @param adminId
     * @return
     */
    List<Menu> getMenusById(Integer adminId);


    /**
     * 查询带有角色的菜单列表
     * @return
     */
    List<Menu> getMenusWithRoles();
}
