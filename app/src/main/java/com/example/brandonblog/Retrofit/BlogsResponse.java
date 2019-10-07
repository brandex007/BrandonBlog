package com.example.brandonblog.Retrofit;

import com.example.brandonblog.Models.Blog;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlogsResponse {
    @SerializedName("results")
    List<Blog> results;

    public List<Blog> getResults() {
        return results;
    }
}
