package com.example.moodswing.moodswing_000;

import android.icu.text.MessagePattern;

import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

public class Participant extends User {
    private FollowingList followingList = new FollowingList();

    public Participant(String username){
        this.username = username;
    }
}
