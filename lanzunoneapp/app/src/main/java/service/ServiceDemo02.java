package service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;


import android.R.integer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.example.lanzun.MainActivity;
import com.example.lanzun.R;
import com.example.lanzun.bean.NotifiectionBean;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.SharedPreferencedUtils;
import com.example.lanzun.tools.Utils;
import com.example.lanzun.tools.WebServiceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/10/24 0024.
 * 任务通知服务，包括请求两次接口（一次是是否有新任务，一次是吧通知的任务改成已经通知状态）
 * 使用了，timer循环每隔5秒循环一次，退出应用程序服务停止
 *
 */
public class ServiceDemo02 extends Service{
    Context context;
    private Timer timer;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  1:
                    if (Utils.isNetworkAvailable(context)){
                        Map<String,String> map=new HashMap<String, String>();
                        String userid= SharedPreferencedUtils.getString(context,"userid");
                        map.put("jsonText","{\"userId\":\""+userid+"\"}");
                        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                                "HasTask", map, new WebServiceUtils.WebServiceCallBack() {
                                    @Override
                                    public void callBack(SoapObject result) {

                                        if (result!=null){
                                            if (result.getProperty(0).toString().equals("false")){

                                            }else {
                                                //TaskNotified
                                                        Map<String,String> map=new HashMap<String, String>();
                                                        map.put("jsonText",result.getProperty(0)+"");
                                                        CreateInform();
                                                        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                                                                "TaskNotified", map, new WebServiceUtils.WebServiceCallBack() {
                                                                    @Override
                                                                    public void callBack(SoapObject result) {



                                                                    }
                                                                });
                                            }
                                        }
                                    }
                                });
                    }
                    break;
            }
        }
    };
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();
    }
    //创建通知
    public void CreateInform() {

        //定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
        Intent intent = new Intent(context,MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("renwuservice","renwuservice");
   /*     Intent intent1 = new Intent();
        intent1.setAction("renwu");
        intent1.putExtra("ok", "ok");
        sendBroadcast(intent1);*/
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 3, intent, 0);

        //创建一个通知
        Notification.Builder notification = new Notification.Builder(context);
             notification.setSmallIcon(R.mipmap.logo);
        notification.setTicker("显示通知");
        notification.setContentTitle("通知");
        notification.setContentText("点击查看详细内容");
        notification.setWhen(System.currentTimeMillis());//发送时间
        notification.setDefaults(Notification.DEFAULT_ALL);//设置默认的提示音，震动方式和灯光
        notification.setAutoCancel(true);//打开程序后图标消失
        notification.setContentIntent(pendingIntent);

        Notification notification1=notification.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(124,notification1);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stubm
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        timer=new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        },0,5000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (timer!=null) {
            //如果timer不为空就取消
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
