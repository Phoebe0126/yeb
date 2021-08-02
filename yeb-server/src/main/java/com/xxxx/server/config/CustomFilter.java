package com.xxxx.server.config;

import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.security.access.SecurityConfig;

import java.util.Collection;
import java.util.List;

/**
 * 根据请求的url获取并设置角色
 * @author: 陈玉婷
 * @create: 2021-08-01 14:36
 **/

@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private IMenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 获取请求的url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        // 获取所有菜单列表及相应的角色
        List<Menu> menusWithRoles = menuService.getMenusWithRoles();
        // 遍历所有菜单，找到该url对应的菜单
        for (Menu menu : menusWithRoles) {
            // 匹配到对应的菜单url
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                // 获取该菜单的角色列表
                String[] roleList = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(roleList);
            }
        }
        // 没找到，默认为登录即可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
