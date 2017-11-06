package com.example.lanzun.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lanzun.R;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.bean.RecordDetailsitembeam;
import com.example.lanzun.bean.Recordlistbean;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.SharedPreferencedUtils;
import com.example.lanzun.tools.WebServiceUtils;
import com.example.lanzun.view.MyGridView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2017/9/25 0025.
 * 记录列表详情
 */

public class RecordDetailsActivity extends ActivityBase {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.layout_back)
    RelativeLayout layout_back;
    @BindView(R.id.textname)
    TextView textname;//打非治违名称
    @BindView(R.id.time)
    TextView time;//上传时间
    @BindView(R.id.showxinxi)
    TextView showxinxi;//上传的文字信息
    @BindView(R.id.imggrid)
    MyGridView imggrid;//图片
    @BindView(R.id.shipingrid)
    MyGridView shipingrid;//视频
    @BindView(R.id.luyingrid)
    MyGridView luyingrid;//语音
    @BindView(R.id.showzhuangtai)
    TextView showzhuangtai;//巡查状态
    @BindView(R.id.imglx)
    ImageView imglx;
    @BindView(R.id.imgly)
    ImageView imgly;
    @BindView(R.id.txtly)
    TextView txtly;
    @BindView(R.id.txtlx)
    TextView txtlx;
    @BindView(R.id.txttx)
    TextView txttx;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recorddeails;
    }

    @Override
    protected void initdata() {
        txtTitle.setText("上报详情");
        layout_back.setVisibility(View.VISIBLE);
        Map<String,String> map=new HashMap<>();
        map.put("jsonText","{\"xh\":\""+getIntent().getStringExtra("xh")+"\"}");
        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                "GetDFZWDetail", map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if (result!=null){
                            try {
                                final List<String> imagelist=new ArrayList<String>();
                                JSONArray jsonArray=new JSONArray(result.getProperty(0)+"");
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    textname.setText(jsonObject.getString("WZMC"));
                                    time.setText(jsonObject.getString("XCSJ"));
                                    showxinxi.setText(jsonObject.getString("XCXX"));
                                    showzhuangtai.setText(jsonObject.getString("XCZT"));
                                    Gson gson=new Gson();
                                    final RecordDetailsitembeam recordDetailsitembeam=gson.fromJson(jsonArray.getJSONObject(0)+"",RecordDetailsitembeam.class);
                                if (recordDetailsitembeam.getXCLX().size()>0){
                                        imglx.setVisibility(View.VISIBLE);
                                    imglx.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //饺子播放器
                                            JZVideoPlayerStandard.startFullscreen(RecordDetailsActivity.this,
                                                    JZVideoPlayerStandard.class,
                                                    ""+RBConstants.URL_video+recordDetailsitembeam.getXCLX().get(0),
                                                    "视频播放");

                                        /* 也可跳转到其他页面进行播放，为了简单直接播放了
                                         Intent intent=new Intent(RecordDetailsActivity.this,VideoJZActivity.class);
                                            intent.putExtra("mp4",recordDetailsitembeam.getXCLX().get(0)+"");
                                            startActivity(intent);
                                            Toast.makeText(RecordDetailsActivity.this,"点击成功",Toast.LENGTH_LONG).show();*/
                                        }
                                    });
                                }else {
                                    txtlx.setVisibility(View.VISIBLE);

                                }
                                if (recordDetailsitembeam.getXCLY().size()>0){
                                    imgly.setVisibility(View.VISIBLE);
                                    imgly.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(RecordDetailsActivity.this,MusicPlayerActivity.class);
                                            intent.putExtra("mp3",recordDetailsitembeam.getXCLY().get(0)+"");
                                            startActivity(intent);
                                        }
                                    });
                                }else {
                                    txtly.setVisibility(View.VISIBLE);
                                }
                                if (recordDetailsitembeam.getXCTX().size()>0){
                                    imggrid.setAdapter(new Imgadapter(recordDetailsitembeam.getXCTX()));
                                }else {
                                    txttx.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.txt_title, R.id.layout_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_title:
                break;
            case R.id.layout_back:
                finish();
                break;

        }
    }

    class Imgadapter extends BaseAdapter{
        List<String> imglist;
        private  Imgadapter (List<String> list){
            this.imglist=list;
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
          class  viewhodler{
              ImageView img;
          }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            viewhodler vh ;
            if (convertView==null){
                convertView= LayoutInflater.from(RecordDetailsActivity.this).inflate(R.layout.item_imgview,null);
                vh=new viewhodler();
                vh.img= (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(vh);
            }else {
                vh= (viewhodler) convertView.getTag();
            }
            final ImageView image= (ImageView) convertView.findViewById(R.id.img);


            Glide.with(RecordDetailsActivity.this)
                    .load(RBConstants.URL_IMAG+imglist.get(position))
                    .into(vh.img);
            vh.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String [] imageUrls=new String[imglist.size()];
                    for (int i=0;i<imglist.size();i++){
                        imageUrls[i]=imglist.get(i);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("imageUrls", imageUrls);
                    intent.putExtra("curImageUrl", imageUrls[position]);
                    intent.putExtra("tasktype",0);//这个页面这个值没什么用，主要是为了在下个页面区分是否是任务详情页面跳转的
                    intent.setClass(RecordDetailsActivity.this, PhotoBrowserActivity.class);
                    RecordDetailsActivity.this.startActivity(intent);

                }
            });
            return convertView;
        }
    }
}
