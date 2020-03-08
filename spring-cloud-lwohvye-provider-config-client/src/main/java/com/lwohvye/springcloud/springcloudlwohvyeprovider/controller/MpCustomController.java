package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;


import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.MpCustomEntity;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.annotation.LogAnno;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.MpCustomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前端控制器
 *
 * @author author
 * @since 2019-10-08
 */
@RestController
@RequestMapping("/mpCustom")
public class MpCustomController {

    @Autowired
    private MpCustomService mpCustomService;

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel
     * @description 获取列表
     * @params []
     * @author Hongyan Wang
     * @date 2019/10/9 15:52
     */
    @ApiOperation(value = "获取客户列表", notes = "获取客户列表，暂不提供分页及搜索")
    @GetMapping("/list")
    public ResultModel<List<MpCustomEntity>> list() {
//        var json = new JSONObject();
//        var list = mpCustomService.list();
//        json.put("flag", true);
//        json.put("list", list);
//        return json.toJSONString();
        return new ResultModel<>(mpCustomService.list());
    }

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel
     * @description 添加企业信息
     * @params [mpCustomEntity]
     * @author Hongyan Wang
     * @date 2019/10/9 16:14
     */
    @LogAnno(operateType = "添加客户信息")
    @ApiOperation(value = "添加客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customName", value = "企业名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "address", value = "企业地址", dataType = "String"),
            @ApiImplicitParam(name = "ownerName", value = "企业法人", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "企业状态", dataType = "Integer", defaultValue = "1")
    })
    @PostMapping("/save")
    public ResultModel<MpCustomEntity> save(MpCustomEntity mpCustomEntity) {
//        var json = new JSONObject();
//        mpCustomService.save(mpCustomEntity);
//        json.put("flag", true);
//        json.put("msg", "添加成功");
//        return json.toJSONString();
        return new ResultModel<>(mpCustomService.insertSelective(mpCustomEntity));
    }

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel
     * @description 根据id获取客户信息
     * @params [customId]
     * @author Hongyan Wang
     * @date 2019/10/10 16:20
     */
    @ApiOperation(value = "获取客户信息", notes = "根据id获取客户信息")
    @ApiImplicitParam(name = "customId", value = "客户编号", required = true, dataType = "Integer")
    @GetMapping("/searchById")
    public ResultModel<MpCustomEntity> searchById(@NonNull int customId) {
//        var json = new JSONObject();
//        var entity = mpCustomService.searchById(customId);
//        json.put("flag", true);
//        json.put("entity", entity);
//        return json.toJSONString();
        return new ResultModel<>(mpCustomService.selectByPrimaryKey(customId));
    }

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel
     * @description 根据企业id删除企业信息
     * @params [customId]
     * @author Hongyan Wang
     * @date 2019/10/9 16:22
     */
    @LogAnno(operateType = "删除客户信息")
    @ApiOperation(value = "删除客户信息", notes = "根据客户id，删除客户信息")
    @ApiImplicitParam(name = "customId", value = "客户编号", required = true, dataType = "Integer")
    @GetMapping("/delete")
    public ResultModel<Integer> delete(@NonNull int customId) {
//        var json = new JSONObject();
//        mpCustomService.delete(customId);
//        json.put("flag", true);
//        json.put("msg", "删除成功");
//        return json.toJSONString();
        return new ResultModel<>(mpCustomService.deleteByPrimaryKey(customId));
    }

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel
     * @description 修改客户信息
     * @params [mpCustomEntity]
     * @author Hongyan Wang
     * @date 2019/10/10 17:20
     */
    @LogAnno(operateType = "修改客户信息")
    @ApiOperation(value = "修改客户信息", notes = "根据客户id，修改客户信息")
    @ApiImplicitParam(name = "customId", value = "客户编号", required = true, dataType = "Integer")
    @PostMapping("/update")
    public ResultModel<MpCustomEntity> update(@NonNull MpCustomEntity mpCustomEntity) {
//        var json = new JSONObject();
//        mpCustomService.update(mpCustomEntity);
//        json.put("flag", true);
//        json.put("msg", "修改成功");
//        return json.toJSONString();
        return new ResultModel<>(mpCustomService.updateByPrimaryKeySelective(mpCustomEntity));
    }
}
