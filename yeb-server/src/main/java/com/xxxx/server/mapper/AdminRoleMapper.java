package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 操作员增加角色
     * @param adminId
     * @param rids
     * @return
     */
    Integer insertRoles(@Param("adminId") Integer adminId, @Param("rids") Integer[] rids);
}
