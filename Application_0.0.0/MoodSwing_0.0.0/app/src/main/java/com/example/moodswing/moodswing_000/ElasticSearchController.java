package com.example.moodswing.moodswing_000;

/**
 * Main controller for interacting for ElasticSearch. This will post things to ElasticSearch
 * as well as download things from ElasticSearch. Mostly in the form of participants.
 *
 * Created by nyitrai on 2/26/2017.
 */

public class ElasticSearchController implements MSController {

    /**
     * Updates participant information on ElasticSearch.
     *
     * This is called when some change is made to the participant info (new mood event,
     * edited mood event, change in following/follower list.)
     * @param participant
     */
    public void updateParticipant(Participant participant) {

    }

    // TODO: Need methods for adding a new participant, and for downloading (a) participant(s).
}
