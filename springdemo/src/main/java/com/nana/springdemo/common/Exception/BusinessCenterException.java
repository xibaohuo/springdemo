package com.nana.springdemo.common.Exception;

import com.nana.springdemo.common.Enum.BusinessCenterExceptionEnum;
import lombok.Data;

/**
 * created by yangzhichao on 2017/11/16
 */
@Data
public class BusinessCenterException extends RuntimeException{

    private int errorCode;

    private String errorMsg;

    public BusinessCenterException(int errorCode, String errorMsg){
        super();
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessCenterException(BusinessCenterExceptionEnum enm) {
        this(enm.getCode(), enm.getMsg());
    }
    public BusinessCenterException(BusinessCenterExceptionEnum enm, String str) {
        this(enm.getCode(), enm.getMsg() + "," + str);
    }
}
