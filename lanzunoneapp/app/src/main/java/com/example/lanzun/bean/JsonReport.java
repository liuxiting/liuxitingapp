package com.example.lanzun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class JsonReport {
    private String wzmc;//打非治违名称
    private String xcxx;//巡查信息
    private String xczt;//巡查状态
    private String xcsj;
    private String lon;//经度
    private String lat;//纬度
    private List<String> xctx;//巡查图像
    private List<String> xclx;//视频
    private List<String> xcly;//巡查录音
    private String userId;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getXcsj() {
        return xcsj;
    }

    public void setXcsj(String xcsj) {
        this.xcsj = xcsj;
    }

    public List<String> getXctx() {
        return xctx;
    }

    public void setXctx(List<String> xctx) {
        this.xctx = xctx;
    }

    public List<String> getXclx() {
        return xclx;
    }

    public void setXclx(List<String> xclx) {
        this.xclx = xclx;
    }

    public List<String> getXcly() {
        return xcly;
    }

    public void setXcly(List<String> xcly) {
        this.xcly = xcly;
    }

    public String getWzmc() {
        return wzmc;
    }

    public void setWzmc(String wzmc) {
        this.wzmc = wzmc;
    }

    public String getXcxx() {
        return xcxx;
    }

    public void setXcxx(String xcxx) {
        this.xcxx = xcxx;
    }

    public String getXczt() {
        return xczt;
    }

    public void setXczt(String xczt) {
        this.xczt = xczt;
    }


}
