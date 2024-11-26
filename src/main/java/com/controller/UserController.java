package com.controller;

import com.entity.User;
import com.service.impl.UserServiceImpl;
import com.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The type User controller.
 */
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
    @GetMapping("/checkLogin")
    public Result<?> checkLogin(@RequestHeader String token) {
       return userServiceImpl.checkLogin(token);
    }


    /**
     * 传入新的user对象，查找到数据库中username相同的记录时，更新数据库中记录的密码为传入的user对象所携带的密码
     * @param user User
     * @return result
     */
    @PutMapping("/updateUserPassword")
    public Result<?> updateUserPassword(@RequestBody User user) {
        return userServiceImpl.updateUserPassword(user);
    }


    /**
     * 根据传入的user对象，更新数据库中的用户信息
     * @param user the user
     * @return the result
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody User user) {
        return userServiceImpl.updateUser(user);
    }

    /**
     * 根据传入的user对象删除数据库中对应记录
     * @param user User
     * @return result
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestBody User user) {
        return userServiceImpl.removeUser(user);
    }

    @PutMapping("/change")
    public Result<?> change(@RequestBody User user) {
        return userServiceImpl.changePassword(user);
    }
}