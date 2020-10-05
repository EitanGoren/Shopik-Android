package com.eitan.shopik.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.eitan.shopik.Macros;
import com.eitan.shopik.R;

import java.util.ArrayList;

public class FullscreenAdapter extends RecyclerView.Adapter<FullscreenAdapter.PicsViewHolder> {

    private final ArrayList<String> imagesUrl;
    private final ImageView mDot1;
    private final ImageView mDot2;
    private final ImageView mDot3;
    private final ImageView mDot4;
    private final ImageView mDot5;
    private final CardView button;
    private final CardView buttons_layout;
    private final CardView comp_info;
    private final String description;
   // private String seller;
    private boolean isClicked = true;
    private final ViewPager2 viewPager;

    public FullscreenAdapter(Context context, ArrayList<String> imagesUrl, String description) {
        this.imagesUrl = imagesUrl;
        this.description = description;
      //  this.seller = seller;

        mDot1 = ((Activity)context).findViewById(R.id.fullscreen_dot_1);
        mDot2 = ((Activity)context).findViewById(R.id.fullscreen_dot_2);
        mDot3 = ((Activity)context).findViewById(R.id.fullscreen_dot_3);
        mDot4 = ((Activity)context).findViewById(R.id.fullscreen_dot_4);
        mDot5 = ((Activity)context).findViewById(R.id.fullscreen_dot_5);

        mDot1.setBackground(ContextCompat.getDrawable(mDot1.getContext(),R.drawable.ic_lens_black_24dp));
        mDot2.setBackground(ContextCompat.getDrawable(mDot2.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
        mDot3.setBackground(ContextCompat.getDrawable(mDot3.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
        mDot4.setBackground(ContextCompat.getDrawable(mDot4.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
        mDot5.setVisibility(View.GONE);

        button = ((Activity)context).findViewById(R.id.close_card);
        buttons_layout = ((Activity)context).findViewById(R.id.buttons_layout);
        comp_info = ((Activity)context).findViewById(R.id.comp_info);

        YoYo.with(Techniques.FadeInRight).playOn(button);
        YoYo.with(Techniques.FadeInDown).playOn(buttons_layout);
        YoYo.with(Techniques.FadeInLeft).playOn(comp_info);

        viewPager = ((Activity)context).findViewById(R.id.fullscreen_image_viewPager);
    }

    @NonNull
    @Override
    public FullscreenAdapter.PicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FullscreenAdapter.PicsViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fullscreen_pic,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FullscreenAdapter.PicsViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return imagesUrl.size();
    }

    private void changeTabs(int position) {
        switch (position){
            case 0:
                mDot1.setBackground(ContextCompat.getDrawable(mDot1.getContext(),R.drawable.ic_lens_black_24dp));
                mDot2.setBackground(ContextCompat.getDrawable(mDot2.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                break;
            case 1:
                mDot1.setBackground(ContextCompat.getDrawable(mDot1.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                mDot2.setBackground(ContextCompat.getDrawable(mDot2.getContext(),R.drawable.ic_lens_black_24dp));
                mDot3.setBackground(ContextCompat.getDrawable(mDot3.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                break;
            case 2:
                mDot2.setBackground(ContextCompat.getDrawable(mDot2.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                mDot3.setBackground(ContextCompat.getDrawable(mDot3.getContext(),R.drawable.ic_lens_black_24dp));
                mDot4.setBackground(ContextCompat.getDrawable(mDot4.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                break;
            case 3:
                mDot3.setBackground(ContextCompat.getDrawable(mDot3.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                mDot4.setBackground(ContextCompat.getDrawable(mDot4.getContext(),R.drawable.ic_lens_black_24dp));
                mDot5.setBackground(ContextCompat.getDrawable(mDot5.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                break;
            case 4:
                mDot4.setBackground(ContextCompat.getDrawable(mDot4.getContext(),R.drawable.ic_baseline_panorama_fish_eye));
                mDot5.setBackground(ContextCompat.getDrawable(mDot5.getContext(),R.drawable.ic_lens_black_24dp));
                break;
        }
    }

    class PicsViewHolder extends RecyclerView.ViewHolder{

        private final ImageView photoView;
        private final TextView textView;
        private final TextView no_video;
        private final VideoView mVideoView;
        private final MediaController videoMediaController;
        private final RelativeLayout videoLayout;
        private final RelativeLayout anchor;

        public PicsViewHolder(@NonNull View view) {
            super(view);
            photoView = view.findViewById(R.id.fullscreen_image_item);
            mVideoView = view.findViewById(R.id.item_video);
            mVideoView.setVisibility(View.GONE);
            videoMediaController = new MediaController(mVideoView.getContext());
            videoLayout = view.findViewById(R.id.video_layout);
            videoLayout.setVisibility(View.GONE);
            anchor = view.findViewById(R.id.video);
            anchor.setVisibility(View.GONE);
            textView = view.findViewById(R.id.fullscreen_item_info);
            no_video = view.findViewById(R.id.No_video_text);
        }

        private void setData(int position){
            textView.setText(description);
            String no_video_text = "No video for this item";
            no_video.setText(no_video_text);

            if (position != imagesUrl.size()) {
                videoLayout.setVisibility(View.INVISIBLE);
                videoMediaController.hide();
                photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                photoView.setPadding(0,0,0,0);
                Macros.Functions.GlidePicture(photoView.getContext(),imagesUrl.get(position),photoView);
                mVideoView.pause();
                videoMediaController.hide();
            }

            viewPager.registerOnPageChangeCallback (new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                @Override
                public void onPageSelected(int pos) {
                    changeTabs(pos);
                   /* if(category.equals("ASOS") && (pos == imagesUrl.size())) {

                        YoYo.with(Techniques.FadeOutRight).playOn(button);
                        YoYo.with(Techniques.FadeOutUp).playOn(buttons_layout);
                        YoYo.with(Techniques.FadeOutLeft).playOn(comp_info);

                        if(path != null) {
                            videoLayout.setVisibility(View.VISIBLE);
                            mVideoView.setVisibility(View.VISIBLE);
                            anchor.setVisibility(View.VISIBLE);
                            mVideoView.setVideoPath(path);
                            mVideoView.setMediaController(videoMediaController);
                            no_video.setVisibility(View.INVISIBLE);
                        }
                        else {
                            getVideoLink getVideoLink = new PicsViewHolder.getVideoLink(id);
                            getVideoLink.execute();
                        }

                        videoMediaController.setAnchorView(anchor);
                        mVideoView.setOnPreparedListener(mp -> {
                            mVideoView.requestFocus();
                            mVideoView.start();
                        });

                        mVideoView.setOnCompletionListener(MediaPlayer::start);
                    }
                    else {*/
                   videoLayout.setVisibility(View.GONE);
                   mVideoView.setVisibility(View.GONE);
                   anchor.setVisibility(View.GONE);
                   photoView.setOnLongClickListener(v -> {
                       if (isClicked) {
                           YoYo.with(Techniques.FadeOutRight).playOn(button);
                           YoYo.with(Techniques.FadeOutUp).playOn(buttons_layout);
                           YoYo.with(Techniques.FadeOutLeft).playOn(comp_info);
                       }
                       else {
                           YoYo.with(Techniques.FadeInRight).playOn(button);
                           YoYo.with(Techniques.FadeInDown).playOn(buttons_layout);
                           YoYo.with(Techniques.FadeInLeft).playOn(comp_info);
                       }
                       isClicked = !isClicked;
                       return true;
                   });
                }

                @Override
                public void onPageScrollStateChanged(int state) {}

            });
        }
    }
}
