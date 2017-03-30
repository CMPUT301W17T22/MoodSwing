package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/** This class generates data for our app for demoing and sample purposes.
 * Created by nyitrai on 3/29/2017.
 */

public class DataGenerator {

    /**
     * The method that actually generates data.
     */
    public void generate() {

        // Get the elastic search controller.
        ElasticSearchController elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Check if paragon56 is already on ElasticSearch.
        Participant paragon56 = elasticSearchController.getParticipantByUsername("paragon56");
        if (paragon56 == null) {

            // If paragon56 is not already on ElasticSearch, create it.
            paragon56 = generateParagon56();

            // Put the participant on ElasticSearch.
            elasticSearchController.updateParticipantByParticipant(paragon56);
        }
    }


    public Participant generateParagon56() {
        Log.i("MoodSwing", "Generating user \"paragon56\".");

        Participant paragon56 = new Participant("paragon56");

        ArrayList<MoodEvent> moodHistory = new ArrayList<>();

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 29,
                "Happiness",
                "Beat my highscore!",
                "Alone",
                53.472269d,
                -113.535082d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 27,
                "Sadness",
                "Physics is hard.",
                "With One Other Person",
                53.528174d,
                -113.525643d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 23,
                "Shame",
                "McDonalds",
                "With Two To Several People",
                53.453379d,
                -113.513932d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 22,
                "Confusion",
                "Chemistry?!",
                "Alone",
                53.525953d,
                -113.521772d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 20,
                "Happiness",
                "HBDay to me!",
                "With A Crowd",
                53.472269d,
                -113.535082d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 18,
                "Anger",
                "Lonely dog!",
                "Alone",
                53.540932d,
                -113.503646d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 17,
                "Sadness",
                "Amazing play.",
                "With One Other Person",
                53.543280d,
                -113.488516d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 16,
                "Happiness",
                "Shopping!",
                "With Two To Several People",
                53.452763d,
                -113.524860d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 15,
                "Fear",
                "Car crash!",
                "Alone",
                53.483112d,
                -113.533462d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 12,
                "Happiness",
                "Matt's House",
                "With Two To Several People",
                53.515834d,
                -113.643278d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 11,
                "Disgust",
                "Bit rotten apple",
                "Alone",
                53.472269d,
                -113.535082d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 9,
                "Surprise",
                "Surprize zoo trip!",
                "With Two To Several People",
                53.511539d,
                -113.554912d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 6,
                "Happiness",
                "Anniversary <3",
                "With One Other Person",
                53.543488d,
                -113.497745d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 5,
                "Shame",
                "Matt's House. Pizza.",
                "With Two To Several People",
                53.515834d,
                -113.643278d));

        moodHistory.add(createMoodEvent(paragon56.getUsername(),
                2017, 9, 2,
                "Disgust",
                "Textbook prices!",
                "With Two To Several People",
                53.525119d,
                -113.528061d));

        // Add all the mood events to the participant.
        for (MoodEvent moodEvent : moodHistory) {
            paragon56.addMoodEvent(moodEvent);
        }

        return paragon56;
    }

    /**
     * Function to handle all the messy parts of creating a mood event from scratch. Takes in
     * the basic parameters to create the mood event.
     *
     * Right now, attaches no image to the mood event.
     */
    private MoodEvent createMoodEvent(String username,
                                      int year, int month, int day,
                                      String emotionalStateName,
                                      String trigger,
                                      String socialSituationName,
                                      double latitude, double longitude) {

        // Create the date for the mood event.
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        Date date = calendar.getTime();

        // Create the emotional state.
        EmotionalState emotionalState =
                new EmotionalStateFactory().createEmotionalStateByName(emotionalStateName);

        // Create the social situation.
        SocialSituation socialSituation =
                new SocialSituationFactory().createSocialSituationByName(socialSituationName);

        return new MoodEvent(username,
                date,
                emotionalState,
                trigger,
                socialSituation,
                latitude,
                longitude,
                null);
    }
}
