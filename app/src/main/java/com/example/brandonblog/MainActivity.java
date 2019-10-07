package com.example.brandonblog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoadingDialog{
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.hide();
    }

    public void showDialog(boolean show){
        if(show){
            dialog.show();
        }else{
            dialog.hide();
        }
    }
}
