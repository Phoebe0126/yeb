package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id获取角色列表
     * @param adminId
     * @return
     */
    List<Role> getRolesByAdminId(Integer adminId);

}
