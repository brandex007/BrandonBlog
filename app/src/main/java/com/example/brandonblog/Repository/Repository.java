package com.example.brandonblog.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.Models.User;
import com.example.brandonblog.Retrofit.AccountInfoResponse;
import com.example.brandonblog.Retrofit.AddBlogResponse;
import com.example.brandonblog.Retrofit.BlogsResponse;
import com.example.brandonblog.Retrofit.LoginResponse;
import com.example.brandonblog.Retrofit.RetrofitClientInstance;
import com.example.brandonblog.Retrofit.RetrofitServices;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class Repository {
    private static Repository instance;
    private Repository(){};
    private static Retrofit retrofit;
    private static RetrofitServices retrofitServices;
    //private User user;
    private static MediatorLiveData<User> user;
    private static LiveData<List<Blog>> blogs;

    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
            retrofit = RetrofitClientInstance.getRetrofit();
            retrofitServices = retrofit.create(RetrofitServices.class);

            user = new MediatorLiveData<>();
        }

        return instance;
    }

    /*
    public MutableLiveData<BlogsResponse> searchBlogs(){
        final MutableLiveData<BlogsResponse> blogsResponse = new MutableLiveData<>();

        Call<BlogsResponse> call = retrofitServices.getBlogs("Token " + user.getValue().getToken());
        call.enqueue(new Callback<BlogsResponse>() {
            @Override
            public void onResponse(Call<BlogsResponse> call, Response<BlogsResponse> response) {
                if(response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().getResults().toString());
                    blogsResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BlogsResponse> call, Throwable t) {

            }
        });

        return blogsResponse;
    }*/


    public LiveData<User> getUser(){
        return user;
    }

    public void loginUser(String username, String password){
        Call<LoginResponse> call = retrofitServices.isValidUser(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.body() != null){
                    Log.d("login", "onResponse: " + response.body().getToken());
                    final String token = response.body().getToken();

                    Call<AccountInfoResponse> call2 = retrofitServices.getUser("Token " + token);
                    call2.enqueue(new Callback<AccountInfoResponse>() {
                        @Override
                        public void onResponse(Call<AccountInfoResponse> call, Response<AccountInfoResponse> response) {
                            if(response.body() != null){
                                user.setValue(new User(token, response.body().getEmail(), response.body().getUsername()));
                                Log.d(TAG, "onResponse: " + response.body().getEmail());
                            }

                        }

                        @Override
                        public void onFailure(Call<AccountInfoResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: error");
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void addBlog(String title, String body, File file){

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("title", title)
                .addFormDataPart("body", body)
                .addFormDataPart("image", file.getName(), RequestBody.create(MultipartBody.FORM, file));

        final RequestBody requestBody = builder.build();

        Call<AddBlogResponse> call = retrofitServices.addBlog("Token " + user.getValue().getToken(),requestBody);
        call.enqueue(new Callback<AddBlogResponse>() {
            @Override
            public void onResponse(Call<AddBlogResponse> call, Response<AddBlogResponse> response) {
                if(response.body() != null){
                    Log.d(TAG, "onResponse: added blog");
                }

                Log.d(TAG, "onResponse: " + response.raw());
            }

            @Override
            public void onFailure(Call<AddBlogResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: failure");
                t.printStackTrace();
            }
        });
    }

}
