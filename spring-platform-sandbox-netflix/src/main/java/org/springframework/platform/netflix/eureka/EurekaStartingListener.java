package org.springframework.platform.netflix.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.Ordered;
import org.springframework.platform.util.RunOnceApplicationListener;

/**
 * Created by sgibb on 7/2/14.
 */
public class EurekaStartingListener extends RunOnceApplicationListener<ApplicationEnvironmentPreparedEvent>
        implements Ordered {

    private static final Log logger = LogFactory.getLog(EurekaStartingListener.class);

    private int order = Ordered.HIGHEST_PRECEDENCE + 17;

    public EurekaStartingListener() {
        System.out.println("Creating "+EurekaStartingListener.class);
    }

    @Override
    public void onApplicationEventInternal(ApplicationEnvironmentPreparedEvent event) {
        try {
            // Register with Eureka
            DiscoveryManager.getInstance().initComponent(
                    //TODO: create datacenter config that reads from environment
                    // this will eliminate the need for sample-backend-eureka.properties
                    new MyDataCenterInstanceConfig(),
                    //TODO: create client config that reads from environment
                    new DefaultEurekaClientConfig());

            // A good practice is to register as STARTING and only change status to UP
            // after the service is ready to receive traffic
            logger.info("Registering service to eureka with STARTING status");
            ApplicationInfoManager.getInstance().setInstanceStatus(
                    InstanceInfo.InstanceStatus.STARTING);
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
