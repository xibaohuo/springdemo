package com.nana.springdemo.service;

//import com.nana.springdemo.common.Enum.BusinessCenterExceptionEnum;
//import com.nana.springdemo.common.Exception.BusinessCenterException;
//import com.nana.springdemo.common.entity.UserEntity;
//import com.nana.springdemo.common.resp.UserResp;
//import com.nana.springdemo.mapper.UserMapper;
//import com.nana.springdemo.redis.IRedisCache;
import com.nana.springdemo.common.Enum.BusinessCenterExceptionEnum;
import com.nana.springdemo.common.Exception.BusinessCenterException;
import com.nana.springdemo.common.entity.UserEntity;
import com.nana.springdemo.mapper.UserMapper;
import com.nana.springdemo.redis.IRedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserService {
    @Resource
    private UserMapper userMapper;


    @Resource
    private IRedisCache redisCache;

    public UserEntity selectByPrimaryKey(Integer id) {
        /**
         * 先查缓存
         */
        if (redisCache.exists(String.valueOf(id)) == 1) {
            return redisCache.get(String.valueOf(id), UserEntity.class);
        }
        UserEntity ret = userMapper.selectByPrimaryKey(id);
        /**
         * 缓存没有结果就查数据库，并且存入缓存
         */
        if (ret != null) {
            redisCache.setEx(String.valueOf(id), ret, 60 * 60 * 24);
        }else{
            throw new BusinessCenterException(BusinessCenterExceptionEnum.SERVER_ERROR);
        }

        return ret;
    }
}
