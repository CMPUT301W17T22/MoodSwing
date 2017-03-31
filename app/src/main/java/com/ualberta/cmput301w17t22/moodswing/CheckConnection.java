package com.ualberta.cmput301w17t22.moodswing;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by htruong1 on 2017-03-30.
 */

/**
 * Code was used from //https://www.youtube.com/watch?v=9H4Ik9hqDlQ
 * getNetwork methods are deprecated as of API 23 so unable to use
 * This class checks for whether the device is connected to a WIFI/Cellular network.
 * Returns true if you are connected, returns false otherwise.
 */
public class CheckConnection {

    Context context;

    public CheckConnection(Context context){
        this.context = context;
    }

    public boolean isConnected(){
        //checks if connected to some form of network
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity != null) {
            //get information regarding network
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
            return false;
        }

}
