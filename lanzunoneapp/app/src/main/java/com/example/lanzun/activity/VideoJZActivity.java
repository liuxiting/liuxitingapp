package com.example.lanzun.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lanzun.R;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.config.RBConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class VideoJZActivity extends ActivityBase {
    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.layout_back)
    RelativeLayout layoutBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_right)
    TextView txtRight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jzvideo;
    }

    @Override
    protected void initdata() {
        layoutBack.setVisibility(View.VISIBLE);
        txtTitle.setText("视频播放");
        videoplayer.setUp(RBConstants.URL_video + "" + getIntent().getStringExtra("mp4")
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.layout_back,R.id.videoplayer})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.layout_back:
                finish();
                break;
            case R.id.videoplayer:

                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }


}
