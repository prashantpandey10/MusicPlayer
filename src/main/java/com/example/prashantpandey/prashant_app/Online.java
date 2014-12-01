package com.example.prashantpandey.prashant_app;

/**
 * Created by Prashant.Pandey on 2014-09-17.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Online extends Fragment {
    private ListView list;
    ArrayList artists;

    private int currentPosition;
    static ArrayList<String> tracklist = new ArrayList<String>();
    static ArrayList<String> streamlist = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ProgressDialog dialog=null;
    private static final int NOTIFICATION_ID = 1    ;
    private ImageButton pause;

    private ImageButton back;
    private ImageButton next;
    private View view;
    private Button search;

    private EditText edit;
    private ImageButton play;
    Communicator c;

    public interface Communicator{
         public void click();
    }

public void onAttach(Activity a){
    super.onAttach(a);
    c = (Communicator) a;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final DatabaseOpenHelper dbhelper = DatabaseOpenHelper.getInstance(getActivity());
        view = inflater.inflate(R.layout.online, container, false);
        //((TextView)windows.findViewById(R.id.textView)).setText("Windows");
        list = (ListView)view.findViewById(R.id.list);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tracklist);
        Log.d("TAGMAIN", "INcreate");

        if (MainActivity1.activeInfo != null && MainActivity1.activeInfo.isConnected())
            new Class1(getActivity()).execute();
        else
            Toast.makeText(getActivity(),"NET DISCONNECTED",Toast.LENGTH_LONG).show();




        //NET connection checking






        search=(Button)view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            public String artistname;

            @Override
            public void onClick(View v) {
                if (MainActivity1.activeInfo != null && MainActivity1.activeInfo.isConnected()) {
                    Intent intent = new Intent(getActivity(), SoundSearch.class);
                    edit = (EditText) view.findViewById(R.id.edit);
                    artistname = edit.getText().toString();

                    Log.d("TAGARTISTIN", artistname);
                    intent.putExtra("artiste", artistname);

                    startActivity(intent);
                }
                else
                    Toast.makeText(getActivity(),"NET DISCONNECTED",Toast.LENGTH_LONG).show();
            }
        });





        return view;
    }


    class Class1 extends AsyncTask<String, Void, Boolean> {
        String append = "?consumer_key=55705d5e4806d3c0cec8c670e0c8bd8a&filter=all&order=created_at&order=original&allow_redirects=True";
        final DatabaseOpenHelper dbhelper = DatabaseOpenHelper.getInstance(getActivity());
        String url = "http://api.soundcloud.com/playlists/405726.json?client_id=55705d5e4806d3c0cec8c670e0c8bd8a";
        private Context context;
        private String result;
        private int trackcount;
        String title, streamurl;
        Boolean isstreamable = false;
        private Activity activity;

        public Class1(Activity activity) {

            this.activity = activity;
            context = activity;
            dialog = new ProgressDialog(context);
        }


        protected void onPreExecute() {
            dialog.setMessage("Progress start");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            TaskHandler handler = new TaskHandler();
            result = handler.execute(url);

            try {

                JSONObject jobject = new JSONObject(result);
                trackcount = jobject.getInt("track_count");
                JSONArray trackarray = jobject.getJSONArray("tracks");
                for (int i = 0; i < trackcount; i++) {
                    JSONObject tracks = trackarray.getJSONObject(i);
                    isstreamable = tracks.getBoolean("streamable");
                    if (isstreamable) {
                        title = tracks.getString("title");
                        streamurl = tracks.getString("stream_url");

                    }
                    tracklist.add(title);
                    streamurl = streamurl + append;
                    streamlist.add(streamurl);

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
                    if (MainActivity1.activeInfo != null && MainActivity1.activeInfo.isConnected()) {
                        currentPosition = i;
                        //dbhelper.addHistory(tracklist.get(i));
                        AudioManager am= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

                        am.setMode(AudioManager.MODE_NORMAL);

                        //getActivity().getSupportFragmentManager().beginTransaction().
                        //        remove(p).commit();
                        //getActivity().getSupportFragmentManager().beginTransaction().
                        //      remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.replace)).commit();
                        //getView().findViewById(R.id.replace).setVisibility(View.GONE);
                        c.click();
                        if(MyMediaPlayerService.mPlayer!=null)
                            MyMediaPlayerService.mPlayer.pause();
                        if(Songlist.am!=null)
                            Songlist.am.setMode(AudioManager.MODE_NORMAL);
                        Intent intent =new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(streamlist.get(currentPosition)),"audio/*");
                        startActivity(intent);

                    }
                    else
                        Toast.makeText(getActivity(),"NET DISCONNECTED",Toast.LENGTH_LONG).show();


                }
            });
            if (dialog!=null && dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adapter.clear();
        adapter.notifyDataSetChanged();


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

