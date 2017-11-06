package com.example.lanzun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lanzun.R;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.bean.TaskDetailean;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.Utils;
import com.example.lanzun.tools.WebServiceUtils;
import com.example.lanzun.view.MyGridView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/20 0020.
 * 任务详情页面
 */

public class TaskDetailActivity extends ActivityBase {
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
    @BindView(R.id.bottom)
    LinearLayout bottom;//底部按钮布局，显示隐藏，根据任务状态
    @BindView(R.id.taskname)
    TextView taskname;
    @BindView(R.id.yinhan)
    TextView yinhan;
    @BindView(R.id.miaoshu)
    TextView miaoshu;
    @BindView(R.id.leixing)
    TextView leixing;
    @BindView(R.id.paifa)
    TextView paifa;
    @BindView(R.id.yijian)
    TextView yijian;
    @BindView(R.id.chulitime)
    TextView chulitime;
    @BindView(R.id.xuncha)
    TextView xuncha;
    @BindView(R.id.xiafatime)
    TextView xiafatime;
    private String taskId;//任务id
    @BindView(R.id.toComplete)
    TextView toComplete;//去完成按钮
    @BindView(R.id.imggrid)
    MyGridView imggrid;//图片
    @BindView(R.id.txttx)
    TextView txttx;//
    @BindView(R.id.wanchengtime)
    TextView wanchengtime;
    @BindView(R.id.jieshoutime)
    TextView jieshoutime;
    @BindView(R.id.lxuncha)
    LinearLayout lxuncha;//巡查人区域
    @BindView(R.id.ljieshou)
    LinearLayout ljieshou;//接收时间区域
    @BindView(R.id.lwancheng)
    LinearLayout lwancheng;//完成时间区域
    @BindView(R.id.tasktype)
    TextView tasktype;//任务类型
    public  static Activity activity;
    @BindView(R.id.lwctx)
    LinearLayout lwctx;//完成图像区域默认隐藏如果是
    @BindView(R.id.txtwc)
    TextView txtwc;//完成图像 无的标记
    @BindView(R.id.imag_wancheng)
    MyGridView imag_wancheng;//完成的图像

    @Override
    protected int getLayoutId() {
        return R.layout.activiy_taskdetail;
    }

    @Override
    protected void initdata() {
        ButterKnife.bind(this);
        txtTitle.setText("任务详情");
        activity=this;
        layoutBack.setVisibility(View.VISIBLE);
        initnetdata();//请求接口
    }

    private void initnetdata() {
        showProgressDialog(TaskDetailActivity.this);
        taskId = getIntent().getStringExtra("taskId");
        Map<String, String> map = new HashMap<>();
        map.put("jsonText", "{\"taskId\":\"" + taskId + "\"}");
        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                "TaskDetail", map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        hideProgressDialog();
                        if (result != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(result.getProperty(0).toString());
                                JSONObject object = jsonArray.getJSONObject(0);
                                Gson gson = new Gson();
                                TaskDetailean taskDetailean = gson.fromJson(object.toString(), TaskDetailean.class);
                                taskname.setText(taskDetailean.getRWMC());//任务名称
                                yinhan.setText(taskDetailean.getDZD());//隐患点
                                miaoshu.setText(taskDetailean.getRWMS());//任务描述
                                leixing.setText(taskDetailean.getRWLX());//任务类型
                                paifa.setText(taskDetailean.getPFR());//派发人
                                xiafatime.setText(taskDetailean.getXFRQ());//下发时间

                                if (taskDetailean.getRWZT().equals("0")){
                                    //未接受
                                    tasktype.setText("未接受");
                                    toComplete.setText("接受任务");
                                }else if (taskDetailean.getRWZT().equals("1")){
                                    //已接受
                                    tasktype.setText("已接受");
                                    toComplete.setText("完成任务");
                                }else if (taskDetailean.getRWZT().equals("2")){
                                    //已完成
                                    tasktype.setText("已完成");
                                    bottom.setVisibility(View.GONE);
                                    lwctx.setVisibility(View.VISIBLE);

                                    if (taskDetailean.getWCTX().size()>0){
                                        imag_wancheng.setAdapter(new Imgadapter(taskDetailean.getWCTX(),2));
                                    }else {
                                        txtwc.setVisibility(View.VISIBLE);
                                    }
                                }else if (taskDetailean.getRWZT().equals("3")){
                                    //已处理
                                    tasktype.setText("已处理");
                                    bottom.setVisibility(View.GONE);
                                    lwctx.setVisibility(View.VISIBLE);
                                    if (taskDetailean.getWCTX().size()>0){
                                        imag_wancheng.setAdapter(new Imgadapter(taskDetailean.getWCTX(),2));
                                    }else {
                                        txtwc.setVisibility(View.VISIBLE);
                                    }
                                }
                                if (taskDetailean.getXCTX().size()>0){
                                    imggrid.setAdapter(new Imgadapter(taskDetailean.getXCTX(),1));
                                }else {
                                    txttx.setVisibility(View.VISIBLE);
                                }

                                if (taskDetailean.getRWZT().equals("3")) {
                                    //已处理
                                    lxuncha.setVisibility(View.VISIBLE);
                                    yijian.setText(taskDetailean.getCLYJ());//处理意见 需要判断状态
                                    chulitime.setText(taskDetailean.getCLRQ());//处理日期 需要判断状态
                                    xuncha.setText(taskDetailean.getXCR());//巡查人  需要判断状态
                                }
                                if (taskDetailean.getRWZT().equals("1")) {
                                    //已经接收
                                    ljieshou.setVisibility(View.VISIBLE);
                                    jieshoutime.setText(taskDetailean.getJSRQ());//接收时间需要判断状态
                                }
                                if (taskDetailean.getRWZT().equals("2")) {
                                    //已经完成任务，但是没有审核
                                    lwancheng.setVisibility(View.VISIBLE);
                                    wanchengtime.setText(taskDetailean.getWCRQ());//完成时间 需要判断状态
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.layout_back, R.id.toComplete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.toComplete://去完成按钮
                if (toComplete.getText().toString().equals("接受任务")){
                    Toast.makeText(TaskDetailActivity.this,"接受任务",Toast.LENGTH_SHORT).show();
                    showProgressDialog(TaskDetailActivity.this);
                    String time=Utils.stampToDate(System.currentTimeMillis() + "");
                    Map<String, String> map = new HashMap<>();
                    map.put("jsonText", "{\"taskId\":\"" + taskId + "\",\"receiveDate\":\""+time+"\"}");
                    WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                            "TaskReceive", map, new WebServiceUtils.WebServiceCallBack() {
                                @Override
                                public void callBack(SoapObject result) {
                                    hideProgressDialog();
                                    if (result!=null){
                                        if (result.getProperty(0).toString().equals("true")){
                                            //接受任务成功，刷新页面
                                            initnetdata();//请求接口
                                        }

                                    }
                                }
                            });
                }else if (toComplete.getText().toString().equals("完成任务")){
                    Intent intent = new Intent(TaskDetailActivity.this, TaskCompleteActivity.class);
                    intent.putExtra("taskId", getIntent().getStringExtra("taskId"));
                    startActivity(intent);
                }else {

                }
                break;
        }
    }


    class Imgadapter extends BaseAdapter {
        List<String> imglist;
        int iswc;//是巡查图像还是完成图像，1巡查图像，2完成图像

        private Imgadapter(List<String> list,int iswc) {
            this.imglist = list;
            this.iswc=iswc;
        }

        @Override
        public int getCount() {
            return imglist.size();
        }

        @Override
        public Object getItem(int position) {
            return imglist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class viewhodler {
            ImageView img;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            TaskDetailActivity.Imgadapter.viewhodler vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(TaskDetailActivity.this).inflate(R.layout.item_imgview, null);
                vh = new viewhodler();
                vh.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(vh);
            } else {
                vh = (TaskDetailActivity.Imgadapter.viewhodler) convertView.getTag();
            }
            final ImageView image = (ImageView) convertView.findViewById(R.id.img);
            if (iswc==1) {
                Glide.with(TaskDetailActivity.this)
                        .load(RBConstants.URL_RENWUXUNCHA + imglist.get(position))
                        .into(vh.img);
            }else {
                Glide.with(TaskDetailActivity.this)
                        .load(RBConstants.URL_RENWU + imglist.get(position))
                        .into(vh.img);
            }
            vh.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] imageUrls = new String[imglist.size()];
                    for (int i = 0; i < imglist.size(); i++) {
                        imageUrls[i] = imglist.get(i);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("imageUrls", imageUrls);
                    intent.putExtra("curImageUrl", imageUrls[position]);
                    intent.putExtra("tasktype",iswc);
                    intent.setClass(TaskDetailActivity.this, PhotoBrowserActivity.class);
                    TaskDetailActivity.this.startActivity(intent);

                }
            });
            return convertView;
        }
    }
 }
