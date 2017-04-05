package com.test.lxh.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by LXH on 17/4/5.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    View view;

    public MyViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;

    }

    public void setText(int viewId, CharSequence string) {
        TextView textView = (TextView) view.findViewById(viewId);
        textView.setText(string);
    }

    public void setImageResId(int viewId, String path) {
        ImageView iv = (ImageView) view.findViewById(viewId);
        iv.setImageURI(Uri.fromFile(new File(path)));
    }
    public ImageView getImage(int viewId){
        ImageView iv = (ImageView) view.findViewById(viewId);
        return iv;
    }

    public View getView(int viewId){
        return view.findViewById(viewId);
    }

}
