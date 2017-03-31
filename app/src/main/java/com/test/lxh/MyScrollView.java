package com.test.lxh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by LXH on 17/3/30.
 * 自定义scrollview监听上滑下拉
 */

public class MyScrollView extends ScrollView {
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置滚动接口
     *
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    boolean isFirstTouch = true;
    int lastY;
    OnScrollListener onScrollListener;

    public interface OnScrollListener {
        /**
         * @param type 1  下拉 0上滑
         */
        void onScrollY(int type);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int y = (int) ev.getRawY();
        int deltaY = y - lastY;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (isFirstTouch) {
                    deltaY = 0;
                    isFirstTouch = false;
                }
                if (deltaY > 0) {
                    Log.i("BBBB", "下拉---");
                    //下拉
                    if (onScrollListener != null) {
                        onScrollListener.onScrollY(1);
                    }
                } else if (deltaY < 0) {
                    //上滑
                    Log.i("BBBB", "上滑---");
                    if (onScrollListener != null) {
                        onScrollListener.onScrollY(0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i("BBBB", "ACTION_UP---ACTION_CANCEL");
                isFirstTouch = true;
                break;
        }
        lastY = y;

        return super.onTouchEvent(ev);
    }
}
