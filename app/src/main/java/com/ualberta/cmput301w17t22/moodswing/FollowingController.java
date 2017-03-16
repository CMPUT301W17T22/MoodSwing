package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

/** Controller for handling a FollowingList of participants that the mainParticipant follows.
 * <p/>
 * Created by nyitrai on 3/1/2017.
 */

public class FollowingController implements MSController {
    MoodSwing ms = null;
    ElasticSearchController elasticSearchController;
    Participant mainParticipant;

    public FollowingController(MoodSwing ms) {

        elasticSearchController = new ElasticSearchController(ms);

        // Get MoodSwingController.
        MoodSwingController moodSwingController =
                MoodSwingApplication.getMoodSwingController();

        // Load MainParticipant.
        mainParticipant = moodSwingController.getMainParticipant();

        this.ms = ms;
    }



    /**This method adds a participant to the FollowingList of the mainParticipant once the
     * request is accepted. Controller calls upon the similar method in the Participant class.
     * @param participant
     */
    //TODO: Implement error handle when the string entered is not an existing participant
    public void followParticipant(String participant){

        Participant newFollowedParticipant = elasticSearchController.getParticipantByUsername(participant);


        mainParticipant.followParticipant(newFollowedParticipant);
        mainParticipant.createFollowerRequest(newFollowedParticipant);

        //not sure if update is necessary?
        elasticSearchController.updateParticipantByParticipant(mainParticipant);
    }

    /**
     * This method removes a participant from the mainParticipants FollowingList.
     * Secondly it calls createUnfollowEvent() which sends an unfollow indicator to the
     * receivingparticipant to invoke unfollowEvent to remove this participant form their
     * followerList.
     * @param receivingparticipant
     */
    //TODO:Need to create a new type of request that tells the receiving participant to remove

    public void unfollowParticipant(Participant receivingparticipant){

        mainParticipant.unfollowParticipant(receivingparticipant);

        mainParticipant.createUnfollowEvent(receivingparticipant);

        //not sure if update is necessary?
        elasticSearchController.updateParticipantByParticipant(mainParticipant);
    }

    /**
     * Approves a pending request for a participant to follow the mainParticipant.
     * @return
     */
    public boolean approveRequest(Participant participant){

        mainParticipant.approveFollowerRequest(participant);
        return true;
    }
    /**
     * Declines a pending request for a participant to follow the .
     * @return
     */
    public boolean declineRequest(Participant participant){

        mainParticipant.declineFollowerRequest(participant);
        return true;
    }

    /**
     * The controller will invoke the blockParticipant method from the main participant.
     * @param participant
     * @return
     */
    public boolean blockParticipant(Participant participant){
        mainParticipant.blockParticipant(participant);
        return true;
    }


}
