package com.example.lanzun.fragment;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISImageServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.ags.MapServiceInfo;
import com.esri.core.geometry.Point;
import com.esri.core.map.DynamicLayerInfo;
import com.example.lanzun.R;
import com.example.lanzun.base.FragmentBase;
import com.example.lanzun.tdtlayer.TianDiTuTiledMapServiceLayer;
import com.example.lanzun.tdtlayer.TianDiTuTiledMapServiceType;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/14 0014.\
 * 地图显示
 */

public class MapFragment extends FragmentBase {
    @BindView(R.id.fgmap)
    MapView mapView;
    Unbinder unbinder;
    @BindView(R.id.location)
    LinearLayout location;
    Unbinder unbinder1;
    private double lat = -1;
    private double lng = -1;
    TianDiTuTiledMapServiceLayer t_vec;
    TianDiTuTiledMapServiceLayer t_cva;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        dingweilocation();//定位
        mapmethod();//map相关
    }
    @OnClick({R.id.location,R.id.jia,R.id.jian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.location:
                if (lat != -1 & lng != -1) {
                    mapView.centerAt(lat, lng, true);
                    mapView.setScale(1105828.1803422251);
                }
                break;
            case R.id.jia:
                mapView.zoomin();
                break;
            case R.id.jian:
                mapView.zoomout();
                break;
        }
    }
    private void mapmethod() {
        /* mapView.setMapOptions(new MapOptions(MapOptions.MapType.SATELLITE));
        * http://map.geoq.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer//中文地图 北京易通科技提供
        * http://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer //天地图全球影像底图
        * http://services.arcgisonline.com/ArcGIS/rest/services/Reference/World_Transportation/MapServer  //天地图全球影像和交通底图
        * http://60.208.94.143:6080/arcgis/rest/services/sdmap/MapServer//山东省地图
        * 	http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer 全球街区图
        * 	http://services.arcgisonline.com/arcgis/rest/services/World_Topo_Map/MapServer全球地形图
        * http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineCommunity/MapServer
        * http://60.208.94.143:6080/arcgis/rest/services/DC/DCimgTest/MapServer 影像东川
        *
       * */


  /*      ArcGISTiledMapServiceLayer gg = new//替换底图
                ArcGISTiledMapServiceLayer
                ("http://60.208.94.143:6080/arcgis/rest/services/DC/DCimgTest/MapServer");
        mapView.addLayer(gg);*/
     /* t_vec=new TianDiTuTiledMapServiceLayer(TianDiTuTiledMapServiceType.VEC_C,null,true);
        Log.i("mapurl",TianDiTuTiledMapServiceType.VEC_C+"");
        mapView.addLayer(t_vec);*/

        ArcGISLocalTiledLayer arcGISLocalTiledLayer=new ArcGISLocalTiledLayer("");//加载离线地图
        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        ArcGISDynamicMapServiceLayer TT = new//添加新的图层
                //http://60.208.94.143:6080/arcgis/rest/services/DC/DCimgTest/MapServer;
                // http://60.208.94.143:6080/arcgis/rest/services/sdmap/MapServer
                ArcGISDynamicMapServiceLayer("http://124.128.9.246:6080/arcgis/rest/services/DC/DCTest/MapServer");
     /*   HashMap<Integer, String> layerDefs = new HashMap<Integer, String>();
        layerDefs.put(Integer.valueOf(0), "1=2");
        TT.setLayerDefinitions(layerDefs);*/

        mapView.addLayer(TT);
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                Point point = new Point(36.661452, 117.153925);
                if (lat != -1 & lng != -1) {
                    mapView.centerAt(lat, lng, true);
                }
                mapView.setScale(1105828.1803422251);//比例尺  99240.8989612349
            }
        });
        mapView.setOnZoomListener(new OnZoomListener() {
            //缩放监听
            @Override
            public void preAction(float v, float v1, double v2) {
            }
            @Override
            public void postAction(float v, float v1, double v2) {

            }
        });
    }

    private void dingweilocation() {
        LocationDisplayManager locationDisplayManager = mapView.getLocationDisplayManager();
        locationDisplayManager.setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude()+0.000498;//纬度
                lng = location.getLongitude()-0.006157;//经度
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        locationDisplayManager.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frgmentmap;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
