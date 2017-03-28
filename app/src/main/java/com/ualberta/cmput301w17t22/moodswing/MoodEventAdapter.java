package com.ualberta.cmput301w17t22.moodswing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/** Adapter to display mood events in list views properly
 * https://www.youtube.com/watch?v=nOdSARCVYic
 * accessed March 26th
 * Created by bbest on 26/03/17.
 * @author bbest
 */

public class MoodEventAdapter extends ArrayAdapter<MoodEvent> {



    public MoodEventAdapter(Context context, ArrayList<MoodEvent> moodList){
        super(context, R.layout.mood_event, moodList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Get the data item for this position
        MoodEvent moodEvent = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mood_event, parent, false);
        }

         TextView usernameTextView = (TextView) convertView.findViewById(R.id.MoodEventHistoryText);
         TextView dateStamp = (TextView) convertView.findViewById(R.id.MoodEvenHistoryDatetimeStamp);
         TextView emotionalStateTextView = (TextView) convertView.findViewById(R.id.EmotionStateTextView_MoodEvent);
         TextView triggerTextView = (TextView) convertView.findViewById(R.id.MoodEventHistoryTriggers);
         ImageView emoticonImageView = (ImageView) convertView.findViewById(R.id.Emoticon_MoodHistory);
         ImageView socialSituationImageView = (ImageView) convertView.findViewById(R.id.SocialSituation_MoodHistory);
         ImageView locationIndicatorImageView = (ImageView) convertView.findViewById(R.id.LocationIndicator_MoodHistory);
         ImageView imageIndicatorImageView = (ImageView) convertView.findViewById(R.id.ImageIndicator_MoodHistory);

        //Sets the Text of the TextView to say You were feeling... if you are the original poster
        //If you are not the original poster then it displays the username of the original poster
        if(moodEvent.getOriginalPoster().equals(MoodSwingApplication.getMoodSwingController().getMainParticipant().getUsername())) {
            usernameTextView.setText("You were feeling ");
        }else{
            usernameTextView.setText(moodEvent.getOriginalPoster()+" was feeling ");
        }
        //Sets the date into its corresponding View
        dateStamp.setText(moodEvent.getDate().toString());

        // Set the image and text for the appropriate Mood Event.
        emotionalStateTextView.setText(moodEvent.getEmotionalState().getDescription());
        switch (moodEvent.getEmotionalState().getDescription()) {
            case "Anger":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.angry));
                break;
            case "Sadness":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.sad));
                break;
            case "Disgust":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.disgusted));
                break;
            case "Shame":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.ashamed));
                break;
            case "Happiness":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.happy));
                break;
            case "Surprise":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.surprised));
                break;
            case "Confusion":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.confused));
                break;
            case "Fear":
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.fearful));
                break;
            default:
                emotionalStateTextView.setTextColor(getContext().getResources().getColor(R.color.white));
                break;
        }

        //Sets trigger
        if(moodEvent.getTrigger().isEmpty()){
            triggerTextView.setVisibility(View.INVISIBLE);
        }else{
            triggerTextView.setVisibility(View.VISIBLE);
            triggerTextView.setText("Reason(s): " + moodEvent.getTrigger());
        }

        //Set emoticon image
        emoticonImageView.setImageDrawable(getContext().getDrawable(moodEvent.getEmotionalState().getDrawableId()));

        //Social situation
        if(moodEvent.getSocialSituation().getDescription() == null){
            socialSituationImageView.setVisibility(View.INVISIBLE);
        } else {
            socialSituationImageView.setVisibility(View.VISIBLE);
            socialSituationImageView.setImageDrawable(getContext().getDrawable(moodEvent.getSocialSituation().getDrawableId()));
        }

        //Location Indicator
        if(moodEvent.getLocation() == null){
            locationIndicatorImageView.setVisibility(View.INVISIBLE);
        }else{
            locationIndicatorImageView.setVisibility(View.VISIBLE);
        }

        //Image indicator
        if(moodEvent.getImage() == null){
            imageIndicatorImageView.setVisibility(View.INVISIBLE);
        } else{
            imageIndicatorImageView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}