package com.example.brandonblog.Retrofit;

import com.google.gson.annotations.SerializedName;

public class AccountInfoResponse {
    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
