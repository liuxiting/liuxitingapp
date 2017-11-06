package com.example.lanzun.tools;

import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.example.lanzun.Interface.OkGoUtilsInterFace;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.io.Flushable;
import java.security.acl.LastOwnerException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6 0006.
 *刘希亭 okgo网络请求框架
 */

public class OkgoUtils {
    /**
     * okgo 的GET请求
     *
     * @param requestCode        标识码  用于标识具体的网络请求
     * @param url                请求的url
     * @param param              请求的参数
     * @param tag                tag用于标识和取消网络请求
     * @param okGoUtilsInterFace 接口
     */
    public static void GET(final int requestCode, String url, Map<String, String> param, Object tag, final OkGoUtilsInterFace okGoUtilsInterFace) {
        if (NetworkUtils.isConnected()) {
            OkGo.<String>get(url)
                    .tag(tag)
                    .params(param)
                    .headers("Content-Type", "application/json; charset=utf-8")
                    .execute(new StringCallback() {
                        @Override
                        public void onStart(Request<String, ? extends Request> request) {
                            super.onStart(request);
                            okGoUtilsInterFace.onStart(request);
                        }
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.i("执行了","执行了");
                            okGoUtilsInterFace.onSuccess(response, requestCode);
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            okGoUtilsInterFace.Error(response);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            okGoUtilsInterFace.onFinsh();
                        }
                    });

        } else {
            okGoUtilsInterFace.onNetWorkError();
        }
    }
    /*
    * 多个文件对一个一个key，同时上传
    * */
    public static void fileonemorepost(final int requestCode, String url, List<File> files,Object tag,final  OkGoUtilsInterFace okgoUtilsInterFace){
         OkGo.<String>post(url)
                 .tag(tag)
                 .addFileParams("files",files)
                 .execute(new StringCallback() {
                     @Override
                     public void onSuccess(Response<String> response) {
                         okgoUtilsInterFace.onSuccess(response,requestCode);
                     }

                     @Override
                     public void onError(Response<String> response) {
                         super.onError(response);
                         okgoUtilsInterFace.Error(response);
                     }

                     @Override
                     public void onFinish() {
                         super.onFinish();
                         okgoUtilsInterFace.onFinsh();
                     }

                 });

    }
    /*
    * 上传文件多个文件，一个文件对于一个key方法
    * */
    public static void filepost(final int requestCode, String url, List<File> files, Object tag, final OkGoUtilsInterFace okGoUtilsInterFace){
        if (NetworkUtils.isConnected()){
       PostRequest<String> request= OkGo.<String>post(url)
                    .tag(tag);
                    for(int i=0;i<files.size();i++){
                        request.params("file_"+i,files.get(i));
                    }
                    request.execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            okGoUtilsInterFace.onSuccess(response, requestCode);
                        }

                        @Override
                        public void uploadProgress(Progress progress) {
                            //进度
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            okGoUtilsInterFace.Error(response);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            okGoUtilsInterFace.onFinsh();
                        }
                    });

        }
    }

    /**
     * okgo POST
     *
     * @param requestCode        标识码  用于标识具体的网络请求
     * @param url                请求的url
     * @param param              请求的参数
     * @param tag                tag用于标识和取消网络请求
     * @param okGoUtilsInterFace 接口
     */
    public static void POST(final int requestCode, String url, Map<String, String> param, Object tag, final OkGoUtilsInterFace okGoUtilsInterFace) {
        if (NetworkUtils.isConnected()) {
            OkGo.<String>post(url)
                    .tag(tag)
                    .params(param)
                    .execute(new StringCallback() {
                        @Override
                        public void onStart(Request<String, ? extends Request> request) {
                            super.onStart(request);
                            okGoUtilsInterFace.onStart(request);
                        }

                        @Override
                        public void onSuccess(Response<String> response) {
                            okGoUtilsInterFace.onSuccess(response, requestCode);
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            okGoUtilsInterFace.Error(response);
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            okGoUtilsInterFace.onFinsh();
                        }
                    });

        } else {
            okGoUtilsInterFace.onNetWorkError();
        }
    }

}
