package com.example.brandonblog.ViewModels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.Models.User;
import com.example.brandonblog.Repository.BlogsDataSource;
import com.example.brandonblog.Repository.BlogsDataSourceFactory;
import com.example.brandonblog.Repository.Repository;
import com.example.brandonblog.Retrofit.BlogsResponse;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BlogsViewModel extends AndroidViewModel {
    private Repository repository;

    private LiveData<BlogsDataSource> blogsDataSourceLiveData;
    public LiveData<PagedList<Blog>> blogs;

    private Executor executor;


    public BlogsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance();


    }

    public void initPagedList(String token){
        BlogsDataSourceFactory blogsDataSourceFactory = new BlogsDataSourceFactory(token);
        blogsDataSourceLiveData = blogsDataSourceFactory.mutableLiveData;
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20)
                .setPrefetchDistance(4)
                .build();
        executor = Executors.newFixedThreadPool(5);
        blogs = (new LivePagedListBuilder<>(blogsDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    /*public LiveData<BlogsResponse> getBlogs(){
        return repository.searchBlogs();
    }*/


    public void addBlog(String title, String body, File image){
        repository.addBlog(title, body, image);
    }



    public LiveData<PagedList<Blog>> getBlogs(){
        return blogs;
    }
}
