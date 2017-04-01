package com.ualberta.cmput301w17t22.moodswing;

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

        String partA =
                "{\n" +
                "  \"_source\": \"mostRecentMoodEvent\",\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": [\n" +
                "        {\n" +
                "          \"match\": {\n" +
                "            \"username\": \"" + username + "\"\n" +
                "          }\n";

        String partB_noFilters =
                "        }\n";

        String partB_withFilters =
                "        },\n" +
                "        {\n" +
                "          \"nested\": {\n" +
                "            \"path\": \"mostRecentMoodEvent\",\n" +
                "            \"query\": {\n" +
                "              \"bool\": {\n" +
                "                \"must\": [\n";

        String partC_weekFilter =
                "                  {\n" +
                "                    \"range\": {\n" +
                "                      \"mostRecentMoodEvent.date\": {\n" +
                "                        \"gte\": \"now-1w\",\n" +
                "                        \"lt\": \"now\"\n" +
                "                      }\n" +
                "                    }\n" +
                "                  }";

        String partC_triggerFilter =
                "                  {\n" +
                "                    \"match\": {\n" +
                "                      \"mostRecentMoodEvent.trigger\": \"" + filterTrigger + "\"\n" +
                "                    }\n" +
                "                  }";

        String partC_emotionFilter =
                "                  {\n" +
                "                    \"nested\": {\n" +
                "                      \"path\": \"mostRecentMoodEvent.emotionalState\",\n" +
                "                      \"query\": {\n" +
                "                        \"bool\": {\n" +
                "                          \"must\": [\n" +
                "                            {\n" +
                "                              \"match\": {\n" +
                "                                \"mostRecentMoodEvent.emotionalState.description\": \"" + filterEmotion + "\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          ]\n" +
                "                        }\n" +
                "                      }\n" +
                "                    }\n" +
                "                  }\n";

        String partD_withFilters =
                "                ]\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String partD_noFilters =
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
            query += partB_withFilters;

            if (weekFilterActive) {
                // The week filter is active.
               query += partC_weekFilter;

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
                query += partC_triggerFilter;

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
                query += partC_emotionFilter;
            }

            query += partD_withFilters;

        } else {
            // No filters are active.
            query += partB_noFilters + partD_noFilters;
        }

        return query;
    }
}