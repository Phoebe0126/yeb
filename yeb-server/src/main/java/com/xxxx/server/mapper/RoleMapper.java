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
    List<Role> getRolesByAdminId(Integer adminId);
}
