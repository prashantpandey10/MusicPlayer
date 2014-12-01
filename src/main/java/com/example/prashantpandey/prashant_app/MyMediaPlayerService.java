package com.example.prashantpandey.prashant_app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

public class MyMediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,AudioManager.OnAudioFocusChangeListener {


    static MediaPlayer mPlayer = null;
    BroadcastReceiver HeadphonesRec;

    private String songname;
   // Handler seekHandler = new Handler();


    static boolean isservicerunning = false;

    private AudioManager am;
    private Notification noti;
    static SharedPreferences sh;
    private HeadphonesRec receiver;

    public MyMediaPlayerService() {

    }

    public int onStartCommand(Intent intent, int flags, int startId) {


        am = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();

            mPlayer = null;
            mPlayer = new MediaPlayer();


        }


        songname = intent.getStringExtra("songname");



        mPlayer = new MediaPlayer();
        try {

            mPlayer.setDataSource(intent.getStringExtra("streamurl"));
            mPlayer.setLooping(true);

        } catch (IOException e) {
            e.printStackTrace();
        }


        mPlayer.setOnPreparedListener(this);
// Request audio focus for playback


        if (reqAudioFocus()) {

            mPlayer.prepareAsync();
        }

        mPlayer.setLooping(false);
        mPlayer.setOnCompletionListener(this);

        notification();


        return START_NOT_STICKY;
    }

    private boolean reqAudioFocus() {
        boolean gotFocus = false;
        int audioFocus = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (audioFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            gotFocus = true;
        } else {
            gotFocus = false;
        }
        return gotFocus;
    }

   /* Runnable run = new Runnable() {

        @Override
        public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {


      //  Log.d("TAGSEEK", String.valueOf(mPlayer.getCurrentPosition()));
        Songlist.currentseekposition=mPlayer.getCurrentPosition();
        //Log.d("TAGSEEK11", String.valueOf(Songlist.currentseekposition));
        Songlist.seek_Bar.setProgress(mPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }*/


    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        HeadphonesRec receiver = new HeadphonesRec();
        registerReceiver( receiver, receiverFilter );
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void notification() {
        int id=1;
        registerReceiver(stopServiceReceiver, new IntentFilter("STOP"));
        Intent play=new Intent("PLAY");

        Intent pause=new Intent("PAUSE");

        Intent next=new Intent("NEXT");

        Intent back=new Intent("BACK");


        PendingIntent pintentnext=PendingIntent.getBroadcast(getApplicationContext(),0,next,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pintentback=PendingIntent.getBroadcast(getApplicationContext(),0,back,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pintentpause=PendingIntent.getBroadcast(getApplicationContext(),0,pause,PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pintentplay=PendingIntent.getBroadcast(getApplicationContext(),0,play,PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity1.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

            noti = new Notification.Builder(this)
                    .setContentTitle(songname)
                    .setContentText("SONG NAME").setSmallIcon(R.drawable.download)
                    .setContentIntent(pIntent)
                    .addAction(R.drawable.icon11, "", pintentback)
                    .addAction(R.drawable.icon12, "", pintentpause)
                    .addAction(R.drawable.icon10, "", pintentnext)
                    .build();




        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1, noti);


    }

    protected BroadcastReceiver stopServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           Intent intent1=new Intent(getApplicationContext(), MainActivity1.class);
            startActivity(intent1);

        }


    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {

            mPlayer.release();

            mPlayer = null;

        }

        isservicerunning = false;
        unregisterReceiver(stopServiceReceiver);
        unregisterReceiver(receiver);
        stopSelf();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called when MediaPlayer is ready
     */
   public void onPrepared(MediaPlayer player) {

        mPlayer.start();
        Songlist.seek_Bar.setMax(mPlayer.getDuration());
        Songlist.seekUpdation();
        isservicerunning = true;
    }


   public void onAudioFocusChange(int focusChange) {

        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            am.abandonAudioFocus(this);
            mPlayer.stop();
        }
    }

    @Override
   public void onCompletion(MediaPlayer mp) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mPlayer = new MediaPlayer();
        }

        if(!Songlist.ishuffle)
            Songlist.currentPosition=(Songlist.currentPosition+1)%Songlist.songlist.size();
        else{
            Songlist.currentPosition=new Random().nextInt(Songlist.songlist.size());
        }

        Log.d("MPLAYERSTARTT", "ISPLAYING");
        mPlayer = new MediaPlayer();
        try {

            mPlayer.setDataSource(Songlist.songlist.get((Songlist.currentPosition)%Songlist.songlist.size()));
            mPlayer.setLooping(true);
            Songlist.songnametext.setText(Songlist.songnamelist.get(Songlist.currentPosition));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPlayer.setOnPreparedListener(this);
// Request audio focus for playback
        if (reqAudioFocus()) {
            Log.d("TAGFOCUS", "PREPP");
            mPlayer.prepareAsync();
        }

        mPlayer.setLooping(false);
        mPlayer.setOnCompletionListener(this);

        notification();
    }
}