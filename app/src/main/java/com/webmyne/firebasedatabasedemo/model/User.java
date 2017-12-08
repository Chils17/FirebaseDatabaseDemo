package com.webmyne.firebasedatabasedemo.model;

import java.io.Serializable;

/**
 * Created by chiragpatel on 07-12-2017.
 */

public class User implements Serializable {
    private String key;
    private String name;
    private String email;
    private String mobile;

    public User() {
    }

    public User(String key, String name, String email, String mobile) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
