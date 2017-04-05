package com.test.lxh.adapter;

/**
 * Created by LXH on 17/4/5.
 */

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseRecyclerAdapter<T> extends
        RecyclerView.Adapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mDatas = new LinkedList<T>();
    public OnItemClickListener<T> mOnItemClickListener;

    private int resLayoutId;

    public BaseRecyclerAdapter(Context context, List<T> datas, int resLayoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.resLayoutId = resLayoutId;
        if (datas != null) {
            mDatas = datas;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mDatas.size() > 0) {
            count = mDatas.size();
        }
        return count;
    }

    public void addItemLast(List<T> datas) {
        mDatas.addAll(datas);
    }

    public void addItemTop(List<T> datas) {
        mDatas = datas;
    }

    public void remove(int position) {
        mDatas.remove(position);
    }

    public void removeAll() {
        mDatas.clear();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    // 点击事件接口
    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T model);

        void onItemLongClick(View view, int position, T model);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }

//    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    public abstract void convert(BaseRecyclerViewHolder holder, T t, int position);

    private BaseRecyclerViewHolder viewHolder;
    View view;

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(resLayoutId, parent, false);
        viewHolder = new BaseRecyclerViewHolder(view);
        return viewHolder;
    }

    class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        public BaseRecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public void setText(int viewId, CharSequence string) {
            TextView textView = (TextView) view.findViewById(viewId);
            textView.setText(string);
        }

        public void setImageResId(int viewId, String path) {
            ImageView iv = (ImageView) view.findViewById(viewId);
            iv.setImageURI(Uri.fromFile(new File(path)));

        }
    }


}
