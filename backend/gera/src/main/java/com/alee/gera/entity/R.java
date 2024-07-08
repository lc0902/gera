package com.alee.gera.entity;

import lombok.Data;

@Data
public class R<T> {
    /*返回体*/
//    code命名方式：前两个数字----业务类型，第三个数字----执行结果，第四个数字----错误类型
    private  Integer code;
    private String msg;
    private T data;
}