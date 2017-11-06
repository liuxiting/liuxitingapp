package com.example.lanzun.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lanzun.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/6 0006.
 * fragment 基类
 */

public abstract class FragmentBase extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        ButterKnife.bind(this,view);//注解

        return view;
    }
    protected abstract void initView(View view, Bundle savedInstanceState);
    //获取布局文件ID
    protected abstract int getLayoutId();
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
