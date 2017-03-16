package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**Test class for the FollowingController class and its methods
 * Created by bbest on 16/03/17.
 */

public class FollowingControllerTest {
    MoodSwingApplication moodSwingApplication = new MoodSwingApplication();
    MoodSwing ms = new MoodSwing();
    ElasticSearchController elasticSearchController = new ElasticSearchController(ms);
    FollowingController followingController = new FollowingController(ms);
    Participant mainParticipant = new Participant("Johnny");


    @Test
    public void followParticipantTest(){
        //Initialize and send follow request
        Participant receivingParticipant = new Participant("Jimmy");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());
        //use FollowingController to follow
        followingController.followParticipant("Jimmy");
        //assert that the sender has a pending follow request that is the proper receiver
        //assert that the receiver has a pending follower request that is the proper sender
        assertTrue(mainParticipant.getPendingFollowing().contains(receivingParticipant));
        assertTrue(receivingParticipant.getPendingFollowers().contains(mainParticipant));

    }

    @Test
    public void unfollowParticipantTest(){
        //Initialize and send follow request
        Participant receivingParticipant = new Participant("Jimmy");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

//        followingController.followParticipant("Jimmy");
//
//        assertTrue(mainParticipant.getPendingFollowing().contains(receivingParticipant));
//        assertTrue(receivingParticipant.getPendingFollowers().contains(mainParticipant));
//
//        followingController.unfollowParticipant(receivingParticipant);
//
//        assertFalse(mainParticipant.getPendingFollowing().contains(receivingParticipant));
//        assertFalse(receivingParticipant.getPendingFollowers().contains(mainParticipant));

    }

    @Test
    public void approveRequestTest(){
        //Initialize and send follow request
        Participant receivingParticipant = new Participant("Jimmy");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

        //followingController.followParticipant("Jimmy");

        //in order to test this properly somehow we need a second FollowingController for Jimmy
        //that following controller calls the approveRequest method.

    }

    @Test
    public void declineRequestTest(){
        //Initialize and send follow request
        Participant receivingParticipant = new Participant("Jimmy");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

        //followingController.followParticipant("Jimmy");

    }

    @Test
    public void blockParticipant(){
        //Initialize and send follow request
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

       // followingController.followParticipant("Jimmy");

    }
}
