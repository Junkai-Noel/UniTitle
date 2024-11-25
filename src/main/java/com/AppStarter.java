package com;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.interceptor.LoginProtectInterceptor;
import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 项目启动器
 */
@MapperScan("com.mapper")
@SpringBootApplication
public class AppStarter implements WebMvcConfigurer {

    private final LoginProtectInterceptor loginProtectInterceptor;

    @Autowired
    public AppStarter(LoginProtectInterceptor loginProtectInterceptor) {
        this.loginProtectInterceptor = loginProtectInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(AppStarter.class, args);
    }

    /**
     * 配置MyBatis插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); //分页配置
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());  //版本号乐观锁拦截器
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());  //防止全表更新和删除（安全性保证
        return interceptor;
    }


    /**
     * 配置拦截器
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        registry.addInterceptor(loginProtectInterceptor).addPathPatterns("/headline/**");
    }
}