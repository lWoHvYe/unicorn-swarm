package com.lwohvye.springcloud.springcloudlwohvyeprovider.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwohvye.springcloud.springcloudlwohvyeapi.common.util.DateTimeUtil;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.JsonTestEntity;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 主要说明字符串与json转换的一些方法
 */
@RestController
@RequestMapping("/jsonTest")
public class JsonTestController {

    //    设置部分参数
    @PostMapping(value = "/test", produces = "application/json")
//    设置ResponseBody用于返回json串
//    @ResponseBody
//    设置RequestBody用于接收json串
    @ApiOperation(value = "接收json串，并进行解析", notes = "需要前台传入json串，后台进行解析后返回")
    @ApiImplicitParam(name = "jsonStr", value = "待解析json串", required = true, dataType = "String")
    public List<JsonTestEntity> jsonTest(@RequestBody String jsonStr) throws JsonProcessingException {

        String result = jsonStr
//                "{\n" +
//                "\t\"code\": \"200\",\n" +
//                "\t\"data\": {\n" +
//                "\t\"rows\":[{\n" +
//                "\t\t\"cameraId\": \"100\",\n" +
//                "\t\t\"cameraType\": 0,\n" +
//                "\t\t\"createTime\": \"2019-03-05\",\n" +
//                "\t\t\"decodetag\": \"dahua\",\n" +
//                "\t\t\"extraField\": {},\n" +
//                "\t\t\"indexCode\": \"1c0de121-2ce9-4b9f-b58e-bec34f66c5b2\",\n" +
//                "\t\t\"isOnline\": 1,\n" +
//                "\t\t\"latitude\": \"18.2557\",\n" +
//                "\t\t\"longitude\": \"135.6358\",\n" +
//                "\t\t\"name\": \"一个卡口\",\n" +
//                "\t\t\"pixel\": 1,\n" +
//                "\t\t\"updateTime\": \"2019-04-18\"\n" +
//                "\t}, {\n" +
//                "\t\t\"cameraId\": \"1004\",\n" +
//                "\t\t\"cameraType\": 0,\n" +
//                "\t\t\"createTime\": \"2018-07-05\",\n" +
//                "\t\t\"decodetag\": \"dahua\",\n" +
//                "\t\t\"extraField\": {},\n" +
//                "\t\t\"indexCode\": \"63717de5-dd4b-4682-ae45-284d600d6cd3\",\n" +
//                "\t\t\"isOnline\": 1,\n" +
//                "\t\t\"latitude\": \"49.2123\",\n" +
//                "\t\t\"longitude\": \"63.6311\",\n" +
//                "\t\t\"name\": \"又一个球机\",\n" +
//                "\t\t\"pixel\": 1,\n" +
//                "\t\t\"updateTime\": \"2019-01-01\"\n" +
//                "\t}, {\n" +
//                "\t\t\"cameraId\": \"101\",\n" +
//                "\t\t\"cameraType\": 0,\n" +
//                "\t\t\"createTime\": \"2017-06-06\",\n" +
//                "\t\t\"decodetag\": \"hikvision\",\n" +
//                "\t\t\"extraField\": {},\n" +
//                "\t\t\"indexCode\": \"\",\n" +
//                "\t\t\"isOnline\": 1,\n" +
//                "\t\t\"latitude\": \"47.2438\",\n" +
//                "\t\t\"longitude\": \"89.6809\",\n" +
//                "\t\t\"name\": \"一个球机\",\n" +
//                "\t\t\"pixel\": 1,\n" +
//                "\t\t\"updateTime\": \"2017-10-05\"\n" +
//                "\t}, {\n" +
//                "\t\t\"cameraId\": \"1063\",\n" +
//                "\t\t\"cameraType\": 0,\n" +
//                "\t\t\"createTime\": \"2019-08-15\",\n" +
//                "\t\t\"decodetag\": \"hikvision\",\n" +
//                "\t\t\"extraField\": {},\n" +
//                "\t\t\"indexCode\": \"\",\n" +
//                "\t\t\"isOnline\": 1,\n" +
//                "\t\t\"latitude\": \"12.2375\",\n" +
//                "\t\t\"longitude\": \"37.6782\",\n" +
//                "\t\t\"name\": \"一个枪机\",\n" +
//                "\t\t\"pixel\": 1,\n" +
//                "\t\t\"updateTime\": \"2019-10-01\"\n" +
//                "\t}],\n" +
//                "\t\t\"page\": 0,\n" +
//                "\t\t\"size\": 4000,\n" +
//                "\t\t\"total\": 2877\n" +
//                "\t},\n" +
//                "\t\"msg\": \"成功\"\n" +
//                "}"
                ;
        //result格式为
        // {
        //	"code": 100,
        //	"msg": "",
        //	"data": {
        //		"pageNo": 1,
        //		"pageSize": 20,
        //		"rows": [{	返回项}]
        //	}
        //}
        // 所以需将rows部分取出来
//        将结果转为json对象，未传实体类class时，转换所有字段
        ObjectMapper objectMapper = new ObjectMapper();
        //        配置不转换实体类中没有的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        JsonNode jsonNode = objectMapper.readTree(result);
        String string = jsonNode.get("data").get("rows").toString();
        List<JsonTestEntity> list = objectMapper.readValue(string, new TypeReference<>() {
        });
////        获取data部分
//        JSONObject data = jsonObject.getJSONObject("data");
////        获取rows部分
//        JSONArray rows = data.getJSONArray("rows");
////        将rows转化为集合，传实体类class时，只转换实体类相关字段（可有多的字段也可有少的字段）
//        List<JsonTestEntity> list = JSON.parseArray(rows.toJSONString(), JsonTestEntity.class);
//        操作集合设置属性
        for (JsonTestEntity jsonTestEntity : list) {
            jsonTestEntity.setCreateTime(DateTimeUtil.getCurFormatTime());
            jsonTestEntity.setUpdateTime(DateTimeUtil.getCurFormatDate());
        }

//        JSONObject json = new JSONObject();
//        json.put("flag", true);
//        json.put("list", list);
//        return json.toJSONString();
        return list;
    }
}
