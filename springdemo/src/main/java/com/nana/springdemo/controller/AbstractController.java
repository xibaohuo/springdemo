package com.nana.springdemo.controller;

import com.nana.springdemo.common.Enum.BusinessCenterExceptionEnum;
import com.nana.springdemo.common.bean.ResultData;
import lombok.extern.slf4j.Slf4j;

/**
 * 所有controller基类
 */
@Slf4j
public abstract class AbstractController {

    public <T> ResultData<T> buildResultData() {
        return new ResultData();
    }

    public <T> ResultData<T> builderResultData(int code, String msg, T data) {
        return new ResultData<T>(code, msg, data);
    }

    public <T> ResultData<T> builderResultData(BusinessCenterExceptionEnum bcee, T data) {
        return new ResultData<T>(bcee.getCode(), bcee.getMsg(), data);
    }

    public <T> ResultData<T> builderResultData(BusinessCenterExceptionEnum bcee, T data, String requestId) {
        return new ResultData<T>(bcee.getCode(), bcee.getMsg(), data, requestId);
    }

    public <T> ResultData<T> buildResultData(T data) {
        if (data instanceof BusinessCenterExceptionEnum) {
            return this.builderResultData((BusinessCenterExceptionEnum) data, null);
        }
        return new ResultData<T>(data);
    }


}
