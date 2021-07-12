package com.wolfking.jeesite.modules.api.entity.md;

public class RestSession {

    private String session;
    private String userId;

    public RestSession(){}


    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
