package com.xxxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import com.xxxx.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private RoleMapper roleMapper;



    /**
     * 用户登录
     * @param username
     * @param password
     * @param code
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {

        String kaptcha = (String) request.getSession().getAttribute("kaptcha");
        // 判断验证码是否正确
        if (StringUtils.isEmpty(code) || !kaptcha.equalsIgnoreCase(code)) {
            return RespBean.error("验证码不正确，请重新输入");
        }
        // 将通过SecurityConfig的配置类的bean中实现loadUserByUsername方法
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码不正确！");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("用户被禁用，请联系管理员");
        }

        // 将用户对象存入SpringSecurity全局上下文中，方便其他方法获取用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 为该用户生成tokenus
        String token = jwtUtil.generateToken(userDetails);
        HashMap<String, Object> map = new HashMap<>();
        map.put("tokenHead", tokenHead);
        map.put("token", token);
        return RespBean.success("登陆成功", map);
    }

    public Admin getAdminByUserName(String username) {
        Admin admin = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username).eq("enabled", true));
        return admin;
    }

    // 根据用户id查询用户角色
    public List<Role> getRolesByAdminId(Integer adminId) {
        return roleMapper.getRolesByAdminId(adminId);
    }

}
