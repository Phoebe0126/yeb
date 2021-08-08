package com.xxxx.server.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 转换日期
 * @author: 陈玉婷
 * @create: 2021-08-03 14:48
 **/
@Component
public class DataConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String s) {
        try {
            return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
