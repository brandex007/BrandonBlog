package com.example.brandonblog.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Blog implements Parcelable {
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;
    @SerializedName("date_updated")
    private Date date_updated;
    @SerializedName("username")
    private String username;
    @SerializedName("image")
    private String image_url;

    public Blog(String title, String body, Date date_updated, String username, String image_url) {
        this.title = title;
        this.body = body;
        this.date_updated = date_updated;
        this.username = username;
        this.image_url = image_url;
    }


    protected Blog(Parcel in) {
        title = in.readString();
        body = in.readString();
        username = in.readString();
        image_url = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(username);
        parcel.writeString(image_url);
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    public String getDateUpdatedFormattedMonthDayYear(){
        String pattern = "yyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(date_updated);
        return date;
    }

    public static final DiffUtil.ItemCallback<Blog> BLOG_COMPARATOR = new DiffUtil.ItemCallback<Blog>() {
        @Override
        public boolean areItemsTheSame(@NonNull Blog oldItem, @NonNull Blog newItem) {
            return oldItem.title.equals(newItem.title);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Blog oldItem, @NonNull Blog newItem) {
            return true;
        }
    };
}
