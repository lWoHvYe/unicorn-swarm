package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.PageUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Permission;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;


    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.ResultModel
     * @description 获取权限列表
     * @params [name, pageUtil]
     * @author Hongyan Wang
     * @date 2019/9/23 17:28
     */
    //    配置在api中不显示的参数
    @ApiOperationSupport(ignoreParameters = {"pageData", "totalCount", "totalPages"})
    @RequestMapping(value = "/findPermission", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultModel<PageUtil<Permission>> findByPermission(String name, PageUtil<Permission> pageUtil) {
//        JSONObject jsonObject = new JSONObject();
        return new ResultModel<>(sysPermissionService.findPermission(name, pageUtil));
//        jsonObject.put("result", "success");
//        jsonObject.put("list", pageUtil);
//        return jsonObject.toJSONString();
//        return pageUtil;
    }


    /**
     * 添加或修改权限
     *
     * @param permission
     * @return
     */
    @ApiOperationSupport(ignoreParameters = {"roles"})
    @RequestMapping(value = "/savePermission", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultModel<Integer> savePermission(@Valid Permission permission) {
//        JSONObject jsonObject = new JSONObject();
//        sysPermissionService.savePermission(permission);
//        jsonObject.put("result", "success");
//        return jsonObject.toJSONString();
        return new ResultModel<>(sysPermissionService.savePermission(permission));
    }


    /**
     * 删除权限
     *
     * @param permission
     * @return
     */
    @RequestMapping(value = "/deletePermission", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultModel<Integer> deleteRole(Permission permission) {
//        JSONObject jsonObject = new JSONObject();
//        sysPermissionService.deletePermission(permission);
//        jsonObject.put("result", "success");
//        return jsonObject.toJSONString();
        return new ResultModel<>(sysPermissionService.deletePermission(permission));
    }


}
