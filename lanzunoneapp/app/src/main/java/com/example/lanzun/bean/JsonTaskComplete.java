package com.example.lanzun.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 任务完成需要上传的数据
 *      //taskId 任务id,
 *     // wcxx 完成信息，
 *     // wctx 完成图像，
 *     // wcrq 完成日期
 */

public class JsonTaskComplete {
    private  String taskId;//任务id
    private String wcxx;//完成信息
    private String wcrq;//完成日期
    private List<String> wctx;//完成图像

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getWcxx() {
        return wcxx;
    }

    public void setWcxx(String wcxx) {
        this.wcxx = wcxx;
    }

    public String getWcrq() {
        return wcrq;
    }

    public void setWcrq(String wcrq) {
        this.wcrq = wcrq;
    }

    public List<String> getWctx() {
        return wctx;
    }

    public void setWctx(List<String> wctx) {
        this.wctx = wctx;
    }
}
