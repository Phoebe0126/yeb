package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.mapper.MenuRoleMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.MenuRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    MenuRoleMapper menuRoleMapper;

    @Override
    @Transactional
    public RespBean updateRole(Integer rid, Integer[] mids) {
        // 先全部清空角色的权限
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        // 然后再添加上权限信息
        if (null == mids || mids.length == 0) {
            return RespBean.success("更新角色权限菜单成功！");
        }
        menuRoleMapper.addMenus(rid, mids);
        return RespBean.success("更新角色权限菜单成功！");
    }
}
