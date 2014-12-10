package org.springframework.cloud.sample.backend;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Spencer Gibb
 */
@Slf4j
@Data
public class MyService {
	private final String name;
}
