package com.lwohvye.springcloud.springcloudlwohvyeapi.service;


import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

//添加Feign注解
@FeignClient(value = "LWOHVYE-PROVIDER")
@RequestMapping("/user")
public interface SysUserFeignClientService {

    @PostMapping("/list")
    ResultModel<PageUtil<User>> list(@RequestParam("username") String username, @RequestParam("order") String order,
                                     @RequestParam("page") int page, @RequestParam("pageSize") int pageSize);

    @GetMapping("/delete/{uid}")
    ResultModel<Integer> delete(@PathVariable("uid") Long uid);

    @PostMapping("/add")
    ResultModel<Integer> add(User record);

    @GetMapping("/get/{uid}")
    User get(@PathVariable("uid") Long uid);

    @PostMapping("/update")
    ResultModel<Integer> update(User record);

    /**
     * @return com.lwohvye.springboot.dubbo.entity.User
     * @description 由于配置了懒加载，项目存在部分问题，登陆验证时需手动获取用户角色及权限信息
     * @params [username]
     * @author Hongyan Wang
     * @date 2019/12/5 10:28
     */
    @GetMapping("/findLoginUser/{username}")
    User findLoginUser(@PathVariable("username") String username);
}

