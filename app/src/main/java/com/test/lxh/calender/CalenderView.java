package com.test.lxh.calender;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.lxh.R;
import com.test.lxh.adapter.MyViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import static com.test.lxh.R.id.tv_dateTitle;

/**
 * Created by LXH on 17/5/5.
 */

public class CalenderView extends LinearLayout implements View.OnClickListener {

    private Calendar curDate = Calendar.getInstance();
    private Context context;
    myAdapter adapter;
    ArrayList<Date> datas;
    TextView tv_dateTitle;
    CharSequence titleStyle;

    public CalenderView(Context context) {
        super(context);
    }

    public CalenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context, attrs);
    }

    public CalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.calender, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CalenderView);
        try {
            titleStyle = array.getText(R.styleable.CalenderView_titleStyle);
            if (TextUtils.isEmpty(titleStyle)) {
                titleStyle = "yyyy MM";
            }
        } catch (Exception e) {

        } finally {
            array.recycle();
        }
        tv_dateTitle = (TextView) findViewById(R.id.tv_dateTitle);
        RecyclerView recy_claender = (RecyclerView) findViewById(R.id.recy_claender);
        TextView tv_dateTitle_left = (TextView) findViewById(R.id.tv_dateTitle_left);
        TextView tv_dateTitle_right = (TextView) findViewById(R.id.tv_dateTitle_right);
        recy_claender.setLayoutManager(new GridLayoutManager(context, 7));
        adapter = new myAdapter();
        recy_claender.setAdapter(adapter);
        tv_dateTitle_left.setOnClickListener(this);
        tv_dateTitle_right.setOnClickListener(this);

        setData();
    }

    public void setData() {


        SimpleDateFormat formatter = new SimpleDateFormat(titleStyle.toString());
        tv_dateTitle.setText(formatter.format(curDate.getTime()));

        datas = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -prevDays + 1);

        int max = 7 * 6;
        while (datas.size() < max) {
            datas.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        adapter.notifyDataSetChanged();
    }

    dateLongPressListener listener;

    public void setDateLongPressListener(dateLongPressListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dateTitle_left:
                curDate.add(Calendar.MONTH, -1);
                setData();
                break;
            case R.id.tv_dateTitle_right:
                curDate.add(Calendar.MONTH, 1);
                setData();
                break;
        }
    }

    public class myAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_calender, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv.setText(datas.get(position).getDate() + "");

            if (datas.get(position).getMonth() == (curDate.get(Calendar.MONTH))) {
                holder.tv.setTextColor(Color.parseColor("#000000"));
            } else {
                holder.tv.setTextColor(Color.parseColor("#7e7d7d"));
            }
            final Date date = datas.get(position);
            Date now = new Date();
            if ((now.getDate() == datas.get(position).getDate()) &&
                    (now.getMonth() == datas.get(position).getMonth()) &&
                    now.getYear() == datas.get(position).getYear()) {
                holder.tv.setTextColor(Color.parseColor("#fc4b4e"));
            }

            if (listener != null) {
                holder.tv.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listener.onDateLongPressListener(date, position);
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }


    }

    public interface dateLongPressListener {
        void onDateLongPressListener(Date date, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_item_calender);

        }
    }
}
