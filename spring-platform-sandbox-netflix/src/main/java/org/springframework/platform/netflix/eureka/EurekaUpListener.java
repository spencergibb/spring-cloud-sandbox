package org.springframework.platform.netflix.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.core.Ordered;
import org.springframework.platform.util.RunOnceApplicationListener;

/**
 * Created by sgibb on 7/2/14.
 */
public class EurekaUpListener extends RunOnceApplicationListener<EmbeddedServletContainerInitializedEvent>
        implements Ordered {

    private static final Log logger = LogFactory.getLog(EurekaUpListener.class);

    private int order = Ordered.HIGHEST_PRECEDENCE + 17;

    @Override
    public void onApplicationEventInternal(EmbeddedServletContainerInitializedEvent event) {
        try {
            logger.info("Registering service to eureka with UP status");
            ApplicationInfoManager.getInstance().setInstanceStatus(
                    InstanceInfo.InstanceStatus.UP);
        }
        catch (Exception ex) {
            logger.warn(String.format("Cannot register with eureka"), ex);
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

}
