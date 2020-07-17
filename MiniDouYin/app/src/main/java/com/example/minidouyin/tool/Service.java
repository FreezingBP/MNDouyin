package com.example.minidouyin.tool;

import com.example.minidouyin.bean.FeedResponse;
import com.example.minidouyin.bean.PostVideoResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @Multipart
    @POST("video")
    Call<PostVideoResponse> post(@Query("student_id") String studentId,
                                 @Query("user_name") String userName,
                                 @Part MultipartBody.Part coverImage,
                                 @Part MultipartBody.Part video);

    @GET("video") Call<FeedResponse> getFeeds();
}
