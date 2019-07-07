package com.nana.springdemo.common.Exception;


import com.nana.springdemo.common.Enum.BusinessCenterExceptionEnum;
import com.nana.springdemo.common.bean.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public ResultData<String> commonErrorHandler(Exception e) throws Exception {
        ResultData<String> r = new ResultData<>();
        r.setMsg(BusinessCenterExceptionEnum.SERVER_ERROR.getMsg());
        r.setCode(BusinessCenterExceptionEnum.SERVER_ERROR.getCode());
        log.error("服务端异常",e);
        return r;
    }






}
