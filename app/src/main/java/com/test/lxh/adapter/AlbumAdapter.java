package com.test.lxh.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.test.lxh.R;
import com.test.lxh.bean.Album;

import java.util.List;

/**
 * Created by LXH on 17/4/5.
 */

public class AlbumAdapter extends BaseAdapter<Album> {

    Context context;
    public OnAlbumClickListener onAlbumClickListener;

    public AlbumAdapter(Context context, List<Album> datas, int resId) {
        super(context, datas, resId);
        this.context = context;
    }

    public void setOnAlbumClickListener(OnAlbumClickListener listener) {
        this.onAlbumClickListener = listener;
    }

    @Override
    public void convert(MyViewHolder holder, final Album album, int position) {
        if (album != null && album.media.size() > 0) {

            Glide.with(context)
                    .load(album.media.get(0).path)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .priority(Priority.HIGH)
                    .centerCrop()
                    .into(holder.getImage(R.id.iv_album_cover));

        }

        holder.setText(R.id.iv_album_title, album.path.substring(album.path.lastIndexOf("/") + 1));
        holder.getView(R.id.album_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAlbumClickListener != null) {
                    onAlbumClickListener.onAlbumClick(album);
                }
            }
        });
    }

    public interface OnAlbumClickListener {
        void onAlbumClick(Album album);
    }
}
