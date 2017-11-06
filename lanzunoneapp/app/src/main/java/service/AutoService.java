package service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LLSInterface;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.lanzun.MyApplication;
import com.example.lanzun.bean.Autoreportbean;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.LocationhelpUtils;
import com.example.lanzun.tools.SharedPreferencedUtils;
import com.example.lanzun.tools.Utils;
import com.example.lanzun.tools.WebServiceUtils;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;

import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 自动上报位置信息的服务
 */

public class AutoService extends Service {
    private LocationServices locationServices;
    private Timer timer;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    android.location.Location gps = LocationhelpUtils.getGPSLocation(AutoService.this);
                 if (gps!=null){
                 }else {
                 }
                    LocationClient mLoclient=new LocationClient(AutoService.this);
                    mLoclient.registerLocationListener(new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            if (bdLocation==null&&bdLocation.getLocType() != BDLocation.TypeServerError){
                                return;
                            }else {
                               String bdlat=bdLocation.getLongitude()+"";
                                String bdlon=bdLocation.getLongitude()+"";
                               if (bdlat.indexOf("E")==-1||bdlon.indexOf("E")==-1){
                            Autoreportbean autoreportbean=new Autoreportbean();
                            autoreportbean.setUserId(SharedPreferencedUtils.getString(AutoService.this,"userid"));
                            autoreportbean.setLat((bdLocation.getLatitude()+0.000498)+"");//纬度lat
                            autoreportbean.setLon(""+(bdLocation.getLongitude()-0.006157));//经度lon
                            autoreportbean.setUploadTime(Utils.stampToDate(System.currentTimeMillis()+""));
                            Gson gosn=new Gson();
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("jsonText",gosn.toJson(autoreportbean));

                            if (Utils.isNetworkAvailable(AutoService.this)){
                                WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                                        "UploadLocation", map, new WebServiceUtils.WebServiceCallBack() {
                                            @Override
                                            public void callBack(SoapObject result) {
                                                if (result!=null){
                                                    if (result.getProperty(0).toString().equals("true")){
                                                    }
                                                }
                                            }
                                        });
                            }
                            }
                            }
                        }
                    });
                    LocationClientOption option = new LocationClientOption();
                    option.setOpenGps(true); // 打开gps
                    option.setCoorType(CoordinateConverter.CoordType.GPS+""); // 设置坐标类型
                    option.setScanSpan(0);
                    option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
                    option.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
                    option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
                    option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
                    option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
                    option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
                    option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
                    option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
                    mLoclient.setLocOption(option);
                    mLoclient.start();

                    break;
            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        locationServices = ((MyApplication) getApplication()).locationServices;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer=new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);

            }
        },0,1000*60);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
