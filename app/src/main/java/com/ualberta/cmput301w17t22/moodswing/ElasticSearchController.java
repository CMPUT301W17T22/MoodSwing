package com.ualberta.cmput301w17t22.moodswing;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
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

    /**
     * The JestDroidClient is a private static because the client is designed to be a singleton,
     * and not constructed for each request, as shown on the Jest github page under Usage:
     * https://github.com/searchbox-io/Jest/tree/master/jest
     */
    private static JestDroidClient client;

    /**
     * Construct a Jest client using a factory.
     */
    public static void verifyConfig() {
        // We use the CMPUT301 ElasticSearch server.
        DroidClientConfig.Builder builder =
                new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");

        DroidClientConfig config = builder.build();
        JestClientFactory factory = new JestClientFactory();
        factory.setDroidClientConfig(config);

        // Grab the client object.
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
     * Function to grab a participant from ElasticSearch by username. Calls on and executes the
     * getParticipantByUsernameTask.
     * @param username The username of the participant we want to grab.
     */
    public Participant getParticipantByUsername(String username) {

        // Initialize null participant. If no user is found with the given username,
        // the participant will remain null.
        Participant participant = new Participant(null);

        // Initialize the task.
        GetParticipantByUsernameTask getParticipantByUsernameTask =
                new GetParticipantByUsernameTask();

        // Try to grab the Participant by the username.
        getParticipantByUsernameTask.execute(username);
        try {
            participant = getParticipantByUsernameTask.get();
        } catch (Exception e) {
            Log.i("ERROR", "Something went wrong when grabbing the participant by username" +
                    "from the AsyncTask.");
        }
        return participant;
    }

    /**
     * Function to add a participant to ElasticSearch/Jest.
     * Calls on and executes the addParticipantTask.
     *
     * @param participant The participant to add to ElasticSearch/Jest.
     */
    public void newParticipantByParticipant(Participant participant) {

        // Initialize the task.
        AddParticipantTask addParticipantTask = new AddParticipantTask();

        // Add the participant.
        addParticipantTask.execute(participant);

        // TODO: Verify that the participant is added.
    }

    /**
     * AsyncTask that gets participants from ElasticSearch. Taken from CMPUT301 ElasticSearch
     * LonelyTwitter Jest Lab.
     *
     * Currently intended only to grab one username at a time.
     * We might be able to expand on this later, and it'll probably be useful to do so for the
     * following/follower lists we need to grab participants for.
     *
     * Calling execute(String username) on an instance of GetParticipantByUsernameTask
     * will search for documents under the username argument.
     */
    public static class GetParticipantByUsernameTask extends AsyncTask<String, Void, Participant> {
        @Override
        protected Participant doInBackground(String ... parameters) {
            // Always verify the ElasticSearch config first.
            verifyConfig();

            // Grab username from the parameters.
            String username = parameters[0];

            // Initialize participant to be gotten from ElasticSearch.
            // TODO: Make sure there is some sort of null participant constructor.
            Participant participant = new Participant(null);

            // Build ElasticSearch search.
            Search search = new Search.Builder(username)
                    .addIndex("cmput301w17t22")
                    .addType("Participant")
                    .build();

            try {
                // Try to execute the search.
                SearchResult result = client.execute(search);

                if (result.isSucceeded()) {
                    // It says that getSourceAsObject is deprecated but I dont know
                    // what it is replaced by.
                    participant = result.getSourceAsObject(Participant.class);
                }
                else {
                    // No participant with given username found.
                    // This isn't a bad thing, this log just helps us track down
                    // the case when no user is found.

                    // TODO: In this case, a new document should be made on ElasticSearch for the
                    // participant.
                    Log.i("ERROR", "No participant with given username found.");
                }
            } catch (IOException e) {
                // TODO: Deal with IOException.
                Log.i("ERROR", "Something went wrong when trying to grab a participant by username" +
                        "from ElasticSearch.");
            }
            return participant;
        }
    }

    /**
     * AsyncTask that adds a new participant to ElasticSearch. Taken from CMPUT301 ElasticSearch
     * LonelyTwitter Jest Lab.
     *
     * Currently should work for adding multiple participants.
     *
     * Calling execute(Participant ... participants) on an instance of AddParticipantByUsernameTask
     * will add a document for the given participant.
     */
    public static class AddParticipantTask extends AsyncTask<Participant, Void, Void> {
        @Override
        protected Void doInBackground(Participant ... participants) {
            // Always verify the ElasticSearch config first.
            verifyConfig();

            // Add all participants in participants.
            for (Participant participant : participants) {

                Gson gson = new Gson();

                String participantJson = gson.toJson(participant);
                Log.i("ERROR", participantJson);


                // Create the index that the Jest droid client will execute on.
                Index index = new Index.Builder(participantJson)
                        .index("cmput301w17t22")
                        .type("Participant")
                        .build();

                // Try to post the participant.
                try {
                    DocumentResult result = client.execute(index);

                    if (result.isSucceeded()) {
                        // Result is good, update the participant's information to include
                        // the ElasticSearch id.
                        participant.setId(result.getId());

                    } else {
                        // ElasticSearch is unable to add the participant.
                        Log.i("ERROR", "ElasticSearch was unable to add the participant.");
                    }
                } catch (IOException e) {
                    Log.i("ERROR", "Something went wrong when adding a participant by" +
                            " username to ElasticSearch.");
                }
            }
            return null;
        }
    }
}
