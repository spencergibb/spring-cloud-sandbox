package org.springframework.cloud.sample.frontend;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sample.backend.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * Created by sgibb on 6/19/14.
 */
@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HelloClient helloClient;

    //@CircuitBreaker
    @HystrixCommand(fallbackMethod = "getDefaultMessage")
    public String getMessage() {
        return getMessageImpl();
    }

    @HystrixCommand(fallbackMethod = "getDefaultMessage")
    public String sendMessage() {
        return helloClient.hello(new Message("World via POST")).getBody();
    }

    @HystrixCommand(fallbackMethod = "getDefaultMessage")
    public Future<String> getMessageFuture() {
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return getMessageImpl();
            }
        };
    }

    @HystrixCommand(fallbackMethod = "getDefaultMessage")
    public Observable<String> getMessageRx() {
        return new ObservableResult<String>() {
            @Override
            public String invoke() {
                return getMessageImpl();
            }
        };
    }

    @HystrixCommand(fallbackMethod = "getDefaultMessage")
    //TODO: setup hystrix to log errors by default
    public String getMessageFail() {
        throw new RuntimeException("I failed on purpose");
    }

    private String getMessageImpl() {
        /*ResponseEntity<Message> message = restTemplate.getForEntity("http://localhost:7080/hello", Message.class);
        return message.getBody().getBody();*/
        return helloClient.hello().getBody();
    }

    private String getDefaultMessage() {
        return "World Default";
    }
}
