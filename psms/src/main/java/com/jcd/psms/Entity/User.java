package com.jcd.psms.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/9 0009.
 */

@Entity
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
    private String getkey;
    public String getGetkey() {
        return this.getkey;
    }
    public void setGetkey(String getkey) {
        this.getkey = getkey;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 674504457)
    public User(Long id, String username, String password, String getkey) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.getkey = getkey;
    }
    @Generated(hash = 586692638)
    public User() {
    }



}
