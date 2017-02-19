package com.example.moodswing.moodswing_000;

import android.icu.text.MessagePattern;

import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

public class Participant extends User {
    //TODO: refactor so that this is not public. Either add access methods or change OO implementation.
    public FollowingList followingList = new FollowingList();

    public Participant(String username){
        this.username = username;
    }

    /*public void confirmFollowRequest(Participant participant){
        followingList.approveParticipant(participant);
    }

    public void denyFollowRequest(Participant participant){
        followingList.removeParticipant(participant);
    }

    public void createFollowRequest(Participant participant){
        followingList.addParticipant(participant);
    }*/
}
