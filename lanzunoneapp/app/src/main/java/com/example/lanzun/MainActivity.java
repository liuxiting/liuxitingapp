package com.example.lanzun;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.core.AllenChecker;
import com.allenliu.versionchecklib.core.VersionParams;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.PopupUtil;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.example.lanzun.Interface.OkGoUtilsInterFace;
import com.example.lanzun.adapter.GridImageAdapter;
import com.example.lanzun.base.ActivityBase;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.fragment.MapFragment;
import com.example.lanzun.fragment.MineFragment;
import com.example.lanzun.fragment.RecordFragment;
import com.example.lanzun.fragment.ReportFragment;
import com.example.lanzun.fragment.TaskFragment;
import com.example.lanzun.tools.OkgoUtils;
import com.example.lanzun.tools.WebServiceUtils;
import com.fastaccess.permission.base.PermissionHelper;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import service.AutoService;
import service.ServiceDemo02;
import service.VersionService;

public class MainActivity extends ActivityBase {
  @BindView(R.id.layout_back)
  RelativeLayout layout_back;//返回按钮
    //R.id.showmap,R.id.report,R.id.record,R.id.mine
    @BindView(R.id.showmap)
    ImageView showmap;
    @BindView(R.id.Task)
    RadioButton task;
    @BindView(R.id.report)
    RadioButton report;
    @BindView(R.id.record)
    RadioButton record;
    @BindView(R.id.mine)
    RadioButton mine;
    @BindView(R.id.txt_title)
    TextView txttile;
    private double lat=-1;
    private double lng=-1;
    FragmentManager manager;//Fragment管理者
    FragmentTransaction transaction;//Fragment事务管理者
    Fragment showfragment;//当前页，即将要隐藏的页面，就是switchFragment（）这个方法的第一个参数
    ReportFragment reportFragment;//上报页面
    RecordFragment recordFragment;//上报列表
    MineFragment mineFragment;//个人中心
    MapFragment mapFragment;//地图fragment
    TaskFragment taskFragment;//任务fragmenet；
  //private GridImageAdapter adapter;
  private PermissionHelper mPermissionHelper;
   public  static    Intent intentdemo02;
  private  List<LocalMedia> selectList=new ArrayList<>();
  private final static String[] MULTI_PERMISSIONS = new String[]{
          //需要动态申请的权限
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.CAMERA
  };
  private MyBroadCastReceiver myBroadCastReceiver;
    private showrenwuBraodCastReceiver showrenwuBraodCastReceivers;
    public  static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txttile.setText("首页");
        activity=this;
         myBroadCastReceiver=new MyBroadCastReceiver();
        IntentFilter intentFilter=new IntentFilter("liu");
        registerReceiver(myBroadCastReceiver,intentFilter);
        showrenwuBraodCastReceivers=new showrenwuBraodCastReceiver();
        IntentFilter intentFilter1=new IntentFilter("renwu");
        registerReceiver(showrenwuBraodCastReceivers,intentFilter1);
        Intent intent=new Intent(this, AutoService.class);
        startService(intent);
        intentdemo02=new Intent(this, ServiceDemo02.class);
        startService(intentdemo02);
                 /*权限动态申请*/
        mPermissionHelper = PermissionHelper.getInstance(this);
        mPermissionHelper.request(MULTI_PERMISSIONS);
        manager=getSupportFragmentManager();//实例话Framgment管理类
        if (mapFragment==null){
            mapFragment=new MapFragment();
            addFragment(mapFragment,"map");
        }else {
            switchFragment(showfragment,mapFragment);
        }
          layout_back.setVisibility(View.GONE);//隐藏公用的返回按钮

        // recyclerviewload();//recyclerview 承载图片，音视频的adapter
        }


    private void recyclerviewload() {
/*        FullyGridLayoutManager manager = new FullyGridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(MainActivity.this,null);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(MainActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(MainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(MainActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });*/
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initdata() {
        VersionParams.Builder builder = new VersionParams.Builder()
//                .setHttpHeaders(headers)
//                .setRequestMethod(requestMethod)
//                .setRequestParams(httpParams)
                .setRequestUrl("http://www.baidu.com")
//                .setDownloadAPKPath(getApplicationContext().getFilesDir()+"/")
                .setService(VersionService.class);
        AllenChecker.startVersionCheck(this,builder.build());


    }
    @OnClick({R.id.showmap,R.id.report,R.id.record,R.id.mine,R.id.Task})
    public void onClick(View view){
      switch (view.getId()){
          case R.id.showmap://显示地图
              task.setSelected(false);
              mine.setSelected(false);
              report.setSelected(false);
              record.setSelected(false);
              if (mapFragment==null){
                  mapFragment=new MapFragment();
                  addFragment(mapFragment,"map");
              }else {
                  switchFragment(showfragment,mapFragment);
              }
              break;
          case R.id.Task://任务
              task.setSelected(true);
              mine.setSelected(false);
              report.setSelected(false);
              record.setSelected(false);
              if (taskFragment==null){
                  taskFragment=new TaskFragment();
                  addFragment(taskFragment,"task");
              }else {
                  switchFragment(showfragment,taskFragment);
              }
              break;
          case R.id.report://上报
              task.setSelected(false);
              mine.setSelected(false);
              report.setSelected(true);
              record.setSelected(false);
              if (reportFragment==null){
                  reportFragment=new ReportFragment();
                  addFragment(reportFragment,"report");
              }else {
                  switchFragment(showfragment,reportFragment);
              }
              break;
          case R.id.record://记录
              task.setSelected(false);
              mine.setSelected(false);
              report.setSelected(false);
              record.setSelected(true);
              if (recordFragment == null) {
                  recordFragment=new RecordFragment();
                  addFragment(recordFragment,"record");
              }else {
                  switchFragment(showfragment,recordFragment);
              }
              break;
          case R.id.mine://个人中心
              task.setSelected(false);
              mine.setSelected(true);
              report.setSelected(false);
              record.setSelected(false);
              if (mineFragment==null){
                  mineFragment=new MineFragment();
                  addFragment(mineFragment,"mine");
              }else {
                  switchFragment(showfragment,mineFragment);
              }
              break;
        case R.id.layout_back://返回按钮，在首页返回按钮是隐藏的
          break;
      }
    }
public  class showrenwuBraodCastReceiver extends  BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra("renwu").equals("renwu")){

            stopService(intentdemo02);
        }
    }
}

    public class MyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("ok").equals("ok")){
                mine.setSelected(false);
                report.setSelected(false);
                record.setSelected(false);
                if (mapFragment==null){
                    mapFragment=new MapFragment();
                    addFragment(mapFragment,"map");
                }else {
                    switchFragment(showfragment,mapFragment);
                }
            }
    }}
   //第一次加载fragment时
    public  void addFragment(Fragment fragment, String tag){
        transaction =manager.beginTransaction();
        if (showfragment==null){
        }else {
            transaction.hide(showfragment);
        }
        transaction.add(R.id.content,fragment);
        transaction.commit();
        showfragment=null;
        showfragment=fragment;
    }

    //切换页面的重载，优化了fragment 的切换
    public void switchFragment(Fragment from,Fragment to){
        if(from==null||to==null){
            return ;
        }else {
            FragmentTransaction transaction=manager.beginTransaction();
            if (!to.isAdded()){
                //隐藏当前的fragment，
                transaction.hide(from).add(R.id.content,to).commit();
            }else {
                transaction.hide(from).show(to).commit();
            }
            showfragment=to;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reportFragment.onActivityResult(requestCode, resultCode, data);

    }
      // carly rae jepsen

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCastReceiver);
        unregisterReceiver(showrenwuBraodCastReceivers);
        stopService(new Intent(this, VersionService.class));
        stopService(intentdemo02);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Mainactivity 是singletask模式，从通知栏跳转到这里在这里能接收传过来的值
        if (intent.getStringExtra("renwuservice")!=null) {
            task.setSelected(true);
            mine.setSelected(false);
            report.setSelected(false);
            record.setSelected(false);
            if (taskFragment==null){
                taskFragment=new TaskFragment();
                addFragment(taskFragment,"task");
            }else {
                switchFragment(showfragment,taskFragment);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
