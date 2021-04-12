package com.example.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.R;
import com.example.myapp.activity.LoginActivity;
import com.example.myapp.adapter.NewsAdapter;
import com.example.myapp.api.Api;
import com.example.myapp.api.ApiConfig;
import com.example.myapp.api.TtitCallback;
import com.example.myapp.entity.NewsEnity;
import com.example.myapp.entity.NewsListResponse;
import com.example.myapp.entity.VideoEntity;
import com.example.myapp.entity.VideoListResponse;
import com.example.myapp.util.StringUtils;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private NewsAdapter newsAdapter;
    private List<NewsEnity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private int pageNum = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    newsAdapter.setDatas(datas);
                    newsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    public NewsFragment() {

    }



    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerview);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        newsAdapter = new NewsAdapter(getActivity());
        recyclerView.setAdapter(newsAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageNum=1;
                getNewList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getNewList(false);
            }
        });
        getNewList(true);
    }
    private void getNewList(final boolean isRefresh){
        String token = getStringFromSp("token");
        if(!StringUtils.isEmpty(token)){
            HashMap<String,Object> params= new HashMap<>();
            params.put("token",token);
            params.put("page",pageNum);
            params.put("limit", ApiConfig.PAGE_SIZE);
            Api.config(ApiConfig.NEWS_LIST,params).getRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    if (isRefresh){
                        refreshLayout.finishRefresh(true);
                    }else {
                        refreshLayout.finishLoadMore(true);
                    }
                    NewsListResponse response = new Gson().fromJson(res,NewsListResponse.class);
                    if (response != null && response.getCode() == 0){
                        List<NewsEnity> list = response.getPage().getList();
                        if (list!=null&&list.size() > 0 ){
                            if (isRefresh){
                                datas=list;
                            }
                            else {
                                datas.addAll(list);
                            }
                            mHandler.sendEmptyMessage(0);
                        }else {
                            if (isRefresh) {
                                showToastSync("暂时加载无数据");
                            } else {
                                showToastSync("没有更多数据");
                            }
                        }

                    }


                }

                @Override
                public void onFailure(Exception e) {
                    if (isRefresh){
                        refreshLayout.finishRefresh(true);
                    }else {
                        refreshLayout.finishLoadMore(true);
                    }
                }
            });
        }else {
            navigateTo(LoginActivity.class);
        }

    }
}