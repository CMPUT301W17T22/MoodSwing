package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

/**
 * Class to generate elastic search queries for the purpose of creating a Mood Feed. Not useful
 * for filtering the Mood History, as this only looks at the most recent mood event of a user.
 * <p/>
 * Built with the help of http://www.jsoneditoronline.org.
 * <p/>
 * Created by nyitrai on 3/31/2017.
 */

public class QueryBuilder {

    public String build(String username, int[] activeFilters,
                        String filterTrigger, String filterEmotion) {

        // Get the states of the filters.
        boolean weekFilterActive = activeFilters[0] == 1;
        boolean emotionFilterActive = activeFilters[1] == 1;
        boolean triggerFilterActive = activeFilters[2] == 1;

        // Source filtering for just the most recent mood event,
        // match search on the username,
        // (optional) range search on the most recent mood event's date,
        // (optional) match search on the most recent mood event's trigger,
        // (optional) match search on the most recent mood event's emotional state's description.

        // What the whole query should look like if all the filters are in place.
        String whole =
                "{\n" +
                "  \"_source\": \"mostRecentMoodEvent\",\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        { \"match\": { \"username\": \"username\" }},\n" +
                "        { \"range\": { \"mostRecentDate\": {\"gte\": \"now-1w\", \"lt\": \"now\" }}},\n" +
                "        { \"match\": { \"mostRecentTrigger\": \"filterTrigger\" }},\n" +
                "        { \"match\": { \"mostRecentEmotionalStateDescription\": \"filterEmotion\" }},\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String partA =
                "{\n" +
                "  \"_source\": \"mostRecentMoodEvent\",\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        { \"match\": { \"username\": \"" + username + "\" }}";

        String partB_weekFilter =
                "        { \"range\": { \"mostRecentDate\": {\"gte\": \"now-1w\", \"lte\": \"now\" }}}";

        String partB_triggerFilter =
                "        { \"match\": { \"mostRecentTrigger\": \"" + filterTrigger + "\" }}";

        String partB_emotionFilter =
                "        { \"match\": { \"mostRecentEmotionalStateDescription\": \"" + filterEmotion + "\" }}\n";

        String partC =
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String endFilterWithOthers = ",\n";

        String endFilterAlone = "\n";

        String query = partA;

        // Check which filters are active and build the appropriate string.
        if (weekFilterActive || triggerFilterActive || emotionFilterActive ) {
            // Some filter is active.
            query += endFilterWithOthers;

            if (weekFilterActive) {
                // The week filter is active.
               query += partB_weekFilter;

                if (triggerFilterActive || emotionFilterActive) {
                    // Some other filter is also active.
                    query += endFilterWithOthers;

                } else {
                    // The week filter is the last filter we need to add.
                    query += endFilterAlone;
                }
            }

            if (triggerFilterActive) {
                // The trigger filter is active.
                query += partB_triggerFilter;

                if (emotionFilterActive) {
                    // The emotion filter is also active.
                    query += endFilterWithOthers;
                } else {
                    // The trigger filter is the last filter we need to add.
                    query += endFilterAlone;
                }
            }

            if (emotionFilterActive) {
                // The emotion filter is active.
                query += partB_emotionFilter;
            }

            query += partC;

        } else {
            // No filters are active.
            query += endFilterAlone + partC;
        }

        Log.i("QueryBuilder", query);

        return query;
    }
}