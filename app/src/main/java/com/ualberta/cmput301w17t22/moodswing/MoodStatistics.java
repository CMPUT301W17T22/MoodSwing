package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.security.AccessController.getContext;

/**
 * Very basic Mood Statistics page displaying various stats about the main participant and their
 * mood events.
 * Bar Chart: http://www.android-examples.com/create-bar-chart-graph-using-mpandroidchart-library/
 * Accessed on: April 2nd 2017
 * @author bbest
 */
public class MoodStatistics extends AppCompatActivity {

    //declare important components
    ElasticSearchController elasticSearchController;
    MoodSwingController moodSwingController;
    Participant mainParticipant;


    /** The main toolbar of the app that lets users navigate to the other parts of the app. */
    Toolbar mainToolbar;
    /**TextView that will show the number of followers the main participant has currently. */
    TextView followerCountView;
    /**TextView that will show the number of participants that follow the main participant currently. */
    TextView followingCountView;
    /**TextView that shows the name of the most used Emotional State. */
    TextView emotionalStateTextView;
    /**TextView that shows the most used Social Situation. */
    TextView mostUsedSocialSituation;

    //counters that keep track of the number of times an emotional state or social situation was used.
    //emotional state counters
    int angryCounter;
    int happyCounter;
    int surprisedCounter;
    int shameCounter;
    int fearCounter;
    int disgustCounter;
    int sadCounter;
    int confusedCounter;
    //social situation counters
    int aloneCounter;
    int withSomeoneCounter;
    int withGroupCounter;
    int crowdCounter;

    //Bar chart declaration variables
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ; //not utilized because of alignment issues
    BarDataSet Bardataset ;
    BarData BARDATA ;

    /**
     * Initializes all the widgets for this activity.
     */
    public void initialize() {
        // Initialize all basic widgets.
        mainToolbar = (Toolbar) findViewById(R.id.mainToolBar);
        mainToolbar.setTitle("");

        followerCountView = (TextView) findViewById(R.id.FollowerCount);
        followingCountView = (TextView) findViewById(R.id.FollowingCount);
        mostUsedSocialSituation = (TextView) findViewById(R.id.MostUsedSSView);
        emotionalStateTextView = (TextView) findViewById(R.id.MostUsedEmotionView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_statistics);

        // Initialize all the widgets of the app.
        initialize();

        setSupportActionBar(mainToolbar);

        // Get MoodSwingController.
         moodSwingController =
                MoodSwingApplication.getMoodSwingController();
        elasticSearchController =
                MoodSwingApplication.getElasticSearchController();
        mainParticipant =
                moodSwingController.getMainParticipant();

        //Bar chart initialization
        chart = (BarChart) findViewById(R.id.chart1);

        BARENTRY = new ArrayList<>();

        BarEntryLabels = new ArrayList<>();



    }
    /**onStart() will get the Mood History of the main participant.
     * Calculate and set the number of followers and following participants to the appropriate views.
     * Iterate through Mood History and increment counters based on emotional states and social situations.*/
    @Override
    protected void onStart() {
        super.onStart();

        ArrayList<MoodEvent> sample = mainParticipant.getMoodHistory();

        //Calculate several statistics below that are then populated into the views.

        //Number of followers
        int followerCount =  mainParticipant.getFollowers().size();
        followerCountView.setText(String.valueOf(followerCount));

        //Number of following
        int followingCount = mainParticipant.getFollowing().size();
        followingCountView.setText(String.valueOf(followingCount));

        //iterate through mood event list and increment counters
        for(int i =0; i < sample.size(); i ++){
            MoodEvent moodEvent = sample.get(i);

            //Check the emotional state
            if(moodEvent.getEmotionalState().getDescription().equals("Anger")){
                angryCounter++;
            } else if(moodEvent.getEmotionalState().getDescription().equals("Sadness")){
                sadCounter++;
            } else if(moodEvent.getEmotionalState().getDescription().equals("Disgust")){
                disgustCounter++;
            }else if(moodEvent.getEmotionalState().getDescription().equals("Shame")){
                shameCounter++;
            } else if(moodEvent.getEmotionalState().getDescription().equals("Happiness")){
                happyCounter++;
            } else if(moodEvent.getEmotionalState().getDescription().equals("Surprise")){
                surprisedCounter++;
            } else if(moodEvent.getEmotionalState().getDescription().equals("Fear")){
                fearCounter++;
            } else if(moodEvent.getEmotionalState().getDescription().equals("Confusion")){
                confusedCounter++;
            }

            //Check Social situation
            if(moodEvent.getSocialSituation().getDescription().equals("")){

            } else if(moodEvent.getSocialSituation().getDescription().equals("Alone")){
                aloneCounter++;
            } else if(moodEvent.getSocialSituation().getDescription().equals("With One Other Person")){
                withSomeoneCounter++;
            } else if(moodEvent.getSocialSituation().getDescription().equals("With Two To Several People")){
                withGroupCounter++;
            } else if(moodEvent.getSocialSituation().getDescription().equals("With A Crowd")){
                crowdCounter++;
            }
        }
        /**Emotion colors resource integers in a list so that the bar chart can assign
         * those colors to the proper bar that represents a specific emotional state. */
        int[] emotionColors = new int[8];
        //set emotion colors by index that correspond to the order of the bars in the chart.
        emotionColors[0] = getResources().getColor(R.color.angry);
        emotionColors[1] = getResources().getColor(R.color.happy);
        emotionColors[2] = getResources().getColor(R.color.sad);
        emotionColors[3] = getResources().getColor(R.color.fearful);
        emotionColors[4] = getResources().getColor(R.color.confused);
        emotionColors[5] = getResources().getColor(R.color.ashamed);
        emotionColors[6] = getResources().getColor(R.color.disgusted);
        emotionColors[7] = getResources().getColor(R.color.surprised);


        findMostUsedEmotion();

        findMostUsedSocial();

        AddValuesToBARENTRY();

        AddValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "Emotions");

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        Bardataset.setColors(emotionColors);


        chart.setData(BARDATA);

        chart.animateY(3000);

    }

    /**
     * Inflates the menu. Connects the menu_main_activity.xml to the
     * menu_main_activity in activity_main.xml.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_mood_statistics, menu);
        return true;
    }

    /**
     * This method handles clicks on menu items from the overflow menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.homeToolBarButton:
                // User chose the "Home" item, should navigate to MainActivity.
                finish();
                return true;

            case R.id.followToolBarButton:
                // User chose the "Follower & Following" action, should navigate to the
                // follower/following activity
                intent = new Intent(MoodStatistics.this, MainFollowActivity.class);
                finish();
                startActivity(intent);
                return true;

            case R.id.newMoodEventToolBarButton:
                // User chose the "New Mood Event" item, should navigate the NewMoodEventActivity.
                intent = new Intent(MoodStatistics.this, NewMoodEventActivity.class);
                finish();
                startActivity(intent);
                return true;

            case R.id.moodHistoryToolBarButton:
                // User chose the "View Mood History" item, should navigate to the
                // MoodHistoryActivity.
                intent = new Intent(MoodStatistics.this, MoodHistoryActivity.class);
                finish();
                startActivity(intent);
                return true;

            case R.id.blockUserToolBarButton:
                // User chose the "Block User" item, should navigate to the
                // BlockUserActivity.
                intent = new Intent(MoodStatistics.this, BlockUserActivity.class);
                finish();
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    /**Using the emotion counters this method sets the appropriate text view to the most used emotional state
     * @author bbest */
    public void findMostUsedEmotion(){
        //http://stackoverflow.com/questions/28020093/comparing-multiple-integers-in-if-statement-java#28020157
        //Accessed: April 1st 2017

        String mostUsedEmotion;
        if(angryCounter == 0 && sadCounter == 0 && happyCounter == 0 && fearCounter ==0 && shameCounter == 0 && confusedCounter ==0 && disgustCounter ==0 && surprisedCounter ==0){
            mostUsedEmotion = "No Data";
        } else if((angryCounter>= happyCounter ) && (angryCounter >= sadCounter) &&
                (angryCounter>= fearCounter) && (angryCounter >= confusedCounter)
                && (angryCounter>=shameCounter) && (angryCounter>=disgustCounter) && (angryCounter >= surprisedCounter)){
            mostUsedEmotion = "Anger";
        } else if((happyCounter>= sadCounter) && (happyCounter >= fearCounter)
                && (happyCounter>= confusedCounter) &&
                (happyCounter>= shameCounter) && (happyCounter>= disgustCounter) && (happyCounter>= surprisedCounter)){
            mostUsedEmotion = "Happiness";
        } else if((sadCounter>= fearCounter) && (sadCounter>=confusedCounter) &&
                (sadCounter>= shameCounter) && (sadCounter>= disgustCounter) && (sadCounter >= surprisedCounter) ){
            mostUsedEmotion = "Sadness";
        } else if((fearCounter>=confusedCounter) && (fearCounter>= shameCounter) &&
                (fearCounter>= disgustCounter) && (fearCounter >= surprisedCounter)){
            mostUsedEmotion = "Fear";
        } else if((confusedCounter>=shameCounter) && (confusedCounter>= disgustCounter) && (confusedCounter>=surprisedCounter)){
            mostUsedEmotion = "Confusion";
        } else if((shameCounter >= disgustCounter) && (shameCounter >= surprisedCounter)){
            mostUsedEmotion = "Shame";
        } else if((disgustCounter>=surprisedCounter)){
            mostUsedEmotion = "Disgust";
        } else {
            mostUsedEmotion = "Surprise";
        }
        // Set the image and text for the appropriate Mood Event.
        emotionalStateTextView.setText(mostUsedEmotion);
        switch (mostUsedEmotion) {
            case "Anger":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.angry));
                break;
            case "Sadness":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.sad));
                break;
            case "Disgust":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.disgusted));
                break;
            case "Shame":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.ashamed));
                break;
            case "Happiness":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.happy));
                break;
            case "Surprise":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.surprised));
                break;
            case "Confusion":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.confused));
                break;
            case "Fear":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.fearful));
                break;
            case "No Data":
                emotionalStateTextView.setTextColor(getResources().getColor(R.color.white));
                break;
        }

    }

    /**This method uses the social situation counters to determine the most used one and set that
     * into the appropriate view.
     * @author bbest*/
    public void findMostUsedSocial(){
        String mostUsedSocial;
        //Check the most used social situation
        if(aloneCounter == 0 && withSomeoneCounter == 0 && withGroupCounter == 0 && crowdCounter == 0){
            mostUsedSocial = "No Data";
        }
        else if((aloneCounter>=withGroupCounter) && (aloneCounter>=withSomeoneCounter) && (aloneCounter>=crowdCounter)){
            mostUsedSocial = "Alone";
        } else if((withGroupCounter>=withSomeoneCounter) && (withGroupCounter>=crowdCounter)){
            mostUsedSocial = "With Two To Several People";
        } else if((withSomeoneCounter>=crowdCounter)){
            mostUsedSocial = "With One Other Person";
        } else{
            mostUsedSocial = "With A Crowd";
        }

        mostUsedSocialSituation.setText(mostUsedSocial);
    }
    /**Adds data to the bar values to determine height */
    public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(angryCounter, 0));
        BARENTRY.add(new BarEntry(happyCounter, 1));
        BARENTRY.add(new BarEntry(sadCounter, 2));
        BARENTRY.add(new BarEntry(fearCounter, 3));
        BARENTRY.add(new BarEntry(confusedCounter, 4));
        BARENTRY.add(new BarEntry(shameCounter, 5));
        BARENTRY.add(new BarEntry(disgustCounter, 6));
        BARENTRY.add(new BarEntry(surprisedCounter, 7));

    }
    /**Adds labels for the bar entries */
    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("");
        BarEntryLabels.add("");
        BarEntryLabels.add("");
        BarEntryLabels.add("");
        BarEntryLabels.add("");
        BarEntryLabels.add("");
        BarEntryLabels.add("");
        BarEntryLabels.add("");


    }
}


