package com.example.prashantpandey.prashant_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Prashant.Pandey on 2014-10-20.
 */
public class IncomingCall extends BroadcastReceiver {
    private AudioManager am;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            // TELEPHONY MANAGER class object to register one listner
            am=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            TelephonyManager tmgr = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            //Create Listner
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

            // Register listener for LISTEN_CALL_STATE
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {



            if (state == 1) {


                if(MyMediaPlayerService.mPlayer!=null && MyMediaPlayerService.mPlayer.isPlaying()) {
                    MyMediaPlayerService.mPlayer.pause();
                }
                am.setMode(AudioManager.MODE_IN_CALL);

            }
            if (state == 0)

            {
                if (!Songlist.pauseisclicked && MyMediaPlayerService.mPlayer!=null) {
                    if (am.isWiredHeadsetOn()) {

                        am.setWiredHeadsetOn(true);
                        am.setSpeakerphoneOn(false);
                        // am.setMode(AudioManager.STREAM_MUSIC);

                    }

                    if (!am.isWiredHeadsetOn()) {
                        am.setSpeakerphoneOn(true);
                        am.setWiredHeadsetOn(false);
                        //   am.setMode(AudioManager.STREAM_MUSIC);
                        //am.setMode(AudioManager.MODE_NORMAL);
                    }
                    am.setMode(AudioManager.MODE_NORMAL);
                    MyMediaPlayerService.mPlayer.start();


                }


            }


        }
    }
}
