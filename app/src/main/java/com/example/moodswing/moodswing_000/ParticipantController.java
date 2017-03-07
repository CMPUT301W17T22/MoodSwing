package com.example.moodswing.moodswing_000;

/**
 * Participant Controller.
 *
 * Handles all getting and setting of participants from the model. This may end up doing
 * everything that we wanted the MoodEventController to do, because Participants
 * are our main packages of information. We'll have to see as we go.
 *
 * Created by nyitrai on 3/7/2017.
 */

public class ParticipantController implements MSController {
    MoodSwing ms = null;

    public ParticipantController(MoodSwing ms) { this.ms = ms; }

    /**
     * Loads a participant into the Model's main participant given a username.
     * @param username
     * @return
     */
    public void getMainParticipantByUsername(String username) {

        // Get the main Model class.
        MoodSwing moodSwing = MoodSwingApplication.getMoodSwing();

        // Have the main Model class fetch the user's information.
        moodSwing.getMainParticipantByUsername(username);

        // moodSwing's mainParticipant should have their information if the username
        // has been entered before. If their username has not been entered,
        // moodSwing's mainParticipant's username will be null.
        if (moodSwing.mainParticipant.username == null) {
            moodSwing.newMainParticipantByUsername(username);
        }
    }
}
