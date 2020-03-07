package com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.shiro;

import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.DateTimeUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeconsumer.common.util.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

//实现AuthorizingRealm接口用户认证
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

//    @Autowired
//    private SysUserService sysUserService;
    @Autowired
    private RestTemplate restTemplate;

    private static final String REST_URL_PREFIX = "http://LWOHVYE-PROVIDER";

    /**
     * @return org.apache.shiro.authz.AuthorizationInfo
     * @description 用户授权
     * @params [principalCollection]
     * @author Hongyan Wang
     * @date 2019/10/12 16:26
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        获取用户信息
        var primaryPrincipal = principalCollection.getPrimaryPrincipal();
//        添加角色和权限
        var authorizationInfo = new SimpleAuthorizationInfo();
        if (primaryPrincipal instanceof User) {
            var user = (User) primaryPrincipal;
            var role = user.getRoles();
//            添加角色
            authorizationInfo.addRole(role.getRoleName());
            for (var permission : role.getPermissions()) {
//                添加权限
                authorizationInfo.addStringPermission(permission.getPermissionStr());
            }
        }
        log.info("授权 ：Shiro认证成功");
        return authorizationInfo;
    }

    /**
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     * @description 身份认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
//        Post请求先进行认证，再到请求
        if (authenticationToken.getPrincipal() != null) {
//            转为CaptchaToken
            var captchaToken = (CaptchaToken) authenticationToken;
//            获取页面输入的验证码
            var captchaCode = captchaToken.getCaptchaCode();
//            验证码为空，抛出相应异常
            if (StringUtils.isEmpty(captchaCode))
                throw new CaptchaEmptyException();
//            获取session
            var session = SecurityUtils.getSubject().getSession();
//            获取当前时间
            var curMilli = DateTimeUtil.getCurMilli();
//            获取验证码生成时间
            var verifyMilli = (Long) session.getAttribute(VerifyCodeUtils.VERIFY_CODE_CREATE_MILLI);
//            验证码经历分钟数
            long useTime = (curMilli - verifyMilli) / 1000 / 60;
//            验证码过期，抛出相应异常
            if (useTime > 5)
                throw new CaptchaExpireException();
            // 从session获取正确的验证码
            var sessionCaptchaCode = (String) session.getAttribute(VerifyCodeUtils.VERIFY_CODE_SESSION_KEY);
//            var sessionCaptchaCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

//            验证码错误，抛出相应异常
            if (!captchaCode.equalsIgnoreCase(sessionCaptchaCode))
                throw new CaptchaErrorException();

//          获取用户名
            var username = ((CaptchaToken) authenticationToken).getUsername();
            //            根据用户名获取用户信息
            String url = REST_URL_PREFIX+"/user/findLoginUser/"+username;
            var user = restTemplate.getForObject(url, User.class);
//            var user = sysUserService.findLoginUser(username);
//            用户不存在，抛出相应异常
            if (user == null)
                throw new UnknownAccountException();
            log.info("认证 ：Shiro认证成功");

//            将用户信息放入session中
            session.setAttribute("curUser", user);
//                验证authenticationToken和authenticationInfo信息
            return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
        }
        return null;
    }
}
