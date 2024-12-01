package com.mapper;


import com.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);
    int updateByUsername(User user);
    int deleteByUsername(String username);
}