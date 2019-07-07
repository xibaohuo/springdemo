package com.nana.springdemo.common.bean;

import lombok.Data;

@Data
public class ResultData<T> {

    private int code;
    private String msg;
    private T data;
    private String requestId;

    public ResultData() {
        this(null);
    }
    public ResultData(T data){
        this.code = 100000;
        this.msg = "success";
        this.data = data;
    }
    public ResultData(T data, String requestId){
        this.code = 100000;
        this.msg = "success";
        this.data = data;
        this.requestId=requestId;
    }

    public ResultData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ResultData(int code, String msg, T data, String requestId) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.requestId=requestId;
    }
}
