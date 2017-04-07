package com.test.lxh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.test.lxh.R;
import com.test.lxh.bean.Media;
import com.test.lxh.view.RotateTransformation;

import org.w3c.dom.Text;

import java.util.Date;


/**
 * Created by dnld on 18/02/16.
 */

@SuppressWarnings("ResourceType")
public class ImageFragment extends Fragment {

    private Media img;

    public static ImageFragment newInstance(Media media) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putSerializable("image", media);
//        args.putParcelable("image", media);
        imageFragment.setArguments(args);

        return imageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img = (Media) getArguments().getSerializable("image");
        Log.i("BBBB","img-->"+img.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.item_fragment_singlemedia, container, false);
//        TextView tv_fragment_title = (TextView) view.findViewById(R.id.tv_fragment_title);
////        tv_fragment_title.setText("3333"+img.toString());
//        tv_fragment_title.setText("3333");
//        if (PreferenceUtil.getInstance(getContext()).getBoolean(getString(R.string.preference_sub_scaling) , false)) {
//            SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(getContext());
//            imageView.setImage(ImageSource.uri(img.getUri()));
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ((SingleMediaActivity) getActivity()).toggleSystemUI();
//                }
//            });
//            return imageView;
//        } else {
        ImageView photoView = new ImageView(getContext());
        displayMedia(photoView, true);
//            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//                @Override
//                public void onPhotoTap(View view, float x, float y) {
//                    ((SingleMediaActivity) getActivity()).toggleSystemUI();
//                }
//
//                @Override
//                public void onOutsidePhotoTap() {
//                    ((SingleMediaActivity) getActivity()).toggleSystemUI();
//                }
//            });
//            photoView.setMaximumScale(5.0F);
//            photoView.setMediumScale(3.0F);

        return photoView;
//        }
    }

   /* private void rotateLoop() { //april fools
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                photoView.setRotationBy(1);
                rotateLoop();
            }
        }, 5);
    }*/

    private void displayMedia(ImageView photoView, boolean useCache) {
        //PreferenceUtil SP = PreferenceUtil.getInstance(getContext());

        Glide.with(getContext())
                .load(img.path)
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .thumbnail(0.5f)
                .transform(new RotateTransformation(getContext(), img.orientation, false))
                .into(photoView);

    }

    public boolean rotatePicture(int rotation) {
        // TODO: 28/08/16 not working yet
        /*PhotoView photoView = (PhotoView) getView();

        int orientation = Measure.rotateBy(img.getOrientation(), rotation);
        Log.wtf("asd", img.getOrientation()+" + "+ rotation+" = " +orientation);

        if(photoView != null && img.setOrientation(orientation)) {
            Glide.clear(photoView);
            Glide.with(getContext())
                    .load(img.getUri())
                    .asBitmap()
                    .signature(img.getSignature())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    //.thumbnail(0.5f)
                    .transform(new RotateTransformation(getContext(), rotation , true))
                    .into(photoView);

            return true;
        }*/
        return false;
    }
}