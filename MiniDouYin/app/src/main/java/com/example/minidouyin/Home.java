package com.example.minidouyin;


import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.minidouyin.bean.Feed;
import com.example.minidouyin.bean.FeedResponse;
import com.example.minidouyin.tool.RetrofitManager;
import com.example.minidouyin.tool.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;



public class Home extends Fragment {

    private List<Feed> feeds = new ArrayList<>();
    private Call<FeedResponse> feedCall = null;
    private String[] mPermissionsArrays = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private final static int REQUEST_PERMISSION = 123;

    public Home() {

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
        }
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.home_rv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = inflater.inflate(R.layout.video_info, container, false);
                ImageView iv = view.findViewById(R.id.iv_video_cover);
                //iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iv.setAdjustViewBounds(true);
                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                View view = viewHolder.itemView;
                ImageView iv = view.findViewById(R.id.iv_video_cover);
                String url = feeds.get(i).getImageUrl();
                Glide.with(iv.getContext()).load(url).into(iv);
                ImageView iv_avatar = view.findViewById(R.id.home_iv_avatar);

                int iconId = R.drawable.icon_girl;
                iv_avatar.setImageResource(iconId);
                TextView tv = view.findViewById(R.id.tv_author);
                tv.setText(feeds.get(i).getUserName());
                TextView tvDate = view.findViewById(R.id.tv_date);
                tvDate.setText(feeds.get(i).getCreateAt());
                final int finalIconId = iconId;
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(container.getContext(), VideoPlayer.class);
                        intent.putExtra("video_path", feeds.get(i).getVideoUrl());
                        intent.putExtra("author", feeds.get(i).getUserName());
                        intent.putExtra("info", feeds.get(i).getCreateAt());
                        intent.putExtra("avatar", finalIconId);
                        startActivity(intent);
                    }
                });
            }

            @Override public int getItemCount() {
                return feeds.size();
            }
        });

        Retrofit retrofit = RetrofitManager.get("https://api.daliapp.net/android-bootcamp/invoke/");
        Call<FeedResponse> feedResponseCall = retrofit.create(Service.class).getFeeds();
        feedCall = feedResponseCall;
        feedResponseCall.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                feeds = response.body().getFeeds();
                recyclerView.getAdapter().notifyDataSetChanged();
                feedCall = null;
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                Toast.makeText(container.getContext(), "获取数据失败", Toast.LENGTH_LONG).show();
                feedCall = null;
            }
        });
        return view;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //destroy the call when destroy
        if(feedCall != null){
            feedCall.cancel();
        }
    }
}
