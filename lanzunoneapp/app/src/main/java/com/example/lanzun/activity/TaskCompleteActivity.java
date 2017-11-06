package com.example.lanzun.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.example.lanzun.R;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.bean.JsonTaskComplete;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.Utils;
import com.example.lanzun.tools.WebServiceUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 任务完成页面
 */

public class TaskCompleteActivity extends ActivityBase {
    @BindView(R.id.layout_back)
    RelativeLayout layoutBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_right)
    TextView txtRight;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edittxt)
    EditText edittxt;
    @BindView(R.id.tupiannum)
    TextView tupiannum;
    @BindView(R.id.tupian)
    RelativeLayout tupian;
    @BindView(R.id.btn_complete)
    CircularProgressButton btn_complete;//
    private QMUITipDialog qmuiTipDialog;
    private  List<LocalMedia> imgselectList=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_taskcomplete;
    }

    @Override
    protected void initdata() {
        ButterKnife.bind(this);
        txtTitle.setText("完成任务");
        layoutBack.setVisibility(View.VISIBLE);
    }
    @OnClick({R.id.layout_back, R.id.tupian,R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tupian:
                PictureSelector.create(TaskCompleteActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(9)//选择图片数
                        .imageSpanCount(4)
                        .minSelectNum(0)
                        .compressGrade(Luban.THIRD_GEAR)//压缩档次
                        .compress(false)//是否压缩
                        .isGif(true)
                        // .selectionMedia(imgselectList)// 是否传入已选图片
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .forResult(PictureConfig.CHOOSE_REQUEST);

                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_complete://完成按钮
                //taskId 任务id,
                // wcxx 完成信息，
                // wctx 完成图像，
                // wcrq 完成日期
                btn_complete.setIndeterminateProgressMode(true);
                if (btn_complete.getProgress() == 0) {
                    btn_complete.setProgress(50);
                } else if (btn_complete.getProgress() == 100) {
                    btn_complete.setProgress(0);
                } else {
                    btn_complete.setProgress(100);
                }
                if (edittxt.getText().toString().equals("")){
                    btn_complete.setProgress(0);
                    Utils.setdialog(qmuiTipDialog,TaskCompleteActivity.this,"请填写完成信息");
                }else {
                    showProgressDialog(TaskCompleteActivity.this);
                    JsonTaskComplete jsonTaskComplete = new JsonTaskComplete();
                    String time = Utils.stampToDate(System.currentTimeMillis() + "");
                    String taskId = getIntent().getStringExtra("taskId");
                    jsonTaskComplete.setWcrq(time);
                    jsonTaskComplete.setWcxx(edittxt.getText().toString());//完成信息
                    jsonTaskComplete.setTaskId(taskId);//任务id
                    List<String> plist=new ArrayList<>();
                    for (int i = 0; i < imgselectList.size(); i++) {
                        Bitmap bitmap = Utils.compressBitmap(imgselectList.get(i).getPath(), 600, 1024, 30);
                        plist.add(Utils.bitmapToBase64(bitmap));
                    }
                    jsonTaskComplete.setWctx(plist);
                    Gson gson=new Gson();
                    Map<String, String> map = new HashMap<>();
                    map.put("jsonText", gson.toJson(jsonTaskComplete));
                    WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                            "TaskComplete", map, new WebServiceUtils.WebServiceCallBack() {
                                @Override
                                public void callBack(SoapObject result) {
                                    if (result!=null){
                                        btn_complete.setProgress(100);
                                        btn_complete.setProgress(0);
                                        hideProgressDialog();
                                        if (result.getProperty(0).toString().equals("true")){
                                            edittxt.setText("");
                                            imgselectList.clear();
                                            tupiannum.setText("" + imgselectList.size() + "张图片");
                                            finish();
                                            TaskDetailActivity.activity.finish();
                                        }
                                    }
                                }
                            });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (PictureSelector.obtainMultipleResult(data).size()==0){
                imgselectList.clear();
            }
            imgselectList = PictureSelector.obtainMultipleResult(data);
            tupiannum.setText(""+imgselectList.size()+"张图片");
        }
    }
}
