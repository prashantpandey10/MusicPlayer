package com.example.prashantpandey.prashant_app;


import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class JSON extends Fragment {


    private Intent intent;
    ListView listview;
    ArrayList<String> streamlist=new ArrayList<String>();
    ArrayList<String> radioname=new ArrayList<String>();
    private ImageButton pause;

    private ImageButton back;
    private ImageButton next;
    private int currentPosition;
    private ImageButton play;

    @Override
    public void onResume() {
        super.onResume();
        if(Songlist.am!=null){
            Songlist.am.setMode(AudioManager.STREAM_MUSIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.json, container, false);
        listview=(ListView)view.findViewById(R.id.list);
        next=(ImageButton)view.findViewById(R.id.next);
        pause=(ImageButton)view.findViewById(R.id.pause);
        back=(ImageButton)view.findViewById(R.id.back);
        play=(ImageButton)view.findViewById(R.id.play);



        streamlist.add("http://calm11.calmradio.com:9056/stream");
        streamlist.add("http://proxy.calmradio.com/4/8556/stream");
        streamlist.add("http://proxy.calmradio.com/4/10256/stream");
        streamlist.add("http://proxy.calmradio.com/9/8756/stream");
        streamlist.add("http://proxy.calmradio.com/3/8256/stream");
        streamlist.add("http://proxy.calmradio.com/7/9556/stream");
        streamlist.add("http://proxy.calmradio.com/3/8556/stream");


        radioname.add("BROADWAY");
        radioname.add("INDIAN CLASSICAL");
        radioname.add("BOLLYWOOD");
        radioname.add("COUNTRY");
        radioname.add("INSTRUMENTAL");
        radioname.add("JAZZ");
        radioname.add("CALM");
        listview.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,radioname));






        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (MainActivity1.activeInfo != null && MainActivity1.activeInfo.isConnected()) {
                    currentPosition = position;
                    if(MyMediaPlayerService.mPlayer!=null)
                        MyMediaPlayerService.mPlayer.pause();
                    if(Songlist.am!=null){

                    Songlist.am.setMode(AudioManager.MODE_NORMAL);}
                    Intent intent =new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(streamlist.get(currentPosition)),"audio/*");
                    startActivity(intent);

                }
                else
                    Toast.makeText(getActivity(),"NET DISCONNECTED",Toast.LENGTH_LONG).show();

            }
        });










        return view;
    }

















    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();




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
            Intent intent=new Intent(getActivity(),ShowHistory.class);
            getActivity().startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}

