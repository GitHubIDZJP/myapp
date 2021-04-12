package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapp.activity.BaseActivity;
import com.example.myapp.activity.HomeActivity;
import com.example.myapp.activity.LoginActivity;
import com.example.myapp.activity.RegisterActivity;
import com.example.myapp.util.StringUtils;

public class MainActivity extends BaseActivity {
    private Button btnLogin;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        btnLogin=findViewById(R.id.btn_login);
        btnRegister=findViewById(R.id.btn_register);
    }

    @Override
    protected void initData() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateto(LoginActivity.class);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in=new Intent(MainActivity.this,RegisterActivity.class);
//                startActivity(in);
                navigateto(RegisterActivity.class);
            }
        });
    }
}