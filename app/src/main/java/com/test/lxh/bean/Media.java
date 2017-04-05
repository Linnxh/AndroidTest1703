package com.test.lxh.bean;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dnld on 26/04/16.
 */
public class Media implements Serializable {

    public String path = null;
    public long dateModified = -1;
    public String mimeType = null;
    public int orientation = 0;

    public String uri = null;

    public long size = 0;
    public boolean selected = false;

    public Media(Cursor cur) {
        this.path = cur.getString(0);
        this.dateModified = cur.getLong(1);
        this.mimeType = cur.getString(2);
        this.size = cur.getLong(3);
        this.orientation = cur.getInt(4);
    }

    @Override
    public String toString() {
        return "Media{" +
                "path='" + path + '\'' +
                ", dateModified=" + dateModified +
                ", mimeType='" + mimeType + '\'' +
                ", orientation=" + orientation +
                ", uri='" + uri + '\'' +
                ", size=" + size +
                ", selected=" + selected +
                '}';
    }

//    private MetadataItem metadata;

//    public Media() { }

//    public Media(String path, long dateModified) {
//        this.path = path;
//        this.dateModified = dateModified;
////        this.mimeType = StringUtils.getMimeType(path);
//    }
//
//    public Media(File file) {
//        this(file.getPath(), file.lastModified());
//        this.size = file.length();
////        this.mimeType = StringUtils.getMimeType(path);
//    }
//
//    public Media(String path) {
//        this(path, -1);
//    }
//
//    public Media(Context context, Uri mediaUri) {
//        this.uri = mediaUri.toString();
//        this.path = null;
//        this.mimeType = context.getContentResolver().getType(getUri());
//    }
//
//    private static int findOrientation(int exifOrientation){
//        switch (exifOrientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90: return 90;
//            case ExifInterface.ORIENTATION_ROTATE_180: return 180;
//            case ExifInterface.ORIENTATION_ROTATE_270: return 270;
//        }
//        return 0;
//    }
//
//    public void setUri(String uriString) {
//        this.uri = uriString;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public String getMimeType() {
//        return mimeType;
//    }
//
//    public boolean isSelected() {
//        return selected;
//    }
//
//    void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//
//    public boolean isGif() { return mimeType.endsWith("gif"); }
//
//    public boolean isImage() { return mimeType.startsWith("image"); }
//
//    public boolean isVideo() { return mimeType.startsWith("video"); }
//
//    public Uri getUri() {
//        return uri != null ? Uri.parse(uri) : Uri.fromFile(new File(path));
//    }
//
////    public String getName() {
////        return StringUtils.getPhotoNameByPath(path);
////    }
//
//    public long getSize() {
//        return size;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public Long getDateModified() {
//        return dateModified;
//    }
//
//
//    public Bitmap getBitmap(){
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
//        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
//        return bitmap;
//    }

//    public MediaSignature getSignature() {
//        return new MediaSignature(this);
//    }
//
//    //<editor-fold desc="Exif & More">
//    public GeoLocation getGeoLocation()  {
//        return metadata.getLocation();
//    }
//
//    public MediaDetailsMap<String, String> getAllDetails() {
//        MediaDetailsMap<String, String> data = new MediaDetailsMap<String, String>();
//        try {
//            Metadata metadata = ImageMetadataReader.readMetadata(new File(path));
//            for(Directory directory : metadata.getDirectories()) {
//
//                for(Tag tag : directory.getTags()) {
//                    data.put(tag.getTagName(), directory.getObject(tag.getTagType())+"");
//                }
//            }
//        } catch (ImageProcessingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
//
////    public MediaDetailsMap<String, String> getMainDetails(Context context){
////        metadata = new MetadataItem(new File(path));
////        MediaDetailsMap<String, String> details = new MediaDetailsMap<String, String>();
////        details.put(context.getString(R.string.path), path != null ? path : getUri().getEncodedPath());
////        details.put(context.getString(R.string.type), getMimeType());
////        String tmp;
////        if ((tmp = metadata.getResolution()) != null)
////            details.put(context.getString(R.string.resolution), tmp);
////
////        details.put(context.getString(R.string.size), StringUtils.humanReadableByteCount(size, true));
////        details.put(context.getString(R.string.date), SimpleDateFormat.getDateTimeInstance().format(new Date(getDateModified())));
////        details.put(context.getString(R.string.orientation), getOrientation()+"");
////        if (metadata.getDateOriginal() != null)
////            details.put(context.getString(R.string.date_taken), SimpleDateFormat.getDateTimeInstance().format(metadata.getDateOriginal()));
////
////        if ((tmp = metadata.getCameraInfo()) != null)
////            details.put(context.getString(R.string.camera), tmp);
////        if ((tmp = metadata.getExifInfo()) != null)
////            details.put(context.getString(R.string.exif), tmp);
////        GeoLocation location;
////        if ((location = metadata.getLocation()) != null)
////            details.put(context.getString(R.string.location), location.toDMSString());
////
////        return details;
////    }
//
//    public boolean setOrientation(final int orientation) {
//        this.orientation = orientation;
//        // TODO: 28/08/16  find a better way
//        new Thread(new Runnable() {
//            public void run() {
//                int exifOrientation = -1;
//                try {
//                    ExifInterface exif = new ExifInterface(path);
//                    switch (orientation) {
//                        case 90: exifOrientation = ExifInterface.ORIENTATION_ROTATE_90; break;
//                        case 180: exifOrientation = ExifInterface.ORIENTATION_ROTATE_180; break;
//                        case 270: exifOrientation = ExifInterface.ORIENTATION_ROTATE_270; break;
//                        case 0: exifOrientation = ExifInterface.ORIENTATION_NORMAL; break;
//                    }
//                    if (exifOrientation != -1) {
//                        exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(exifOrientation));
//                        exif.saveAttributes();
//                    }
//                }
//                catch (IOException ignored) {  }
//            }
//        }).start();
//        return true;
//    }
//
//    private long getDateTaken() {
//        // TODO: 16/08/16 improved
//        Date dateOriginal = metadata.getDateOriginal();
//        if (dateOriginal != null) return metadata.getDateOriginal().getTime();
//        return -1;
//    }
//
//    public boolean fixDate(){
//        long newDate = getDateTaken();
//        if (newDate != -1){
//            File f = new File(path);
//            if (f.setLastModified(newDate)) {
//                dateModified = newDate;
//                return true;
//            }
//        }
//        return false;
//    }
//    //</editor-fold>
//
//    public File getFile() {
//        if (path != null) return new File(path);
//        return null;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.path);
//        dest.writeLong(this.dateModified);
//        dest.writeString(this.mimeType);
//        dest.writeLong(this.size);
//        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
//    }
//
//    protected Media(Parcel in) {
//        this.path = in.readString();
//        this.dateModified = in.readLong();
//        this.mimeType = in.readString();
//        this.size = in.readLong();
//        this.selected = in.readByte() != 0;
//    }
//
//    public static final Creator<Media> CREATOR = new Creator<Media>() {
//        @Override
//        public Media createFromParcel(Parcel source) {
//            return new Media(source);
//        }
//
//        @Override
//        public Media[] newArray(int size) {
//            return new Media[size];
//        }
//    };
//
//    public int getOrientation() {
//        return orientation;
//    }

    //</editor-fold>
}