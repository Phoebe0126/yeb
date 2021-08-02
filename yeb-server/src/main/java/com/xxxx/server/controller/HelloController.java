package com.xxxx.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 陈玉婷
 * @create: 2021-08-01 15:04
 **/
@RestController
public class HelloController {

    @GetMapping("/employee/basic/hello")
    public String basic() {
        return "/system/basic/**";
    }

    @GetMapping("/employee/advanced/hello")
    public String advanced() {
        return "/employee/advanced/**";
    }
}
