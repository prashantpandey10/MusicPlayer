package com.example.prashantpandey.prashant_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.prashantpandey.prashant_app.R.layout.rowlayout;

/**
 * Created by Prashant.Pandey on 2014-09-23.
 */
public class CustomAdapter extends ArrayAdapter<Songs> {


    private static Context context;
    private ArrayList<Songs> songdetaillist;


    public CustomAdapter(Context context,ArrayList<Songs> songdetaillist) {

        super(context, R.layout.rowlayout,songdetaillist);


        this.context=context;
        this.songdetaillist=songdetaillist;


        }
    public View getView(int position,View parent,ViewGroup container)
    {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           View rowView=inflater.inflate(rowlayout,container,false);
        TextView text1=(TextView)rowView.findViewById(R.id.title);
        text1.setText(songdetaillist.get(position).getTitle());
        TextView text2=(TextView)rowView.findViewById(R.id.time);
        text2.setText(songdetaillist.get(position).getTime());
        TextView text3=(TextView)rowView.findViewById(R.id.artistname);
        text3.setText(songdetaillist.get(position).getArtistname());
        return rowView;




    }
}
