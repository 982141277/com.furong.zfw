package com.jcd.psms.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
@Entity
public class Message {
    @Id
    private Long id;
    private Long current;
    private String user;
    private String to;
    private String type;
    private String message;
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTo() {
        return this.to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getUser() {
        return this.user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Long getCurrent() {
        return this.current;
    }
    public void setCurrent(Long current) {
        this.current = current;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 848099144)
    public Message(Long id, Long current, String user, String to, String type,
            String message) {
        this.id = id;
        this.current = current;
        this.user = user;
        this.to = to;
        this.type = type;
        this.message = message;
    }
    @Generated(hash = 637306882)
    public Message() {
    }


}
