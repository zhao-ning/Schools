package com.example.myshools.Mine.organization.publish;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myshools.Mine.organization.historyActivityManage.entity.ApplyForm;
import com.example.myshools.R;
import com.example.myshools.entity.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IdentificationResult extends AppCompatActivity {
    private int status;
    private int id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identification_result);

        id=getSharedPreferences("user",MODE_PRIVATE).getInt("id",-1);
        int who=getIntent().getIntExtra("who",-3);

        if(who==666){//从我的页面点击进来的
            getStatus(id);

        }else {//从编辑页面转过来的
            status=getIntent().getIntExtra("status",-1);
        }
        TextView textView=findViewById(R.id.ren_zh_status);
        Button button=findViewById(R.id.btn_return_s);
        //返回按钮
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(status==0){
            textView.setText("进度：未认证");
        }
        if(status==1){
            textView.setText("进度：已认证");
        }
        if(status==2){
            textView.setText("进度：正在认证...");

        }if(status==3){
            textView.setText("进度：认证失败!");
        }

        if(status==-1) {
            textView.setText("进度：???请返回重新点开");
        }


    }

    private void getStatus(int id) {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("oId", String.valueOf(id)).build();
        final Request request = new
                Request.Builder().url(Constant.url + "activities/findApplyInformationByAId").post(body).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 1;//网络连接失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                List<ApplyForm> list = gson.fromJson(string, new TypeToken<List<ApplyForm>>() {
                }.getType());

            }
        });



    }
}
