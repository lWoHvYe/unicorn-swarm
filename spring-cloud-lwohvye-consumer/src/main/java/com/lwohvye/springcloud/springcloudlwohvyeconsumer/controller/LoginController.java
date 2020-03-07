package com.lwohvye.springcloud.springcloudlwohvyeconsumer.controller;

import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.DateTimeUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.HttpContextUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.UserLog;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro.CaptchaEmptyException;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro.CaptchaErrorException;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro.CaptchaExpireException;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro.CaptchaToken;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.util.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(value = "用户登陆相关API")
@Controller
public class LoginController {

//    @Autowired
//    private UserLogService userLogService;

    @ApiIgnore
    @RequestMapping(value = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index() {
        return "index";
    }

    /**
     * @return java.lang.String
     * @description 登陆方法
     * Controller层向页面跳转时，需注意，地址前不要加/ ，
     * 比如跳转登陆页面 return "/login"; 在开发时能正常，但以jar部署时就会报错，所以需使用 return "login";
     * @params [username, password, captchaCode, request, map]
     * @author Hongyan Wang
     * @date 2019/10/14 16:13
     */
    @ApiIgnore
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(String username, String password, String captchaCode, HttpServletRequest request, Map<String, Object> map) {
        String exception = null;
//        未传用户名，直接返回，主要解决项目启动及退出登陆时报用户名不存在的错误的问题
        if (StringUtils.isEmpty(username))
            return "login";
        try {
            var subject = SecurityUtils.getSubject();
            //     使用用户名+密码并反转的方式作为验证密码
            var strPassword = new StringBuilder(username + password).reverse().toString();
            var usernamePasswordToken = new CaptchaToken(username, strPassword, captchaCode, WebUtils.isTrue(request, "rememberMe"), subject.getSession().getHost());
            subject.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            exception = e.getClass().getName();
        }

        var msg = "登陆成功";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                msg = "UnknownAccountException --> 账号不存在";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                msg = "IncorrectCredentialsException --> 密码不正确";
            } else if (CaptchaEmptyException.class.getName().equals(exception)) {
                msg = "CaptchaEmptyException --> 验证码不可为空";
            } else if (CaptchaErrorException.class.getName().equals(exception)) {
                msg = "CaptchaErrorException --> 验证码错误";
            } else if (CaptchaExpireException.class.getName().equals(exception)) {
                msg = "CaptchaExpireException --> 验证码过期";
            } else {
                msg = "else --> " + exception;
            }
            map.put("msg", msg);
            return "login";
        }

        //            加入日志中
        UserLog log = new UserLog();
        log.setActType("类名 :" + this.getClass().getName() + " ; 方法名 : login ; 方法描述 : 登陆系统");// 操作说明
        log.setUsername(username);
//            获取并设置参数
        String actParams = " 用户名 : " + username + " : 密码 : " + password;
        log.setActParams(actParams);
        String ip = HttpContextUtil.getIpAddress();
        log.setIpAddr(ip);
//        设置执行结果，只记录登陆成功的
        log.setActResult(msg);
//        userLogService.insertSelective(log);// 添加日志记录

        map.put("msg", msg);
        return "index";
    }

    /**
     * 另一种获取登陆验证码的方式
     *
     * @param request
     */
    @ApiOperation(value = "验证码生成API", notes = "用于生成随机验证码")
    @RequestMapping(value = "/verify", method = {RequestMethod.GET, RequestMethod.POST})
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");

            //生成随机字串
            var verifyMap = VerifyCodeUtils.generateVerifyCode(4);
            //当前时间毫秒值
            var nowTime = DateTimeUtil.getCurMilli();
            //存入会话session
            var session = request.getSession();
            session.setAttribute(VerifyCodeUtils.VERIFY_CODE_SESSION_KEY, verifyMap.get("result"));
            session.setAttribute(VerifyCodeUtils.VERIFY_CODE_CREATE_MILLI, nowTime);
            //生成图片
            int w = 150;
            int h = 50;
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyMap.get("verifyCodeStr"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiIgnore
    @RequestMapping(value = "/403", method = {RequestMethod.GET, RequestMethod.POST})
    public String unauthorizedRole() {
        return "403";
    }

    @ApiIgnore
    @RequestMapping(value = "/kickout", method = {RequestMethod.GET, RequestMethod.POST})
    public String kickout() {
        return "kickout";
    }

    @ApiIgnore
    @RequestMapping(value = "/jsonTestPage", method = {RequestMethod.GET, RequestMethod.POST})
    public String jsonTestPage() {
        return "jsonTest";
    }
}
