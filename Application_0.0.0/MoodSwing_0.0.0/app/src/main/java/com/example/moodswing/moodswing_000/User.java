package com.example.moodswing.moodswing_000;

/**
 * Created by Fred on 2017-02-18.
 */

public abstract class User {
    protected String username;

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object otherObject){
        if(this == otherObject){
            return true;
        }
        if(!(otherObject instanceof FollowRequest)){
            return false;
        }
        User otherUser = (User) otherObject;
        return(this.username == otherUser.username);
    }
}
