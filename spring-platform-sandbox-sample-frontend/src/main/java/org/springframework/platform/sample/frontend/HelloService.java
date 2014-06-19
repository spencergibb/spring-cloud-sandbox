package org.springframework.platform.sample.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.platform.circuitbreaker.annotations.CircuitBreaker;
import org.springframework.platform.sample.backend.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sgibb on 6/19/14.
 */
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    @CircuitBreaker
    public String getMessage() {
        ResponseEntity<Message> message = restTemplate.getForEntity("http://localhost:8080/hello", Message.class);
        return message.getBody().getBody();
    }
}
