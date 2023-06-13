package com.chaz.reggie.common;

/**
 * @author chaz
 * @time 2023-06-02 21:37:49
 * @description 基于TreadLocal封装工具类，用户保存和获取当前登录用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * @description 设置值
     * @author chaz
     * @date 21:45 2023/6/2
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * @description 获取id值
     * @author chaz
     * @date 21:45 2023/6/2
     * @return java.lang.Long
     */
    public static  Long getCurrentId(){
        return threadLocal.get();
    }
}
