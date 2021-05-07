package com.example.myshools.Consult;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myshools.Activity.Adapter.ActivitiesRecycleViewItemAdapter;
import com.example.myshools.Consult.Adapter.ShareRecycleViewAdapter;
import com.example.myshools.Consult.Model.RrportModel;
import com.example.myshools.Mine.student.PublishShareActivity;
import com.example.myshools.Mine.student.StudentReport;
import com.example.myshools.Mine.student.model.PublishShareModel;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/*
* 校园圈Fragment
*
* */
public class ConsultFragemnt extends Fragment implements RrportModel.getReportListStatuss {
    private int identity=-1;//用户是否认证
    private int userSchoolID;
    private RecyclerView recyclerView;
    private ImageButton addShare;
    private int page=1;
    private List<StudentReport> reportList=new ArrayList<>();//所有的数据
    private List<StudentReport> newReList = new ArrayList<>();//上拉加载的数据
    private ShareRecycleViewAdapter itemAdapter;
    private RefreshLayout refreshLayout;

    private int onLoad=-1;//上拉加载：1，下拉刷新0
    private RrportModel rrportModel=new RrportModel();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(Color.parseColor("#00CCFF"));
        }
        View newView = inflater.inflate(R.layout.inside_school, container, false);
        return newView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout = getActivity().findViewById(R.id.refreshLayout);
        recyclerView=getActivity().findViewById(R.id.in_school_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ShareRecycleViewAdapter(reportList, getContext());
        recyclerView.setAdapter(itemAdapter);
        //获取用户个人信息
        getUserData();
        setView();
        getNowReport();
    }



    private void setView() {
        addShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(identity==1){
                    Intent intent=new Intent(getActivity(), PublishShareActivity.class);
                    startActivity(intent);
                }else {
                    Message message=new Message();
                    message.what=44;
                    handler.sendMessage(message);
                }

            }
        });

        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                onLoad = 0;
                page = 1;
                getNowReport();
                refreshlayout.finishRefresh(2000);///*,false*/传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                onLoad = 1;
                page = page + 1;
                getNowReport();
                refreshlayout.finishLoadMore(2000);///*,false*/传入false表示加载失败
            }
        });
    }

    private void getNowReport() {
        rrportModel.getStudentREportByShcoolId(this,userSchoolID,page);
    }

    private void getUserData(){
        String s=getActivity().getSharedPreferences("nextAccount", MODE_PRIVATE).getString("account", null);
        identity=getActivity().getSharedPreferences(s,MODE_PRIVATE).getInt("identity",0);
        userSchoolID=getActivity().getSharedPreferences(s,MODE_PRIVATE).getInt("schoolId",1);
        addShare=getActivity().findViewById(R.id.add_share_button);
    }

    @Override
    public void getReportListFailed(int i) {

    }

    @Override
    public void getReportListSuccess(@NotNull List<StudentReport> list) {
        Message message = new Message();
        message.what = 1;
        List<StudentReport> getList=new ArrayList<StudentReport>();
        getList=list;
        if (onLoad==1) {
            if (reportList.size() > 0 && list!=null&&list.size()>0) {
                for (int i=0;i<list.size();i++) {
                    for (int j=0;j<reportList.size();j++) {
                        if (list.get(i).getId() == reportList.get(j).getId()) {
                            getList.remove(i);
                            continue;
                        }
                    }
                }
            }
        }
        newReList.clear();
        newReList = getList;
        handler.sendMessage(message);
    }
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
         if (onLoad == 1) {//上拉加载更多
                    for (int i = 0; i < newReList.size(); i++) {
                        reportList.add(newReList.get(i));
                    }
                    onLoad = -1;
                    itemAdapter.notifyDataSetChanged();
                } else if (onLoad == 0) {
                    reportList.clear();//下拉刷新
                    for (int i = 0; i < newReList.size(); i++) {
                        reportList.add(i, newReList.get(i));
                    }
                    onLoad = -1;
                    itemAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < newReList.size(); i++) {
                        reportList.add(i, newReList.get(i));
                    }
                    itemAdapter.notifyDataSetChanged();
                }

            }else if(msg.what==44){
                Toast toast = Toast.makeText(getContext(), "未完成认证，无法操作", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        onLoad=0;
        page=1;
        getNowReport();
    }
}