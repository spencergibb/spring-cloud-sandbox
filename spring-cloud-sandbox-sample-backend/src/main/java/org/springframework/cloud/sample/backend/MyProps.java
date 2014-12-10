package org.springframework.cloud.sample.backend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Spencer Gibb
 */
@ConfigurationProperties("myprops")
@Data
public class MyProps {
	private String name = "UNKNOWN";
}
