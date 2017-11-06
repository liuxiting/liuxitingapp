package com.example.lanzun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.core.internal.tasks.TaskListener;
import com.example.lanzun.MainActivity;
import com.example.lanzun.R;
import com.example.lanzun.activity.TaskDetailActivity;
import com.example.lanzun.base.FragmentBase;
import com.example.lanzun.bean.Recordlistbean;
import com.example.lanzun.bean.TaskRequest;
import com.example.lanzun.bean.Tasklist;
import com.example.lanzun.config.RBConstants;
import com.example.lanzun.tools.SharedPreferencedUtils;
import com.example.lanzun.tools.WebServiceUtils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.listener.RequestLoadMoreListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/10/20 0020.
 * 任务页面
 */

public class TaskFragment extends FragmentBase implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.layout_back)
    RelativeLayout layoutBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_right)
    TextView txtRight;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.noshow)
    TextView noshow;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipregresh)
    SwipeRefreshLayout swipregresh;
    Unbinder unbinder;
    BaseRecyclerAdapter<Tasklist> baseRecyclerAdapter;
    List<Tasklist> list=new ArrayList<>();
    private int istiao=0;//是否跳转到任务详情
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
                    list.clear();
                    initnetdata(page);
                    swipregresh.setRefreshing(false);
                    break;

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        txtTitle.setText("任务");
        swipregresh.setOnRefreshListener(this);
        initadapter();//适配器
        initnetdata(page);//请求接口
    }
    private void initnetdata(final int page) {
        final TaskRequest taskjson=new TaskRequest();
        taskjson.setUserId(SharedPreferencedUtils.getString(getActivity(), "userid"));
        taskjson.setPageNum(page+"");
        Gson gson=new Gson();
        Map<String,String> map=new HashMap<>();
        map.put("jsonText",gson.toJson(taskjson));
        WebServiceUtils.callWebService(RBConstants.URL_HOME_FIRST_NATIVE,
                "TaskList", map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(SoapObject result) {
                        if (result!=null){
                            try {
                                List<Tasklist> itemlist=new ArrayList<Tasklist>();
                                JSONArray jsonarray=new JSONArray(result.getProperty(0)+"");
                                for (int i=0;i<jsonarray.length();i++){
                                    JSONObject jsonobject=jsonarray.optJSONObject(i);
                                    Gson gson=new Gson();
                                    Tasklist tasklist=gson.fromJson(jsonobject+"", Tasklist.class);
                                    itemlist.add(tasklist);
                                }
                                if (page==1){
                                    list.clear();
                                }else {

                                }
                                list.addAll(itemlist);
                                if (list.size()>0){
                                    noshow.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    baseRecyclerAdapter.setData(list);
                                    pagenum=Integer.valueOf(list.get(0).getTOTAL());
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

    private void initadapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        baseRecyclerAdapter=new BaseRecyclerAdapter<Tasklist>(getActivity(),null, R.layout.item_taskview) {
            @Override
            protected void convert(BaseViewHolder helper, final Tasklist item) {
                helper.setText(R.id.taskname,"任务名称："+item.getRWMC());
                helper.setText(R.id.time,"任务时间："+item.getXFRQ());
                if (item.getRWZT().equals("0")){
                    //未接受
                    helper.setText(R.id.task,"任务状态：未接受");
                }else if (item.getRWZT().equals("1")){
                    //已接受
                    helper.setText(R.id.task,"任务状态：已接受");
                }else if (item.getRWZT().equals("2")){
                    //已完成
                    helper.setText(R.id.task,"任务状态：已完成");
                }else if (item.getRWZT().equals("3")){
                    //已处理
                    helper.setText(R.id.task,"任务状态：已处理");
                }
             helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     //跳转到任务详情
                     Intent intent=new Intent(getActivity(),TaskDetailActivity.class);
                     intent.putExtra("rwzt",item.getRWZT());//向详情页面传递任务状态
                     intent.putExtra("taskId",item.getID());//id
                     startActivity(intent);
                 }
             });
            }
        };
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
                            initnetdata(page);
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
    @OnClick({R.id.layout_back, R.id.noshow, R.id.recyclerView, R.id.swipregresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                break;
            case R.id.noshow:
                break;
            case R.id.recyclerView:
                break;
            case R.id.swipregresh:
                break;
        }
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);
    }
}
