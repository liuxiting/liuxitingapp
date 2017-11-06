package com.example.lanzun.bean;

/**
 * Created by Administrator on 2017/10/20 0020.
 * 获取tasklist列表的参数
 */

public class TaskRequest {
    private String userId;
    private String pageNum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}
