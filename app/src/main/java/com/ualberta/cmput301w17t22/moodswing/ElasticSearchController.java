package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Main controller for interacting for ElasticSearch. This will post things to ElasticSearch
 * as well as download things from ElasticSearch, mostly in the form of participants.
 * <p/>
 * Created by nyitrai on 2/26/2017.
 */

public class ElasticSearchController implements MSController {
    MoodSwing ms = null;
    private static final String FILENAME = "moodswingFile.sav";

    public ElasticSearchController(MoodSwing ms) { this.ms = ms; }

    /**
     * The JestDroidClient is a private static because the client is designed to be a singleton,
     * and not constructed for each request, as shown on the Jest github page under Usage:
     * https://github.com/searchbox-io/Jest/tree/master/jest
     */
    private static JestDroidClient client;

    /**
     * Construct a Jest client using a factory that points to the correct server.
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
     * @param username The participant's username to add to ElasticSearch/Jest.
     */
    public Participant newParticipantByUsername(String username) {

        // Create null participant.
        Participant participant = new Participant(null);

        // Initialize the task.
        AddParticipantByUsernameTask addParticipantByUsernameTask =
                new AddParticipantByUsernameTask();

        // Add the participant.
        addParticipantByUsernameTask.execute(username);
        try {
           participant = addParticipantByUsernameTask.get();
        } catch (Exception e) {
            Log.i("ERROR", "Something went wrong when getting the" +
                    " new participant from the AsyncTask.");
        }
        return participant;
    }

    /**
     * Updates a given participant on elastic search.
     * @param participant The participant to update.
     */
    public void updateParticipantByParticipant(Participant participant) {
        // Get the task.
        UpdateParticipantByParticipantTask updateParticipantByParticipantTask =
                new UpdateParticipantByParticipantTask();

        // Execute the task.
        updateParticipantByParticipantTask.execute(participant);
    }

    /**
     * Creates and returns an array list of mood events for a given participant according to their
     * following list and the filters chosen.
     *
     * @param participant
     * @param activeFilters
     * @return
     */
    public ArrayList<MoodEvent> buildMoodFeed(Participant participant, int[] activeFilters,
                                              String filterTrigger,
                                              String filterEmotion) {

        // Create empty mood feed.
        ArrayList<MoodEvent> moodFeed = new ArrayList<>();

        // Get the task.
        BuildMoodFeedTask buildMoodFeedTask = new BuildMoodFeedTask();

        // Execute the task.
        buildMoodFeedTask.execute(participant, activeFilters, filterTrigger, filterEmotion);

        try {
            moodFeed = buildMoodFeedTask.get();
        } catch (Exception e) {
            Log.i("ERROR", "Something went wrong when getting the" +
                    " mood feed from the AsyncTask.");
        }

        // Sort the mood feed by date.
        if (!moodFeed.isEmpty()) {
            Collections.sort(moodFeed, new Comparator<MoodEvent>() {
                @Override
                public int compare(MoodEvent o1, MoodEvent o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
        }

        return moodFeed;

    }

    public static class BuildMoodFeedTask extends AsyncTask<Object, Void, ArrayList<MoodEvent>> {
        @Override
        protected ArrayList<MoodEvent> doInBackground(Object ... parameters) {
            // Always verify the ElasticSearch config first.
            verifyConfig();

            // Initialize mood feed.
            ArrayList<MoodEvent> moodFeed = new ArrayList<>();

            // Get the participant and the active filters from the parameters.
            Participant participant = (Participant) parameters[0];
            int[] activeFilters = (int[]) parameters[1];
            String filterTrigger = (String) parameters[2];
            String filterEmotion = (String) parameters[3];

            // For each user in the participant's following list, grab their most recent mood event
            // if it passes all the queries.
            ArrayList<String> followingList = participant.getFollowing();
            for (String username : followingList) {

                // Build the query string.
                String query = new QueryBuilder()
                        .build(username, activeFilters, filterTrigger, filterEmotion);

                Log.i("MoodSwing", query);

                // Build Jest/ElasticSearch search.
                Search search = new Search.Builder(query)
                        .addIndex("cmput301w17t22")
                        .addType("Participant")
                        .build();

                try {
                    // Try to execute the search.
                    SearchResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        // Use a gson object that can deserialize our dates properly.
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                                .create();

                        // Get source as string, because getting as an object does not work
                        // for nested objects.
                        String moodEventJson = result.getSourceAsString();

                        Log.i("MoodSwing", moodEventJson);
                        // Get the proper json.
                        moodEventJson = moodEventJson.substring(23, moodEventJson.length() - 1);
                        Log.i("MoodSwing", moodEventJson);

                        // Create the mood event from the json.
                        MoodEvent moodEvent = gson.fromJson(moodEventJson, MoodEvent.class);

                        // Add the mood event to the mood feed.
                        moodFeed.add(moodEvent);
                    }
                    else {
                        // No participant with given username found.
                        Log.i("MoodSwing", "No participant with given username found.");
                    }
                } catch (IOException e) {
                    // TODO: Deal with IOException.
                    Log.i("ERROR", "Something went wrong when trying to grab a participant by username" +
                            "from ElasticSearch.");
                }
            } // end for loop.

            // Return the mood feed.
            return moodFeed;
        }
    }

    /**
     * AsyncTask that gets participants from ElasticSearch. Taken from CMPUT301 ElasticSearch
     * LonelyTwitter Jest Lab.
     * <p/>
     * Works only for grabbing one participant at a time.
     * <p/>
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
            Participant participant = new Participant(null);

            // Create the query string. We do a match search on username.
            String query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"username\" : \"" + username + "\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            // Build Jest/ElasticSearch search.
            Search search = new Search.Builder(query)
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
                    // No participant with given username found. In this case, the participant
                    // returned is the null participant.

                    // This isn't a bad thing, this log just helps us track down
                    // the case when no user is found.
                    Log.i("MoodSwing", "No participant with given username found.");
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
     * <p/>
     * Works only for adding a single participant.
     * <p/>
     * Calling execute(Participant participant) on an instance of AddParticipantByUsernameTask
     * will add a document for the given participant return that participant with its proper
     * Jest id.
     */
    public static class AddParticipantByUsernameTask extends AsyncTask<String, Void, Participant> {
        @Override
        protected Participant doInBackground(String ... params) {
            // Always verify the ElasticSearch config first.
            verifyConfig();

            // Grab the username from the params and create the participant.
            String username = params[0];
            Participant participant = new Participant(username);

            // Need proper date format, so it doesn't get upset when trying to load the date.
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            // Convert to Json.
            String participantJson = gson.toJson(participant);

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
            return participant;
        }
    }

    /**
     * AsyncTask that updates a participant on ElasticSearch. Only handles full updating, not
     * partial updating. This means that the participant to update is removed and replaced with the
     * updated version of itself, not that individual parts of the participant to update are
     * changed.
     * <p/>
     * Works only for one participant at a time.
     * <p/>
     * Calling execute(Participant participant) on an instance of
     * UpdateParticipantByParticipantTask will whole update the document for that participant
     * on ElasticSearch.
     *
     * TODO: offline behavior
     */
    public static class UpdateParticipantByParticipantTask extends AsyncTask<Participant, Void, Void> {
        @Override
        protected Void doInBackground(Participant ... participants) {
            // Verify the ElasticSearch config.
            verifyConfig();

            // Grab the participant.
            Participant participant = participants[0];

            // Need proper date format, so it doesn't get upset when trying to load the date.
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                    .create();
            // Convert to Json.
            String participantJson = gson.toJson(participant);

            // Grab ElasticSearch id.
            String id = participant.getId();

            // Create the index that the Jest droid client will execute on.
            Index index = new Index.Builder(participantJson)
                    .index("cmput301w17t22")
                    .type("Participant")
                    .id(id)
                    .build();

            // Try to update the participant.
            /*try {
                DocumentResult result = client.execute(index);

                if (result.isSucceeded()) {
                    // Result is good.
                    Log.i("MoodSwing", "Participant " + participant.getUsername() +
                            " successfully updated.");
                    Log.i("offlineTest", "success" + result.toString());
                } else {
                    // ElasticSearch is unable to add the participant.
                    Log.i("ERROR", "ElasticSearch was unable to add the participant.");
                    Log.i("offlineTest", "failure" + result.toString());
                }
            } catch (IOException e) {
                Log.i("ERROR", "Something went wrong when adding a participant by" +
                        " username to ElasticSearch.");
                Log.i("offlineTest", "exception");
                saveInFile(index);
            }*/
            executeElasticSearch(index);
            return null;
        }
    }

    //returns true if successful
    public static void executeElasticSearch(Index index){
        // Try to update the participant.
        try {
            DocumentResult result = client.execute(index);

            if (result.isSucceeded()) {
                // Result is good.
                //Log.i("MoodSwing", "Participant " + participant.getUsername() +
                //        " successfully updated.");
                Log.i("offlineTest", "success" + result.toString());
            } else {
                // ElasticSearch is unable to add the participant.
                Log.i("ERROR", "ElasticSearch was unable to add the participant.");
                Log.i("offlineTest", "failure" + result.toString());
            }
        } catch (IOException e) {
            Log.i("ERROR", "Something went wrong when adding a participant by" +
                    " username to ElasticSearch.");
            Log.i("offlineTest", "exception");
            saveInFile(index);
        }
    }

    /**
     * Saves tweets to a specified file in JSON format.
     * @throws FileNotFoundException if file folder doesn't exist
     */
    private static void saveInFile(Index index) {
        try {
            //FileOutputStream fos = openFileOutput(FILENAME,
            //        Context.MODE_PRIVATE); //MODE_PRIVATE is also '0'
            FileOutputStream fos = new FileOutputStream(FILENAME, true);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(index, out);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Handle the Exception properly later
            throw new RuntimeException(); //crashes app
        } catch (IOException e) {
            // TODO Handle the Exception properly later
            throw new RuntimeException(); //crashes app
        }
    }
    
}

