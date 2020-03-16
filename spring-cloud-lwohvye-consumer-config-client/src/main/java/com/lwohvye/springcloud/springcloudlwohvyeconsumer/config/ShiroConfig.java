package com.lwohvye.springcloud.springcloudlwohvyeconsumer.config;

import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro.CaptchaFormAuthenticationFilter;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro.KickoutSessionControlFilter;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro.MyShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.config
 * @className ShiroConfig
 * @description Shiro相关配置类，替代原xml配置方式
 * @date 2019/05/12 12:21
 */
//TODO 配置指定条件下清除缓存
@Configuration
public class ShiroConfig {

    //    加载配置文件信息
    @Value("${spring.redis.host}")
    public String redisHost;
    @Value("${spring.redis.port}")
    public String redisPort;
    @Value("${spring.redis.password}")
    public String redisPassword;

    /**
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     * @description Filter工厂，配置对应的过滤及跳转条件
     * @params [securityManager]
     * @author Hongyan Wang
     * @date 2019/10/12 14:55
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        var shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);

//        登陆页
        shiroFilterFactoryBean.setLoginUrl("/login");
//        首页
        shiroFilterFactoryBean.setSuccessUrl("/index");
//        错误页面
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

//        自定义过滤器集，注意设置的顺序
        var filtersMap = new LinkedHashMap<String, Filter>();
//        配置验证码过滤
        filtersMap.put("authc", new CaptchaFormAuthenticationFilter());
//        配置并发登陆过滤器
        filtersMap.put("kickout", kickoutSessionControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);

//        权限控制map
        var filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
//        配置获取验证码不拦截
        filterChainDefinitionMap.put("/verify", "anon");
//        配置swagger-ui相关不拦截
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/v2/api-docs-ext", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
//        配置总线刷新不被拦截
        filterChainDefinitionMap.put("/bus/refesh", "anon");
//        配置登出 具体登出已有shiro内部完成
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/kickout", "anon");
//        过滤器链，从上到下顺序执行，所以需要把/**放在最下面
//        authc:所有url都必须认证通过才可访问,anon所有url都可以匿名访问,kickout为自定义单点登陆
        filterChainDefinitionMap.put("/**", "authc,kickout");
//        配置过滤器链
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;

    }

    /**
     * @return org.apache.shiro.mgt.SessionsSecurityManager
     * @description 配置Realm管理认证, 返回类型建议设置为SessionsSecurityManager，需注意
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 14:54
     */
    @Bean
    public SessionsSecurityManager securityManager() {
        var securityManager = new DefaultWebSecurityManager();
//        配置自定义身份认证realm
        securityManager.setRealm(myShiroRealm());
//        配置自定义缓存实现cacheManager，使用redis
        securityManager.setCacheManager(cacheManagers());
//        配置自定义session管理
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeprovider.common.shiro.MyShiroRealm
     * @description 身份认证realm
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 14:53
     */
    @Bean
    public MyShiroRealm myShiroRealm() {
        var shiroRealm = new MyShiroRealm();
//        配置凭证匹配器
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return shiroRealm;
    }

    /**
     * @return org.apache.shiro.authc.credential.HashedCredentialsMatcher
     * @description 自定义凭证匹配器
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:06
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        var credentialsMatcher = new HashedCredentialsMatcher();
//        散列算法，这里使用md5算法
        credentialsMatcher.setHashAlgorithmName("md5");
//        散列次数
        credentialsMatcher.setHashIterations(2);

        return credentialsMatcher;
    }

    /**
     * @return org.crazycake.shiro.RedisCacheManager
     * @description 自定义缓存管理器
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:02
     */
    @Bean
    public RedisCacheManager cacheManagers() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
//        配置redis管理器
        redisCacheManager.setRedisManager(redisManager());
//        配置缓存过期时间，只是认证相关的缓存
        redisCacheManager.setExpire(1200);
//        设置缓存字段的唯一标识，一般是主键
        redisCacheManager.setPrincipalIdFieldName("uid");

        return redisCacheManager;
    }

    /**
     * @return org.crazycake.shiro.RedisManager
     * @description 自定义redis配置类
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:28
     */
    @Bean
    public RedisManager redisManager() {
        var redisManager = new RedisManager();
//        配置redis主机地址 ip:port
        redisManager.setHost(redisHost + ":" + redisPort);
//        配置redis连接密码
        redisManager.setPassword(redisPassword);
//        配置连接超时
        redisManager.setTimeout(1200);

        return redisManager;
    }

    /**
     * @return org.apache.shiro.web.session.mgt.DefaultWebSessionManager
     * @description 自定义session管理器
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:05
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        var webSessionManager = new DefaultWebSessionManager();
//        设置session数据层实现，这里使用redis的
        webSessionManager.setSessionDAO(redisSessionDAO());

        return webSessionManager;
    }

    /**
     * @return org.crazycake.shiro.RedisSessionDAO
     * @description RedisSessionDAO是对redis-session Dao层的相关实现，可以设置相关属性
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:36
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        var sessionDAO = new RedisSessionDAO();
//        设置redis属性
        sessionDAO.setRedisManager(redisManager());
//        设置缓存过期时间
        sessionDAO.setExpire(2000);

        return sessionDAO;
    }

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeprovider.common.shiro.KickoutSessionControlFilter
     * @description 并发登陆控制
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 14:37
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
        var kickoutSessionControlFilter = new KickoutSessionControlFilter();
//        设置相关属性
        kickoutSessionControlFilter.setCacheManager(cacheManagers());
        kickoutSessionControlFilter.setSessionManager(sessionManager());
//        false表示踢出前面登陆的用户
        kickoutSessionControlFilter.setKickoutAfter(false);
//        设置并发登陆数
        kickoutSessionControlFilter.setMaxSession(1);
//        设置踢出提示页
        kickoutSessionControlFilter.setKickoutUrl("/kickout");

        return kickoutSessionControlFilter;
    }

    /**
     * @return org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
     * @description 开启spring aop支持
     * @params [securityManager]
     * @author Hongyan Wang
     * @date 2019/10/12 15:51
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        var attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        attributeSourceAdvisor.setSecurityManager(securityManager);
        return attributeSourceAdvisor;
    }

    /**
     * @return org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
     * @description 权限相关异常处理类
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:52
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        var mappingExceptionResolver = new SimpleMappingExceptionResolver();
        var mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException", "403");
        mappingExceptionResolver.setExceptionMappings(mappings);
        mappingExceptionResolver.setDefaultErrorView("error");
        mappingExceptionResolver.setExceptionAttribute("ex");
        return mappingExceptionResolver;
    }

    /**
     * @return org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator
     * @description 授权用配置
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:52
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        var defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        /*
         * setUsePrefix(true)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);

        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * @return org.apache.shiro.spring.LifecycleBeanPostProcessor
     * @description shiro生命周期处理器
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/12 15:54
     */
    //TODO 在配置类中使用@Value注入配置文件的值时，可能由于加载顺序的原因无法成功注入，需要将Lifecycle相关方法设置为静态方法static
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
