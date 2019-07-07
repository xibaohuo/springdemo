package com.nana.springdemo.controller;

//import com.nana.springdemo.common.bean.ResultData;
//import com.nana.springdemo.common.entity.UserEntity;
//import com.nana.springdemo.common.req.UserReq;
//import com.nana.springdemo.common.resp.UserResp;
import com.nana.springdemo.common.entity.UserEntity;
import com.nana.springdemo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @RequestMapping(value="/get",method = RequestMethod.GET )
    public UserEntity selectByPrimaryKey(Integer id){

        return userService.selectByPrimaryKey(id);
    }

//    @PostMapping("/showUser")
//    public ResultData<UserResp> getUser(UserReq userReq) {
//        try {
//            UserEntity user = userService.selectByPrimaryKey(userReq.getId());
//            UserResp userResp = new UserResp();
//            BeanUtils.copyProperties(user, userResp);
//            return this.buildResultData(userResp);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//    }
}
