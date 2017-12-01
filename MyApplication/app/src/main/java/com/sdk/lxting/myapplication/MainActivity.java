package com.sdk.lxting.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements  GestureDetector.OnGestureListener*/{
    private TextView text;
    GestureDetector detector;
    private  float fontSize=30;
    MapView mapView;
    private RecyclerView recyclerView;
    BaseRecyclerAdapter<String> baseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text= (TextView) findViewById(R.id.text);
        mapView= (MapView) findViewById(R.id.map);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        String strMapUrl="http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer";
        ArcGISTiledMapServiceLayer   arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(strMapUrl);
        this.mapView.addLayer(arcGISTiledMapServiceLayer);
        text.setTextSize(fontSize);
       // detector=new GestureDetector(this,this);//手势
        initdate();

    }

    private void initdate() {
        List<String> list=new ArrayList<>();
        for(int i=0;i<100;i++){
            list.add(i+"");
        }
        baseRecyclerAdapter=new BaseRecyclerAdapter<String>(this,null,R.layout.item_view) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {

            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.setData(list);

    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        return detector.onTouchEvent(e);
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        showShortToast("The method has been called - onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        showShortToast("The method has been called - onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        showShortToast("The method has been called - onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float vx, float vy)
    {
        showShortToast("The method has been called - onScroll");
        if (vx>0){
            if (fontSize>10){
                fontSize-=5;
                text.setTextSize(fontSize);

            }
        }else {
            if (fontSize<60){
                fontSize+=5;
                text.setTextSize(fontSize);

            }

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        showShortToast("The method has been called - onFling");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        showShortToast("The method has been called - onFling");
        return false;
    }
    //我们将Toast封装一下，以便调用时只需要传入待显示的消息，而略去了填写Context和持续时间等参数。
    public void showShortToast(String message){
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }*/


}
