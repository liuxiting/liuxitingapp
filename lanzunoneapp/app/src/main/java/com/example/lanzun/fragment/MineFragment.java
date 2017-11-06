package com.example.lanzun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lanzun.R;
import com.example.lanzun.activity.LoginActivity;
import com.example.lanzun.base.FragmentBase;
import com.example.lanzun.tools.SharedPreferencedUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/14 0014.
 * 个人中心
 */

public class MineFragment extends FragmentBase {

    Unbinder unbinder;
    @BindView(R.id.outlogin)
    TextView outlogin;
    Unbinder unbinder1;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.showuserid)
    TextView showuserid;
    @BindView(R.id.showusername)
    TextView showusername;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        txt_title.setText("个人中心");
        if (SharedPreferencedUtils.getString(getActivity(),"userid")!=null){
            showuserid.setText(SharedPreferencedUtils.getString(getActivity(),"userid"));
            showusername.setText(SharedPreferencedUtils.getString(getActivity(),"username"));
        }else {
            showuserid.setText("");
        }


    }
    @Override
    protected int getLayoutId() {
        return R.layout.minefragment;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @OnClick(R.id.outlogin)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.outlogin:
                //退出登录
                SharedPreferencedUtils.setString(getActivity(),"userid",null);
                SharedPreferencedUtils.setString(getActivity(),"username",null);
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }
}
