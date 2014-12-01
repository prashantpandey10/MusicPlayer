package com.example.prashantpandey.prashant_app;

import android.app.ActionBar;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.example.prashantpandey.prashant_app.R.*;

public class MainActivity1 extends FragmentActivity implements Online.Communicator{

    private ActionBar actionbar;
    private ViewPager viewPager;
    private Tabs mAdapter;
    private ActionBar actionBar;

    private String[] tabs = { "LOCAL", "ONLINE", "JSON" };

    private Intent intent;
     ConnectivityManager connMgr;
    static NetworkInfo activeInfo;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Songlist.am.setMode(AudioManager.MODE_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_main);

        Songlist.am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);


        connMgr =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected())
            Toast.makeText(this,"NET CONNECTED",Toast.LENGTH_LONG).show();
        else
        Toast.makeText(this,"NET DISCONNECTED",Toast.LENGTH_LONG).show();
        mAdapter = new Tabs(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(id.pager);
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {

                        actionBar = getActionBar();

                        actionBar.setSelectedNavigationItem(position);  }
                });
        viewPager.setAdapter(mAdapter);
        actionBar = getActionBar();

        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
            @Override
            public void onTabReselected(android.app.ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(android.app.ActionBar.Tab tab,
                                        FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }};
        //Add New Tab
        actionBar.addTab(actionBar.newTab().setText("LOCAL").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("ONLINE").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("RADIO").setTabListener(tabListener));
        Player fv =new Player();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.replace, fv);
        ft.commit();
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
        if(id==R.id.albums)
        {
            Intent intent=new Intent(this,OnlineAlbums.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void click() {
        Player frag=new Player();
        frag.show();
    }
}
