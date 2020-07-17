package com.example.minidouyin;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    private EditText etID;
    private EditText etName;
    private List<Feed> feeds = new ArrayList<>();
    private List<String> usr = new ArrayList<>();
    private Call<FeedResponse> feedCall = null;
    private String[] mPermissionsArrays = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private final static int REQUEST_PERMISSION = 123;

    public Login() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
        }
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etID = (EditText)view.findViewById(R.id.et_id);
        etName = (EditText)view.findViewById(R.id.et_name);
        final RecyclerView recyclerView = view.findViewById(R.id.home_rv);
        FloatingActionButton loginBtn = view.findViewById(R.id.fab_submit);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uID = etID.getText().toString();
                String uName = etName.getText().toString();
                if (usr.size() == 0) {
                    usr.add(uID);
                    usr.add(uName);
                }
                else {
                    usr.set(0, uID);
                    usr.set(1, uName);
                }

                Retrofit retrofit = RetrofitManager.get("https://api.daliapp.net/android-bootcamp/invoke/");
                Call<FeedResponse> feedResponseCall = retrofit.create(Service.class).getFeeds();
                feedCall = feedResponseCall;
                feedResponseCall.enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                        feeds = response.body().getFeeds();
//                String id = (usr.get(0));
                        String id = "";
                        if (usr.size() != 0) {
                            id = usr.get(0);
                        }
                        String name = "";
                        if (usr.size() > 0) {
                            name = usr.get(1);
                        }

                        Iterator<Feed> it = feeds.iterator();
                        while (it.hasNext()) {
                            Feed v = it.next();
                            if (!v.getId().equals(id) || !v.getUserName().equals(name)) {
                                it.remove();
                            }
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                        feedCall = null;
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        Toast.makeText(container.getContext(), "获取数据失败", Toast.LENGTH_LONG).show();
                        feedCall = null;
                    }
                });
            }
        });


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
                int iconId = R.mipmap.youxizi_pic;

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
