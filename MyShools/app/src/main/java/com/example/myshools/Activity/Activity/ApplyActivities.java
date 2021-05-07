package com.example.myshools.Activity.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myshools.Activity.Model.ApplyModel;
import com.example.myshools.Mine.organization.publish.ApplyQuestionListAdapter;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.ApplyQuestion;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.EventMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
* 申请页面
* */
public class ApplyActivities extends AppCompatActivity implements ApplyModel.doSendApplyDataStatus,ApplyModel.doCancelZhanWeiStatus{
    private Button no;
    private TextView ok;
    private int aId,position;
    private int userId;
    private int appStatus=3;//申请状态：-1是未申请过，1已提交申请，3未提交，（2是申请成功，4是申请失败，由举办方设置）
    private List<String> questionList=new ArrayList<>();
    private List<ApplyQuestion> applyQuestionList=new ArrayList<>();
    private ListView listView;
    private TextView tip;//顶部提示
    private SharedPreferences ApplyQuestionsharedPref;//问卷
    private boolean isHaveQuestion;//是否包含问卷
    private Boolean isOk;//是否回答了所有的问题
    private String applyQuestionAndAnswer="";//提交的问卷
    private TextView dao_ji_shi;
    private Boolean isFinish=true;//用来判断是否取消占位，当退出页面时取消占位，提交的时候： isfinish=false
    private CountDownTimer timer;
    private Boolean isNormal=false;//判断页面是否是按照返回，确定正常退出的,如果是点击事件时设置为true
    private Boolean isEventBus=false;//判断是否向adapter发送过数据
    private Boolean timerIsStop=false;//判断计时器是否结束，尚未结束就结束掉timer
    private Boolean noCancelApply=false;//判断是否删除申请表中的数据
    private ApplyModel applyModel;
    private String question;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_activities);
        isOk=true;
        applyModel=new ApplyModel();
        daojishi();
        //获取活动信息
        aId=getIntent().getIntExtra("aId",-1);
        userId=getIntent().getIntExtra("userId",-1);
        position=getIntent().getIntExtra("position",-1);
        question=getIntent().getStringExtra("question");
        if(aId==-1||userId==-1){
            finish();
        }

       if(aId!=0&& position!=-1){
           initView();//获取问题数据
           findView();
           setAdapter();
       }
    }
    private void daojishi() {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
                dao_ji_shi.setText("倒计时："+millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                        finish();

            }
        };
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start();
    }
    //获取问题列表
    private void initView() {
        if(question!=null&&question.length()>0){
            String[] strings=question.split(",");
            isHaveQuestion=true;
            for(String s:strings){
                ApplyQuestion applyQuestion=new ApplyQuestion();
                applyQuestion.setQuestion(s);
                applyQuestion.setAnswer("");
                applyQuestionList.add(applyQuestion);
            }
            Message message=new Message();
            message.what=1;
            handler.sendMessage(message);
        }else {
            isHaveQuestion=false;
            Message message=new Message();
            message.what=2;
            handler.sendMessage(message);
        }
    }
    public void quXiaoZhanWei(int aId) {
            applyModel.doCancelZhanWei(this,aId);
    }
    private void setAdapter() {
        MyListener2 my=new MyListener2();
        no.setOnClickListener(my);
        ok.setOnClickListener(my);
    }
    private void findView() {
        no=findViewById(R.id.return0);
        ok=findViewById(R.id.apply_o);
        listView=findViewById(R.id.apply_question_list);
        tip=findViewById(R.id.apply_question_tip);
        dao_ji_shi=findViewById(R.id.dao_ji_shi);
    }

    //开始提交。。。
    @Override
    public void sendingApplyData() {

    }

    //提交失败
    @Override
    public void sendApplyDataFailed(int i) {
        Message message=new Message();
        message.what=100;
        handler.sendMessage(message);
    }

    //提交成功
    @Override
    public void sendApplyDataSuccess(int i) {
        if(i==1){
            Message message=new Message();
            message.what=3;
            handler.sendMessage(message);
        }
    }

    //取消占位
    @Override
    public void cancelSetZhanwei() {
    }
    //取消占位失败
    @Override
    public void cancelZhanweiFailed(int i) {
            Message message = new Message();
            message.what = 71;
            handler.sendMessage(message);
    }
    //取消占位成功
    @Override
    public void cancelZhanweiSuccess(int i) {
        if(i==1){
            isFinish=false;
        }else {
            finish();
        }
    }

    private class MyListener2 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.return0:
                    appStatus=0;
                    isNormal=true;//正常退出
                    finish();
                    break;
                case R.id.apply_o:
                    //提交申请
                    ApplyQuestionsharedPref=getSharedPreferences("ApplyQuestion",
                            MODE_PRIVATE);
                    String s=ApplyQuestionsharedPref.getString("data","");
                    applyQuestionAndAnswer=s;
                    if(ApplyQuestionsharedPref!=null&&s!=null&& !s.equals("")){
                        Gson gson=new Gson();
                        List<ApplyQuestion> list=gson.fromJson(s,new TypeToken<List<ApplyQuestion>>(){}.getType());
                      for(ApplyQuestion applyQuestion:list){//验证是否回答了所有的问题
                          if(applyQuestion.getAnswer()==null || applyQuestion.getAnswer().equals("")){
                              isOk=false;
                          }
                      }
                      if(isOk==true){//回答了所有的问题
                          //向后台服务器提交申请

                          submitApply();
                      }else {
                          Message message=new Message();
                          message.what=5;
                          handler.sendMessage(message);
                      }

                    }else {
                        //向后台服务器提交申请
                        submitApply();
                    }
                    break;
            }

        }
    }

    private void submitApply() {
        SimpleDateFormat df =new SimpleDateFormat("yyyyMMddHHmm"); //设置日期格式
        String currentTime = df.format(new Date());
        applyModel.doSendApplyData(this,aId,userId,applyQuestionAndAnswer,currentTime);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                ApplyQuestionListAdapter questionListAdapter=new ApplyQuestionListAdapter(applyQuestionList,ApplyActivities.this,R.layout.apply_question_item);
                listView.setAdapter(questionListAdapter);
                tip.setText("请在十分钟内填写完毕，否则无法提交申请");
            }if(msg.what==2){
                tip.setText("请直接提交申请，超时则无法提交申请");
            }
            if(msg.what==3){
                //
                isFinish=false;//提交
                noCancelApply=true;//关闭取消申请开关
                appStatus=1;
                //清除share
                Toast toast3 = Toast.makeText(ApplyActivities.this, "提交成功", Toast.LENGTH_SHORT);
                toast3.setGravity(Gravity.CENTER, 0, 0);
                toast3.show();
                isNormal=true;//正常退出
                finish();
            }
            if(msg.what==5){//部分问题没有回答
                isOk=true;
                Toast toast3 = Toast.makeText(ApplyActivities.this, "所有问题回答完毕，才能提交", Toast.LENGTH_SHORT);
                toast3.setGravity(Gravity.CENTER, 0, 0);
                toast3.show();
            }
        }
    };

    @Override
    public void finish() {
        super.finish();
       if(isNormal==false){//非正常退出
           appStatus=0;
       }
        if(isFinish==true){
            Log.e("取消占位","quxiaozhanwei");
            quXiaoZhanWei(aId);
        }
        /*if(noCancelApply==false){//取消之前的申请操作
            cancelApply(aId,userId);

        }*/
        if(timerIsStop==false){
            Log.e("关闭计时器","guanbijishiqi");
            timer.cancel();
            timerIsStop=true;
        }
        if(isEventBus==false){
            EventMessage msg = new EventMessage(position,appStatus);
            EventBus.getDefault().post(msg);
            isEventBus=true;
        }

    }

    private void cancelApply(int aId, int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("aId", String.valueOf(aId)).add("userId", String.valueOf(userId)).build();
        final Request request = new
                Request.Builder().url(Constant.url + "activities/cancelApplyToJoin").post(body).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finish();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                int re= Integer.parseInt(str);
                if(re==1){
                    noCancelApply=true;
                    appStatus=0;
                }else {
                    finish();
                }
            }
        });
    }
}
