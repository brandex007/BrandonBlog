package com.example.brandonblog.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.Repository.BlogsDataSource;
import com.example.brandonblog.Repository.BlogsDataSourceFactory;
import com.example.brandonblog.Repository.Repository;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BlogsViewModel extends AndroidViewModel {
    private Repository repository;

    private LiveData<BlogsDataSource> blogsDataSourceLiveData;
    public LiveData<PagedList<Blog>> blogs;

    private Executor executor;


    public BlogsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance();


    }

    public void initPagedList(String token) {
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


    public void addBlog(String title, String body, File image) {
        repository.addBlog(title, body, image);
    }


    public MediatorLiveData<String> editBlog(String title, String body, File image, String slug) {
        return repository.editBlog(title, body, image, slug);
    }

    public LiveData<PagedList<Blog>> getBlogs() {
        return blogs;
    }
}
