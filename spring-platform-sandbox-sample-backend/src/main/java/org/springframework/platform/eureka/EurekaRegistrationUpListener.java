package org.springframework.platform.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

import javax.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by sgibb on 7/2/14.
 */
public class EurekaRegistrationUpListener implements
        ApplicationListener<ApplicationPreparedEvent>, Ordered {

    private static final Log logger = LogFactory.getLog(EurekaRegistrationUpListener.class);

    private static final AtomicBoolean registered = new AtomicBoolean(false);

    private int order = Ordered.HIGHEST_PRECEDENCE + 17;

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        if (registered.compareAndSet(false, true)) {
            try {
                logger.info("Registering service to eureka with UP status");
                ApplicationInfoManager.getInstance().setInstanceStatus(
                        InstanceInfo.InstanceStatus.UP);
            }
            catch (Exception ex) {
                logger.warn(String.format("Cannot register with eureka"), ex);
            }
        }
    }

    @Override
    public int getOrder() {
        return order;
    }

}
