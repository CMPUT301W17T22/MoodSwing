package com.ualberta.cmput301w17t22.moodswing;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by htruong1 on 2017-03-30.
 */

public class checkConnection { //https://www.youtube.com/watch?v=9H4Ik9hqDlQ

    Context context;

    public checkConnection(Context context){
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity != null) {
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
