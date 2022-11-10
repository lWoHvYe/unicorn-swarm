package com.lwohvye.springcloud.springcloudlwohvyeprovider.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ContextRefreshScheduler {

    @Autowired
    ContextRefresher refresher;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void autoRefresh() {
        refresher.refresh(); // call refresh的endpoint 本质上也是调用了这个方法
    }
}
