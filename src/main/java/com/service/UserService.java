package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.User;
import com.utils.Result;
import org.jetbrains.annotations.NotNull;

public interface UserService extends IService<User> {
     Result<?> login(@NotNull User user);
    Result<?> getUserInfo(String token);
    Result<?> checkUserName(String userName);
    Result<?> registerUser(@NotNull User user);
    Result<?> checkLogin(String token);
}
