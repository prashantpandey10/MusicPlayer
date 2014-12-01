package com.example.prashantpandey.prashant_app;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Prashant.Pandey on 2014-09-25.
 */
public class MusicIntentReceiver extends android.content.BroadcastReceiver {


    @Override

    public void onReceive(Context ctx, Intent intent) {
        if (intent.getAction().equals(
                android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {

            Log.d("TAGREC", "NOISYY");

            if(MyMediaPlayerService.mPlayer!=null && MyMediaPlayerService.mPlayer.isPlaying())
            {
                Toast.makeText(ctx,"HEADPHONES DISCONNECTED",Toast.LENGTH_LONG).show();
                Songlist.am.setSpeakerphoneOn(true);
                Songlist.am.setWiredHeadsetOn(false);
               // Songlist.am.setMode(AudioManager.MODE_IN_CALL);
                Songlist.speakers.setVisibility(View.INVISIBLE);
                Songlist.headphones.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(ctx, "HEADPHONES DISCONNECTED", Toast.LENGTH_LONG).show();

            }
        }







        }
    }



