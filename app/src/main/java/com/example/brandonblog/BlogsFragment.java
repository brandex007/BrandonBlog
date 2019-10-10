package com.example.brandonblog;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.Models.User;
import com.example.brandonblog.RecyclerViews.BlogsRecyclerViewAdapter;
import com.example.brandonblog.ViewModels.AccountViewModel;
import com.example.brandonblog.ViewModels.BlogsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class BlogsFragment extends Fragment implements BlogsRecyclerViewAdapter.Listener, PickiTCallbacks {
    private static final int OPEN_DOCUMENT_CODE = 2;
    private User user;
    private TextView blogText;
    private AccountViewModel accountViewModel;
    private BlogsViewModel blogsViewModel;
    private ArrayList<Blog> blogList;
    private FloatingActionButton fab;
    private View view1;
    private Uri chosenImageUri;
    private String mediaPath;
    private NavController navController;
    private PickiT pickiT;
    private File file;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blogs_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pickiT = new PickiT(getContext(), this);
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();

                view1 = inflater.inflate(R.layout.add_blog, null);
                builder.setView(view1);

                final AlertDialog dialog = builder.create();
                Button button = view1.findViewById(R.id.submit_blog_btn);

                final TextView title = view1.findViewById(R.id.title);
                final TextView content = view1.findViewById(R.id.body);
                view1.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, OPEN_DOCUMENT_CODE);
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Get the Image from data

                        /*
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        assert cursor != null;
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        mediaPath = cursor.getString(columnIndex);

                        File file = new File(mediaPath);*/

                        blogsViewModel.addBlog(title.getText().toString(), content.getText().toString(), file);
                        dialog.hide();

                        showSnackBar();
                    }
                });

                dialog.show();
            }
        });

        user = getArguments().getParcelable("user");

        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final BlogsRecyclerViewAdapter adapter = new BlogsRecyclerViewAdapter(getActivity());
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        blogList = new ArrayList<>();

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user1) {
                user = user1;
            }
        });

        blogsViewModel = ViewModelProviders.of(this).get(BlogsViewModel.class);
        blogsViewModel.initPagedList(user.getToken());
        blogsViewModel.getBlogs().observe(this, new Observer<PagedList<Blog>>() {
            @Override
            public void onChanged(PagedList<Blog> blogs) {
                Log.d(TAG, "onChanged: " + blogs.toString());
                adapter.submitList(blogs);
            }
        });
    }

    public void showSnackBar() {
        Snackbar.make(getView(), "Blog post added", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // this is the image selected by the user
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
            }
        }
    }

    @Override
    public void onClick(Blog blog) {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        final Bundle bundle = new Bundle();
        bundle.putParcelable("blog", blog);
        bundle.putParcelable("user", user);
        navController.navigate(R.id.action_blogsFragment_to_blogFragment, bundle);
    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        file = new File(path);

        view1.findViewById(R.id.add_btn).setVisibility(View.INVISIBLE);
        ImageView imageView = view1.findViewById(R.id.image_view);
        Glide.with(getContext()).load(file.getPath()).into(imageView);
    }
}
