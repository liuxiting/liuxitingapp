package com.example.lanzun.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.lanzun.R;
import com.example.lanzun.tools.StatusBarCompat;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/5 0005.
 * Activity 基类 刘希亭
 *
 */

public  abstract  class ActivityBase extends AppCompatActivity implements OnPermissionCallback {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);//注解
        //输入法不顶导航栏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarCompat.compat(this);
        }
        initdata();
    }
    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    protected  abstract  void initdata();



    @Override
    public void onPermissionDeclined(String[] permissionName) {

    }

    @Override
    public void onPermissionPreGranted(String permissionsName) {

    }

    @Override
    public void onPermissionNeedExplanation(String permissionName) {

    }

    @Override
    public void onPermissionReallyDeclined(String permissionName) {

    }
    @Override
    public void onPermissionGranted( String[] permissionName) {

    }
    @Override
    public void onNoPermissionNeeded() {

    }
    /**
     * 显示dialog
     * @param context
     */
    Dialog progressDialog = null;
    public  void showProgressDialog(Context context) {

        if (progressDialog == null) {
            progressDialog = new Dialog(context, R.style.progress_dialog);
        }
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText(R.string.loading);
        progressDialog.show();
    }

    /**
     * 隐藏dialog
     */
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}

