package com.example.myshools.Mine.organization.publish;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myshools.Mine.QuestionListAdapter;
import com.example.myshools.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* 发布活动时设置问题表单页面
*
*
* */
public class SetQuestion extends AppCompatActivity {
    private ListView listView;
    private List<String> dataSource=new ArrayList<>();
    private Button addQuestion;
    private Button return_to_publish;
    private TextView set_question_ok;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff00CCFF);
        }
        setContentView(R.layout.set_question);

        //如果之前设置过问题，点击进来查看或者修改，需先获取之前的数据

        preferences = getSharedPreferences("questionList",
                MODE_PRIVATE);
        Set<String> stringSet=preferences.getStringSet("dataSource",null);
        if(stringSet.size()!=0){
            for(String str:stringSet){
                dataSource.add(str);
            }
        }
       /* Log.e("点击进来查看数据",dataSource.toString());*/

        findView();
        setOnClickListener();

        QuestionListAdapter questionListAdapter=new QuestionListAdapter(dataSource,SetQuestion.this,R.layout.question_item);
        listView.setAdapter(questionListAdapter);
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.add("");
                questionListAdapter.notifyDataSetChanged();
            }
        });
    }


    private void findView() {
        listView=findViewById(R.id.question_list);
        addQuestion=findViewById(R.id.add_question);
        return_to_publish=findViewById(R.id.btn_return_publish);
        set_question_ok=findViewById(R.id.set_question_ok);

    }
    private void setOnClickListener() {
        MyListener3 my=new MyListener3();
        return_to_publish.setOnClickListener(my);
        set_question_ok.setOnClickListener(my);
    }


    private class MyListener3 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_return_publish:
                    finish();
                    break;
                case R.id.set_question_ok:
                    //保存之前确定是在表单中设置了问题
                    int isNull=0;//是为了查看设置的问题是否都是空问题
                    if(dataSource.size()>0){//设置了问题就进行保存
                        for(String data:dataSource){//首先对所有的问题进行判断，是否都是空白问题
                            if(data==""){
                            }else {
                                isNull=1;
                            }
                        }
                        if(isNull==1){//如果有不是空问题的进行保存
                            //保存dataSource
                            preferences = getSharedPreferences("questionList",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor ed = preferences.edit();
                            Set<String> strings=new HashSet<>();
                            for(String data:dataSource){
                                if(data==""){
                                }else {
                                    strings.add(data);
                                }
                            }
                            ed.putStringSet("dataSource",strings);
                            ed.putInt("update",1);//设置问题是否完成
                            ed.commit();
                            finish();
                        }else {
                            Toast toast7 = Toast.makeText(SetQuestion.this,"问题描述为空，请添加问题描述",Toast.LENGTH_SHORT);
                            toast7.setGravity(Gravity.CENTER, 0, 0);
                            toast7.show();
                        }
                        /*Log.e("保存后显示的数据：",dataSource.toString());*/

                    }else {//没有设置问题就对其进行提示
                        Toast toast7 = Toast.makeText(SetQuestion.this,"您尚未设置问题，不能进行此操作",Toast.LENGTH_SHORT);
                        toast7.setGravity(Gravity.CENTER, 0, 0);
                        toast7.show();
                    }

                    break;


            }

        }
    }
}
