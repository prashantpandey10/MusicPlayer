package com.example.prashantpandey.prashant_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Created by Prashant.Pandey on 2014-10-01.
 */
public class SoundSearch extends Activity {

    ArrayList<String> artistsonglist = new ArrayList<String>();
    ArrayList<String> songstreamlist = new ArrayList<String>();

    private int currentPosition;
    private ListView list;
    private String artist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soundsearch);
        list=(ListView)findViewById(R.id.list);
        Intent intent=getIntent();
        artist=intent.getStringExtra("artiste");
        Log.d("TAGARTIST",artist);
        new searchactivity(this).execute();
    }

    public class searchactivity extends AsyncTask<String, String, Boolean> {


        String append = "?consumer_key=55705d5e4806d3c0cec8c670e0c8bd8a&filter=all&order=created_at&order=original&allow_redirects=True";


        String url = "https://api.soundcloud.com/tracks.json?consumer_key=55705d5e4806d3c0cec8c670e0c8bd8a&q="+""+artist+""+"&filter=all&order=created_at&order=default";

        private Context context;
        private String result;
        private int trackcount;
        String title, streamurl;
        Boolean isstreamable = false;
        private Activity activity;
        private ArrayAdapter<String> adapter;

        public searchactivity(Activity activity) {

            this.activity = activity;
            context = activity;

        }




        @Override
        protected Boolean doInBackground(String... params) {

            TaskHandler handler = new TaskHandler();
            result = handler.execute(url);


            try {
                JSONArray jsonArray=new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject tracks = jsonArray.getJSONObject(i);
                    isstreamable = tracks.getBoolean("streamable");
                    if (isstreamable) {

                        Log.d("TAGURLL",url);
                        title = tracks.getString("title");
                        Log.d("SEARCHTITLE", title);
                        streamurl = tracks.getString("stream_url");

                    }
                    artistsonglist.add(title);
                    streamurl = streamurl + append;
                    songstreamlist.add(streamurl);
                    Log.d("SEARCHTITLE", streamurl);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,artistsonglist);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    currentPosition = i;

                    /*Intent intent = null;

                    intent = new Intent(getApplicationContext(), MyMediaPlayerService.class);


                    stopService(intent);
                    intent.putExtra("streamurl", songstreamlist.get(currentPosition));
                    intent.setAction("START");
                    intent.putExtra("songname", artistsonglist.get(i));

                    Toast.makeText(getApplicationContext(), "Buffering      " + ((TextView) view).getText().toString(), Toast.LENGTH_LONG).show();
                    startService(intent);*/
                    if(Songlist.am!=null)
                        Songlist.am.setMode(AudioManager.MODE_NORMAL);
                    Intent intent =new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(songstreamlist.get(currentPosition)),"audio/*");
                    startActivity(intent);
                }
            });


        }
    }
}