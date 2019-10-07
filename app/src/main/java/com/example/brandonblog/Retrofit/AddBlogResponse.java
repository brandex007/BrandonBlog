package com.example.brandonblog.Retrofit;

import com.google.gson.annotations.SerializedName;

import okhttp3.RequestBody;

public class AddBlogResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("title")
    String title;

    @SerializedName("body")
    String body;

    @SerializedName("image")
    RequestBody image;

}
