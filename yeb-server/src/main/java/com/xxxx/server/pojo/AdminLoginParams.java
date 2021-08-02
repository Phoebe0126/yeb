package com.xxxx.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: gouzi
 * @create: 2021-07-28 18:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Admin登录对象")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminLoginParams {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;

}
