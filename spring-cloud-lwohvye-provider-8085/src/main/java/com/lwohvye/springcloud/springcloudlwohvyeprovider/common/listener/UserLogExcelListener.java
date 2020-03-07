package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.UserLog;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.service.UserLogService;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hongyan Wang
 * @description Excel操作监听，可以加入一些业务逻辑
 * @date 2020/2/26 8:35
 */
public class UserLogExcelListener extends AnalysisEventListener<UserLog> {

    //自定义用于暂时存储list。
    //可以通过实例获取该值
    @Getter
    @Setter
    private List<UserLog> list = new ArrayList<>();
    /**
     * 每隔3000条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    //    引入业务层
    private UserLogService userLogService;

    public UserLogExcelListener(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    /**
     * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
     */
    @Override
    public void invoke(UserLog data, AnalysisContext context) {
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        list.add(data);
        //根据业务自行 do something
        //如数据过大，可以进行定量分批处理
        if (list.size() >= BATCH_COUNT) {
//            对于读取，一般是将数据写入库
            doSomething();
//            存储完毕，清理list
            list.clear();
        }


    }

    /**
     * 根据业务自行实现该方法
     */
    private void doSomething() {
//        加入数据库
        list.parallelStream().forEach(userLogService::insert);
    }

    /**
     * @return void
     * @description 所有数据解析完成后，会调用该方法
     * @params [context]
     * @author Hongyan Wang
     * @date 2020/2/28 13:02
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
//        对于分批处理的，记得这里也要操作数据
        doSomething();
//        解析结束销毁不用的资源
        list.clear();

    }
}