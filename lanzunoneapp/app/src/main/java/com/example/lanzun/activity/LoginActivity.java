package com.example.lanzun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.lanzun.MainActivity;
import com.example.lanzun.R;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.bean.Loginbean;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.MD5;
import com.example.lanzun.tools.SharedPreferencedUtils;
import com.example.lanzun.tools.Utils;
import com.example.lanzun.tools.WebServiceUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class LoginActivity extends ActivityBase {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btn_login)
    CircularProgressButton btnLogin;

    private QMUITipDialog qmuiTipDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initdata() {
        ButterKnife.bind(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                btnLogin.setIndeterminateProgressMode(true);
                if (btnLogin.getProgress() == 0) {
                    btnLogin.setProgress(50);
                } else if (btnLogin.getProgress() == 100) {
                    btnLogin.setProgress(0);
                } else {
                    btnLogin.setProgress(100);
                }
                if (Utils.isNetworkAvailable(LoginActivity.this)){
                    if (name.getText().toString().equals("")){
                        btnLogin.setProgress(0);
                        Utils.setdialog(qmuiTipDialog,this,"账号不能为空");

                    }else if (password.getText().toString().equals("")){
                        Utils.setdialog(qmuiTipDialog,this,"密码不能为空");
                        btnLogin.setProgress(0);
                    }else {
                        showProgressDialog(LoginActivity.this);
                        Loginbean loginbean = new Loginbean();
                        loginbean.setPassword("" + password.getText());
                        loginbean.setUserId("" + name.getText());
                        Gson gson = new Gson();
                        // Log.i("登录加密",MD5.getMD5(password.getText().toString())+"");
                        Log.i("登录", gson.toJson(loginbean));
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("jsonText", gson.toJson(loginbean));
                        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                                "Login", map, new WebServiceUtils.WebServiceCallBack() {
                                    @Override
                                    public void callBack(SoapObject result) {
                                        Log.i("登录请求数据", result + "");
                                        btnLogin.setProgress(0);
                                        if (result != null) {
                                            if (result.getProperty(0).toString().equals("anyType{}")|result.getProperty(0).toString().equals("false")) {
                                                hideProgressDialog();
                                                Utils.setdialog(qmuiTipDialog,LoginActivity.this,"登录失败，请重新登录");
                                            } else {
                                                hideProgressDialog();
                                                SharedPreferencedUtils.setString(LoginActivity.this, "userid", name.getText() + "");
                                                SharedPreferencedUtils.setString(LoginActivity.this, "username", result.getProperty(0).toString() + "");
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        } else {
                                            hideProgressDialog();
                                            Utils.setdialog(qmuiTipDialog,LoginActivity.this,"登录失败，请重新登录");
                                        }

                                    }
                                });

                    }
                }else {
                   btnLogin.setProgress(0);
                    Utils.setdialog(qmuiTipDialog,LoginActivity.this,"暂无网络");
                }
                break;
        }
    }

}
