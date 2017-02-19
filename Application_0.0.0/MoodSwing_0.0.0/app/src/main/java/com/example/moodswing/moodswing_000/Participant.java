package com.example.moodswing.moodswing_000;

import android.icu.text.MessagePattern;

import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

public class Participant extends User {
    //might only need one of the following lists depending on implementation
    private ArrayList<Participant> following = new ArrayList<Participant>();
    private ArrayList<Participant> followers = new ArrayList<Participant>();

    public Participant(String username){
        this.username = username;
    }
}
