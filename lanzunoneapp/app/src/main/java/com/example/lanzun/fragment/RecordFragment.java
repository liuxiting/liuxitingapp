package com.example.lanzun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lanzun.R;
import com.example.lanzun.activity.RecordDetailsActivity;
import com.example.lanzun.base.FragmentBase;
import com.example.lanzun.bean.Recorditembean;
import com.example.lanzun.bean.Recordlistbean;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.SharedPreferencedUtils;
import com.example.lanzun.tools.WebServiceUtils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.RequestLoadMoreListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/14 0014.
 * 上报记录，页面
 */

public class RecordFragment extends FragmentBase implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noshow)
    TextView noshow;
    @BindView(R.id.txt_title)
    TextView txt_title;
   BaseRecyclerAdapter<Recorditembean> baseRecyclerAdapter;
    List<Recorditembean> list=new ArrayList<>();
    @BindView(R.id.swipregresh)
    SwipeRefreshLayout swipeRefreshLayout;
    private  int page=1;
    private int pagenum=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //下拉刷新
                    page=1;
                    qingqiumethod(page);
                    swipeRefreshLayout.setRefreshing(false);
                    break;

            }
        }
    };
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        txt_title.setText("上报记录");
        swipeRefreshLayout.setOnRefreshListener(this);
        initload();
        qingqiumethod(page);
    }

    private void qingqiumethod(final  int page) {
        final Recordlistbean recordlistbean=new Recordlistbean();
        recordlistbean.setUserId(SharedPreferencedUtils.getString(getActivity(), "userid"));
        recordlistbean.setPageNum(page+"");
        Gson gson=new Gson();
        Map<String,String> map=new HashMap<>();
        map.put("jsonText",gson.toJson(recordlistbean));
        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                "GetDFZWList", map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {

                        if (result!=null){
                            Gson itemgson=new Gson();
                            try {
                                JSONArray jsonArray=new JSONArray(result.getProperty(0)+"");

                                List<Recorditembean> list1=new ArrayList<Recorditembean>();
                                for (int i=0;i<jsonArray.length();i++){
                                    Recorditembean recorditembean=itemgson.fromJson(jsonArray.get(i).toString()+"",Recorditembean.class);
                                    list1.add(recorditembean);
                                }

                                if (page==1){
                                    list.clear();
                                }else {

                                }
                                list.addAll(list1);
                                if (list.size()>0){
                                    noshow.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    baseRecyclerAdapter.setData(list);
                                    pagenum=Integer.valueOf(list1.get(0).getTOTAL());
                                    if (page<pagenum){
                                        //可刷新
                                        baseRecyclerAdapter.notifyDataChangeAfterLoadMore(true);
                                    }else {
                                        baseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                                        baseRecyclerAdapter.addNoMoreView();
                                    }
                                }else {
                                    noshow.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            baseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                        }
                    }
                });
    }

    private void initload() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        baseRecyclerAdapter=new BaseRecyclerAdapter<Recorditembean>
                (getActivity(),null,R.layout.item_record_view) {
            @Override
            protected void convert(final BaseViewHolder helper, final Recorditembean item) {
                helper.setText(R.id.wzmc,"位置名称："+item.getWZMC());
                helper.setText(R.id.xczt,"事件类型："+item.getXCZT());
                helper.setText(R.id.time,item.getXCSJ());
                helper.setText(R.id.userid,"上报人员："+SharedPreferencedUtils.getString(getActivity(), "username"));
                 helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         //点击进入详情
                         Intent intent=new Intent(getActivity(), RecordDetailsActivity.class);
                         intent.putExtra("xh",item.getXH());
                       startActivity(intent);
                     }
                 });
            }
        };
        baseRecyclerAdapter.openLoadAnimation(true);
        recyclerView.setAdapter(baseRecyclerAdapter);
        baseRecyclerAdapter.openLoadingMore(true);
        baseRecyclerAdapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                              if (pagenum>page){
                                  page++;
                                  qingqiumethod(page);
                              }else {
                                  baseRecyclerAdapter.notifyDataChangeAfterLoadMore(false);
                                  if (pagenum> 1) {
                                      baseRecyclerAdapter.addNoMoreView();
                                  }
                              }
                       }
                   },2000);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragmentrecord;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onRefresh() {
        //下拉刷新
        handler.sendEmptyMessageDelayed(1, 2000);

    }
}
