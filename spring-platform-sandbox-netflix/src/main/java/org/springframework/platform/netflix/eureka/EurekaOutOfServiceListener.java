package org.springframework.platform.netflix.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.Ordered;
import org.springframework.platform.util.RunOnceApplicationListener;

/**
 * Created by sgibb on 7/2/14.
 */
public class EurekaOutOfServiceListener extends RunOnceApplicationListener<ContextClosedEvent>
        implements Ordered {
    @Override
    public void onApplicationEventInternal(ContextClosedEvent event) {
        ApplicationInfoManager.getInstance().setInstanceStatus(
                InstanceInfo.InstanceStatus.OUT_OF_SERVICE);

        //FIXME: hack, how to synchronously tell eureka we are out of service
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
