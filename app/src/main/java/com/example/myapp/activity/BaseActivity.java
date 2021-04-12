package com.example.myapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.MainActivity;

public  abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(initLayout());//传入layout布局
        initView();//传入view控件
        initData();
    }
    protected abstract int  initLayout();

    protected   abstract void initView();
    protected   abstract void initData();

    public void showToast(String msg){

        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    public void showToastSync(String msg){
        Looper.prepare();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateto(Class cls){
        Intent in=new Intent(mContext, cls);
        startActivity(in);

    }

    public void navigateToWithFlag(Class cls, int flags) {
        Intent in = new Intent(mContext, cls);
        in.setFlags(flags);
        startActivity(in);
    }
    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("sp_ttit", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

}