package com.example.brandonblog.Retrofit;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private int status;

    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }
}
