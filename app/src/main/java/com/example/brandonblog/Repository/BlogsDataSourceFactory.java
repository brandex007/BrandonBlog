package com.example.brandonblog.Repository;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.brandonblog.Models.Blog;

public class BlogsDataSourceFactory extends DataSource.Factory<Long, Blog> {
    public MutableLiveData<BlogsDataSource> mutableLiveData = new MutableLiveData<>();
    private String token;

    public BlogsDataSourceFactory(String token) {
        this.token = token;
    }

    @Override
    public DataSource<Long, Blog> create() {
        BlogsDataSource blogsDataSource = new BlogsDataSource(token);
        mutableLiveData.postValue(blogsDataSource);
        return blogsDataSource;
    }
}
