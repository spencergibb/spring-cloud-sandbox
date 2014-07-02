package org.springframework.platform.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;

import javax.annotation.PreDestroy;

/**
 * Created by sgibb on 7/2/14.
 */
public class EurekaShutdownHandler {
    @PreDestroy
    public void shutdown() {
        ApplicationInfoManager.getInstance().setInstanceStatus(
                InstanceInfo.InstanceStatus.OUT_OF_SERVICE);
    }
}
