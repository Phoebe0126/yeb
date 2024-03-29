package com.xxxx.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.server.pojo.RespBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用户没有权限的访问结果
 * @author: gouzi
 * @create: 2021-07-28 19:16
 **/
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        RespBean respBean = RespBean.error("没有权限，请联系管理员！");
        respBean.setCode(403);
        writer.write(new ObjectMapper().writeValueAsString(respBean));
        writer.flush();
        writer.close();
    }
}
