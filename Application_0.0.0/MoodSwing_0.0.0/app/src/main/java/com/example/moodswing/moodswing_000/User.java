package com.example.moodswing.moodswing_000;

/**
 * A person who uses the app, identified by their username.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 */

public abstract class User {
    protected String username;

    public String getUsername() {
        return username;
    }

    /**
     * Compares Users using their unique usernames
     *
     * @param otherObject
     * @return whether or not the usernames are the same
     */
    @Override
    public boolean equals(Object otherObject){
        if(this == otherObject){
            return true;
        }
        if(!(otherObject instanceof User)){
            return false;
        }
        User otherUser = (User) otherObject;
        return(this.username == otherUser.username);
    }
}
