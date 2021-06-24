package com.evanjmg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
// import java.util.Date;
import java.util.Calendar;

public class HomeWatcher {

    static final String TAG = "hg";
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier;
    private  int pressCount = 0;
    private Calendar lastTime = Calendar.getInstance();
    
    public HomeWatcher(Context context) {
        mContext = context;
        mFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mFilter.addAction(Intent.ACTION_SCREEN_ON);
        mFilter.addAction(Intent.ACTION_DREAMING_STARTED);
        mFilter.addAction(Intent.ACTION_DREAMING_STOPPED);
        // mFilter.addAction(Intent.ACTION_USER_PRESENT);
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
        mRecevier = new InnerRecevier();
    }

    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, mFilter);
        }
    }

    public void stopWatch() {
        if (mRecevier != null) {
            mContext.unregisterReceiver(mRecevier);
        }
    }

    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Calendar _now  = Calendar.getInstance();
            lastTime.add(Calendar.SECOND, 1);
            if(_now.before(lastTime)) 
            { // this is recurrent.. 
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    pressCount = pressCount + 1;
                    Log.e(TAG, "ACTION_SCREEN_OFF - " + pressCount);     
                }
                if (action.equals(Intent.ACTION_DREAMING_STOPPED)) {
                    pressCount = pressCount + 1;
                    Log.e(TAG, "ACTION_DREAMING_STOPPED - " + pressCount);     
                }
                if (action.equals(Intent.ACTION_DREAMING_STARTED)) {
                    pressCount = pressCount + 1;
                    Log.e(TAG, "ACTION_DREAMING_STARTED - " + pressCount);     
                }
                // if (action.equals(Intent.ACTION_USER_PRESENT)) {
                //     pressCount = pressCount + 1;
                //     Log.e(TAG, "ACTION_USER_PRESENT - " + pressCount);     
                // }
                if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    pressCount = pressCount + 1;
                    Log.e(TAG, "ACTION_SCREEN_ON - " + pressCount);
                    //String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                    //if (reason != null) {
                    //    Log.e(TAG, "action:" + action + ",reason:" + reason);
                    //    if (mListener != null) {
                    //        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    //            mListener.onHomePressed();
                    //        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    //            mListener.onRecentAppPressed();
                    //        }
                    //    }
                    //}
                }
            }  else {
                pressCount = 0;
             } 
             if (pressCount >= 7) {
                 pressCount = 0;
                mListener.onHomePressed();
             }
           lastTime = Calendar.getInstance();
            
        }
    }
}
