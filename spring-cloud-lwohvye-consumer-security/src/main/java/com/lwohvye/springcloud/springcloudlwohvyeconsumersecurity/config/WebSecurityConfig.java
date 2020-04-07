package com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.config;

import com.lwohvye.springcloud.springcloudlwohvyeconsumersecurity.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
//通过 @EnableWebSecurity注解开启Spring Security的功能。
@EnableWebSecurity
//使用@EnableGlobalMethodSecurity(prePostEnabled = true)这个注解，可以开启security的注解，
// 从而可以在需要控制权限的方法上面使用@PreAuthorize，@PreFilter这些注解。
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Order(2)//ResourceServerConfig 是比 SecurityConfig 的优先级低的
//继承 WebSecurityConfigurerAdapter 类，并重写它的方法来设置一些web安全的细节
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public UserDetailsService userDetailsService() { //覆盖写userDetailsService方法 (1)
        return new UserServiceImpl();

    }

    /**
     * If subclassed this will potentially override subclass configure(HttpSecurity)
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.csrf().disable();
        http.requestMatchers().antMatchers("/oauth/**", "/login/**", "/logout/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .formLogin().permitAll(); //新增login form支持用户登录及授权
//        http.csrf().disable()
//                .requestMatchers().antMatchers("/oauth/**").and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated()
//                .antMatchers("/").permitAll()
//                .antMatchers("/amchart/**",
//                        "/bootstrap/**",
//                        "/build/**",
//                        "/css/**",
//                        "/dist/**",
//                        "/documentation/**",
//                        "/fonts/**",
//                        "/js/**",
//                        "/pages/**",
//                        "/plugins/**"
//                ).permitAll() //默认不拦截静态资源的url pattern （2）
//                .anyRequest().authenticated().and()
//                .formLogin()
//                .loginPage("/login")// 登录url请求路径 (3)
//                .defaultSuccessUrl("/")// 登录成功跳转路径url(4)
//                .permitAll().and()
//                .logout().permitAll()
//        ;
//        http.logout().logoutSuccessUrl("/login"); // 退出默认跳转页面 (5)
    }

    /**
     * @return void
     * @description Spring Security提供了Spring EL表达式，允许在定义URL路径访问(@RequestMapping)的方法上面添加注解，来控制访问权限。在标注访问权限时，根据对应的表达式返回结果，控制访问权限：
     * true，表示有权限;fasle，表示无权限
     * Spring Security可用表达式对象的基类是SecurityExpressionRoot。
     * 在Controller方法上添加@PreAuthorize这个注解，value="hasRole('ADMIN')")是Spring-EL expression，当表达式值为true，标识这个方法可以被调用。如果表达式值是false，标识此方法无权限访问。
     * 其他权限参照spring-cloud-lwohvye-consumer-security/src/main/resources/img/Spring Security EL表达式.png
     * @params [auth]
     * @author Hongyan Wang
     * @date 2020/3/25 15:08
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("admin")
//                //对密码进行bcrypt加密
//                .password(new BCryptPasswordEncoder().encode("1234567"))
//                .roles("USER")
//                .and()
//                .withUser("root")
//                .password(new BCryptPasswordEncoder().encode("root"))
//                .roles("ADMIN")
//        ;
        //AuthenticationManager使用我们的 Service来获取用户信息，Service可以自己写，其实就是简单的读取数据库的操作
        auth.userDetailsService(userDetailsService()); // （6）
    }

    /**
     * 不定义没有password grant_type,密码模式需要AuthenticationManager支持
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 默认不拦截静态资源的url pattern。可以用下面的WebSecurity这个方式跳过静态资源的认证。
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resourcesDir/**");
    }
    //如果要获取登陆用户信息，可以使用
    /*
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
        String username = ((UserDetails)principal).getUsername();
    } else {
        String username = principal.toString();
    }
     */
}