package com.test.lxh.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.test.lxh.R;
import com.test.lxh.adapter.MediaPagerAdapter;
import com.test.lxh.bean.Media;
import com.test.lxh.view.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class SingleMediaActivity extends AppCompatActivity {

    ViewPager pager_singleMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_media);
        initView();
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Media> media = (ArrayList<Media>) getIntent().getSerializableExtra("media");
        pager_singleMedia = (ViewPager) findViewById(R.id.pager_singleMedia);
        MediaPagerAdapter adapter = new MediaPagerAdapter(getSupportFragmentManager(), media);
        pager_singleMedia.setAdapter(adapter);
        pager_singleMedia.setPageTransformer(true, new DepthPageTransformer());

        pager_singleMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pager_singleMedia.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
}
