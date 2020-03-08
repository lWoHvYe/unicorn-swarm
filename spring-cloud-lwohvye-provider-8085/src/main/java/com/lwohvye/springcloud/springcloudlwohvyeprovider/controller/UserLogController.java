package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.excel.ExcelUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.UserLog;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.listener.UserLogExcelListener;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.UserLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Hongyan Wang
 * @packageName
 * @className UserLogController
 * @description
 * @date 2019/12/18 16:42
 */
@RestController
@RequestMapping("/userLog")
public class UserLogController {

    @Autowired
    private UserLogService userLogService;

    /**
     * @return com.lwohvye.springcloud.springcloudlwohvyeapi.entity.ResultModel
     * @description 获取日志列表
     * @params [username, searchTime, pages, limits]
     * @author Hongyan Wang
     * @date 2019/12/18 16:54
     */
    @ApiOperation(value = "获取日志列表", notes = "获取日志列表，包含根据用户名及操作时间分页查询")
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
    public ResultModel<PageInfo<UserLog>> list(String username, String searchTime, int page, int pageSize) {
//        JSONObject json = new JSONObject();
//
        String startDate = null;
        String endDate = null;
        if (!StringUtils.isEmpty(searchTime)) {
            String[] searchTimes = searchTime.split(" - ");
            startDate = searchTimes[0];
            endDate = searchTimes[1];
        }
//
//        PageInfo<UserLog> pageInfo = userLogService.list(username, startDate, endDate, page, pageSize);
//
//        json.put("flag", true);
//        json.put("result", pageInfo);
//        return json.toJSONString();
        return new ResultModel<>(userLogService.list(username, startDate, endDate, page, pageSize));
    }

    /**
     * @description 文件下载
     * @params [response]
     * @return void
     * @author Hongyan Wang
     * @date 2020/2/28 13:28
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
//        设置响应信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        var fileName = URLEncoder.encode("日志信息" + IdUtil.simpleUUID(), StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        var list = this.list(null, null, 1, 40);
//        下载文件
        new ExcelUtil<UserLog>().download(response, list.getData().getList(), new UserLog(), "第一栏");
    }

    @PostMapping("/upload")
    public String update(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(),UserLog.class,new UserLogExcelListener(userLogService)).sheet().doRead();
        return "success";
    }

    @GetMapping("/test")
    public String test(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("线程暂停");
        }
        return "success";
    }
}
