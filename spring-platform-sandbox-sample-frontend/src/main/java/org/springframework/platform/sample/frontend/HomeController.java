package org.springframework.platform.sample.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.platform.sample.backend.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by sgibb on 6/19/14.
 */
@Controller
public class HomeController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Application.FrontendProperties frontendProperties;

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        ResponseEntity<Message> message = restTemplate.getForEntity("http://localhost:8080/hello", Message.class);
        model.put("message", "Hello "+message.getBody().getBody());
        model.put("foo", frontendProperties.getFoo());
        model.put("title", "Hello Home");
        model.put("date", new Date());
        //List<Post> posts = postClient.posts();
        model.put("posts", new ArrayList<Object>());
        return "home";
    }
}
