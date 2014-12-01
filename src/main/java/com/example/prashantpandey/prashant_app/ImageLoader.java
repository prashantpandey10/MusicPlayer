package com.example.prashantpandey.prashant_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by chirag.kadam on 06-10-2014.
 */
public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

    private int mPosition;
    private ViewHolder mHolder;
    String url;

    ImageLoader(ViewHolder holder, int position, String url) {
        mPosition = position;
        mHolder = holder;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mHolder.imageView.setImageResource(R.drawable.white);
        if (!url.equals("android.resource://com.example.prashantpandey.prashant_app/" + R.drawable.album))
            mHolder.pb.setVisibility(View.VISIBLE);
        mHolder.imageView.setVisibility(View.GONE);
    }

    @Override
    protected Bitmap doInBackground(Void... arg) {
        if (mHolder.position == mPosition) {
            Bitmap bp = BitmapFactory.decodeFile(url);
            return bp;
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result == null) {
            mHolder.pb.setVisibility(View.INVISIBLE);
            return;
        }
        mHolder.pb.setVisibility(View.INVISIBLE);
        super.onPostExecute(result);

        if (mHolder.position == mPosition) {
            mHolder.imageView.setVisibility(View.VISIBLE);
            mHolder.imageView.setImageBitmap(result);
        }

    }
}