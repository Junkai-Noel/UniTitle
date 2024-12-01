package com.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Spring Security配置类
 * @author junkai
 */
@Slf4j
@Configuration
public class WebSecurityConfig {

    private final PasswordUtil passwordUtil;

    @Autowired
    public WebSecurityConfig(PasswordUtil passwordUtil) {
        this.passwordUtil = passwordUtil;
    }

    /**
     *该方法是 Spring Security 中的安全过滤器链 (SecurityFilterChain) 的配置，用于定义应用的访问控制规则,以及自定义各种安全配置：
     * <p>1、requestMatcher指定要匹配的路径</p>
     * <p>2、permitAll对该路径下的所有请求放行</p>
     * <p>3、anyRequest().authenticated()指除指定路径之外的所有路径都要求认证</p>
     * <p>formLogin用于自定义登录页，并做相关配置，例如登陆成功响应，登录失败响应</p>
     * <p>csrf是一种认证保护机制</p>
     * <p>logout为配置用户登出时的响应和规则</p>
     * <p>exceptionHandling用于配置自定义的异常处理器，authenticationEntryPoint对应用户未认证的异常处理</p>
     * <p>cors配置跨源访问规则,默认为运行跨源访问</p>
     * @param http HttpSecurity
     * @return http.build() 构建并返回SecurityFilterChain，使配置的规则生效
     * @throws Exception 潜在错误
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/login")
                .permitAll()
                .anyRequest()
                .authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .permitAll()
                        .successHandler(new MyAuthenticationSuccessHandler())
                        .failureHandler(new MyAuthenticationFailureHandler()))
                .csrf( AbstractHttpConfigurer::disable)
                .logout( logout ->
                        logout.logoutSuccessHandler(new MyLogoutSuccessHandler()))
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(new MyAuthenticationEntryPoint()))
                .cors(Customizer.withDefaults());

        return http.build();
    }

    /**
     * 方法为何实现：
     * <p>If you create your own UserDetailsService bean, there is no need to manually define a bean for AuthenticationProvider, cos by default a DaoAuthenticationProvider bean will be automatically created for us, which will automatically pick up your defined UserDetailsService bean.
     * But if you define two or more UserDetailsService beans, then you need to define your own AuthenticationProvider. I made a mistake, as I don't realize I have another class that implements UserDetailsService interface and annotated with @service, which creates a second UserDetailsService bean.</p>
     * <p>在方法中指定UserDetailsService的实现类，和密码的加密算法</p>
     * @param manager UserDetailsService实现类
     * @return auth DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider( MyUserDetailsManager manager) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(manager);
        auth.setPasswordEncoder(passwordUtil.getEncoder());
        return auth;
    }
}