package com.test.lxh.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.test.lxh.MyScrollView;
import com.test.lxh.R;

public class MainActivity extends AppCompatActivity {
    int lastScrollY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn2 = (Button) findViewById(R.id.btn2);
        setFloatBtnVisable();
        setColor();
    }

    /**
     * 监听scrollview的上滑和下拉
     */
    public void setFloatBtnVisable() {
        final FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_camera);
        MyScrollView scrollView = (MyScrollView) findViewById(R.id.ScrollView);
        scrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScrollY(int type) {
                if (type == 1) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                } else {
                    floatingActionButton.setVisibility(View.GONE);
                }
            }
        });
    }

    /***
     * 通过代码改变图片颜色
     */
    public void setColor() {
        Drawable drawable;
        try {
            drawable = VectorDrawableCompat.create(this.getResources(), R.drawable.ic_scrollbar, null);
        } catch (Resources.NotFoundException e) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ic_scrollbar);
        }
//        PorterDuffColorFilter filter = new PorterDuffColorFilter(color, mode);
//        Drawable drawable = getDrawable(R.mipmap.ic_launcher);
//        drawable.setColorFilter(filter);

        drawable.setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);

//        Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
//        DrawableCompat.setTint(wrapDrawable, Color.parseColor("#ffaadd"));

//        wrapDrawable.setTint(Color.parseColor("#ffaadd"));
//        btn2.setBackgroundDrawable(wrapDrawable);

//        scrollView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

//        scrollView.
//        Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
//        DrawableCompat.setTint(wrapDrawable, color);
    }


}
