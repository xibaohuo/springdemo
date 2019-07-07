package com.nana.springdemo.mapper;

import com.nana.springdemo.common.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserEntity selectByPrimaryKey(Integer id);

}
