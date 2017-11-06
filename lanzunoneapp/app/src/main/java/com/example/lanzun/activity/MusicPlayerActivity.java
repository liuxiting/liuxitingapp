package com.example.lanzun.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.lanzun.R;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.Player;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/28 0028.
 * //播放在线录音的activity
 */

public class MusicPlayerActivity extends ActivityBase {
    @BindView(R.id.btnPlayUrl)
    TextView btnPlayUrl;
    @BindView(R.id.btnPause)
    TextView btnPause;
    @BindView(R.id.btnStop)
    TextView btnStop;
    @BindView(R.id.skbProgress)
    SeekBar skbProgress;
    private Player player;
   // private String

    @Override
    protected int getLayoutId() {
        return R.layout.activity_musicplayer;
    }

    @Override
    protected void initdata() {
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player=new Player(skbProgress);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnPlayUrl, R.id.btnPause, R.id.btnStop, R.id.skbProgress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnPlayUrl:
                player.playUrl(RBConstants.URL_AUDIO+""+getIntent().getStringExtra("mp3")+"");
                break;
            case R.id.btnPause:
                player.pause();//暂停
                break;
            case R.id.btnStop:
                player.stop();//停止
                break;
            case R.id.skbProgress:
                break;
        }
    }


    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }
}
