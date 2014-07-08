package org.springframework.platform.netflix.eureka;

import com.netflix.appinfo.AbstractInstanceConfig;
import com.netflix.appinfo.DataCenterInfo;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sgibb on 7/7/14.
 */
public class EnvironmentEurekaInstanceConfig extends AbstractInstanceConfig {
    private static final String UNKNOWN_APPLICATION = "unknown";
    private static final String DEFAULT_STATUSPAGE_URLPATH = "/Status";
    private static final String DEFAULT_HOMEPAGE_URLPATH = "/";
    private static final String DEFAULT_HEALTHCHECK_URLPATH = "/healthcheck";
    private static final String APP_GROUP_ENV_VAR_NAME = "NETFLIX_APP_GROUP";

    protected final Environment env;
    protected String namespace = "eureka.";
    private String propSecurePort = namespace + "securePort";
    private String propSecurePortEnabled = propSecurePort + ".enabled";
    private String propNonSecurePort;
    private String propName;
    private String propPortEnabled;
    private String propLeaseRenewalIntervalInSeconds;
    private String propLeaseExpirationDurationInSeconds;
    private String propSecureVirtualHostname;
    private String propVirtualHostname;
    private String propMetadataNamespace;
    private String propASGName;
    private String propAppGroupName;

    public EnvironmentEurekaInstanceConfig(Environment env) {
        this.env = env;
        init(namespace);
    }

    public EnvironmentEurekaInstanceConfig(String namespace, DataCenterInfo info, Environment env) {
        super(info);
        this.env = env;
        init(namespace);
    }

    public EnvironmentEurekaInstanceConfig(String namespace, Environment env) {
        this.env = env;
        init(namespace);
    }

    @Override
    public boolean isInstanceEnabledOnit() {
        return env.getProperty(namespace + "traffic.enabled", Boolean.class, super.isInstanceEnabledOnit());
    }

    @Override
    public int getNonSecurePort() {
        return env.getProperty("server.port", Integer.class,
                env.getProperty(propNonSecurePort, Integer.class, super.getNonSecurePort()));
    }

    @Override
    public int getSecurePort() {
        return env.getProperty(propSecurePort, Integer.class, super.getSecurePort());
    }

    @Override
    public boolean isNonSecurePortEnabled() {
        return env.getProperty(propPortEnabled, Boolean.class, super.isNonSecurePortEnabled());
    }

    @Override
    public boolean getSecurePortEnabled() {
        return env.getProperty(propSecurePortEnabled, Boolean.class, super.getSecurePortEnabled());
    }

    @Override
    public int getLeaseRenewalIntervalInSeconds() {
        return env.getProperty(propLeaseRenewalIntervalInSeconds, Integer.class, super.getLeaseRenewalIntervalInSeconds());
    }

    @Override
    public int getLeaseExpirationDurationInSeconds() {
        return env.getProperty(propLeaseExpirationDurationInSeconds, Integer.class, super.getLeaseExpirationDurationInSeconds());
    }

    @Override
    public String getVirtualHostName() {
        if (this.isNonSecurePortEnabled()) {
            return env.getProperty(propVirtualHostname, super.getVirtualHostName());
        } else {
            return null;
        }
    }

    @Override
    public String getSecureVirtualHostName() {
        if (this.getSecurePortEnabled()) {
            return env.getProperty(propSecureVirtualHostname, super.getSecureVirtualHostName());
        } else {
            return null;
        }
    }

    @Override
    public String getASGName() {
        return env.getProperty(propASGName, super.getASGName());
    }

    /**
     * Gets the metadata map associated with the instance. The properties that
     * will be looked up for this will be <code>namespace + ".metadata"</code>.
     *
     * <p>
     * For instance, if the given namespace is <code>eureka.appinfo</code>, the
     * metadata keys are searched under the namespace
     * <code>eureka.appinfo.metadata</code>.
     * </p>
     */
    @Override
    public Map<String, String> getMetadataMap() {
        //FIXME: hacked, does this work? How do I know?
        Map<String, String> metadataMap = new LinkedHashMap<>();
        String subsetPrefix = propMetadataNamespace.charAt(propMetadataNamespace.length() - 1) == '.'
                ? propMetadataNamespace.substring(0, propMetadataNamespace.length() - 1)
                : propMetadataNamespace;

        Map<String, Object> subProperties = new RelaxedPropertyResolver(env).getSubProperties(subsetPrefix);
        for (Map.Entry<String, Object> entry : subProperties.entrySet()) {
            metadataMap.put(entry.getKey(), entry.getValue().toString());
        }
        return metadataMap;
    }

    @Override
    public String getAppname() {
        return env.getProperty("spring.application.name", env.getProperty(propName, UNKNOWN_APPLICATION)).trim();
    }

    @Override
    public String getAppGroupName() {
        return env.getProperty(propAppGroupName, env.getProperty(APP_GROUP_ENV_VAR_NAME, UNKNOWN_APPLICATION)).trim();
    }

    public String getIpAddress() {
        return super.getIpAddress();
    }


    @Override
    public String getStatusPageUrlPath() {
        return env.getProperty(namespace + "statusPageUrlPath", DEFAULT_STATUSPAGE_URLPATH);
    }

    @Override
    public String getStatusPageUrl() {
        return env.getProperty(namespace + "statusPageUrl");
    }


    @Override
    public String getHomePageUrlPath() {
        return env.getProperty(namespace + "homePageUrlPath", DEFAULT_HOMEPAGE_URLPATH);
    }

    @Override
    public String getHomePageUrl() {
        return env.getProperty(namespace + "homePageUrl");
    }
    @Override
    public String getHealthCheckUrlPath() {
        return env.getProperty(namespace + "healthCheckUrlPath", DEFAULT_HEALTHCHECK_URLPATH);
    }

    @Override
    public String getHealthCheckUrl() {
        return env.getProperty(namespace + "healthCheckUrl");
    }

    @Override
    public String getSecureHealthCheckUrl() {
        return env.getProperty(namespace + "secureHealthCheckUrl");
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    private void init(String namespace) {
        this.namespace = namespace;
        propSecurePort = namespace + "securePort";
        propSecurePortEnabled = propSecurePort + ".enabled";
        propNonSecurePort = namespace + "port";

        propName = namespace + "name";
        propPortEnabled = propNonSecurePort + ".enabled";
        propLeaseRenewalIntervalInSeconds = namespace + "lease.renewalInterval";
        propLeaseExpirationDurationInSeconds = namespace + "lease.duration";
        propSecureVirtualHostname = namespace + "secureVipAddress";
        propVirtualHostname = namespace + "vipAddress";
        propMetadataNamespace = namespace + "metadata.";
        propASGName = namespace + "asgName";
        propAppGroupName = namespace + "appGroup";
    }
}
