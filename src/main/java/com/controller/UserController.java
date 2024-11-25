package com.controller;

import com.entity.User;
import com.service.impl.UserServiceImpl;
import com.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * 已有user登录
     * @param user User
     * @return result Result<?>
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        return userServiceImpl.login(user);
    }

    /**
     * 获取user信息
     * @param token String
     * @return result Result<?>
     */
    @GetMapping("/getUserInfo")
    public Result<?> getUserInfo(@RequestHeader String token) {
        return userServiceImpl.getUserInfo(token);
    }

    /**
     * 注册userName检查
     * @param username String
     * @return result Result<?>
     */
    @PostMapping("/checkUsername")
    public Result<?> checkUserName(@RequestParam String username) {
        return userServiceImpl.checkUserName(username);
    }

    /**
     * 注册user
     * @param user User
     * @return result Result<?>
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return userServiceImpl.registerUser(user);
    }

    /**
     * 检查token
     * @param token String
     * @return result
     */
    @GetMapping("checkLogin")
    public Result<?> checkLogin(@RequestHeader String token) {
       return userServiceImpl.checkLogin(token);
    }
}