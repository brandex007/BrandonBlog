package com.example.brandonblog.ViewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.Models.User;
import com.example.brandonblog.Repository.Repository;
import com.example.brandonblog.Retrofit.BlogsResponse;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BlogsViewModel extends ViewModel {
    private Repository repository;
    private LiveData<List<Blog>> blogs;

    public BlogsViewModel() {
        this.repository = Repository.getInstance();
    }

    public LiveData<BlogsResponse> getBlogs(){
        return repository.searchBlogs();
    }

    public void addBlog(String title, String body, File image){
        repository.addBlog(title, body, image);
    }

}
