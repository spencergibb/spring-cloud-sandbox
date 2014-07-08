package org.springframework.platform.netflix.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.platform.util.RunOnceApplicationListener;

/**
 * Created by sgibb on 7/2/14.
 */
public class EurekaStartingListener extends RunOnceApplicationListener<ApplicationEnvironmentPreparedEvent>
        implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(EurekaStartingListener.class);

    private int order = Ordered.HIGHEST_PRECEDENCE + 17;

    public EurekaStartingListener() {
        System.out.println("Creating "+EurekaStartingListener.class);
    }

    @Override
    public void onApplicationEventInternal(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        //TODO: is there a way to not explicitly check for this here?
        // don't listen to events in a bootstrap context
        if (environment.getPropertySources().contains("bootstrap")) {
            return;
        }
        try {
            // Register with Eureka
            DiscoveryManager.getInstance().initComponent(
                    //instance config that reads from environment
                    // this eliminates the need for eureka.properties
                    new EnvironmentEurekaInstanceConfig(event.getEnvironment()),
                    //new MyDataCenterInstanceConfig(),
                    //client config that reads from environment
                    new EnvironmentEurekaClientConfig(event.getEnvironment()));
                    //new DefaultEurekaClientConfig());

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
