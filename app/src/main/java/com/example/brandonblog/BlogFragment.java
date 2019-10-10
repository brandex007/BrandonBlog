package com.example.brandonblog;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.brandonblog.Models.Blog;
import com.example.brandonblog.Models.User;
import com.example.brandonblog.ViewModels.BlogsViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class BlogFragment extends Fragment implements PickiTCallbacks {
    private static final int OPEN_DOCUMENT_CODE = 2;
    private Blog blog;
    private EditText title, content;
    private TextView dateUpdated, userText;
    private ImageView blogImage;
    private LoadingDialog loadingDialog;
    private Button editBtn, changeImageBtn;
    private User user;
    private boolean editMode = false;
    private Uri chosenImageUri;
    private String mediaPath;
    private BlogsViewModel blogsViewModel;
    private PickiT pickiT;
    private File file;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        pickiT = new PickiT(getContext(), this);

        blog = getArguments().getParcelable("blog");
        user = getArguments().getParcelable("user");

        title = view.findViewById(R.id.blog_title);
        content = view.findViewById(R.id.blog_content);
        dateUpdated = view.findViewById(R.id.blog_date_updated);
        userText = view.findViewById(R.id.blog_author);
        blogImage = view.findViewById(R.id.image_view);
        editBtn = view.findViewById(R.id.edit_btn);
        changeImageBtn = view.findViewById(R.id.change_image_btn);

        setEditVisible(false);

        title.setText(blog.getTitle());
        content.setText(blog.getBody());


        dateUpdated.setText(blog.getDateUpdatedFormattedMonthDayYear());
        Log.d(TAG, "onViewCreated: " + blog.getDateUpdatedFormattedMonthDayYear());


        userText.setText(blog.getUsername());
        Glide.with(view).load(blog.getImage_url()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(blogImage);

        loadingDialog = (LoadingDialog) getActivity();
        loadingDialog.showDialog(false);

        blogsViewModel = ViewModelProviders.of(this).get(BlogsViewModel.class);

        // check if user is owner of blog
        if (!blog.getUsername().equals(user.getUsername())) {
            editBtn.setVisibility(View.INVISIBLE);
        } else {
            editBtn = view.findViewById(R.id.edit_btn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (!editMode) {
                        setEditVisible(true);
                    } else {
                        // update
                        // Get the Image from data

                        if (file != null) {

                            /*
                            String id = DocumentsContract.getDocumentId(chosenImageUri);
                            InputStream inputStream = null;
                            try {
                                inputStream = getActivity().getContentResolver().openInputStream(chosenImageUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            File file = new File(getActivity().getCacheDir().getAbsolutePath()+"/"+id);
                            writeFile(inputStream, file);
                            String filePath = file.getAbsolutePath();

                             */

                            blogsViewModel.editBlog(title.getText().toString(), content.getText().toString(), file, blog.getSlug()).observe(getActivity(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    showSnackBar(s);
                                }
                            });

                            Glide.get(getContext()).clearMemory();

                            setEditVisible(false);
                        }
                    }
                }
            });
        }
    }

    private void setEditVisible(boolean visible) {
        editMode = visible;

        if (editMode) {
            editBtn.setText("Save");

            title.setEnabled(true);
            title.setBackgroundResource(android.R.drawable.editbox_background);
            content.setEnabled(true);
            content.setBackgroundResource(android.R.drawable.editbox_background);
            content.setPadding(0, 0, 0, 0);
            changeImageBtn.setVisibility(View.VISIBLE);
            changeImageBtn.getBackground().setAlpha(50);

            changeImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, OPEN_DOCUMENT_CODE);
                }
            });
        } else {
            editBtn.setText("Edit");

            title.setEnabled(false);
            title.setBackgroundResource(android.R.color.transparent);
            content.setEnabled(false);
            content.setBackgroundResource(android.R.color.transparent);
            changeImageBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // this is the image selected by the user
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);

                Glide.with(getContext()).load(chosenImageUri).into(blogImage);
            }
        }
    }

    public void showSnackBar(String response) {
        Snackbar.make(getView(), response, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
        Glide.with(view).load(path).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(blogImage);
    }
}
