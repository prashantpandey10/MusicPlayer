package com.example.prashantpandey.prashant_app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class Player extends Fragment {
    static ImageButton pause;
    static ImageButton play;
    static ImageButton back;
    static ImageButton next;
    static ImageButton headphones;
    static ImageButton speakers;
    static SeekBar seek_Bar;
    static ImageView shuffle;
    static TextView songnametext;
    public View view=null;
    static Handler seekHandler1 = new Handler();static int i;

    public void show(){
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.INVISIBLE);
    }


    static Runnable run = new Runnable() {

        @Override
        public void run() {
            seekUpdation();
        }
    };
    static boolean clickedonce;

    static void seekUpdation() {

        if(Songlist.pauseisclicked && Songlist.nextisclicked)
        {

            seek_Bar.setProgress(0);
            seekHandler1.postDelayed(run,1000);
        }
        else if(Songlist.pauseisclicked && !Songlist.nextisclicked)
        {

            seek_Bar.setProgress(Songlist.pauseclickposition);
            seekHandler1.postDelayed(run, 1000);
        }
        else {

            seek_Bar.setProgress(MyMediaPlayerService.mPlayer.getCurrentPosition());
            seekHandler1.postDelayed(run, 1000);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.player, container, false);
        Log.d("check","Re");
        i=0;
        final DatabaseOpenHelper dbhelper=DatabaseOpenHelper.getInstance(getActivity());
        next=(ImageButton)rootView.findViewById(R.id.next);
        back=(ImageButton)rootView.findViewById(R.id.back);

        pause=(ImageButton)rootView.findViewById(R.id.pause);

        play=(ImageButton)rootView.findViewById(R.id.play);
        shuffle=(ImageButton)rootView.findViewById(R.id.shuffle);

        /*if(MyMediaPlayerService.mPlayer !=null && MyMediaPlayerService.mPlayer.isPlaying()){
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.INVISIBLE);
        }
        else{
            pause.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
        }*/

        headphones=(ImageButton)rootView.findViewById(R.id.headphones);
        speakers=(ImageButton)rootView.findViewById(R.id.speakers);
        seek_Bar = (SeekBar) rootView.findViewById(R.id.seekbar);

       /* if(Songlist.am!=null){
            if((Songlist.am.isWiredHeadsetOn() ))
            {
                Log.d("TAGFI", String.valueOf(1231231111));
                //Songlist.speaker=false;
                speakers.setVisibility(View.VISIBLE);
                headphones.setVisibility(View.INVISIBLE);
                Songlist.am.setWiredHeadsetOn(true);
                Songlist.am.setSpeakerphoneOn(false);i=1;
                Songlist.am.setMode(AudioManager.STREAM_MUSIC);
            }
            else if( !Songlist.am.isWiredHeadsetOn())
            {
                Log.d("TAGFI", String.valueOf(1231222222));
                //Songlist.speaker=true;
                speakers.setVisibility(View.INVISIBLE);
                headphones.setVisibility(View.VISIBLE);
                Songlist.am.setSpeakerphoneOn(true);
                Songlist.am.setWiredHeadsetOn(false);
                Songlist.am.setMode(AudioManager.STREAM_MUSIC);
            }}*/



        seek_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser && MyMediaPlayerService.mPlayer!=null) {
                    MyMediaPlayerService.mPlayer.seekTo(progress);
                    if(Songlist.pauseisclicked){
                        MyMediaPlayerService.mPlayer.seekTo(progress);
                        seek_Bar.setMax(MyMediaPlayerService.mPlayer.getDuration());
                        seekUpdation();
                        Songlist.pauseisclicked=false;
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

         shuffle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Songlist.ishuffle = !Songlist.ishuffle;
                 if (Songlist.ishuffle)
                     shuffle.setImageResource(R.drawable.icon71);
                 else
                     shuffle.setImageResource(R.drawable.icon7);
             }
         });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Songlist.pauseisclicked=true;
                Songlist.nextisclicked=false;
                if (MyMediaPlayerService.mPlayer.isPlaying()) {
                    Log.d("check","PAUSE");
                    MyMediaPlayerService.mPlayer.pause();
                    Songlist.pauseclickposition=MyMediaPlayerService.mPlayer.getCurrentPosition();
                    Log.d("check", "bah"+String.valueOf(pause.getVisibility()));
                    Log.d("check", "bah2"+String.valueOf(play.getVisibility()));

                    pause.setVisibility(ImageView.INVISIBLE );

                    play.setVisibility(ImageView.VISIBLE);
                    //play.setVisibility(View.VISIBLE);
                    int i=pause.getVisibility();
                }
                else
                {
                    Toast.makeText(getActivity(), "No Song selected", Toast.LENGTH_LONG).show();
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Songlist.songlist!=null) {
                   pause.setVisibility(View.VISIBLE);
                   play.setVisibility(View.INVISIBLE);
                   Songlist.pauseisclicked = false;

                   //first click
                   if (MyMediaPlayerService.mPlayer == null) {
                       Intent intent = null;
                       intent = new Intent(getActivity(), MyMediaPlayerService.class);
                       intent.putExtra("streamurl", Songlist.songlist.get(0));
                       intent.setAction("START");
                       intent.putExtra("songname", Songlist.songnamelist.get(0));
                       getActivity().startService(intent);
                   }

                   if (MyMediaPlayerService.mPlayer != null && !MyMediaPlayerService.mPlayer.isPlaying() && !Songlist.nextisclicked) {
                       MyMediaPlayerService.mPlayer.start();
                   }
                   //if(MyMediaPlayerService.mPlayer!=null && !MyMediaPlayerService.mPlayer.isPlaying() && Songlist.nextisclicked && Songlist.pauseisclicked)

                   if (MyMediaPlayerService.mPlayer != null && !MyMediaPlayerService.mPlayer.isPlaying() && Songlist.nextisclicked ) {
                       dbhelper.addHistory(Songlist.songnamelist.get(Songlist.currentPosition), Songlist.songlist.get(Songlist.currentPosition), Songlist.dura.get(Songlist.currentPosition));
                       Intent intent = null;
                       intent = new Intent(getActivity(), MyMediaPlayerService.class);
                       intent.putExtra("streamurl", Songlist.songlist.get(Songlist.currentPosition));
                       intent.setAction("START");

                       intent.putExtra("songname", Songlist.songnamelist.get(Songlist.currentPosition));
                       getActivity().startService(intent);

                   }


               }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Songlist.songlist!=null) {
                        Songlist.nextisclicked = true;
                        if (!Songlist.ishuffle)
                            Songlist.currentPosition = (Songlist.currentPosition + 1) % Songlist.songlist.size();
                        if (Songlist.ishuffle)
                            Songlist.currentPosition = new Random().nextInt(Songlist.songlist.size());
                        //songnametext.setText(Songlist.songnamelist.get(Songlist.currentPosition));

                        if (Songlist.pauseisclicked) {
                            Songlist.nextisclicked = true;
                            Songlist.seekUpdation();
                        }
                        if (!Songlist.pauseisclicked) {
                            //dbhelper.addHistory(Songlist.songlist.get(Songlist.currentPosition));
                            Intent intent = null;
                            intent = new Intent(getActivity(), MyMediaPlayerService.class);
                            intent.putExtra("streamurl", Songlist.songlist.get(Songlist.currentPosition));
                            intent.setAction("START");
                            intent.putExtra("songname", Songlist.songnamelist.get(Songlist.currentPosition));
                            getActivity().startService(intent);
                        }
                    }
            }
        });

        speakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Songlist.speaker!=null){
                if (!Songlist.speaker) {
                    if(Songlist.am==null)
                        Songlist.am=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
                    Songlist.am.setSpeakerphoneOn(true);
                    Songlist.am.setWiredHeadsetOn(false);
                    Songlist.am.setMode(AudioManager.STREAM_MUSIC);

                    Songlist.speaker = true;
                    headphones.setVisibility(View.VISIBLE);
                    speakers.setVisibility(View.INVISIBLE);
                }
                else
                    Toast.makeText(getActivity(),"SPEAKERS ALREADY",Toast.LENGTH_LONG).show();
            }}
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Songlist.speaker!=null){
                if (Songlist.speaker && Songlist.am.isWiredHeadsetOn()) {

                    Songlist.am.setSpeakerphoneOn(false);
                    Songlist.am.setWiredHeadsetOn(true);
                    Songlist.am.setMode(AudioManager.STREAM_MUSIC);
                    Songlist.speaker = false;
                    headphones.setVisibility(View.INVISIBLE);
                    speakers.setVisibility(View.VISIBLE);
                }
                else
                    Toast.makeText(getActivity(),"CONNECT HEADSET",Toast.LENGTH_SHORT).show();
            }
        }});
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(Songlist.songlist!=null) {

                        Songlist.nextisclicked = true;

                        if (!Songlist.ishuffle) {
                            if (Songlist.currentPosition >= 1)
                                Songlist.currentPosition = (Songlist.currentPosition - 1) % Songlist.songlist.size();
                            if (Songlist.currentPosition == 0)
                                Songlist.currentPosition = Songlist.songlist.size() - 1;
                        }
                        if (Songlist.ishuffle) {
                            Songlist.currentPosition = new Random().nextInt(Songlist.songlist.size());
                        }
                        //songnametext.setText(Songlist.songnamelist.get(Songlist.currentPosition));
                        if (Songlist.pauseisclicked) {
                            Songlist.nextisclicked = true;
                            Songlist.seekUpdation();
                        }
                        if (!Songlist.pauseisclicked) {
                            //dbhelper.addHistory(Songlist.songlist.get(Songlist.currentPosition));
                            Intent intent = null;
                            intent = new Intent(getActivity(), MyMediaPlayerService.class);
                            intent.putExtra("streamurl", Songlist.songlist.get(Songlist.currentPosition));
                            intent.setAction("START");
                            intent.putExtra("songname", Songlist.songnamelist.get(Songlist.currentPosition));
                            getActivity().startService(intent);
                        }
                    }
            }
        });
        view=rootView;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Songlist.pauseisclicked || MyMediaPlayerService.mPlayer==null || !MyMediaPlayerService.mPlayer.isPlaying()) {
            Log.d("check","11");
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);}
        else{play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE); }
        if(MyMediaPlayerService.mPlayer!=null)
        {

            seek_Bar.setMax(MyMediaPlayerService.mPlayer.getDuration());
            seekUpdation();

        }
        if(Songlist.pauseisclicked)
        {
            seek_Bar.setMax(MyMediaPlayerService.mPlayer.getDuration());
            seekUpdation();
            Songlist.pauseisclicked=false;
        }
        if(Songlist.ishuffle)
            shuffle.setImageResource(R.drawable.icon71);
        else
            shuffle.setImageResource(R.drawable.icon7);
       // AudioManager am=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        //am.setMode(AudioManager.STREAM_MUSIC);

        if(Songlist.am!=null && Songlist.speaker!=null){
            Log.d("TAGFI", String.valueOf(Songlist.speaker));
            if((Songlist.am.isWiredHeadsetOn() && !Songlist.speaker))
        {
            Log.d("TAGFI", String.valueOf(1231231111));
            //Songlist.speaker=false;
            speakers.setVisibility(View.VISIBLE);
            headphones.setVisibility(View.INVISIBLE);
            Songlist.am.setWiredHeadsetOn(true);
            Songlist.am.setSpeakerphoneOn(false);i=1;
            Log.d("check","oye");
            //Songlist.am.setMode(AudioManager.STREAM_MUSIC);
            Log.d("check","oye");
        }
        else if((Songlist.am.isWiredHeadsetOn() && Songlist.speaker) || !Songlist.am.isWiredHeadsetOn())
        {
            Log.d("TAGFI", String.valueOf(1231222222));
            //Songlist.speaker=true;
            speakers.setVisibility(View.INVISIBLE);
            headphones.setVisibility(View.VISIBLE);
            Songlist.am.setSpeakerphoneOn(true);
            Songlist.am.setWiredHeadsetOn(false);
            Log.d("check","oye");
            //Songlist.am.setMode(AudioManager.STREAM_MUSIC);
            Log.d("check","oye");
        }}
    }
}

