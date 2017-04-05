package com.test.lxh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by LXH on 17/4/5.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {

    private MyViewHolder viewHolder;
    View view;
    Context context;
    List<T> datas;
    int resLayoutId;

    public  BaseAdapter(Context context, List<T> datas, int resLayoutId) {
        this.context = context;
        this.datas = datas;
        this.resLayoutId = resLayoutId;
    }

    public void setDatas(List<T> datas){
        this.datas=datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(resLayoutId, parent, false);
        viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        convert(holder, datas.get(position), position);
    }

    public abstract void convert(MyViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
