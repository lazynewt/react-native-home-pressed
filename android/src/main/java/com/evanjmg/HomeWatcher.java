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
    private Calendar firstInstance = Calendar.getInstance();
    
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
            //lastTime.add(Calendar.SECOND, 1);
            Calendar  limitTime = (Calendar) firstInstance.clone();
            limitTime.add(Calendar.MILLISECOND,1300);
            if(_now.before(limitTime)) 
            { // this is recurrent.. 
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    pressCount = pressCount + 1;
                    Log.e(TAG, "ACTION_SCREEN_OFF - " + pressCount);     
                }
                //if (action.equals(Intent.ACTION_DREAMING_STOPPED)) {
                //    pressCount = pressCount + 1;
                //    Log.e(TAG, "ACTION_DREAMING_STOPPED - " + pressCount);     
                //}
                //if (action.equals(Intent.ACTION_DREAMING_STARTED)) {
                //    pressCount = pressCount + 1;
                //    Log.e(TAG, "ACTION_DREAMING_STARTED - " + pressCount);     
                //}
                if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    pressCount = pressCount + 1;
                    Log.e(TAG, "ACTION_SCREEN_ON - " + pressCount);
                }
            } else {
                firstInstance = Calendar.getInstance();
                pressCount = 0;
                if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                    pressCount = 1;
                    Log.e(TAG, "ACTION_SCREEN_OFF - " + pressCount);     
                }
                //if (action.equals(Intent.ACTION_DREAMING_STOPPED)) {
                //    pressCount = 1;
                //    Log.e(TAG, "ACTION_DREAMING_STOPPED - " + pressCount);     
                //}
                //if (action.equals(Intent.ACTION_DREAMING_STARTED)) {
                //    pressCount = 1;
                //    Log.e(TAG, "ACTION_DREAMING_STARTED - " + pressCount);     
                //}
                if (action.equals(Intent.ACTION_SCREEN_ON)) {
                    pressCount = 1;
                    Log.e(TAG, "ACTION_SCREEN_ON - " + pressCount);
                }
            }
                  
            //else {
            //    pressCount = 0;
            //    firstInstance = Calendar.getInstance();
            //} 
            
            
            if (pressCount >= 3) {
                 pressCount = 0;
                mListener.onHomePressed();
                firstInstance = Calendar.getInstance();
            }
            
        }
    }
}
