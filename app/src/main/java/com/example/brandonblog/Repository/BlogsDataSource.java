package com.example.brandonblog.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.Retrofit.BlogsResponse;
import com.example.brandonblog.Retrofit.RetrofitClientInstance;
import com.example.brandonblog.Retrofit.RetrofitServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class BlogsDataSource extends PageKeyedDataSource<Long, Blog> {
    private RetrofitServices retrofitServices;
    private String token;
    private Retrofit retrofit;

    public BlogsDataSource(String token) {
        this.token = token;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Blog> callback) {

        retrofit = RetrofitClientInstance.getRetrofit();
        retrofitServices = retrofit.create(RetrofitServices.class);

        Call<BlogsResponse> call = retrofitServices.getBlogs("Token " + token, (long) 1);
        call.enqueue(new Callback<BlogsResponse>() {
            @Override
            public void onResponse(Call<BlogsResponse> call, Response<BlogsResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse1: " + response.body().getResults().get(0).getTitle());
                    List<Blog> blogs = response.body().getResults();
                    callback.onResult(blogs, null, (long) 2);
                }
            }

            @Override
            public void onFailure(Call<BlogsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Blog> callback) {
        Call<BlogsResponse> call = retrofitServices.getBlogs("Token " + token, params.key);
        call.enqueue(new Callback<BlogsResponse>() {
            @Override
            public void onResponse(Call<BlogsResponse> call, Response<BlogsResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getResults().toString());
                    List<Blog> blogs = response.body().getResults();

                    long key;
                    if (params.key > 1) {
                        key = params.key - 1;
                    } else {
                        key = 0;
                    }

                    callback.onResult(blogs, key);
                }
            }

            @Override
            public void onFailure(Call<BlogsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Blog> callback) {
        Call<BlogsResponse> call = retrofitServices.getBlogs("Token " + token, params.key);
        call.enqueue(new Callback<BlogsResponse>() {
            @Override
            public void onResponse(Call<BlogsResponse> call, Response<BlogsResponse> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getResults().toString());
                    List<Blog> blogs = response.body().getResults();
                    callback.onResult(blogs, params.key + 1);
                }
            }

            @Override
            public void onFailure(Call<BlogsResponse> call, Throwable t) {

            }
        });
    }
}
