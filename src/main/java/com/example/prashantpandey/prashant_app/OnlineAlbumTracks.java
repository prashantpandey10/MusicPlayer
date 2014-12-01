package com.example.prashantpandey.prashant_app;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OnlineAlbumTracks extends Activity {

    private String albumname;
    private String albumcontent;
    private JSONObject jobject;
    private int trackcount;
    private String songtitle;
 ArrayList<String> songtitlelist=new ArrayList<String>();
    private String songstreamurl;
  ArrayList<String> songstreamlist=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ListView list;
    private String append;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_album_tracks);
        Intent intent=getIntent();
        albumname=intent.getStringExtra("albumname");
        append = "?consumer_key=55705d5e4806d3c0cec8c670e0c8bd8a&filter=all&order=created_at&order=original&allow_redirects=True";
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,songtitlelist);
        text=(TextView)findViewById(R.id.text);
        text.setText(albumname);
        albumcontent=intent.getStringExtra("albumcontent");

        try {
            jobject=new JSONObject(albumcontent);
            trackcount = jobject.getInt("track_count");

            JSONArray trackarray=jobject.getJSONArray("tracks");
            for(int j=0;j<trackcount;j++)
            {
                JSONObject trackobject=trackarray.getJSONObject(j);
                songtitle=trackobject.getString("title");
                songtitlelist.add(songtitle);
                songstreamurl=trackobject.getString("stream_url");
                songstreamlist.add(songstreamurl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url=songstreamlist.get(position);
                String mainurl=url+append;
                if(Songlist.am!=null)
                    Songlist.am.setMode(AudioManager.MODE_NORMAL);
                Intent intent =new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(mainurl),"audio/*");
                startActivity(intent);

                /*

                String url=songstreamlist.get(position);
                String mainurl=url+append;
                Intent intent = null;

                intent = new Intent(getApplicationContext(), MyMediaPlayerService.class);


                stopService(intent);
                intent.putExtra("streamurl", mainurl);
                intent.setAction("START");
                intent.putExtra("songname", songtitlelist.get(position));

                Toast.makeText(getApplicationContext(), "Buffering      " + ((TextView) view).getText().toString(), Toast.LENGTH_LONG).show();
                startService(intent);*/


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.online_album_tracks, menu);
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
        return super.onOptionsItemSelected(item);
    }
}
