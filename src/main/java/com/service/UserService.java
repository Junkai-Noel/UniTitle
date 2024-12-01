package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.User;
import com.utils.Result;
import org.jetbrains.annotations.NotNull;

public interface UserService extends IService<User> {
    Result<?> login(@NotNull User user);
    Result<?> getUserInfo();
    Result<?> checkUserName(String userName);
    Result<?> registerUser(@NotNull User user);
    Result<?> checkLogin(String token);
    Result<?> updateUserPassword(User user);
    Result<?> updateUser(User user);
    Result<?> removeUser(User user);
    Result<?> changePassword(User user,String newPassword);
}
