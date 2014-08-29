package org.springframework.cloud.sample.backend;

import java.util.Date;

public class Message {
    private String body;
    private Date date;

    private Message() {}

    public Message(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
