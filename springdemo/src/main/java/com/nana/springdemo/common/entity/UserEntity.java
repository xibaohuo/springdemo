package com.nana.springdemo.common.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserEntity {
    private Integer id;

    private String userName;

    private String password;

    private Date gmtCreate;

    private Date gmtModified;

}
