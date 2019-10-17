package com.wows.status;

public class User {

    private String userName;
    private String  userId;


    public User(String userName, String userId){

        this.userName = userName;
        this.userId   = userId;


    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }
}
