package com.example.myapp.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.example.myapp.R;
import com.example.myapp.activity.LoginActivity;
import com.example.myapp.adapter.VideoAdapter;
import com.example.myapp.api.Api;
import com.example.myapp.api.ApiConfig;
import com.example.myapp.api.TtitCallback;
import com.example.myapp.entity.VideoEntity;
import com.example.myapp.entity.VideoListResponse;
import com.example.myapp.listener.OnItemChildClickListener;
import com.example.myapp.util.StringUtils;
import com.example.myapp.util.Tag;
import com.example.myapp.util.Utils;
import com.google.gson.Gson;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.BezierRadarHeader;
import com.scwang.smart.refresh.header.radar.*;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VideoFragment extends BaseFragment implements OnItemChildClickListener {
    private String title;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;//加载动画
    private int pageNum = 1;
    private VideoAdapter videoAdapter;
    private  List<VideoEntity> datas = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    public VideoFragment(){

    }
    public static VideoFragment newInstance(String title){
        VideoFragment fragment = new VideoFragment();
        fragment.title=title;
        return fragment;

    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        initVideoView();
        recyclerView=mRootView.findViewById(R.id.recyclerview);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        linearLayoutManager =new LinearLayoutManager(getActivity());
        //item设置为垂直方向
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(getActivity());
        videoAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(videoAdapter);

        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                pageNum=1;
                getVideoList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                pageNum++;
                getVideoList(false);
            }
        });
        getVideoList(true);
    }


    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new com.dueeeke.videoplayer.player.VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == com.dueeeke.videoplayer.player.VideoView.STATE_IDLE) {
                    Utils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;

        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    /**
     * PrepareView被点击
     */
    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * 开始播放
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoEntity videoEntity = datas.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
        mCurPos = position;

    }

    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if(getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                              Bundle savedInstanceState){
//        View v=inflater.inflate(R.layout.fragment_video,container,false);
//        recyclerView=v.findViewById(R.id.recyclerview);
//        refreshLayout = v.findViewById(R.id.refreshLayout);
//        refreshLayout.setRefreshHeader(new BezierRadarHeader(v.getContext()).setEnableHorizontalDrag(true));
//        refreshLayout.setRefreshFooter(new BallPulseFooter(v.getContext()).setSpinnerStyle(SpinnerStyle.Scale));        return  v;
//    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
//        //item设置为垂直方向
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        videoAdapter = new VideoAdapter(getActivity());
//        recyclerView.setAdapter(videoAdapter);
//
//
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
////                refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
//                pageNum=1;
//                getVideoList(true);
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
//                pageNum++;
//                getVideoList(false);
//            }
//        });
//        getVideoList(true);
//    }



    private void getVideoList(boolean isRefresht){
        String token = getStringFromSp("token");
        if(!StringUtils.isEmpty(token)){
            HashMap<String,Object> params= new HashMap<>();
            params.put("token",token);
            params.put("page",pageNum);
            params.put("limit",ApiConfig.PAGE_SIZE);
            Api.config(ApiConfig.VIDEO_LIST_BY_CATEGORY,params).getRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRefresht){
                                refreshLayout.finishRefresh(true);
                            }else {
                                refreshLayout.finishLoadMore(true);
                            }
                            VideoListResponse response = new Gson().fromJson(res,VideoListResponse.class);
                            if (response != null && response.getCode() == 0){
                                List<VideoEntity> list = response.getPage().getList();
                                if (list!=null&&list.size() > 0 ){
                                    if (isRefresht){
                                        datas=list;
                                    }
                                    else {
                                        datas.addAll(list);
                                    }
                                    videoAdapter.setDatas(datas);
                                    videoAdapter.notifyDataSetChanged();
                                }else {
                                    if (isRefresht) {
                                        showToast("暂时加载无数据");
                                    } else {
                                        showToast("没有更多数据");
                                    }
                                }

                            }
                        }
                    });


                }

                @Override
                public void onFailure(Exception e) {
                    if (isRefresht){
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
