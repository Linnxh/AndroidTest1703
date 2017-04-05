package com.test.lxh.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.test.lxh.R;
import com.test.lxh.adapter.AlbumAdapter;
import com.test.lxh.adapter.MediaAdapter;
import com.test.lxh.bean.Album;
import com.test.lxh.bean.Media;
import com.test.lxh.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LXH on 17/4/5.
 */

public class AlbumActivity extends AppCompatActivity implements AlbumAdapter.OnAlbumClickListener {

    RecyclerView recy_album;
    List<Album> albumList;
    AlbumAdapter albumAdapter;
    List<Media> mediaList;
    MediaAdapter mediaAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acti_album);
        recy_album = (RecyclerView) findViewById(R.id.recy_album);
        albumList = new ArrayList<>();
        albumAdapter = new AlbumAdapter(AlbumActivity.this, albumList, R.layout.item_album);
        recy_album.setLayoutManager(new GridLayoutManager(this,2));
        recy_album.setAdapter(albumAdapter);
        new PrefetchAlbumsData().execute();
        albumAdapter.setOnAlbumClickListener(this);
    }

    @Override
    public void onAlbumClick(Album album) {
        mediaList = getMedia(this, album.id, -1);
        recy_album.setLayoutManager(new GridLayoutManager(this,3));
        mediaAdapter = new MediaAdapter(this, mediaList, R.layout.item_media);
        recy_album.setAdapter(mediaAdapter);
    }


    private class PrefetchAlbumsData extends AsyncTask<Boolean, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... arg0) {

            String[] projection = new String[]{
                    MediaStore.Files.FileColumns.PARENT,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            };

            String selection, selectionArgs[];
            selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=? or " +
                    MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                    + " ) GROUP BY ( " + MediaStore.Files.FileColumns.PARENT + " ";

            selectionArgs = new String[]{
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
            };

            Cursor cur = AlbumActivity.this.getContentResolver().query(
                    MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, null);

            if (cur != null) {
                if (cur.moveToFirst()) {
                    int idColumn = cur.getColumnIndex(MediaStore.Files.FileColumns.PARENT);
                    int nameColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    do {
                        Media media = getLastMedia(AlbumActivity.this, cur.getLong(idColumn));
                        if (media != null && media.path != null) {
                            String path = StringUtils.getBucketPathByImagePath(media.path);
                            Album album = new Album(AlbumActivity.this, path, cur.getLong(idColumn), cur.getString(nameColumn), getAlbumCount(AlbumActivity.this, cur.getLong(idColumn)));
                            album.media.add(getLastMedia(AlbumActivity.this, album.id));
                            Log.i("BBBB", "media-->" + album.media.toString());
                            albumList.add(album);
                        }
                    }
                    while (cur.moveToNext());
                }
                cur.close();

            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            albumAdapter.notifyDataSetChanged();
        }

    }


    @Nullable
    private static Media getLastMedia(Context context, long albumId) {
        ArrayList<Media> list = getMedia(context, albumId, 1);
        return list.size() > 0 ? list.get(0) : null;
    }

    private static ArrayList<Media> getMedia(Context context, long albumId, int n) {

        String limit = n == -1 ? "" : "LIMIT " + n;
        ArrayList<Media> list = new ArrayList<Media>();


        String[] projection = new String[]{
                // NOTE: don't change the order!
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.ORIENTATION
        };

        Uri images = MediaStore.Files.getContentUri("external");
        String selection, selectionArgs[];


//        if (includeVideo) {
        selection = "( " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? or "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? ) and " + MediaStore.Files.FileColumns.PARENT + "=?";

        selectionArgs = new String[]{
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                String.valueOf(albumId)
        };
//        } else {
//            selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=?  and " + MediaStore.Files.FileColumns.PARENT + "=?";
//            selectionArgs = new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), String.valueOf(albumId)};
//        }

        Cursor cur = context.getContentResolver().query(
                images, projection, selection, selectionArgs,
                " " + MediaStore.Images.Media.DATE_TAKEN + " DESC " + limit);

        if (cur != null) {
            if (cur.moveToFirst()) do list.add(new Media(cur)); while (cur.moveToNext());
            cur.close();
        }
        return list;
    }

    private static int getAlbumCount(Context context, long id) {
        int c = 0;
        String selection = "( " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? or "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? ) and " + MediaStore.Files.FileColumns.PARENT + "=?";

        String[] selectionArgs = new String[]{
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                String.valueOf(id)
        };

        Cursor cur = context.getContentResolver().query(MediaStore.Files.getContentUri("external"),
                new String[]{MediaStore.Files.FileColumns.PARENT}, selection, selectionArgs, null);

        if (cur != null) {
            c = cur.getCount();
            cur.close();
        }
        return c;
    }

}
