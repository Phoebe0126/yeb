package com.xxxx.server.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author: 陈玉婷
 * @create: 2021-08-14 16:33
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatMsg {

    private String from;

    private String to;

    private String formNickName;

    private String content;

    private LocalDateTime date;

}
