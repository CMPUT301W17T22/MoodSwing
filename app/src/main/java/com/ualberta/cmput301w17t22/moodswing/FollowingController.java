package com.ualberta.cmput301w17t22.moodswing;

/** Controller for handling a FollowingList of participants that the mainParticipant follows.
 * <p/>
 * Created by nyitrai on 3/1/2017.
 */

public class FollowingController implements MSController {
    MoodSwing ms = null;
    //main Participant that is called on to access their approval lists.
    Participant mainParticipant;

    public FollowingController(MoodSwing ms) { this.ms = ms; }

    /**
     * Loads in the mainParticipant so we can manipulate their ApprovalLists.
     */
    //TODO: Could be taken from another file?
    public void loadMainParticipant(){

    }

    /**This method adds a participant to the FollowingList of the mainParticipant once the
     * request is accepted. Controller calls upon the similar method in the Participant class.
     * @param participant
     */
    //TODO: Implement body
    public void followParticipant(Participant participant){

    }

    /**
     * This method removes a participant from the mainParticipants FollowingList.
     * @param participant
     */
    //TODO:Implement body
    /* might be need take in a index from the ListView from which the item was clicked
       to make it easier to remove from the list.
    */
    public void unfollowParticipant(Participant participant){

    }

    /**
     * Approves a pending request for a participant to follow the mainParticipant.
     * @return
     */
    public boolean approveRequest(){

        return true;
    }
    /**
     * Declines a pending request for a participant to follow the .
     * @return
     */
    public boolean declineRequest(){

        return true;
    }



}
