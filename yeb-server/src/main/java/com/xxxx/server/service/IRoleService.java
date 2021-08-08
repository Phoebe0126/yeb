package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface IRoleService extends IService<Role> {

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    RespBean updateRole(Integer rid, Integer[] mids);
}
