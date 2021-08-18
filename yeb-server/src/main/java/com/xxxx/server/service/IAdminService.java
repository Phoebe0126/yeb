package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gouzi
 * @since 2021-07-28
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 根据用户名返回用户信息
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据用户id返回角色列表
     * @param id
     * @return
     */
    List<Role> getRolesByAdminId(Integer id);

    /**
     * 查询所有操作员
     * @param keywords
     * @return
     */
    List<Admin> getAllAdmins(String keywords);

    /**
     * 更新操作员的角色
     * @param adminId
     * @param rids
     * @return
     */
    RespBean updateAdminWithRoles(Integer adminId, Integer[] rids);

    /**
     * 修改用户密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    RespBean updatePassword(String oldPass, String pass, Integer adminId);
}
