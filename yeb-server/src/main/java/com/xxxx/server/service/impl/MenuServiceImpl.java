package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.MenuMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    MenuMapper menuMapper;

    @Autowired
    KeyGenerator keyGenerator;

    @Override
    public List<Menu> getMenusById(Integer adminId) {
        List<Menu> menuList = menuMapper.getMenusById(adminId);
        return menuList;
    }

    public List<Menu> getMenusWithRoles() {
        return menuMapper.getMenusWithRoles();
    }

    @Override
    @Cacheable(value = "menu", keyGenerator = "keyGenerator")
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
