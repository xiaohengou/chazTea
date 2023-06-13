package com.chaz.reggie.common;

/**
 * @author chaz
 * @time 2023-06-05 19:16:31
 * @description 自定义业务异常类
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
