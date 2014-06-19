package org.springframework.platform.sample.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by sgibb on 6/19/14.
 */
@Controller
public class HomeController {

    @Autowired
    HelloService helloService;

    @Autowired
    FrontendProperties frontendProperties;

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        model.put("message", "Hello "+helloService.getMessage());
        model.put("foo", frontendProperties.getFoo());
        model.put("title", "Hello Home");
        model.put("date", new Date());
        //List<Post> posts = postClient.posts();
        model.put("posts", new ArrayList<Object>());
        return "home";
    }
}
