package com.example.prashantpandey.prashant_app;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chirag.kadam on 30-09-2014.
 */
public class TrackAdapter extends BaseAdapter{

    ArrayList<String> tracks=new ArrayList<String>();
    ArrayList<String> duration=new ArrayList<String>();
    Context ctx;
    public TrackAdapter(Context context,ArrayList tracks,ArrayList duration){

        this.duration=duration;
        this.tracks=tracks;
        ctx=context;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView tv1 = null;
        TextView tv2=null;

        // First let's verify the convertView is not null

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.l_track_row, null);
        }
        tv1 = (TextView) v.findViewById(R.id.textView1);
        tv1.setTextColor(Color.BLACK);
        tv2 = (TextView) v.findViewById(R.id.textView2);

        //Log.d("check", "S"+String.valueOf(History.clicked));
        //Log.d("check", String.valueOf(Track.last));
        //Log.d("check", String.valueOf(Local.check));
        //Log.d("check", String.valueOf(position));
        //if(Track.last!=-1 && Track.last==position && Track.lastAlbum.equals(Track.album) && Track.from.equals("Track") && Local.check==true ) {
        //    Log.d("check","go");
        //    tv1.setTextColor(Color.rgb(250, 20, 20));

        //}
        tv1.setText(tracks.get(position));
        //Integer d=Integer.parseInt(duration.get(position));
        //d=d/1000;
        //int min=d/60;
       // int sec=d%60;
        //if(sec<10)
        //    tv2.setText(min+":0"+sec);
        //else
        //    tv2.setText(min+":"+sec);
        tv2.setText(duration.get(position));
        return v;

    }
}