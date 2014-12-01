package com.example.prashantpandey.prashant_app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prashantpandey.prashant_app.DatabaseOpenHelper;

import java.util.ArrayList;

/**
 * Created by chirag.kadam on 17-09-2014.
 */
public class ShowHistory extends Activity {

    ListView list=null;
    ArrayList<String> tracks=new ArrayList<String>();
    ArrayList<String> duration=new ArrayList<String>();
    ArrayList<String> uri=new ArrayList<String>();
    static boolean clicked=true;
    @Override
    public void onCreate(Bundle s) {
        super.onCreate(s);

        setContentView(R.layout.songlist);

        //Database helper=new Database(this.getActivity());
        DatabaseOpenHelper helper=DatabaseOpenHelper.getInstance(this);
        ListView list = (ListView) findViewById(R.id.list);
        list.setTag("abc");

        Cursor cursor = (Cursor) helper.getAllHistory();
        cursor.moveToFirst();
        tracks.clear();
        duration.clear();
        uri.clear();
        int i=0;
        while(i<cursor.getCount()){
            tracks.add(cursor.getString(1));
            duration.add(cursor.getString(3));
            uri.add(cursor.getString(2));
            i++;
            cursor.moveToNext();
        }
        //SimpleCursorAdapter sc = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor, new String[]{Database.TRACK_NAME}, new int[]{android.R.id.text1}, 0);
        final TrackAdapter adapter=new TrackAdapter(this,tracks,duration);
        list.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        //Track.from="History";
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh(){
        //final Database helper=new Database(this.getActivity());
        final DatabaseOpenHelper helper=DatabaseOpenHelper.getInstance(this);
        Cursor cursor = (Cursor) helper.getAllHistory();
        tracks.clear();
        duration.clear();
        uri.clear();
        cursor.moveToFirst();
        //Log.d("check","ooo"+cursor.getString(1));
        int i=0;
        while(i<cursor.getCount()){
            tracks.add(cursor.getString(1));
            uri.add(cursor.getString(2));
            duration.add(cursor.getString(3));
            i++;
            cursor.moveToNext();
        }

         list = (ListView) findViewById(R.id.list);
        //SimpleCursorAdapter sc = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor, new String[]{Database.TRACK_NAME}, new int[]{android.R.id.text1}, 0);
        final TrackAdapter adapter=new TrackAdapter(this,tracks,duration);
        list.setAdapter(adapter);
        Log.d("check","eee"+tracks.get(0));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int itemPosition = position;
                //Cursor cursor = (Cursor) list.getItemAtPosition(position);

                TextView t=(TextView) view.findViewById(R.id.textView1);
                int i;
                for( i=0;i<tracks.size();i++){
                    if(t.getText().equals(tracks.get(i))){
                        break;
                    }
                }
                clicked=false;

                //helper.addHistory(t.getText().toString(), uri.get(i), duration.get(i));
                /*Intent intent = new Intent(Intent.ACTION_VIEW);
                String str = "file://" + uri.get(i);
                //helper.addToHistory(tracks.get(position), str);
                Uri uri = Uri.parse(str);
                String type = "audio/*";
                intent.setDataAndType(uri, type);
                startActivity(intent);*/
                /*Intent intent1 = null;
                adapter.notifyDataSetChanged();
                intent1 = new Intent(getApplicationContext(), MyMediaPlayerService.class);


                stopService(intent1);
                /*Intent intent1 = new Intent("android.intent.action.VIEW");
                intent1.setData(u);
                startActivity(intent1);*/   //this works....DONT DELETE


/*
                int currentPosition = position;
                //Log.d("TAGGG",track.get(currentPosition));
                Intent intent=new Intent(getApplicationContext(),MyMediaPlayerService.class);

                intent.putExtra("streamurl",uri.get(currentPosition));
                startService(intent);*/

            }



        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_history, menu);
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
            final DatabaseOpenHelper dbhelper=DatabaseOpenHelper.getInstance(this);
            dbhelper.deleteHistory();
            //ada.notifyDataSetChanged();
            list.setAdapter(null);
        }
        if(id==R.id.albums)
        {
            Intent intent=new Intent(this,OnlineAlbums.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

