package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import io.searchbox.core.Index;

import static java.security.AccessController.getContext;

/**
 * http://stackoverflow.com/questions/3767591/check-intent-internet-connection
 * grab date 01-04-2017
 */

public class NetworkStateReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d(TAG, "Network connectivity change");

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                Log.d(TAG, "Network " + ni.getTypeName() + " connected");
                //TODO: retry pending moodevent saving on elasticsearch
                Index index = loadFromFile();
                if(index != null) {
                    ElasticSearchController.executeElasticSearch(index);
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(TAG, "There's no network connectivity");
            }
        }
    }

    /**
     * Loads tweet from specified file.
     * //TODO: wipe file after loading.
     *
     * @exception FileNotFoundException if the file is not created first.
     */
    private Index loadFromFile() {
        Index index;
        MoodSwingController moodSwingController =
                MoodSwingApplication.getMoodSwingController();
        String filename = moodSwingController.getMainParticipant().getUsername() + ".sav";
        try {
            FileInputStream fis = MoodSwingApplication.getContext().openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            //Taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            //2017-01-24 18:19
            //Type listType = new TypeToken<Index>(){}.getType();

            //Log.d("offlineSucks","in: " + in + " listType" + listType);
            //index = gson.fromJson(in, listType);
            index = gson.fromJson(in, Index.class);

        } catch (FileNotFoundException e) {
            index = null;
        } catch (IOException e) {
            // TODO Handle the Exception properly later
            throw new RuntimeException(); //crashes app
        }
        return index;
    }
}