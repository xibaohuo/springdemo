package com.nana.springdemo.common.Enum;

/**
 * created by yangzhichao on 2017/11/16
 */
public enum BusinessCenterExceptionEnum {

    SERVER_ERROR(100001, "服务器错误"),


    SUCCESS(100000, "成功")
    ;


    private int code;
    private String msg;

    public static String getMsgByCode (int code) {
        for (BusinessCenterExceptionEnum c : BusinessCenterExceptionEnum.values()) {
            if (c.getCode()==(code)) {
                return c.getMsg();
            }
        }
        return null;
    }
    public static BusinessCenterExceptionEnum getEnumByCode (int code) {
        for (BusinessCenterExceptionEnum c : BusinessCenterExceptionEnum.values()) {
            if (c.getCode()==(code)) {
                return c;
            }
        }
        return null;
    }


    BusinessCenterExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
