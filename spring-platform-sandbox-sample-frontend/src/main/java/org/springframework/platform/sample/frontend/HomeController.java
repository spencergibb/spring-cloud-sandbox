package org.springframework.platform.sample.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
    public String home(Map<String, Object> model, HttpServletRequest req) {
        String message = helloService.sendMessage();
        model.put("message", "Hello "+ message);
        values(model, req);
        return "home";
    }

    @RequestMapping("/future")
    public String future(Map<String, Object> model, HttpServletRequest req) throws ExecutionException, InterruptedException {
        model.put("message", "Hello Future "+helloService.getMessageFuture().get());
        values(model, req);
        return "home";
    }

    @RequestMapping("/rx")
    public String rx(Map<String, Object> model, HttpServletRequest req)  {
        model.put("message", "Hello Observable "+helloService.getMessageRx().toBlockingObservable().first());
        values(model, req);
        return "home";
    }

    @RequestMapping("/fail")
    public String fail(Map<String, Object> model, HttpServletRequest req) {
        model.put("message", "Hello "+helloService.getMessageFail());
        values(model, req);
        return "home";
    }

    private void values(Map<String, Object> model, HttpServletRequest req) {
        model.put("foo", frontendProperties.getFoo());
        model.put("title", "Hello Home");
        model.put("date", new Date());
        model.put("port", req.getLocalPort());
        //List<Post> posts = postClient.posts();
        model.put("posts", new ArrayList<>());
    }

    @RequestMapping("/loglevels")
    @ResponseBody
    public Map<String, Object> loglevels() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Logger logger = LoggerFactory.getLogger("loglevels");
        map.put("trace", logger.isTraceEnabled());
        map.put("debug", logger.isDebugEnabled());
        map.put("info",  logger.isInfoEnabled());
        map.put("warn",  logger.isWarnEnabled());
        map.put("error", logger.isErrorEnabled());
        return map;
    }
}
