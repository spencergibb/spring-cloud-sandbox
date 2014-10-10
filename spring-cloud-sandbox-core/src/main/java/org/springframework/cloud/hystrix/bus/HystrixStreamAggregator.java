package org.springframework.cloud.hystrix.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import rx.subjects.PublishSubject;

import java.io.IOException;
import java.util.Map;

/**
 * @author Spencer Gibb
 */
@MessageEndpoint
@Slf4j
public class HystrixStreamAggregator {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PublishSubject<Map<String, Object>> subject;

    @ServiceActivator(inputChannel = "hystrixStreamAggregator")
    public void handle(String payload) {
        try {
            Map<String, Object> map = objectMapper.readValue(payload, Map.class);
            Map<String, Object> data = getPayloadData(map);

            log.info("Received hystrix stream payload: {}", data);
            subject.onNext(data);
        } catch (IOException e) {
            log.info("Error receiving hystrix stream payload: " + payload, e);
        }
    }

    public static Map<String, Object> getPayloadData(Map<String, Object> jsonMap) {
        Map<String, Object> origin = (Map<String, Object>) jsonMap.get("origin");
        //TODO: instanceid template
        String instanceId = origin.get("serviceId")+":"+origin.get("host")+":"+origin.get("port");
        //String instanceId = origin.get("serviceId") + ":" + origin.get("ipAddress") + ":" + origin.get("port");
        Map<String, Object> data = (Map<String, Object>) jsonMap.get("data");

        data.put("instanceId", instanceId);
        return data;
    }

}
