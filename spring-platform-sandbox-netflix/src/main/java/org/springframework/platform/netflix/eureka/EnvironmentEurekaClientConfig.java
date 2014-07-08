package org.springframework.platform.netflix.eureka;

import com.netflix.discovery.EurekaClientConfig;
import org.springframework.core.env.Environment;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sgibb on 7/7/14.
 */
public class EnvironmentEurekaClientConfig implements EurekaClientConfig {
    private static final String DEFAULT_ZONE = "defaultZone";

    private final Environment env;

    private String namespace = "eureka.";

    public EnvironmentEurekaClientConfig(Environment env) {
        this.env = env;
    }

    @Override
    public int getRegistryFetchIntervalSeconds() {
        return env.getProperty(namespace + "client.refresh.interval", Integer.class, 30);
    }

    @Override
    public int getInstanceInfoReplicationIntervalSeconds() {
        return env.getProperty(namespace + "appinfo.replicate.interval", Integer.class, 30);
    }

    @Override
    public int getEurekaServiceUrlPollIntervalSeconds() {
        return env.getProperty(namespace + "serviceUrlPollIntervalMs", Integer.class, 5 * 60 * 1000) / 1000;
    }

    @Override
    public String getProxyHost() {
        return env.getProperty(namespace + "eurekaServer.proxyHost");
    }

    @Override
    public String getProxyPort() {
        return env.getProperty(namespace + "eurekaServer.proxyPort");
    }

    @Override
    public boolean shouldGZipContent() {
        return env.getProperty(namespace + "eurekaServer.gzipContent", Boolean.class, true);
    }

    @Override
    public int getEurekaServerReadTimeoutSeconds() {
        return env.getProperty(namespace + "eurekaServer.readTimeout", Integer.class, 8);
    }

    @Override
    public int getEurekaServerConnectTimeoutSeconds() {
        return env.getProperty(namespace + "eurekaServer.connectTimeout", Integer.class, 5);
    }

    @Override
    public String getBackupRegistryImpl() {
        return env.getProperty(namespace + "backupregistry");
    }

    @Override
    public int getEurekaServerTotalConnections() {
        return env.getProperty(namespace + "eurekaServer.maxTotalConnections", Integer.class, 200);
    }

    @Override
    public int getEurekaServerTotalConnectionsPerHost() {
        return env.getProperty(namespace + "eurekaServer.maxConnectionsPerHost", Integer.class, 50);
    }

    @Override
    public String getEurekaServerURLContext() {
        return env.getProperty(
                namespace + "eurekaServer.context",
                env.getProperty(namespace + "context"));
    }

    @Override
    public String getEurekaServerPort() {
        return env.getProperty(
                namespace + "eurekaServer.port",
                env.getProperty(namespace + "port"));
    }

    @Override
    public String getEurekaServerDNSName() {
        return env.getProperty(
                namespace + "eurekaServer.domainName",
                env.getProperty(namespace + "domainName"));
    }

    @Override
    public boolean shouldUseDnsForFetchingServiceUrls() {
        return env.getProperty(namespace + "shouldUseDns", Boolean.class, false);
    }

    @Override
    public boolean shouldRegisterWithEureka() {
        return env.getProperty(namespace + "registration.enabled", Boolean.class, true);
    }

    @Override
    public boolean shouldPreferSameZoneEureka() {
        return env.getProperty(namespace + "preferSameZone", Boolean.class, true);
    }

    @Override
    public boolean shouldLogDeltaDiff() {
        return env.getProperty(namespace + "printDeltaFullDiff", Boolean.class, false);
    }

    @Override
    public boolean shouldDisableDelta() {
        return env.getProperty(namespace + "disableDelta", Boolean.class, false);
    }

    @Nullable
    @Override
    public String fetchRegistryForRemoteRegions() {
        return env.getProperty(namespace + "fetchRemoteRegionsRegistry");
    }

    @Override
    public String getRegion() {
        return env.getProperty("eureka.region", "us-east-1");
    }

    @Override
    public String[] getAvailabilityZones(String region) {
        return env.getProperty(namespace + "" + region + ".availabilityZones",
                DEFAULT_ZONE).split(",");
    }

    @Override
    public List<String> getEurekaServerServiceUrls(String myZone) {
        String serviceUrls = env.getProperty(namespace + "serviceUrl." + myZone);
        if (serviceUrls == null || serviceUrls.isEmpty()) {
            serviceUrls = env.getProperty(namespace + "serviceUrl." + "default");

        }
        if (serviceUrls != null) {
            return Arrays.asList(serviceUrls.split(","));
        }

        return new ArrayList<>();
    }

    @Override
    public boolean shouldFilterOnlyUpInstances() {
        return env.getProperty(namespace + "shouldFilterOnlyUpInstances", Boolean.class, true);
    }

    @Override
    public int getEurekaConnectionIdleTimeoutSeconds() {
        return env.getProperty(namespace + "eurekaserver.connectionIdleTimeoutInSeconds", Integer.class, 30);
    }

    @Override
    public boolean shouldFetchRegistry() {
        return env.getProperty(namespace + "shouldFetchRegistry", Boolean.class, true);
    }

    @Override
    public String getRegistryRefreshSingleVipAddress() {
        return env.getProperty(namespace + "registryRefreshSingleVipAddress");
    }
}
