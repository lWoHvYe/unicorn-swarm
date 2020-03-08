package com.lwohvye.springcloud.springcloudlwohvyeconsumer.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import com.lwohvye.springcloud.springcloudlwohvyeapi.service.SysUserFeignClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "用户相关操作API")
@Validated
//RestController相当于@Controller+@ResponseBody
@RestController
@RequestMapping("/consumer/user")
public class UserController_Consumer {

    @Autowired
    private SysUserFeignClientService sysUserFeignClientService;

    /**
     * @return PageUtil<User>
     * @description 获取用户列表
     * @params [username, pageUtil]
     * @author Hongyan Wang
     * @date 2019/9/23 13:26
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表，可以通过用户名模糊查询，包含PageUtil分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名称", dataType = "String")
    })
//    配置在api中不显示的参数,暂未生效
    @ApiOperationSupport(ignoreParameters = {"pageData", "totalCount", "totalPages"})
    @PostMapping(value = "/list")
    public ResultModel<PageUtil<User>> list(@RequestParam("username") String username, @RequestParam("order") String order,
                                            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        return sysUserFeignClientService.list(username, order, page, pageSize);
    }

    /**
     * @return Integer
     * @description 添加用户
     * @params [user]
     * @author Hongyan Wang
     * @date 2020/1/14 10:28
     */
    @ApiOperation(value = "添加新用户", notes = "添加新用户，包含用户的授权")
    @ApiOperationSupport(ignoreParameters = {"roles"})
    @RequestMapping(value = "/add")
    public ResultModel<Integer> add(@Valid User user) {
        return sysUserFeignClientService.add(user);
    }

    /**
     * @return Integer
     * @description 删除用户
     * @params [uid]
     * @author Hongyan Wang
     * @date 2020/1/14 10:24
     */
    @ApiOperation(value = "删除指定用户", notes = "根据用户的id删除对应的用户与权限")
    @ApiImplicitParam(name = "uid", value = "用户ID", required = true, dataType = "Long")
    @GetMapping(value = "/delete/{uid}")
    public ResultModel<Integer> delete(@PathVariable("uid") Long uid) {
        return sysUserFeignClientService.delete(uid);
    }

    /**
     * @return Integer
     * @description 修改用户信息
     * @params [user]
     * @author Hongyan Wang
     * @date 2020/1/13 13:35
     */
    @ApiOperation(value = "修改用户信息", notes = "根据用户id修改用户信息，包含部分信息修改。用户名username不可修改")
    @ApiOperationSupport(ignoreParameters = {"roles"})
    @PostMapping(value = "/update")
    public ResultModel<Integer> update(@Valid User user) {
        return sysUserFeignClientService.update(user);
    }

    @GetMapping(value = "/findLoginUser/{username}")
    public User findLoginUser(@PathVariable("username") String username) {
        return sysUserFeignClientService.findLoginUser(username);
    }

}
