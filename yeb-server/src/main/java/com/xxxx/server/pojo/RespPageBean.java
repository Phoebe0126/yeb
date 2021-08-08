package com.xxxx.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 分页返回的数据格式
 * @author: 陈玉婷
 * @create: 2021-08-03 14:33
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {
    /**
     * 总条数
     */
    private Long total;

    /**
     * 分页数据
     */
    private List<?> data;
}
