package com.example.brandonblog.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.brandonblog.Models.User;
import com.example.brandonblog.Repository.Repository;

public class AccountViewModel extends ViewModel {
    Repository repository;
    public AccountViewModel(){
        repository = Repository.getInstance();
    }

    public LiveData<User> getUser(){
        return repository.getUser();
    }

    public void loginUser(String username, String password){
        repository.loginUser(username,password);
    }
}
