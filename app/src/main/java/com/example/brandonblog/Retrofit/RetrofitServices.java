package com.example.brandonblog.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitServices {
    @FormUrlEncoded
    @POST("/brandonblog/api/account/login")
    Call<LoginResponse> isValidUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("/brandonblog/api/account/properties")
    Call<AccountInfoResponse> getUser(
            @Header("Authorization") String Token
    );

    @GET("brandonblog/api/blog/list")
    Call<BlogsResponse> getBlogs(
            @Header("Authorization") String Token
    );

    @POST("/brandonblog/api/blog/create")
    Call<AddBlogResponse> addBlog(
            @Header("Authorization") String Token,
            @Body RequestBody file
            );
}
