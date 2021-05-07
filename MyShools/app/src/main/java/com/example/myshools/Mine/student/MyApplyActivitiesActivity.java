package com.example.myshools.Mine.student;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myshools.Mine.organization.historyActivityManage.HistoryItemAdapter;
import com.example.myshools.Mine.organization.model.HistotryManageModel;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.GetStudentApplyActivitiies;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

/*
* 我的活动管理页
* */
public class MyApplyActivitiesActivity extends AppCompatActivity implements HistotryManageModel.getStuApplyStatus {
    private TextView returnToMine;
    private ListView listView;
    private OkHttpClient okHttpClient;
    private List<GetStudentApplyActivitiies> AcList;
    private int num=1;//当前页数
    private int oId;
    private HistotryManageModel manageModel;
    private int onLoad=0;//是否是上拉加载
    private int position=0;
    private String user_phone;
    private MyApplyActivityItemAdapter itemAdapter;
    private RefreshLayout refreshLayout;//刷新加载
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#00CCFF"));
        }
        setContentView(R.layout.my_apply);
        AcList=new ArrayList<GetStudentApplyActivitiies>();
        manageModel=new HistotryManageModel();
        user_phone=getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account","");
        oId=getSharedPreferences(user_phone, Context.MODE_PRIVATE).getInt("id",-1);
        //获取数据

        listView=findViewById(R.id.history_list);
        itemAdapter=new MyApplyActivityItemAdapter(MyApplyActivitiesActivity.this,AcList,R.layout.my_apply_activity_item);
        listView.setAdapter(itemAdapter);
        initData();
        returnToMine=findViewById(R.id.activity_manage_back);
        refreshLayout= findViewById(R.id.history_refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(MyApplyActivitiesActivity.this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                onLoad=0;
                num=1;
                initData();
                refreshlayout.finishRefresh(2000);///*,false*/传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                onLoad=1;
                num=num+1;
                initData();
                refreshlayout.finishLoadMore(2000);
            }
        });
        returnToMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initData() {
        manageModel.doGetAllActivityBySId(this,oId);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Toast toast4 = Toast.makeText(MyApplyActivitiesActivity.this, "网络连接失败", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
            }
            if(msg.what==2){

                itemAdapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    public void getSAllOAFailed(int i) {

    }


    @Override
    public void getSAllOASuccess(@NotNull List<GetStudentApplyActivitiies> list) {
        this.AcList.clear();
        for (GetStudentApplyActivitiies activities:list){
            AcList.add(activities);
        }
        Message message=new Message();
        message.what=2;
        handler.sendMessage(message);
    }
}
