package com.example.lanzun.activity;

import android.content.Intent;
import android.os.Handler;

import com.example.lanzun.MainActivity;
import com.example.lanzun.R;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.tools.SharedPreferencedUtils;

import service.AutoService;

/**
 * Created by Administrator on 2017/9/19 0019.'
 * 启动页，刘习亭
 */

public class LaunchActivity extends ActivityBase {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initdata() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencedUtils.getString(LaunchActivity.this,"userid")==null){
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));

                }else {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                }
                finish();
            }
        },2000);
    }
}
