package com.example.myshools.Activity.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.Activity.Adapter.CommentAdapter;
import com.example.myshools.Activity.Adapter.GridViewItemImgAdapter;
import com.example.myshools.Activity.Model.CommentsModel;
import com.example.myshools.Activity.ResultComment;
import com.example.myshools.Activity.entity.NoScrollGridView;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.ClickEventMessage;
import com.example.myshools.entity.Comment;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.Orgnaization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*
* 活动详情页面
* */
public class ActivityDetail extends AppCompatActivity implements CommentsModel.getCommentStatus,CommentsModel.sendCommentsStatus {
    private ImageView headImg;
    private TextView name;
    private TextView lev;
    private TextView upDay;
    private TextView upTime;
    private TextView content;
    private NoScrollGridView gridView;
    private TextView list_biaoqian;
    private TextView num_views;
    private LinearLayout content_main;
    private TextView to_finish;
    private RecyclerView comments;
    private Activities activities;
    private CommentAdapter commentAdapter;
    private CommentsModel commentsModel=new CommentsModel();
    private List<ResultComment> commentList;
    private TextView sentComment;
    private Button Send;
    private Comment comment;
    private int role=-1;
    private int userId=0;
    private RequestOptions requestOptions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#00CCFF"));
        }
        setContentView(R.layout.activity_detail);
        String  phone=getSharedPreferences("nextAccount",MODE_PRIVATE).getString("account","");
        if(phone.length()==11){
            role=getSharedPreferences(phone,MODE_PRIVATE).getInt("role",-1);
        }
        findView();

        setView();
        EventBus.getDefault().register(this);
    }
    private void findView() {
        headImg=findViewById(R.id.img_header);
        name=findViewById(R.id.name);
        lev=findViewById(R.id.lev);
        upDay=findViewById(R.id.up_day);
       upTime=findViewById(R.id.up_time);
        gridView=findViewById(R.id.grid_view);
        content=findViewById(R.id.content_text);
        list_biaoqian=findViewById(R.id.list_biao_qian);
        num_views=findViewById(R.id.num_liulan);
        content_main=findViewById(R.id.content_main);
        to_finish=findViewById(R.id.btn_return_setting);
        comments=findViewById(R.id.comment_recycle);
        sentComment=findViewById(R.id.to_comment);
        Send=findViewById(R.id.share_send_comment);
    }
    private void setView() {
        String userPhone = getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account","");
                SharedPreferences userSharedPreferences=getSharedPreferences(userPhone, Context.MODE_PRIVATE);
        role=userSharedPreferences.getInt("role",-1);
        userId=userSharedPreferences.getInt("id",-1);
        commentList=new ArrayList<>();
        to_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        requestOptions = new RequestOptions().placeholder(R.drawable.loading).fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop();


        Gson gson = new GsonBuilder().serializeNulls().create();
        String str = getIntent().getStringExtra("activity");
        activities = gson.fromJson(str, Activities.class);
        String time = activities.getUpTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        String currentTime = df.format(new Date());
        String a_day = time.substring(0, 8);
        int d = Integer.parseInt(a_day);

        String c_day = currentTime.substring(0, 8);
        int c = Integer.parseInt(c_day);
        if (a_day.equals(c_day)) {//表示是当天发的活动
            upDay.setText("今天");
        } else if (c - 1 == d) {
            upDay.setText("昨天");
        } else if (c - 2 == d) {
            upDay.setText("前天");
        } else {
            upDay.setText(a_day.substring(0, 4) + "." + a_day.substring(4, 6) + "." + a_day.substring(6, 8));
        }
        upTime.setText(time.substring(8, 10) + ":" + time.substring(10, 12));
        //发布者信息
        Orgnaization orgnat = activities.getOrganization();
        Glide.with(this).load(Constant.imgUrl+orgnat.getHeadPath()).apply(requestOptions).into(headImg);
        name.setText(orgnat.getName());
        if (activities.getImgs()!=null&&activities.getImgs().length()>0){
            String[] imgList=activities.getImgs().split(",");
            List<String> stringList=new ArrayList<String>();
            for (String string:imgList) {
                stringList.add(string);
            }
            if(imgList.length>0) {
                GridViewItemImgAdapter gridViewItemImgAdapter = new GridViewItemImgAdapter(this, stringList, R.layout.grid_view_item);
                gridView.setAdapter(gridViewItemImgAdapter);
            }
        }

        lev.setText(orgnat.getLevel());
        content.setText(activities.getContent());
        //标签
        String[] strings=activities.getLabel().split(",");
        String s="";
        for(int i=0;i<strings.length;i++){
            s=s+strings[i]+"\u3000";
        }
        list_biaoqian.setText(s);
        //点击量
        num_views.setText("点击量 "+activities.getViewNum());

        commentAdapter=new CommentAdapter(commentList,this);
        comments.setLayoutManager(new LinearLayoutManager(this));
        comments.setAdapter(commentAdapter);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sentComment.getText().toString().length()>0){
                    comment=new Comment();
                    comment.setContent(sentComment.getText().toString());
                    comment.setNewsId(activities.getId());
                    comment.setParentId(-1);
                    comment.setStatus(0);
                    comment.setUserId(userId);
                    comment.setType(role);
                    comment.setId(-1);
                    SimpleDateFormat s=new SimpleDateFormat("yyyyMMddHHmm");
                    String  upTime = df.format(new Date());
                    comment.setCreateTime(upTime);
                    Message message=new Message();
                    message.what=5;
                    handler.sendMessage(message);
                }else {
                    Message message=new Message();
                    message.what=6;
                    handler.sendMessage(message);

                }
            }
        });
        initComments();

    }

    private void initComments() {
        if(activities!=null){
            commentsModel.getCommentsByAId(this,activities.getId());
        }
    }

    @Override
    public void getCommentFailed(int i) {
        Log.e("TAG",i+"sss");
    }

    @Override
    public void getCommentSuccess(@NotNull List<ResultComment> list) {
        commentList.clear();
        for(ResultComment resultComment:list){
            commentList.add(resultComment);
        }
        Message message=new Message();
        message.what=1;
        handler.sendMessage(message);
    }

    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
              commentAdapter.notifyDataSetChanged();
                Log.e("TAG",commentList.toString()+"sss");
            }
            if(msg.what==2){
                initComments();
            }
            if (msg.what==5){
              senData();
            }if(msg.what==6){

            }
        }
    };
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void setType(Boolean update){
     if (update){
         Message message=new Message();
         message.what=2;
         handler.sendMessage(message);
     }
    }
    private void senData(){
        commentsModel.sendComment(this,comment,2);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void sendCommendsFailed(int i) {

    }

    @Override
    public void sendCommendSuccess(int position) {
        Message message=new Message();
        message.what=2;
        handler.sendMessage(message);
    }
}
