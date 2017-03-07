package com.ualberta.cmput301w17t22.moodswing;

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

    /**
     * based on code from:
     * http://www.mkyong.com/java/java-how-to-overrides-equals-and-hashcode/
     * grab date: 2017-03-03
     *
     * @return hash code
     */
    @Override
    public int hashCode(){
        int hash = 17;
        hash = 31 * hash + username.hashCode();
        return hash;
    }
}
