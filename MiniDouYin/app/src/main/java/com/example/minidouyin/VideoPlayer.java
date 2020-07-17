package com.example.minidouyin;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.minidouyin.tool.FullScreenVideoView;
import com.example.minidouyin.tool.OnDoubleClickListener;

public class VideoPlayer extends AppCompatActivity {

    private boolean isPlay = false;
    private long time = 0;
    private FullScreenVideoView videoView;
    private TextView tvAuthor;
    private TextView tvInfo;
    private ImageView avatar;
    private LottieAnimationView like;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        videoView = findViewById(R.id.video_view);
        tvAuthor = findViewById(R.id.player_tv_author);
        tvInfo = findViewById(R.id.player_tv_info);
        avatar = findViewById(R.id.player_iv_avatar);
        like = findViewById(R.id.like);
        videoView.setVideoPath(getIntent().getStringExtra("video_path"));
        tvAuthor.setText("@" + getIntent().getStringExtra("author"));
        tvInfo.setText(getIntent().getStringExtra("info"));
        avatar.setImageResource(getIntent().getIntExtra("avatar", 0));
        like.setVisibility(View.GONE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                isPlay = true;
            }
        });
        videoView.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback(){

            @Override
            public void onDoubleClick() {
                Toast.makeText(VideoPlayer.this, "双击", Toast.LENGTH_LONG).show();
                like.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        like.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        }));
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    videoView.pause();
                    isPlay = false;
                } else {
                    videoView.start();
                    isPlay = true;
                }
            }
        });

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacksAndMessages(this);
    }
}
