package com.example.prashantpandey.prashant_app;

/**
 * Created by Prashant.Pandey on 2014-09-17.
 */




import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


public class Local extends Fragment {

    String[] thumb=new String[1000];
    String[] album=new String[1000];
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.local, container, false);

       Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.AlbumColumns.ALBUM_ART,MediaStore.Audio.AlbumColumns.ALBUM},
                null, null,
                null);



        String res="android.resource://com.example.prashantpandey.prashant_app/" + R.drawable.album;

        if(cursor!=null)
        {
            int i=0;
            while(cursor.moveToNext())
            {
                String uri;
                if(cursor.getString(0)!=null) {
                    uri = cursor.getString(0);
                    album[i]=cursor.getString(1);
                    thumb[i++] = uri;
                }
                else{
                    album[i]=cursor.getString(1);
                    thumb[i++] = res;
                }
            }
        }

        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity(),thumb));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {



                Intent intent=new Intent(getActivity(),Songlist.class);
                intent.putExtra("position",position);
                intent.putExtra("album",album[position]);
                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
