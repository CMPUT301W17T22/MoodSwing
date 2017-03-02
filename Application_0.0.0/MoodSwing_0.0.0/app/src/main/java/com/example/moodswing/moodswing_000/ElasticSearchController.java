package com.example.moodswing.moodswing_000;

import android.os.AsyncTask;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Main controller for interacting for ElasticSearch. This will post things to ElasticSearch
 * as well as download things from ElasticSearch. Mostly in the form of participants.
 *
 * Created by nyitrai on 2/26/2017.
 */

public class ElasticSearchController implements MSController {
    MoodSwing ms = null;

    public ElasticSearchController(MoodSwing ms) { this.ms = ms; }

    private static JestDroidClient client;

    /**
     * Taken from verifyConfig from CMPUT 301 ElasticSearch LonelyTwitter JEST Lab.
     */
    public static void verifyConfig() {
        DroidClientConfig.Builder builder =
                new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");

        DroidClientConfig config = builder.build();
        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);
        client = (JestDroidClient) factory.getObject();
    }

    /**
     * Updates participant information on ElasticSearch by username.
     *
     * This is called when some change is made to the participant info (new mood event,
     * edited mood event, change in following/follower list.)
     * @param username The Participant to update's username.
     */
    public void updateParticipantByUsername(String username) {

    }

    /**
     * Function to grab a participant from ElasticSearch by username.
     * @param username
     */
    public Participant getParticipantByUsername(String username) {
        Participant participant = new Participant(username);

        return participant;
    }

    /**
     * AsyncTask that gets participants from ElasticSearch. Taken from CMPUT301 ElasticSearch
     * LonelyTwitter JEST Lab.
     */
    public static class GetParticipantTask extends AsyncTask<String, Void, Participant> {
        @Override
        protected Participant doInBackground(String ... params) {
            verifyConfig();

            // Initialize new Participant without username.
            // TODO: Change "null" here to something more informative/correct.
            // Null is just a placeholder.
            Participant participantFromOnline = new Participant(null);

            // Build ElasticSearch search.
            String searchString = params[0];
            Search search = new Search.Builder(searchString)
                    .addIndex("MoodSwing")
                    .addType("Participant")
                    .build();

            try {
                // Try to execute the search.
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    participantFromOnline = result.getSourceAsObject(Participant.class);
                }
            } catch (IOException e) {
                // TODO: Deal with IOException.
                e.printStackTrace();
            }
            return participantFromOnline;
        }
    }

    // TODO: Need methods for adding a new participant, and for downloading (a) participant(s).
}
