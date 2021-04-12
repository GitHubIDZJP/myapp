package com.example.myapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.R;
import com.example.myapp.activity.LoginActivity;
import com.example.myapp.adapter.HomeAdapter;
import com.example.myapp.api.Api;
import com.example.myapp.api.ApiConfig;
import com.example.myapp.api.TtitCallback;
import com.example.myapp.entity.CategoryEnity;
import com.example.myapp.entity.VideoCategoryResponse;
import com.example.myapp.entity.VideoEntity;
import com.example.myapp.entity.VideoListResponse;
import com.example.myapp.util.StringUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends BaseFragment {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private  String[] mTitles ;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        viewPager = mRootView.findViewById(R.id.fixedViewPager);
        slidingTabLayout = mRootView.findViewById(R.id.slidingTabLayout);
    }

    @Override
    protected void initData() {
        getVideoCatagoryList();
    }

    private  void getVideoCatagoryList(){
        String token = getStringFromSp("token");
        if(!StringUtils.isEmpty(token)){
            HashMap<String,Object> params= new HashMap<>();
            params.put("token",token);
            Api.config(ApiConfig.VIDEO_CATEGORY_LIST,params).getRequest(new TtitCallback() {
                @Override
                public void onSuccess(String res) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VideoCategoryResponse response = new Gson().fromJson(res,VideoCategoryResponse.class);

                            if (response != null && response.getCode() == 0){
                                List<CategoryEnity> list = response.getPage().getList();
                                if (list!=null&&list.size() > 0 ){
                                       mTitles = new  String[list.size()];
                                       for (int i=0;i<list.size();i++){
                                           mTitles[i] = list.get(i).getCategoryName();

                                       }
                                    viewPager.setOffscreenPageLimit(mFragments.size());
                                    viewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
                                    slidingTabLayout.setViewPager(viewPager);
                                }
                            }
                        }
                    });


                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }else {
            navigateTo(LoginActivity.class);
        }
    }
}