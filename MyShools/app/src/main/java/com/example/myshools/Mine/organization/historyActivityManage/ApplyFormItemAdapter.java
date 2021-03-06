package com.example.myshools.Mine.organization.historyActivityManage;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myshools.Activity.entity.MyListView;
import com.example.myshools.Mine.organization.historyActivityManage.entity.ApplyForm;
import com.example.myshools.Mine.organization.historyActivityManage.entity.SimpleUserData;
import com.example.myshools.R;
import com.example.myshools.entity.ApplyManage;
import com.example.myshools.entity.ApplyQuestion;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.DeleteRestful;
import com.example.myshools.entity.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApplyFormItemAdapter extends BaseAdapter {
    private List<ApplyManage> dataSource=new ArrayList<>();
    private int resId;
    private Context context;
   private List<ApplyQuestion> qAndAnswer=new ArrayList<>();
   private ApplyQuestionAnswerListAdapter answerListAdapter;


    public ApplyFormItemAdapter(Context context, List<ApplyManage> dataSource1, int resId){

        this.dataSource=dataSource1;
        this.context=context;
        this.resId=resId;
    }

    @Override
    public int getCount() {

        return dataSource.size();
    }

    @Override
    public ApplyManage getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataSource.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ApplyManage applyForm=dataSource.get(position);
        Student userData=applyForm.getStudent();




        Gson gson=new Gson();
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resId, null);
            holder=new ViewHolder();
            holder.apply_stu_name=view.findViewById(R.id.apply_stu_name);
            holder.apply_stu_college=view.findViewById(R.id.apply_stu_college);
            holder.apply_stu_number=view.findViewById(R.id.apply_stu_number);
            holder.apply_time_day=view.findViewById(R.id.apply_time_day);
            holder.apply_time_time=view.findViewById(R.id.apply_time_time);
            holder.apply_information_question_answer_list=view.findViewById(R.id.apply_information_question_answer_list);
            holder.apply_show_all=view.findViewById(R.id.apply_show_all);
            holder.apply_no_show_all=view.findViewById(R.id.apply_no_show_all);
            holder.apply_to_no=view.findViewById(R.id.apply_to_no);
            holder.apply_to_ok=view.findViewById(R.id.apply_to_ok);
            view.setTag(holder);
        } else {
            holder= (ViewHolder) view.getTag();
        }
        /*???????????????????????????*/
        holder.apply_stu_name.setText(userData.getStuName());
        holder.apply_stu_number.setText(userData.getStuNumber());
        holder.apply_stu_college.setText(userData.getStuCollege());
        /*????????????*/
        String time = applyForm.getApplyTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//??????????????????
        String currentTime = df.format(new Date());
        String a_day = time.substring(0, 8);
        int d = Integer.parseInt(a_day);
        String c_day = currentTime.substring(0, 8);
        int c = Integer.parseInt(c_day);
        if (c==d) {//???????????????????????????
            holder.apply_time_day.setText("??????");
        } else if (c - 1 == d) {
            holder.apply_time_day.setText("??????");
        } else if (c - 2 == d) {
            holder.apply_time_day.setText("??????");
        } else {
            holder.apply_time_day.setText(a_day.substring(0, 4) + "." + a_day.substring(4, 6) + "." + a_day.substring(6, 8));
        }
        holder.apply_time_time.setText(time.substring(8, 10) + ":" + time.substring(10, 12));



        /*
        * ???????????????
        * ???????????????????????????
        * */
        holder.apply_show_all.setVisibility(View.GONE);
        holder.apply_no_show_all.setVisibility(View.GONE);

        /*??????????????????????????????
        *
        * */

        String questionan=applyForm.getQuestionanswer();
        if(questionan!=null&&questionan.length()>0){
            qAndAnswer=gson.fromJson(questionan,new TypeToken<List<ApplyQuestion>>(){}.getType());
        }
        if(qAndAnswer.size()>0){

            if(qAndAnswer.size()>1){//???????????? ?????? ??????
                holder.apply_show_all.setVisibility(View.VISIBLE);
            }
            qAndAnswer=qAndAnswer.subList(0,1);//??????????????????
            answerListAdapter=new ApplyQuestionAnswerListAdapter(qAndAnswer,context,R.layout.apply_question_answer_item);
            holder.apply_information_question_answer_list.setAdapter(answerListAdapter);
            answerListAdapter.notifyDataSetChanged();
        }
        //????????????????????????
        holder.apply_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qAndAnswer=gson.fromJson(questionan,new TypeToken<List<ApplyQuestion>>(){}.getType());
                answerListAdapter=new ApplyQuestionAnswerListAdapter(qAndAnswer,context,R.layout.apply_question_answer_item);
                holder.apply_information_question_answer_list.setAdapter(answerListAdapter);

                holder.apply_show_all.setVisibility(View.GONE);
                holder.apply_no_show_all.setVisibility(View.VISIBLE);

                answerListAdapter.notifyDataSetChanged();
            }
        });
        holder.apply_no_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qAndAnswer=qAndAnswer.subList(0,1);//??????????????????
                answerListAdapter=new ApplyQuestionAnswerListAdapter(qAndAnswer,context,R.layout.apply_question_answer_item);
                holder.apply_information_question_answer_list.setAdapter(answerListAdapter);
                holder.apply_no_show_all.setVisibility(View.GONE);
                holder.apply_show_all.setVisibility(View.VISIBLE);
                answerListAdapter.notifyDataSetChanged();

            }
        });
        if(applyForm.getApplyResult()==4){
            holder.apply_to_no.setEnabled(false);
            holder.apply_to_ok.setEnabled(false);
            holder.apply_to_no.setBackgroundColor(Color.parseColor("#95d4f6"));
            holder.apply_to_no.setTextColor(Color.parseColor("#ffffff"));
            holder.apply_to_no.setText("?????????");

        }
        else if(applyForm.getApplyResult()==2){
            holder.apply_to_no.setEnabled(false);
            holder.apply_to_ok.setEnabled(false);
            holder.apply_to_ok.setBackgroundColor(Color.parseColor("#95d4f6"));
            holder.apply_to_ok.setTextColor(Color.parseColor("#ffffff"));
            holder.apply_to_ok.setText("?????????");
        }else if(applyForm.getApplyResult()==-1){
            holder.apply_to_no.setVisibility(View.GONE);
            holder.apply_to_ok.setVisibility(View.GONE);
        }else if(applyForm.getApplyResult()==-2){
            holder.apply_to_no.setEnabled(true);
            holder.apply_to_ok.setEnabled(true);
            holder.apply_to_no.setText("??????");
            holder.apply_to_ok.setText("??????");
        }
        else {
            holder.apply_to_no.setEnabled(true);
            holder.apply_to_ok.setEnabled(true);
        }


/*
*  private int status;//??????????????????1?????????1????????????0????????????3??????????????????
    private int who;//??????????????????0:?????????1?????????????????????2 ??????????????????*/
        //????????????
        holder.apply_to_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.apply_to_no.setEnabled(false);
                holder.apply_to_ok.setEnabled(false);
                holder.apply_to_no.setText("?????????");
                sendUserApplyStatus(position,applyForm.getaId(),applyForm.getuId(),4);
            }
        });
        /*
*  private int status;//??????????????????1?????????1????????????0????????????3??????????????????
    private int who;//??????????????????0:?????????1?????????????????????2 ??????????????????*/
        //????????????
        holder.apply_to_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.apply_to_no.setEnabled(false);
                holder.apply_to_ok.setEnabled(false);
                holder.apply_to_ok.setText("?????????");


                sendUserApplyStatus(position,applyForm.getaId(),applyForm.getuId(),2);

            }
        });










        return view;
    }

    //??????????????????
    private void sendUserApplyStatus(int position, int aId,int uId,int status) {

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body=new FormBody.Builder().add("aId", String.valueOf(aId))
                .add("status", String.valueOf(status))
                .add("uId",String.valueOf(uId))
                .build();
        final Request request = new
                Request.Builder().url(Constant.url + "apply/setApplyStatus").post(body).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dataSource.get(position).setApplyResult(-2);//???????????????????????????????????????????????????
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                DeleteRestful deleteRestful=new Gson().fromJson(string,DeleteRestful.class);
                Message message=new Message();
                if(deleteRestful.getCode()==2000){//??????
                    message.what=2;
                    if(status==2){
                        dataSource.get(position).setApplyResult(2);
                    }if(status==4){
                        dataSource.get(position).setApplyResult(4);
                    }
                }else{//??????
                            message.what=4;
                            dataSource.get(position).setApplyResult(-2);//???????????????????????????????????????????????????
                }
                message.obj=position;
                handler.sendMessage(message);
            }
        });




    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if(msg.what==1){
                Toast toast4 = Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
            }
            if(msg.what==2){
                //??????eventbus?????????????????????????????????
                EventBus.getDefault().post("Ok");
                Log.e("EventBus","????????????");
                notifyDataSetChanged();

            }
            if(msg.what==3){//???????????????????????????????????????
                int position= Integer.parseInt(msg.obj.toString());
                Toast toast4 = Toast.makeText(context, dataSource.get(position).getStudent().getStuName()+" ?????????????????????????????????", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
                notifyDataSetChanged();

            }
            if(msg.what==4){
                int position= Integer.parseInt(msg.obj.toString());
                Toast toast4 = Toast.makeText(context, "??????????????????????????????"+dataSource.get(position).getStudent().getStuName()+"?????????", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
                notifyDataSetChanged();
            }
        }
    };


    public class ViewHolder{
        private TextView apply_stu_name ;
       private TextView apply_stu_college;
       private TextView apply_stu_number;
       private TextView apply_time_day;
       private TextView apply_time_time;
       private TextView apply_show_all;
       private TextView apply_no_show_all;
       private Button apply_to_no;
       private Button apply_to_ok;
       private MyListView apply_information_question_answer_list;
   }
}
