package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * CORSFilter 解决跨域问题
 */
@Component
//使用lombok的该注解代替变量的生成
@Slf4j
public class InitCORSFilter extends OncePerRequestFilter {
    //  private Logger log = LoggerFactory.getLogger(InitCORSFilter.class);
    public InitCORSFilter() {
        log.info("==== 初始化系统允许跨域请求 ====");
    }

    /**
     * 解决跨域：Access-Control-Allow-Origin，值为*表示服务器端允许任意Domain访问请求
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//    if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, sid, mycustom, smuser");
        response.addHeader("Access-Control-Max-Age", "1800");//30 min
//    }
        filterChain.doFilter(request, response);
    }
}