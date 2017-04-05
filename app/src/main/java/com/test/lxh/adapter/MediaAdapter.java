package com.test.lxh.adapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.test.lxh.R;
import com.test.lxh.bean.Media;

import java.util.List;

/**
 * Created by LXH on 17/4/5.
 */

public class MediaAdapter extends BaseAdapter<Media> {
    public MediaAdapter(Context context, List<Media> datas, int resLayoutId) {
        super(context, datas, resLayoutId);
    }

    @Override
    public void convert(MyViewHolder holder, Media media, int position) {
//        holder.setImageResId(R.id.iv_media,media.path);
        Glide.with(context).load(media.path).into(holder.getImage(R.id.iv_media));
    }

}
