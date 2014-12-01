package com.example.prashantpandey.prashant_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Songlist extends Activity{

    private ListView listView=null;
    static ArrayList<String> dura=new ArrayList<String>();
    public CustomAdapter adapter;
    static ImageButton pause;
    static ImageButton play;
    static ImageButton back;
    static ImageButton next;
    static ImageButton shuffle;

    static int currentPosition=-1;
    private String albumname;
    private Cursor cur;
    public ArrayList<Songs> songsdetaillist=new ArrayList<Songs>();

    static AudioManager am;
    static ArrayList<String> songnamelist;
    private Uri u;
    private String songid;
    private View view1;

    static int songcurrentposition;
    static ImageButton speakers;
    static ImageButton headphones;
    static Boolean speaker;
    static TextView songnametext;
    static SeekBar seek_Bar;
    private String songname;
    static Handler seekHandler = new Handler();
    static ArrayList<String> songlist;
     static boolean nextisclicked;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    static String currentsongname;
    static int mainpos;
    static boolean ishuffle;
    static int currentseekposition=-1;
    static boolean pauseisclicked;
    static int pauseclickposition;



    static Runnable run = new Runnable() {

        @Override
        public void run() {
            seekUpdation();
        }
    };
    static boolean clickedonce;

    static void seekUpdation() {


        if(pauseisclicked && nextisclicked)
        {

            seek_Bar.setProgress(0);
            seekHandler.postDelayed(run,1000);
        }
        else if(pauseisclicked && !nextisclicked)
        {

            seek_Bar.setProgress(pauseclickposition);
            seekHandler.postDelayed(run, 1000);
        }
        else {

            Songlist.seek_Bar.setProgress(MyMediaPlayerService.mPlayer.getCurrentPosition());
            seekHandler.postDelayed(run, 1000);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    public void onCreate(Bundle s) {
        super.onCreate(s);

        setContentView(R.layout.songlist);

        final DatabaseOpenHelper dbhelper=DatabaseOpenHelper.getInstance(this);
        songlist=new ArrayList<String>();
        songnamelist=new ArrayList<String>();

        //registerReceiver(new HeadphonesRec(), new IntentFilter(Intent.ACTION_HEADSET_PLUG));

        listView = (ListView) findViewById(R.id.list);
        Intent intent = getIntent();
        next=(ImageButton)findViewById(R.id.next);
        pause=(ImageButton)findViewById(R.id.pause);
        back=(ImageButton)findViewById(R.id.back);
        play=(ImageButton)findViewById(R.id.play);
        headphones=(ImageButton)findViewById(R.id.headphones);
        seek_Bar = (SeekBar) findViewById(R.id.seekbar);
        speakers=(ImageButton)findViewById(R.id.speakers);
        shuffle=(ImageButton)findViewById(R.id.shuffle);


        if(MyMediaPlayerService.mPlayer!=null)
        {

            seek_Bar.setMax(MyMediaPlayerService.mPlayer.getDuration());
            seekUpdation();

        }




        seek_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser && MyMediaPlayerService.mPlayer!=null) {
                    MyMediaPlayerService.mPlayer.seekTo(progress);
                    if(pauseisclicked ){
                        seek_Bar.setMax(MyMediaPlayerService.mPlayer.getDuration());
                        seekUpdation();
                       pauseisclicked=false;
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

        if(speaker==null)
            speaker=false;
        Log.d("TAGSPE", String.valueOf(speaker));
        am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if(am.isWiredHeadsetOn() && !speaker)
        {
            speaker=false;
            speakers.setVisibility(View.VISIBLE);
            headphones.setVisibility(View.INVISIBLE);
            am.setWiredHeadsetOn(true);
            am.setSpeakerphoneOn(false);
            //am.setMode(AudioManager.STREAM_MUSIC);
        }
        if((am.isWiredHeadsetOn() && speaker) || !am.isWiredHeadsetOn())
        {
            speaker=true;
            speakers.setVisibility(View.INVISIBLE);
            headphones.setVisibility(View.VISIBLE);
            am.setSpeakerphoneOn(true);
            am.setWiredHeadsetOn(false);
            //am.setMode(AudioManager.STREAM_MUSIC);
        }

        albumname = intent.getStringExtra("album");
        songnametext=(TextView)findViewById(R.id.songname);

        if(ishuffle)
            shuffle.setImageResource(R.drawable.icon71);
        else
            shuffle.setImageResource(R.drawable.icon7);


        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ishuffle=!ishuffle;
                if(ishuffle)
                    shuffle.setImageResource(R.drawable.icon71);
                else
                    shuffle.setImageResource(R.drawable.icon7);
            }
        });
        speakers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!speaker) {
                    Log.d("SPEAKLE","ASDA");

                    am.setSpeakerphoneOn(true);
                    am.setWiredHeadsetOn(false);
                    am.setMode(AudioManager.STREAM_MUSIC);

                    speaker = true;
                    headphones.setVisibility(View.VISIBLE);
                    speakers.setVisibility(View.INVISIBLE);

                }
                else
                    Toast.makeText(getApplicationContext(),"SPEAKERS ALREADY",Toast.LENGTH_LONG).show();

            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speaker && am.isWiredHeadsetOn()) {

                    Log.d("check", "How " + speaker);
                    am.setSpeakerphoneOn(false);
                    am.setWiredHeadsetOn(true);
                    am.setMode(AudioManager.STREAM_MUSIC);

                    headphones.setVisibility(View.INVISIBLE);
                    speakers.setVisibility(View.VISIBLE);
                    speaker=false;

                }
                else
                    Toast.makeText(getApplicationContext(),"CONNECT HEADSET",Toast.LENGTH_SHORT).show();
            }
        });


        String whereVal[] = {""};
        String orderBy = android.provider.MediaStore.Audio.Media.TITLE;
        whereVal[0] = albumname;

        String[] column = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,};
        String where = android.provider.MediaStore.Audio.Media.ALBUM + "=?";
        cur = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                column, where, whereVal, null);
        if (cur.moveToFirst()) {
            do {

                Songs onesong=new Songs();

                String songid = cur.getString(cur
                        .getColumnIndex(MediaStore.Audio.Media.DATA));
                songname = cur.getString(cur
                        .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String duration = cur.getString(cur
                        .getColumnIndex(MediaStore.Audio.Media.DURATION));
                String artist = cur.getString(cur
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST));


                String duration1=String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)),
                        TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)))
                );
                songlist.add(songid);
                onesong.setTitle(songname);
                songnamelist.add(songname);
                onesong.setTime(duration1);
                onesong.setArtistname(artist);
                dura.add(duration1);
                songsdetaillist.add(onesong);

            } while (cur.moveToNext());

        }

        if(MyMediaPlayerService.mPlayer!=null && MyMediaPlayerService.mPlayer.isPlaying())
        {
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
        }
        else
        {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
        }


        songnametext.setTextColor(Color.RED);

        adapter = new CustomAdapter(this,songsdetaillist);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                songid=songlist.get(position);
                view1=view;
                u = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songid);
                Log.d("TAGURIII", String.valueOf(position));
                dbhelper.addHistory(songnamelist.get(position),songlist.get(position),dura.get(position));
                if(pauseisclicked)
                {
                    seek_Bar.setMax(MyMediaPlayerService.mPlayer.getDuration());
                    seekUpdation();
                    pauseisclicked=false;
                }
                currentPosition = position;
                songnametext.setText(songnamelist.get(position));
                Intent intent=new Intent(getApplicationContext(),MyMediaPlayerService.class);

                Animation a = new TranslateAnimation(-200, 450, 0, 0);
                a.setDuration(10000);
                songnametext.setAnimation(a);
                intent.putExtra("streamurl",songlist.get(currentPosition));
                intent.putExtra("songname", songnamelist.get(currentPosition));
                MyMediaPlayerService.isservicerunning=true;
                startService(intent);
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
                pauseisclicked=true;
                nextisclicked=false;
                if(MyMediaPlayerService.mPlayer.isPlaying())
                {
                    MyMediaPlayerService.mPlayer.pause();
                    pauseclickposition=MyMediaPlayerService.mPlayer.getCurrentPosition();
               }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.INVISIBLE);
                pauseisclicked=false;

                //first click
                if(MyMediaPlayerService.mPlayer==null)
                {
                    Intent intent = null;
                    intent = new Intent(getApplicationContext(), MyMediaPlayerService.class);
                    intent.putExtra("streamurl", songlist.get(0));
                    intent.setAction("START");
                    intent.putExtra("songname", songnamelist.get(0));
                    startService(intent);
                }

                if(MyMediaPlayerService.mPlayer!=null && !MyMediaPlayerService.mPlayer.isPlaying() && !nextisclicked)
                {
                    MyMediaPlayerService.mPlayer.start();
                }
                if(MyMediaPlayerService.mPlayer!=null && !MyMediaPlayerService.mPlayer.isPlaying() && nextisclicked)
                {
                    dbhelper.addHistory(songnamelist.get(currentPosition),songlist.get(currentPosition),dura.get(currentPosition));
                    Intent intent = null;
                    intent = new Intent(getApplicationContext(), MyMediaPlayerService.class);
                    intent.putExtra("streamurl", songlist.get(currentPosition));
                    intent.setAction("START");
                    intent.putExtra("songname", songnamelist.get(currentPosition));
                    startService(intent);

                }



            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextisclicked=true;

                if(!ishuffle)
                    currentPosition=(currentPosition+1)%songlist.size();
                if(ishuffle)
                    currentPosition=new Random().nextInt(songlist.size());
                songnametext.setText(songnamelist.get(currentPosition));

                if(pauseisclicked)
                {
                    nextisclicked=true;
                    seekUpdation();
                }
                if(!pauseisclicked)
                {
                    dbhelper.addHistory(songnamelist.get(currentPosition),songlist.get(currentPosition),dura.get(currentPosition));
                    Intent intent = null;
                    intent = new Intent(getApplicationContext(), MyMediaPlayerService.class);
                    intent.putExtra("streamurl", songlist.get(currentPosition));
                    intent.setAction("START");
                    intent.putExtra("songname", songnamelist.get(currentPosition));
                    startService(intent);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextisclicked=true;

                if(Songlist.songlist!=null) {
                    if (!ishuffle) {
                        if (currentPosition >= 1)
                            currentPosition = (currentPosition - 1) % songlist.size();
                        if (currentPosition == 0)
                            currentPosition = songlist.size() - 1;

                    }
                    if (ishuffle) {
                        currentPosition = new Random().nextInt(songlist.size());
                    }
                    if(songnamelist!=null){
                    songnametext.setText(songnamelist.get(currentPosition));
                    if (pauseisclicked) {
                        nextisclicked = true;
                        seekUpdation();
                    }
                    if (!pauseisclicked) {
                        dbhelper.addHistory(songnamelist.get(currentPosition),songlist.get(currentPosition),dura.get(currentPosition));
                        Intent intent = null;
                        intent = new Intent(getApplicationContext(), MyMediaPlayerService.class);
                        intent.putExtra("streamurl", songlist.get(currentPosition));
                        intent.setAction("START");
                        intent.putExtra("songname", songnamelist.get(currentPosition));
                        startService(intent);
                    }
                }}
            }
        });
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //am.setMode(AudioManager.MODE_NORMAL);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }


        if(id==R.id.show)
        {

            Intent intent=new Intent(this,ShowHistory.class);
            startActivity(intent);

        }
        if(id==R.id.albums)
        {
            Intent intent=new Intent(this,OnlineAlbums.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



}