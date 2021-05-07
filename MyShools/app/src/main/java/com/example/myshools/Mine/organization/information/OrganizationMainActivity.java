package com.example.myshools.Mine.organization.information;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.Activity.entity.NoScrollListView;
import com.example.myshools.Mine.organization.model.HistotryManageModel;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.Orgnaization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;


public class OrganizationMainActivity extends AppCompatActivity implements HistotryManageModel.getAllOAStatus{
    private ImageView btn_return_main;
    private ImageView organ_head_img;
    private TextView club_name;
    private TextView club_sum;
    private TextView text_jie_shao;
    private TextView content;
    private NoScrollListView lishi;
    private Orgnaization orgnat;
    private List<Activities> list;
    private HistotryManageModel histotryManageModel;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1){
                InformationHistoryItemAdapter historyItemAdapter=new InformationHistoryItemAdapter(OrganizationMainActivity.this,list,R.layout.lishi_item);
                lishi.setAdapter(historyItemAdapter);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_main);
        String s=getIntent().getStringExtra("organt");
        Gson gson=new GsonBuilder().serializeNulls().create();
        orgnat=gson.fromJson(s,Orgnaization.class);
        findHistory();
        findView();
        setAdapter();
        setView();
            histotryManageModel=new HistotryManageModel();


    }

    private void findHistory() {
        histotryManageModel.doGetAllActivityByOId(this,orgnat.getId());


    }



    private void setView() {
        //头像
        RequestOptions requestOptions=new RequestOptions().centerCrop().circleCrop();
        Glide.with(this).load(Constant.imgUrl+orgnat.getHeadPath()).apply(requestOptions).into(organ_head_img);
        //club名字
        club_name.setText(orgnat.getName());
        //点击量：
        club_sum.setText(orgnat.getPopularity()+"");
        //简介
        text_jie_shao.setText(orgnat.getRecommend());
        //联系方式文字
        content.setText(orgnat.getConnectionContent());
    }

    private void setAdapter() {
    MyListener  my=new MyListener();
    btn_return_main.setOnClickListener(my);

    }


    private void findView() {
        list=new ArrayList<Activities>();
        btn_return_main=findViewById(R.id.btn_return_main);
        organ_head_img=findViewById(R.id.organ_head_img);

        club_name=findViewById(R.id.club_name);
        club_sum=findViewById(R.id.club_sum);
        text_jie_shao=findViewById(R.id.text_jie_shao);
        content=findViewById(R.id.content_lian);
        lishi=findViewById(R.id.lishi_item);
    }

    @Override
    public void getAllOAFailed(int i) {

    }

    @Override
    public void getAllOASuccess(@NotNull List<Activities> list) {
        Message message=new Message();
        message.what=1;
        this.list=list;
        handler.sendMessage(message);
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_return_main:
                    finish();
                    break;
            }
        }
    }
}
