package com.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.entity.User;
import com.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

/**
 * 实现了UserDetailsService
 */

@Slf4j
@Component
public class MyUserDetailsManager implements UserDetailsManager {


    private final UserMapper userMapper;
    private final PasswordUtil passwordUtil;

    @Autowired
    public MyUserDetailsManager(UserMapper userMapper,
                                PasswordUtil passwordUtil) {
        this.userMapper = userMapper;
        this.passwordUtil = passwordUtil;
    }

    /**
     * 通过username认证用户
     *
     * @param username String
     * @return user org.springframework.security.core.userdetails.User
     * @throws UsernameNotFoundException 用户名不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User loginUser = userMapper.selectOne(queryWrapper);
        if (loginUser == null) {
            throw new UsernameNotFoundException("未查询到用户：" + username);
        }
        return loginUser;
    }

    /**
     * 将service层的user插入操作转移到UserDetailsManage实现类中，实现用户管理与spring security的整合
     *
     * @param user UserDetails的具体实现类
     */
    @Override
    public void createUser(@NotNull UserDetails user) {
        //传入的是UserDetails接口，必须显式的指明参数是UserDetails的哪个实现类
        User finalUser = (User) user;
        log.info("原始用户：{}，{}，{}", user.getUsername(), user.getUsername(), user.getPassword());
        log.info("最终用户：{}，{}，{}", finalUser.getUsername(), finalUser.getUsername(), finalUser.getPassword());
        userMapper.insert(finalUser);
    }

    /**
     * 用于不需要知道旧密码的情况下直接更改密码，管理员层面操作
     * <p>原本重写自UserDetailsPasswordService,后来发现该接口为函数式接口，实现类只有InMemoryUserDetailsManager,
     * 推测该方法只用于内存存储方式的用户管理。且重写的方法必须有UserDetails返回值，违反数据库CRUD逻辑，故不再重写接口方法，也不再继承接口，
     * 改为独立实现。</p>
     *
     * @param user        UserDetails
     * @param newPassword String
     * @return boolean
     */
    public int updatePassword(@NotNull UserDetails user, String newPassword) {
        User loginUser = (User) user;
        if (userExists(loginUser.getUsername())) {
            return userMapper.updatePasswordByUsername(loginUser.getUsername(), newPassword);
        }
        return 0;
    }

    /**
     * 根据传入的User对象，更新数据库中相应记录的username和nickname
     *
     * @param user User
     */
    @Override
    public void updateUser(UserDetails user) {
        User loginUser = (User) user;
        User DBUSer = findUserByUsername(loginUser.getUsername());
        if(DBUSer != null) {
            User finalUser = new User.Builder()
                    .username(loginUser.getUsername())
                    .nickname(loginUser.getNickname())
                    .password(DBUSer.getPassword())
                    .build();
            userMapper.updateByUsername(finalUser);
        }
        else throw new UsernameNotFoundException("usernameNotFound");
    }

    @Override
    public void deleteUser(String username) {
        if(userExists(username))
            userMapper.deleteByUsername(username);
        else
            throw new UsernameNotFoundException("usernameNotFound");
    }

    /**
     * 用于必须知道旧密码的情况下更改密码，用户层面操作
     *
     * @param oldPassword String
     * @param newPassword String
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // 获取当前登录的用户名
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        // 验证旧密码，更新新密码
        newPassword = passwordUtil.encodePassword(newPassword);
        if((userMapper.updatePasswordByUsername(username, newPassword))==0)
            throw new UsernameNotFoundException("usernameNotFound");
    }

    /**
     * 根据username验证用户是否存在
     * @param username String
     * @return boolean
     */
    @Override
    public boolean userExists(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User loginUser = userMapper.selectOne(queryWrapper);
        return loginUser != null;
    }

    /**
     * 根据用户名查询用户，返回用户
     *
     * @param username String
     * @return User
     */
    public User findUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 检查密码是否合法
     *
     * @param originalPassword 用户传入的密码
     * @param encodedPassword  数据库中的密码
     * @return boolean
     */
    public boolean isPasswordCorrect(String originalPassword, String encodedPassword) {
        return (!StringUtils.isEmpty(originalPassword) &&
                passwordUtil.matches(originalPassword, encodedPassword));
    }
}