package com.zhuhodor.myblog.common;

import lombok.Data;

import java.io.Serializable;

//返回的实体类
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public static Result success(int code, String msg, Object data){
        Result r = new Result();
        r.setMsg(msg);
        r.setCode(code);
        r.setData(data);
        return r;
    }


    public static Result fail(int code, String msg, Object data){
        Result r = new Result();
        r.setMsg(msg);
        r.setCode(code);
        r.setData(data);
        return r;
    }

    public static Result success(Object data){
        return success(200,"successful",data);
    }

    public static Result fail(String msg){
        return fail(400,msg,null);
    }
}
