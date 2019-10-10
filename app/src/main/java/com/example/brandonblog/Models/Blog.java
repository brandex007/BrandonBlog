package com.example.brandonblog.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Blog implements Parcelable {
    @SerializedName("pk")
    private int primaryKey;

    @SerializedName("slug")
    private String slug;

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

    protected Blog(Parcel in) {
        primaryKey = in.readInt();
        slug = in.readString();
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

    @Override
    public String toString() {
        return "Blog{" +
                "primaryKey=" + primaryKey +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", date_updated=" + date_updated +
                ", username='" + username + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }

    public Blog() {

    }

    public Blog(int primaryKey, String slug, String title, String body, Date date_updated, String username, String image_url) {
        this.primaryKey = primaryKey;
        this.slug = slug;
        this.title = title;
        this.body = body;
        this.date_updated = date_updated;
        this.username = username;
        this.image_url = image_url;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

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

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
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

    public String getDateUpdatedFormattedMonthDayYear() {
        String pattern = "yyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(date_updated);
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(primaryKey);
        dest.writeString(slug);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(username);
        dest.writeString(image_url);
    }

}
