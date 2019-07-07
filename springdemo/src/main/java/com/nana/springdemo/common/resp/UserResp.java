package com.nana.springdemo.common.resp;

import lombok.Data;

import java.util.Date;

@Data
public class UserResp {
    private Integer id;

    private String userName;

    private String passWord;

    private Date gmtCreate;

    private Date gmtModified;
}
