package com.example.lanzun;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import service.AutoService;
import service.LocationServices;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class MyApplication extends MultiDexApplication {
    public LocationServices locationServices;

    private static Context context;
    public  static  Context getContext(){
        return context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Utils.init(this);
        locationServices = new LocationServices(getApplicationContext());

        OkGo.getInstance().init(this);//最简单的配置
    }


    private void initOkGo() {
        //自己写的配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);          //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);         //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);       //全局的连接超时时间
        OkGo.getInstance().init(this)                                                    //必须调用初始化
                .setOkHttpClient(builder.build())                                       //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)                                       //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)                              //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                     //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }

}
