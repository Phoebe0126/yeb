package com.xxxx.server;


/**
 * @author: 陈玉婷
 * @create: 2021-08-03 15:11
 **/

public class TestGlobal {

    public static void main(String[] args) {
        System.out.println(test());
    }

    public static String test() {
        try {
            return "sss";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        System.out.println("aaa");
        return "bbbb";
    }
}
