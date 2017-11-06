package com.example.lanzun.config;

import com.esri.core.geometry.Point;

import java.security.PublicKey;

import static com.example.lanzun.config.Contants.BASE_URL;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class RBConstants {
    /**
     * 首页第一行导航栏
     * http://192.168.1.28:5011/DHXTWebService.asmx?op=GetDZYHDFXQK
     */
    //public static final String URL_HOME_FIRST_NATIVE = BASE_URL + "/AppIndex/getIndexGoodsCategory";
    public static final String URL_HOME_FIRST_NATIVE ="http://124.128.9.246:5011/DHXTWebService.asmx";
    public static final String URL_IMAG="http://124.128.9.246:5012/UpLoadFile/DFZW/Photo/";//图片
    public  static  final  String URL_RENWU="http://124.128.9.246:5012/UpLoadFile/DFZW/TaskCompleteImage/";//完成图像
    public  static  final String URL_RENWUXUNCHA="http://124.128.9.246:5012/UpLoadFile/DFZW/TaskSendImage/";//巡查图像
    public static  final String URL_AUDIO="http://124.128.9.246:5012/UpLoadFile/DFZW/Audio/";//录音
    public static  final String URL_video="http://124.128.9.246:5012/UpLoadFile/DFZW/Video/";//视频
    public static final int REQUEST_CODE_HOME_FIRST_NATIVE = 1;
}
