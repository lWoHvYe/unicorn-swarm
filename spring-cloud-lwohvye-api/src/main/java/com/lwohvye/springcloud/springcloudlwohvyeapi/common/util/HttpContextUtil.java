package com.lwohvye.springcloud.springcloudlwohvyeapi.common.util;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class HttpContextUtil {
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    /**
     * 获取IP地址的方法
     * 使用第三方工具包
     * @return
     */
    public static String getIpAddress() {
        return ServletUtil.getClientIP(getRequest());
//        var request = getRequest();
//        var ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//
//        if (ip != null && ip.contains(",")) {
//            ip = ip.substring(0, ip.indexOf(",")).trim();
//        }
//        ip = ("0:0:0:0:0:0:0:1").equals(ip) ? "127.0.0.1" : ip;
//        var outIp = request.getHeader("ipHeader");
//
//        var ips = new ArrayList<String>();
//        ips.add("192.168.1.1");
//        if (outIp != null && !"".equals(outIp) && ips.contains(ip)) {
//            ip = outIp;
//        }
//
//        return ip;
    }
}