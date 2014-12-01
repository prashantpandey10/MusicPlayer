package com.example.prashantpandey.prashant_app;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    String[] thumbs=new String[2000];

    int width,height;

    public ImageAdapter(Context c, String[] thumb) {
        mContext = c;
        thumbs=thumb;
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

    }

    public int getCount() {
        int i;
        for(i=0;thumbs[i]!=null;i++);
        return i;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        ViewHolder holder=null;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            holder=new ViewHolder();
            //holder.imageView=new ImageView((mContext));

            holder.position=position;
            //convertView=holder.imageView;

            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.grid_item, null);
            //GridView.LayoutParams params = new GridView.LayoutParams(mMainActivity.albumWidth,mMainActivity.albumHeight);
            //convertView.setLayoutParams(params);

            //imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            //holder.imageView.setImageResource(R.drawable.album);
            holder.pb = (ProgressBar)convertView.findViewById(R.id.grid_progressBar);
            holder.imageView = (ImageView)convertView.findViewById(R.id.grid_imageview);
            convertView.setTag(position);
            holder.imageView.setImageResource(R.drawable.album);

            holder.pb.setLayoutParams(new RelativeLayout.LayoutParams(300,300));
            holder.imageView.setAdjustViewBounds(true);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.imageView.setPadding(15, 15, 15, 15);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder)convertView.getTag();
            //holder.imageView = (ImageView) convertView;
            holder.position=position;
            holder.imageView.setVisibility(View.VISIBLE);
            //holder.imageView.setImageResource(R.drawable.album);
            //imageView = (ImageView) convertView;
            //imageView.setImageResource(R.drawable.album);
        }
        if(thumbs[position].equals("android.resource://com.example.prashantpandey.prashant_app/" + R.drawable.album))
            holder.imageView.setImageResource(R.drawable.album);
        else {
            ImageLoader task = new ImageLoader(holder,position,thumbs[position]);
            task.execute();
            //imageView.setImageBitmap(BitmapFactory.decodeFile(thumbs[position],options));
        }
        return convertView;
    }
}

