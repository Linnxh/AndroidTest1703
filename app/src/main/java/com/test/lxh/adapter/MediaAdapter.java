package com.test.lxh.adapter;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.test.lxh.R;
import com.test.lxh.bean.Media;

import java.util.List;

/**
 * Created by LXH on 17/4/5.
 */

public class MediaAdapter extends BaseAdapter<Media> {

    public List<Media> medias;
    public MediaAdapter(Context context, List<Media> datas, int resLayoutId) {
        super(context, datas, resLayoutId);
        medias=datas;
    }

    public onMediaListener listener;

    public void setOnMediaClickListner(onMediaListener listner) {
        this.listener = listner;
    }

    @Override
    public void convert(MyViewHolder holder, final Media media, int position) {
//        holder.setImageResId(R.id.iv_media,media.path);
        Glide.with(context).load(media.path).into(holder.getImage(R.id.iv_media));
        holder.getView(R.id.iv_media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMediaClickListener(medias);
                }
            }
        });
    }

    public interface onMediaListener {
        void onMediaClickListener(List<Media> media);
    }
}
