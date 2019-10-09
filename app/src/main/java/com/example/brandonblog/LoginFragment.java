package com.example.brandonblog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.brandonblog.Models.User;
import com.example.brandonblog.Retrofit.RetrofitClientInstance;
import com.example.brandonblog.Retrofit.RetrofitServices;
import com.example.brandonblog.ViewModels.AccountViewModel;

import retrofit2.Retrofit;

public class LoginFragment extends Fragment {
    private NavController navController;
    private AccountViewModel accountViewModel;

    private Button loginBtn;
    private EditText usernameText, passwordText;
    private Retrofit retrofit;
    private RetrofitServices retrofitServices;

    private static final String TAG = "MainActivity";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setVisibility(View.INVISIBLE);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);


        loginBtn = view.findViewById(R.id.login_btn);
        usernameText = view.findViewById(R.id.username);
        passwordText = view.findViewById(R.id.password);

        retrofit = RetrofitClientInstance.getRetrofit();
        retrofitServices = retrofit.create(RetrofitServices.class);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        String email = sharedPreferences.getString("email", "none");
        final String password = sharedPreferences.getString("password", "none");

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        loadingDialog = (LoadingDialog) getActivity();
        loadingDialog.showDialog(true);

        if(!email.equals("none")) {
            loginUser(email, password, false);
        }else {
            loadingDialog.showDialog(false);
            view.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginUser(usernameText.getText().toString(), passwordText.getText().toString(), true);
                }
            });
        }
    }

    public void loginUser(String email, String password, final boolean newUser){
        accountViewModel.loginUser(email, password);
        accountViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "onChanged: " + user.getEmail());

                loadingDialog.showDialog(false);

                if(newUser) {
                    editor = sharedPreferences.edit();
                    editor.putString("email", user.getEmail());
                    editor.putString("password", passwordText.getText().toString());
                    editor.apply();
                }

                final Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);

                navController.navigate(R.id.action_loginFragment_to_blogsFragment, bundle);
            }
        });
    }

}
