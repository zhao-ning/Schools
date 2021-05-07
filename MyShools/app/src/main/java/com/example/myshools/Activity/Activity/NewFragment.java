package com.example.myshools.Activity.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myshools.Activity.Adapter.ActivitiesRecycleViewItemAdapter;
import com.example.myshools.Login.Model.GetSchoolNewsStatus;
import com.example.myshools.Login.Model.NewsFragmentModel;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class NewFragment extends Fragment implements GetSchoolNewsStatus {
    private RecyclerView recyclerView;
    private List<Activities> AcList = new ArrayList<>();//所有的数据
    private List<Activities> newAcList = new ArrayList<>();//上拉加载的数据
    private int num = 1;
    private int onLoad = -1;//是否是上拉加载
    private ActivitiesRecycleViewItemAdapter itemAdapter;
    private int schoolId;//用户学校id
    private int selectSchoolId;//选择查看的学校id
    private boolean isUserSchoolNews;
    private String type;
    private NewsFragmentModel newsFragmentModel;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (onLoad == 1) {//上拉加载更多
                    for (int i = 0; i < newAcList.size(); i++) {
                        AcList.add(newAcList.get(i));
                    }
                    onLoad = -1;
                    itemAdapter.notifyDataSetChanged();
                } else if (onLoad == 0) {
                    AcList.clear();//下拉刷新
                        for (int i = 0; i < newAcList.size(); i++) {
                            AcList.add(i, newAcList.get(i));
                    }
                    onLoad = -1;
                    itemAdapter.notifyDataSetChanged();
                } else {
                    AcList.clear();
                    for (int i = 0; i < newAcList.size(); i++) {
                        AcList.add(i, newAcList.get(i));
                    }
                    itemAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public String getType() {
        return type;
    }

    public NewFragment() {
    }

    public NewFragment(String s) {
        this.type = s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.new_fragment, container, false);
        return newView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity().getApplicationContext(), type, Toast.LENGTH_SHORT).show();
        newsFragmentModel = new NewsFragmentModel();

        recyclerView = getActivity().findViewById(R.id.list_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        schoolId= getActivity().getSharedPreferences("nowSchool", Context.MODE_PRIVATE).getInt("studentSchoolId",1);
        selectSchoolId=getActivity().getSharedPreferences("nowSchool",Context.MODE_PRIVATE).getInt("nowSchoolId",1);
        if(schoolId==selectSchoolId){
            isUserSchoolNews=true;
        }else {
            isUserSchoolNews=false;
        }
        itemAdapter = new ActivitiesRecycleViewItemAdapter(AcList, getContext(),isUserSchoolNews);
        recyclerView.setAdapter(itemAdapter);
        //获取数据
        newsFragmentModel.doGetSchoolNews(this, num, selectSchoolId, type);
        RefreshLayout refreshLayout = getActivity().findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                onLoad = 0;
                num = 1;
                getNowNews();
                refreshlayout.finishRefresh(2000);///*,false*/传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                onLoad = 1;
                num = num + 1;
                getNowNews();
                refreshlayout.finishLoadMore(2000);///*,false*/传入false表示加载失败
            }
        });
        getNowNews();
    }

    //刷新当前学校数据
    private void getNowNews() {
        //请求该学校的新闻数据
        if (type.equals("最新")) {
            type = "";
        }
        newsFragmentModel.doGetSchoolNews(this, num, selectSchoolId, type);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void doGettingNews() {
        Log.e("TAG", type + "NewFragment正在 获取活动信息");
    }

    @Override
    public void doGetNewsFailed() {
        Log.e("TAG", type + "NewFragment 获取活动信息失败");
    }

    @Override
    public void doGetNewsSucceed(List<Activities> dataList) {
        Log.e("TAG", type + "NewFragment获取活动信息成功");
        Message message = new Message();
        message.what = 1;
        List<Activities> getList=new ArrayList<Activities>();
        getList=dataList;
        if (onLoad==1) {
            if (newAcList.size() > 0 && dataList!=null&&dataList.size()>0) {
                for (int i=0;i<dataList.size();i++) {

                    for (int j=0;j<newAcList.size();j++) {
                         if (dataList.get(i).getId() == newAcList.get(j).getId()) {
                              getList.remove(i);
                         continue;
                    }
                }
            }
            }
        }
        newAcList.clear();
        newAcList = getList;
        handler.sendMessage(message);
    }

    @Override
    public void onResume() {
        super.onResume();
        onLoad=0;
        num=1;
        getNowNews();
    }
}