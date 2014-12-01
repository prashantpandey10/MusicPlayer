package com.example.prashantpandey.prashant_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OnlineAlbums extends Activity {

    private ProgressDialog dialog;
    private ListView list;
    private int albumcount;

    private String albumname;
    ArrayList<String> albumnamelist=new ArrayList<String>();



    private ArrayAdapter<String> adapter;
    private int currentPosition;
    private JSONObject jobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_albums);
        list=(ListView)findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,albumnamelist);
        new Class2(this).execute();

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


        if(id==R.id.show)
        {
            Intent intent=new Intent(this,ShowHistory.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    class Class2 extends AsyncTask<String, Void, Boolean> {


        String url = "https://api.soundcloud.com/users/114793208/playlists.json?consumer_key=55705d5e4806d3c0cec8c670e0c8bd8a";
        private Context context;
        private String result;


        public Class2(Context context) {


            this.context =context;

        }


        ArrayList<JSONObject> jsonObjectArrayList=new ArrayList<JSONObject>();


        @Override
        protected Boolean doInBackground(String... params) {

            TaskHandler handler = new TaskHandler();
            result = handler.execute(url);

            try {

                JSONArray jsonArray = new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++)
                {
                    jobject=jsonArray.getJSONObject(i);
                    albumname=jobject.getString("permalink");
                    albumnamelist.add(albumname);
                    jsonObjectArrayList.add(jobject);

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

            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    currentPosition = i;

                    Intent intent=new Intent(getApplicationContext(), OnlineAlbumTracks.class);
                    intent.putExtra("albumname",albumnamelist.get(currentPosition));
                    intent.putExtra("albumcontent", String.valueOf(jsonObjectArrayList.get(currentPosition)));

                   startActivity(intent);
                }
            });


        }
    }
}
