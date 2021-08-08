package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 为角色添加相应的菜单列表
     * @param rid
     * @param mids
     */
    void addMenus(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
