package com.example.prashantpandey.prashant_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;

/**
 * Created by Prashant.Pandey on 2014-10-10.
 */
public class HeadphonesRec extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_HEADSET_PLUG))
        {
            int i=intent.getIntExtra("state",0);
            Log.d("check", "Here "+String.valueOf(i));
            if(i==1)
            {
                Log.d("TAGFI", "vsfrgb"+String.valueOf(i));
                Songlist.am.setSpeakerphoneOn(false);
                Songlist.am.setWiredHeadsetOn(true);
                Songlist.am.setMode(AudioManager.STREAM_MUSIC);
                Songlist.speaker = false;
                if(Songlist.headphones!=null){
                    Songlist.headphones.setVisibility(View.INVISIBLE);
                Songlist.speakers.setVisibility(View.VISIBLE);}
                if(Player.headphones!=null){
                    Player.headphones.setVisibility(View.INVISIBLE);
                    Player.speakers.setVisibility(View.VISIBLE);}
            }
            else if(i==0)
            {
                Log.d("TAGFI","INNNNNNN");
                if(Songlist.headphones!=null){
                Songlist.headphones.setVisibility(View.VISIBLE);
                Songlist.speakers.setVisibility(View.INVISIBLE);}
                if(Player.headphones!=null){
                    Player.headphones.setVisibility(View.VISIBLE);
                    Player.speakers.setVisibility(View.INVISIBLE);}
            }
        }


    }
}

