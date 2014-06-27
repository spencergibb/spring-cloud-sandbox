package org.springframework.platform.sample.frontend;

import feign.Headers;
import feign.RequestLine;
import org.springframework.platform.sample.backend.Message;

/**
 * Created by sgibb on 6/26/14.
 */
public interface HelloClient {
    @RequestLine("GET /hello")
    Message hello();

    @RequestLine("POST /hello")
    @Headers("Content-Type: application/json")
    Message hello(Message message);
}
