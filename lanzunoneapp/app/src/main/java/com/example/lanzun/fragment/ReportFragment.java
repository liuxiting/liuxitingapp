package com.example.lanzun.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.dd.CircularProgressButton;
import com.example.lanzun.MainActivity;
import com.example.lanzun.MyApplication;
import com.example.lanzun.R;
import com.example.lanzun.activity.LoginActivity;
import com.example.lanzun.base.FragmentBase;
import com.example.lanzun.bean.JsonReport;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.SharedPreferencedUtils;
import com.example.lanzun.tools.Utils;
import com.example.lanzun.tools.WebServiceUtils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIListPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import service.LocationServices;

import static android.R.style.Widget;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.UI_MODE_SERVICE;
import static com.qmuiteam.qmui.widget.popup.QMUIPopup.DIRECTION_TOP;

/**
 * Created by Administrator on 2017/9/14 0014.
 * 上报页面
 */
public class ReportFragment extends FragmentBase {
    @BindView(R.id.txt_title)
    TextView title;
    @BindView(R.id.edittxt)
    EditText edittxt;
    Unbinder unbinder;
    @BindView(R.id.layout_back)
    RelativeLayout layoutBack;
    @BindView(R.id.txttime)
    TextView txttime;
    @BindView(R.id.zhuangtai)
    TextView zhuangtai;
    @BindView(R.id.tupian)
    RelativeLayout tupian;
    @BindView(R.id.shipin)
    RelativeLayout shipin;
    @BindView(R.id.luyin)
    RelativeLayout luyin;
    @BindView(R.id.tupiannum)
    TextView tupiannum;//选中了几个图片
    @BindView(R.id.shipinnum)
    TextView shipinnum;//选中了几个视频
    @BindView(R.id.luyinnum)
    TextView luyinnum;//选中了几个录音
    private int israyn=0;
    private String rayn;//异常正常
    List<String> list = new ArrayList<>();//打非治违名称list
    List<String> listname=new ArrayList<>();//事件类型，巡查状态

    private  List<LocalMedia> imgselectList=new ArrayList<>();
    private List<LocalMedia> shipinlist=new ArrayList<>();
    private List<LocalMedia> luyinlist=new ArrayList<>();
    BaseRecyclerAdapter<String> baseRecyclerAdapter;
    @BindView(R.id.rename)
    RelativeLayout rename;
    @BindView(R.id.sptiem)
    TextView sptiem;//打非治违名称textview
    @BindView(R.id.rezhuangtai)
    RelativeLayout rezhuangtai;//巡查状态
    @BindView(R.id.spzhuangtai)
    TextView spzhuangtai;//
    @BindView(R.id.time)
    TextView time;//巡查时间
    private LocationServices locationServices;
    private String lat="";//纬度
    private String lon="";//经度
    private QMUIListPopup mListPopup;
    private  QMUIListPopup namelistpopup;//位置名称弹出框
    private QMUITipDialog qmuiTipDialog;
    @BindView(R.id.tijiao)
    CircularProgressButton tijiao;
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        title.setText("上报");
        initview();
    }
    private void initview() {
        time.setText(Utils.stampToDate(System.currentTimeMillis()+""));
        shipinnum.setText(shipinlist.size()+"个视频");
        tupiannum.setText(""+imgselectList.size()+"张图片");
        luyinnum.setText(luyinlist.size()+"个录音");
         locationServices=((MyApplication) getActivity().getApplication()).locationServices;
        locationServices.registerListener(mListener);
        locationServices.start();// 定位SDK
    }
    private void initload(final View view) {
         //位置名称

            listname.clear();

        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                "GetDFZWD", null, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (result!=null) {
                    Log.i("webserver请求数据", result.getProperty(0).toString());
                    try {
                        JSONArray jsonarray = new JSONArray(result.getProperty(0).toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject object = jsonarray.getJSONObject(i);
                            listname.add(object.getString("DLWZ").toString());
                        }
                        //  baseRecyclerAdapter.setData(list);
                        namelisrPopupIfNeed(listname);
                        namelistpopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                        namelistpopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
                        namelistpopup.show(view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            /*    Gson gson= new Gson();
                JsonArray jsonarray=new JsonArray();
                Reportlistbean reportlistbean=gson.fromJson(result.getProperty(0).toString(),Reportlistbean.class);
                Log.i("webserver请求数据=",reportlistbean.toString());*/
                }else{
                    Utils.setdialog(qmuiTipDialog,getContext(),"请求失败");

                }
            }
        });
    }

    private void xczt(final View view){
        //事件类型，巡查状态
        israyn=1;
        list.clear();
        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                "GetXCZT", null, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if (result!=null) {
                            Log.i("webserver请求数据", result.getProperty(0).toString());
                            JSONArray jsonarray = null;
                            try {
                                jsonarray = new JSONArray(result.getProperty(0).toString());
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject object = jsonarray.getJSONObject(i);

                                    list.add(object.getString("MC").toString());
                                }
                                //baseRecyclerAdapter.setData(list);
                                initListPopupIfNeed(list);
                                mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                                mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
                                mListPopup.show(view);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Utils.setdialog(qmuiTipDialog,getContext(),"请求失败");
                        }
                    }
                });


    }

    @OnClick({R.id.tupian, R.id.shipin, R.id.luyin,R.id.rename,R.id.tijiao,R.id.rezhuangtai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rezhuangtai:
                if (Utils.isNetworkAvailable(getActivity())){
                   // renameshowpopview(rezhuangtai);
                    xczt(rezhuangtai);//事件类型，巡查状态
                }else {
                    Utils.setdialog(qmuiTipDialog,getContext(),"暂无网络");


                }

                break;
            case R.id.tijiao:
                tijiao.setIndeterminateProgressMode(true);
                if (tijiao.getProgress() == 0) {
                    tijiao.setProgress(50);
                } else if (tijiao.getProgress() == 100) {
                    tijiao.setProgress(0);
                } else {
                    tijiao.setProgress(100);
                }
                if (!lon.equals("")){
                if (sptiem.getText().toString().equals("")){
                    tijiao.setProgress(0);
                    Utils.setdialog(qmuiTipDialog,getContext(),"请选择站点");
                }else if (spzhuangtai.getText().toString().equals("")){
                    tijiao.setProgress(0);
                    Utils.setdialog(qmuiTipDialog,getContext(),"请选择巡查状态");
                }else if (edittxt.getText().toString().equals("")){
                    tijiao.setProgress(0);
                    Utils.setdialog(qmuiTipDialog,getContext(),"巡查信息不能为空");
                }else {
                    if (Utils.isNetworkAvailable(getActivity())) {
                        showProgressDialog(getActivity());
                        JsonReport jsonReport = new JsonReport();
                        jsonReport.setWzmc(sptiem.getText().toString());//打非治违名称
                        jsonReport.setXcxx("" + edittxt.getText());//巡查信息
                        jsonReport.setXczt("" + spzhuangtai.getText());//巡查状态
                        jsonReport.setXcsj(Utils.stampToDate(System.currentTimeMillis() + ""));//时间
                        jsonReport.setLat(lat);//纬度
                        jsonReport.setLon(lon);//经度
                        jsonReport.setUserName(SharedPreferencedUtils.getString(getActivity(), "username"));
                        jsonReport.setUserId(SharedPreferencedUtils.getString(getActivity(), "userid"));
                        final List<String> imglist = new ArrayList<>();
                        List<String> shipinlists = new ArrayList<>();
                        List<String> luyinlists = new ArrayList<>();
                        for (int i = 0; i < imgselectList.size(); i++) {
                            Bitmap bitmap = Utils.compressBitmap(imgselectList.get(i).getPath(), 600, 1024, 30);
                            Log.i("图片流", Utils.bitmapToBase64(bitmap));
                            imglist.add(Utils.bitmapToBase64(bitmap));
                        }
                        for (int i = 0; i < shipinlist.size(); i++) {
                            Log.i("shipin", shipinlist.get(i).getPath() + "/" + new File(shipinlist.get(i).getPath()));
                            File file = new File(shipinlist.get(i).getPath());
                            String result = Utils.getByetsFromFile(file);
                            Log.i("视频流", result);
                            shipinlists.add(result);
                        }
                        for (int i = 0; i < luyinlist.size(); i++) {
                            Log.i("luyin", luyinlist.get(i).getPath() + "");
                            File file = new File(luyinlist.get(i).getPath());
                            String result = Utils.getluyinfile(file);
                            Log.i("音频流", result);
                            luyinlists.add(result);
                        }
                        Log.i("几张图片", imglist.size() + "");
                        jsonReport.setXctx(imglist);
                        jsonReport.setXclx(shipinlists);
                        jsonReport.setXcly(luyinlists);
                        Gson gson = new Gson();
                        Log.i("json数据转", gson.toJson(jsonReport));
                        Map<String, String> map = new HashMap<>();
                        map.put("jsonText", gson.toJson(jsonReport));
                        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                                "UploadXCJL", map, new WebServiceUtils.WebServiceCallBack() {
                                    @Override
                                    public void callBack(SoapObject result) {
                                        Log.i("webserver请求数据", result + "");
                                        tijiao.setProgress(100);
                                        tijiao.setProgress(0);
                                       // tijiao.clearComposingText();
                                        hideProgressDialog();
                                        if (result != null) {
                                            if (result.getProperty(0).toString().equals("true")) {
                                                Utils.setdialog(qmuiTipDialog,getContext(),"上报成功");
                                                sptiem.setText("");
                                                spzhuangtai.setText("");
                                                time.setText(Utils.stampToDate(System.currentTimeMillis() + ""));
                                                edittxt.setText("");
                                                imgselectList.clear();
                                                tupiannum.setText("" + imgselectList.size() + "张图片");
                                                shipinlist.clear();
                                                luyinlist.clear();
                                                shipinnum.setText(shipinlist.size() + "个视频");

                                                luyinnum.setText(luyinlist.size() + "个录音");
                                                Intent intent = new Intent();
                                                intent.setAction("liu");
                                                intent.putExtra("ok", "ok");
                                                getActivity().sendBroadcast(intent);

                                            } else {
                                                imgselectList.clear();
                                                shipinlist.clear();
                                                luyinlist.clear();
                                                tupiannum.setText("");
                                                luyinnum.setText("");
                                                shipinnum.setText("");
                                                Utils.setdialog(qmuiTipDialog,getContext(),"上报失败，请重新上传");

                                            }
                                        } else {
                                            imgselectList.clear();
                                            shipinlist.clear();
                                            luyinlist.clear();
                                            tupiannum.setText("");
                                            luyinnum.setText("");
                                            shipinnum.setText("");
                                            Utils.setdialog(qmuiTipDialog,getContext(),"上报失败，请重新上传");

                                        }
                                    }
                                });
                    } else {
                        tijiao.setProgress(0);
                        Utils.setdialog(qmuiTipDialog,getContext(),"暂无网络");
                    }
                }
                }else {
                    tijiao.setProgress(0);
                    Utils.setdialog(qmuiTipDialog,getContext(),"定位失败请稍后重试");
                }
                break;
            case R.id.rename:
                if (Utils.isNetworkAvailable(getActivity())){
                    //下拉打非治违名称
                   // renameshowpopview(rename);
                    initload(rename);//位置名称
                }else {
                    Utils.setdialog(qmuiTipDialog,getContext(),"暂无网络");
                }
                break;
            case R.id.tupian:
                imgselectList.clear();
                tupiannum.setText(""+imgselectList.size()+"张图片");
                PictureSelector.create(getActivity())
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
            case R.id.shipin:
                shipinlist.clear();
                shipinnum.setText(shipinlist.size()+"个视频");
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofVideo())
                        .maxSelectNum(1)//选择视频数
                        .imageSpanCount(4)
                        .minSelectNum(0)
                        .compress(true)//是否压缩
                        .videoQuality(0)//录制视频质量
                        .recordVideoSecond(8)//录制视频秒数
                       // .selectionMedia(shipinlist)// 是否传入已选图片
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.luyin:
                luyinlist.clear();
                luyinnum.setText(luyinlist.size()+"个录音");
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofAudio())
                        .maxSelectNum(1)//选择图片数
                        .imageSpanCount(4)
                        .minSelectNum(0)
                        .videoQuality(0)//录制视频质量
                        .recordVideoSecond(8)//录制视频秒数
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .compress(true)//是否压缩
                        //.selectionMedia(luyinlist)// 是否传入已选图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmentreport;//上报布局
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    private void namelisrPopupIfNeed(final List<String> list){
        //位置名称

        if (namelistpopup==null){
            namelistpopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_NONE, new listnameadapter(list));
            namelistpopup.create(QMUIDisplayHelper.getScreenWidth(getActivity()), QMUIDisplayHelper.dp2px(getActivity(),200),
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            sptiem.setText(list.get(i));
                            namelistpopup.dismiss();
                    }
                });
            namelistpopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    // mActionButton2.setText(getContext().getResources().getString(R.string.popup_list_action_button_text_show));
                }
            });

        }
    }
    private void initListPopupIfNeed(final List<String> list) {
        //事件类型
        if (mListPopup == null) {
                mListPopup = new QMUIListPopup(getContext(), QMUIPopup.DIRECTION_NONE, new listadapter(list));
            mListPopup.create(QMUIDisplayHelper.getScreenWidth(getActivity()), QMUIDisplayHelper.dp2px(getActivity(),200),
                    new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (israyn==1){
                        spzhuangtai.setText(list.get(i));
                        israyn=0;
                    }else {
                        sptiem.setText(listname.get(i));
                    }
                    mListPopup.dismiss();
                }
            });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                   // mActionButton2.setText(getContext().getResources().getString(R.string.popup_list_action_button_text_show));
                }
            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    if (PictureSelector.obtainMultipleResult(data).size()==0){
                        imgselectList.clear();
                        luyinlist.clear();
                        shipinlist.clear();
                    }

                    // 图片选择结果回调
                    if (PictureSelector.obtainMultipleResult(data).get(0).getMimeType()==PictureMimeType.ofImage()){
                        //选中了图片
                        imgselectList = PictureSelector.obtainMultipleResult(data);
                        tupiannum.setText(""+imgselectList.size()+"张图片");
                    }else if (PictureSelector.obtainMultipleResult(data).get(0).getMimeType()==PictureMimeType.ofAudio()){
                        luyinlist= PictureSelector.obtainMultipleResult(data);
                        luyinnum.setText(luyinlist.size()+"个录音");
                    }else if (PictureSelector.obtainMultipleResult(data).get(0).getMimeType()==PictureMimeType.ofVideo()){
                        shipinlist=PictureSelector.obtainMultipleResult(data);
                        shipinnum.setText(shipinlist.size()+"个视频");
                    }

                   /* adapter.setList(selectList);
                   adapter.notifyDataSetChanged();*/
                   // Toast.makeText(getActivity(),""+imgselectList.size()+"",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    shipinlist=PictureSelector.obtainMultipleResult(data);
                    break;
                case 2:
                    luyinlist=PictureSelector.obtainMultipleResult(data);
                    break;
            }
        }
    }
    class  listnameadapter extends BaseAdapter{
        //位置名称
        List<String> data;
        private listnameadapter(List<String> data){

            this.data=data;
        }
        @Override
        public int getCount() {
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                convertView=LayoutInflater.from(getActivity()).inflate(R.layout.item_spinner_view,null);
                TextView txt= (TextView) convertView.findViewById(R.id.txt);
                txt.setText(data.get(position));

            return convertView;
        }
    }
     class  listadapter extends  BaseAdapter{
         //事件类型，巡查状态
         List<String> data;
         private listadapter(List<String> data){
             this.data=data;
         }
         @Override
         public int getCount() {
             return data.size();
         }

         @Override
         public Object getItem(int position) {
             return data.get(position);
         }

         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             if (convertView==null){
                 convertView=LayoutInflater.from(getActivity()).inflate(R.layout.item_spinner_view,null);
                 TextView txt= (TextView) convertView.findViewById(R.id.txt);
                 txt.setText(data.get(position));
             }
             return convertView;
         }
     }
    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {


            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                lat=(location.getLatitude()+0.000498)+"";//纬度
                lon=(location.getLongitude()-0.006157)+"";//经度
               /* StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                *//**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 *//*
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
//                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());


                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市

                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
//                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }*/
                // logMsg(sb.toString());
                //Log.i("liuxiting=",sb.toString());
                //double[] dd=HelpUtils.gcj02towgs84(location.getLatitude(),location.getLongitude());
               /* Log.i("天地图和百度--",location.getLatitude()+"/"+location.getLongitude()+"//"+dd[0]
                        +"//"+dd[1]);*/
            }
        }

    };
}
