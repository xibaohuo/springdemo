package com.nana.springdemo.common.req;

import lombok.Data;

@Data
public class UserReq {
    private Integer id;

    private String userName;

    private String passWord;
}
