package com.example.brandonblog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.brandonblog.Models.Blog;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BlogFragment extends Fragment {
    private Blog blog;
    private TextView title, content, dateUpdated, user;
    private ImageView blogImage;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blog = getArguments().getParcelable("blog");

        title = view.findViewById(R.id.blog_title);
        content = view.findViewById(R.id.blog_content);
        dateUpdated = view.findViewById(R.id.blog_date_updated);
        user = view.findViewById(R.id.blog_author);
        blogImage = view.findViewById(R.id.blog_image);

        title.setText(blog.getTitle());
        content.setText(blog.getBody());


        dateUpdated.setText(blog.getDateUpdatedFormattedMonthDayYear());
        Log.d(TAG, "onViewCreated: " + blog.getDateUpdatedFormattedMonthDayYear());


        user.setText(blog.getUsername());
        Glide.with(view).load(blog.getImage_url()).into(blogImage);

        loadingDialog = (LoadingDialog) getActivity();
        loadingDialog.showDialog(false);
    }
}
