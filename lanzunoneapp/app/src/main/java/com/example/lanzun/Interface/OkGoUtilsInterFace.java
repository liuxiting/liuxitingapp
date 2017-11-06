package com.example.lanzun.Interface;


import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * Created by Administrator on 2017/9/6 0006.
 * okgo接口请求
 */

public interface OkGoUtilsInterFace {
    void onStart(Request request);
    void onSuccess(Response<String> response,int requestCode);
    void onFinsh();
    void Error(Response<String> response);
    void onNetWorkError();
}
