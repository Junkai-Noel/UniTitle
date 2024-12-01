package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.User;
import com.mapper.UserMapper;
import com.security.JwtSecurityProperties;
import com.security.PasswordUtil;
import com.security.MyUserDetailsManager;
import com.service.UserService;
import com.utils.Result;
import com.utils.ResultCodeEnum;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final PasswordUtil passwordUtil;
    private final JwtSecurityProperties jwtSecurityProperties;
    private final MyUserDetailsManager myUserDetailsManager;


    @Autowired
    public UserServiceImpl(UserMapper userMapper,
                           PasswordUtil passwordUtil,
                           JwtSecurityProperties jwtSecurityProperties,
                           MyUserDetailsManager myUserDetailsManager) {
        this.userMapper = userMapper;
        this.passwordUtil = passwordUtil;
        this.jwtSecurityProperties = jwtSecurityProperties;
        this.myUserDetailsManager = myUserDetailsManager;
    }

    /**
     * 第一次登录或者token过期的user，调用此方法（只关注user登录，不关注user注册）：
     * <p>1、根据controller传入的user对象获取到username,查询数据库中是否有此user。没有，返回USERNAME_ERROR</p>
     * <p>2、user存在，输入的password不为空且与数据库中的一致，给用户生成一个token，将token封装到data中后返回给controller</p>
     * <p>3、password为空或者password不匹配，返回PASSWORD_ERROR</p>
     *
     * @param user User
     * @return result Result<?>
     */

    public Result<?> login(@NotNull User user) {
        //查询账号
        User loginUser = myUserDetailsManager.findUserByUsername(user.getUsername());
        if (loginUser == null) {
            return new Result.Builder<>()
                    .resultCodeEnum(ResultCodeEnum.USERNAME_ERROR)
                    .build();
        }
        if (myUserDetailsManager.isPasswordCorrect(user.getPassword(), loginUser.getPassword())) {
            String token = jwtSecurityProperties.generateToken(loginUser);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return Result.ok(data);
        }
        return new Result.Builder<>()
                .resultCodeEnum(ResultCodeEnum.PASSWORD_ERROR)
                .build();
    }

    /**
     * 处理controller获取到的token
     * <P>1、检查token是否过期，如果过期，返回用户未登录。未过期，根据token中存放的数据获取userId</P>
     * <p>2、根据获取到的userId查询数据库中的user数据封装到user对象中，将密码设为null，返回到controller</p>
     * <p>3、如数据库中不存在用户，返回未登录</p>
     * <p>getPrincipal() 返回的是经过身份验证的用户的 主体信息，通常是实现了 UserDetails 接口的一个对象。在大多数情况下，这个对象是你定义的 User 类（它通常实现了 UserDetails 接口）。
     * UserDetails 接口是 Spring Security 用来封装用户信息的接口，里面包含了用户的基本信息（如用户名、密码、权限等）。因此，getPrincipal() 会返回实现了该接口的对象。</p>
     * @return result Result
     */

    public Result<?> getUserInfo() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        /*
             getName方法为 Principal接口方法，该方法在AbstractAuthenticationToken抽象类中实现。
             该方法会验证principal的类型，然后调用类型的getName方法获取用户名。源码中可选的类型有：
             1、UserDetails实现类
             2、AuthenticatedPrincipal实现类
             3、Principal实现类
         */
        Object principal = authentication.getName();
        Object credentials = authentication.getCredentials();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Map<String, Object> data = new HashMap<>();
        data.put("username", principal);
        data.put("password", credentials);
        data.put("authorities", authorities);
        return Result.ok(data);
        /*if (jwtSecurityProperties.isTokenExpired(token)) {
            return new Result.Builder<>()
                    .resultCodeEnum(ResultCodeEnum.NOT_LOGIN)
                    .build();
        }
        String username = jwtSecurityProperties.getUsernameFromToken(token);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            User finalUser = new User.Builder()
                    .username(user.getUsername())
                    .password(null)
                    .nickname(user.getNickname())
                    .build();
            Map<String, Object> data = new HashMap<>();
            data.put("LoginUser", finalUser);
            return Result.ok(data);
        }
        return new Result.Builder<>()
                .resultCodeEnum(ResultCodeEnum.USERNAME_ERROR)
                .build();*/
    }

    /**
     * 用户注册系列方法
     * <p>从controller获取到userName，与数据库中存放的userName比对，判断userName是否可用</p>
     *
     * @param userName String
     * @return result Result<?>
     */
    public Result<?> checkUserName(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userName);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return new Result.Builder<>()
                    .resultCodeEnum(ResultCodeEnum.USERNAME_USED)
                    .build();
        }
        return Result.ok();
    }

    /**
     * 注册用户方法
     * <p>1、判断用户名是否存在：
     * <p>获取传入的user对象中的username</p>
     * <p>调用checkUserName方法</p>
     * <p>比对checkUserName方法返回的result对象中封装的Enum枚举中封装的msg是否为USERNAME_USED</p>
     * <p>是则将checkUserName方法返回的result对象原封不动地返回，不是则结束判断</p>
     * </p>
     * <p>2、将传入的user对象中封装的password进行加密后，存储到user对象中</p>
     * <p>3、执行插入操作</p>
     *
     * @param user user
     * @return result Result<?>
     */
    public Result<?> registerUser(@NotNull User user) {
        Result<?> result = checkUserName(user.getUsername());
        if (result.matches(ResultCodeEnum.USERNAME_USED)) {
            return result;
        }
        User finalUser = new User.Builder()
                .username(user.getUsername())
                .password(passwordUtil.encodePassword(user.getPassword()))
                .nickname(user.getNickname())
                .build();
        myUserDetailsManager.createUser(finalUser);
        return Result.ok();
    }

    /**
     * 检查用户是否登录
     *
     * @param token String
     * @return result
     */
    public Result<?> checkLogin(String token) {
        if (jwtSecurityProperties.isTokenExpired(token) ||
                token == null) {
            return new Result.Builder<>()
                    .resultCodeEnum(ResultCodeEnum.NOT_LOGIN)
                    .build();
        }
        return Result.ok();
    }

    public Result<?> updateUserPassword(User user) {
        if (myUserDetailsManager.updatePassword(user, user.getPassword()) > 0)
            return Result.ok();
        return new Result.Builder<>()
                .code(999)
                .msg("UNEXPECTED_ERROR")
                .data(null)
                .build();
    }

    @Override
    public Result<?> updateUser(User user) {
        myUserDetailsManager.updateUser(user);
        return Result.ok();
    }

    @Override
    public Result<?> removeUser(@NotNull User user) {
        myUserDetailsManager.deleteUser(user.getUsername());
        return Result.ok();
    }

    /**
     * 业务执行前检查参数，检查用户名是否存在、新密码是否为空以及旧密码是否匹配数据库中密码。检查通过则执行业务
     * @param user User
     * @param newPassword String
     * @return result
     */
    @Override
    public Result<?> changePassword(@NotNull User user,String newPassword) {
        if(StringUtils.isBlank(newPassword)) {
            return new Result.Builder<>()
                    .resultCodeEnum(ResultCodeEnum.PASSWORD_NULL)
                    .build();
        }
        User DBUser = myUserDetailsManager.findUserByUsername(user.getUsername());
        if(DBUser == null)
            return new Result.Builder<>()
                    .resultCodeEnum(ResultCodeEnum.USERNAME_ERROR)
                    .build();
        if (myUserDetailsManager.isPasswordCorrect(user.getPassword(), DBUser.getPassword())) {
            myUserDetailsManager.changePassword(DBUser.getPassword(),
                    newPassword);
            return Result.ok();
        } else return new Result.Builder<>()
                .resultCodeEnum(ResultCodeEnum.PASSWORD_ERROR)
                .build();
    }
}