package com.example.prashantpandey.prashant_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Prashant.Pandey on 2014-10-13.
 */
public class NotiReceiver extends BroadcastReceiver {


     private boolean nextisclicked;
     static boolean pausepressed=false;
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("PAUSE")) {
            Log.d("TAGNOTI", "Pause");
            pausepressed = !pausepressed;
            if (pausepressed) {
                if (MyMediaPlayerService.isservicerunning) {
                    MyMediaPlayerService.mPlayer.pause();
                    Songlist.pause.setVisibility(View.INVISIBLE);
                    Songlist.play.setVisibility(View.VISIBLE);
                } else {
                    Log.d("TAGSERVICEE", String.valueOf(MyMediaPlayerService.isservicerunning));
                    Toast.makeText(context, "No Song selected", Toast.LENGTH_LONG).show();

                }
            }
            else
            {
                Log.d("TAGNOTI","PLAYYY");
                if (MyMediaPlayerService.mPlayer != null && !nextisclicked)
                    MyMediaPlayerService.mPlayer.start();
                else {
                    Intent intent1 = null;

                    intent1 = new Intent(context, MyMediaPlayerService.class);

                    Songlist.songnametext.setText(Songlist.songnamelist.get(Songlist.currentPosition));
                    //stopService(intent);
                    intent1.putExtra("streamurl", Songlist.songlist.get(Songlist.currentPosition));
                    intent1.setAction("START");
                    intent1.putExtra("songname", Songlist.songnamelist.get(Songlist.currentPosition));


                    context.startService(intent1);
                }
            }

        }
        if(intent.getAction().equals("BACK"))
        {
            Log.d("TAGNOTI","BACK");
            if(Songlist.currentPosition>=1)
                Songlist.currentPosition=(Songlist.currentPosition-1)%Songlist.songlist.size();
            if(Songlist.currentPosition==0)
                Songlist.currentPosition=Songlist.songlist.size()-1;
            Songlist.songnametext.setText(Songlist.songnamelist.get(Songlist.currentPosition));


            if(MyMediaPlayerService.mPlayer!=null && MyMediaPlayerService.mPlayer.isPlaying()) {
                //dbhelper.addHistory(Songlist.songlist.get(Songlist.currentPosition));
                Intent intent1 = null;

                intent1 = new Intent(context, MyMediaPlayerService.class);


                //stopService(intent);
                intent1.putExtra("streamurl", Songlist.songlist.get(Songlist.currentPosition));
                intent1.setAction("START");
                intent1.putExtra("songname", Songlist.songnamelist.get(Songlist.currentPosition));


                context.startService(intent1);
            }
        }
        if(intent.getAction().equals("NEXT"))
        {


            Log.d("TAGNOTI","NEXT");
            nextisclicked=true;
            if(!Songlist.ishuffle)
                Songlist.currentPosition=(Songlist.currentPosition+1)%Songlist.songlist.size();
            else{

                Songlist.currentPosition=new Random().nextInt(Songlist.songlist.size());
            }
          //  Songlist.currentPosition=(Songlist.currentPosition+1)%Songlist.songlist.size();
            Songlist.songnametext.setText(Songlist.songnamelist.get(Songlist.currentPosition));
            if(MyMediaPlayerService.mPlayer!=null && MyMediaPlayerService.mPlayer.isPlaying()) {
                //dbhelper.addHistory(songlist.get(currentPosition));
                Intent intent1 = null;

                intent1 = new Intent(context, MyMediaPlayerService.class);


                //stopService(intent);
                intent1.putExtra("streamurl", Songlist.songlist.get(Songlist.currentPosition));
                intent1.setAction("START");
                intent1.putExtra("songname", Songlist.songnamelist.get(Songlist.currentPosition));


                context.startService(intent1);
            }
        }
    }
}
