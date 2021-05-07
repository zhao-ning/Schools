package com.example.myshools.Mine.organization.historyActivityManage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.Activity.Adapter.GridViewItemImgAdapter;
import com.example.myshools.Activity.entity.NoScrollGridView;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.DeleteRestful;
import com.example.myshools.entity.PeopleNumManagement;
import com.example.myshools.entity.Restful;
import com.example.myshools.entity.ResultStatus;
import com.google.gson.Gson;

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

public class HistoryItemAdapter extends BaseAdapter {
    private List<Activities> dataSource;
    private int resId;//布局
    private Context context;//上下文
    private Dialog dialog;//管理弹出框
    private Boolean isSetApply;

    public HistoryItemAdapter(){}
    public HistoryItemAdapter(Context context, List<Activities> dataSource, int resId){
        this.dataSource=dataSource;
        this.context=context;
        this.resId=resId;
    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Activities getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataSource.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resId, null);
            holder=new ViewHolder();
            holder.activity_title=view.findViewById(R.id.activity_title);
            holder.activity_manage=view.findViewById(R.id.activity_manage);
            holder.activity_up_day=view.findViewById(R.id.activity_up_day);
            holder.activity_up_time=view.findViewById(R.id.activity_up_time);
            holder.activity_apply_num=view.findViewById(R.id.activity_apply_num);
            holder.activity_applying_num=view.findViewById(R.id.activity_applying_num);
            holder.activity_free_num=view.findViewById(R.id.activity_free_num);
            holder.grid_view=view.findViewById(R.id.grid_view);
            holder.content_text=view.findViewById(R.id.content_text);
            holder.num_liulan=view.findViewById(R.id.num_liulan);
            holder.click_num=view.findViewById(R.id.click_num);
            holder.list_biao_qian=view.findViewById(R.id.list_biao_qian);
            holder.activity_status=view.findViewById(R.id.activity_status);
            view.setTag(holder);
        } else {
            holder= (ViewHolder) view.getTag();
        }
        final Activities activity=dataSource.get(position);
        //活动是否含有申请表，设置 isSetApply 的值
        if(activity.getIsSetJoinNum()==1){
            isSetApply=true;
        }else {
            isSetApply=false;
        }
        //活动标题
        holder.activity_title.setText(activity.getTitle());
        //管理
        holder.activity_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popMenu(view,activity,position);
            }
        });
        //活动发布时间：
        String time = activity.getUpTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        String currentTime = df.format(new Date());
        String a_day = time.substring(0, 8);
        int d = Integer.parseInt(a_day);
        String c_day = currentTime.substring(0, 8);
        int c = Integer.parseInt(c_day);
        if (c==d) {//表示是当天发的活动
            holder.activity_up_day.setText("今天");
        } else if (c - 1 == d) {
            holder.activity_up_day.setText("昨天");
        } else if (c - 2 == d) {
            holder.activity_up_day.setText("前天");
        } else {
            holder.activity_up_day.setText(a_day.substring(0, 4) + "." + a_day.substring(4, 6) + "." + a_day.substring(6, 8));
        }
        holder.activity_up_time.setText(time.substring(8, 10) + ":" + time.substring(10, 12));

        //活动状态：
        if(activity.getStatus()==1){
            holder.activity_status.setTextColor(Color.parseColor("#2591ff"));
            holder.activity_status.setText("活动进行中...");
        }else {
            holder.activity_status.setTextColor(Color.parseColor("#E73111"));
            holder.activity_status.setText("活动已结束");
        }
        //申请表
        if(activity.getIsSetJoinNum()==1){
            PeopleNumManagement applyData=activity.getPeopleNumManagement();
            holder.activity_apply_num.setVisibility(View.VISIBLE);
            holder.activity_applying_num.setVisibility(View.VISIBLE);
            holder.activity_free_num.setVisibility(View.VISIBLE);
            holder.activity_apply_num.setText("申请："+applyData.getJoinNum()+" 人");
            holder.activity_applying_num.setText("正在申请："+applyData.getApplyingNum()+" 人");
            holder.activity_free_num.setText("空余："+applyData.getFreeNum()+" 人");
        }else {
            holder.activity_apply_num.setVisibility(View.GONE);
            holder.activity_applying_num.setVisibility(View.GONE);
            holder.activity_free_num.setVisibility(View.GONE);
        }
        //活动图片
        if(activity.getImgs()!=null){
            String[] imgList=activity.getImgs().split(",");
            List<String> stringList=new ArrayList<String>();
            if(imgList!=null&&imgList.length>0){
                for (String string:imgList) {
                    stringList.add(string);
                }
                holder.grid_view.setVisibility(View.GONE);
                if(imgList!=null &&imgList.length>0){
                    holder.grid_view.setVisibility(View.VISIBLE);
                    GridViewItemImgAdapter gridViewItemImgAdapter = new GridViewItemImgAdapter(context, stringList, R.layout.grid_view_item);
                    holder.grid_view.setAdapter(gridViewItemImgAdapter);
                }
            }
        }
       else{
            holder.grid_view.setVisibility(View.GONE);
        }


        //活动内容
        String content1 = "";
        if (activity.getContent().length() > 100) {
            content1 = activity.getContent().substring(0, 100) + "...";
        } else {
            content1 = activity.getContent();
        }
        holder.content_text.setText(content1);
        //活动标签
        String[] strings = activity.getLabel().split(",");
        String s = "";
        for (int i = 0; i < strings.length; i++) {
            s = s + strings[i] + "\u3000";
        }
        holder.list_biao_qian.setText(s);

        //浏览量
        holder.num_liulan.setText("浏览量"+activity.getViewNum());

        //点击量
        holder.click_num.setText("点击"+activity.getViewNum());

       /* RequestOptions requestOptions=new RequestOptions().placeholder(R.drawable.huodong).error(R.drawable.huodong).fallback(R.drawable.huodong);
        final Gson gson=new Gson();*/
        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              *//*  Intent intent=new Intent(context,ActivityDetail.class).putExtra("activity",gson.toJson(activity));
                context.startActivity(intent);*//*
            }
        });*/
        //notifyDataSetChanged();
        return view;
    }
    public class ViewHolder{
        private TextView activity_title;
        private TextView activity_manage;
        private TextView activity_up_day;
        private TextView activity_up_time;
        private TextView activity_status;
        private TextView activity_apply_num;
        private TextView activity_applying_num;
        private TextView activity_free_num;
        private TextView content_text;
        private TextView list_biao_qian;
        private TextView num_liulan;
        private TextView click_num;
        private NoScrollGridView grid_view;
    }
    /*管理*/
    public void popMenu(View v,Activities activity,int position) {

        PopupMenu popupMenu = new PopupMenu(context, v);
        MenuInflater inflater = popupMenu.getMenuInflater();

        //单机事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //结束活动
                    case R.id.activity_end:
                        //如果活动已结束
                        if(activity.getStatus()==0){
                            Toast toast4 = Toast.makeText(context, "活动已经结束", Toast.LENGTH_SHORT);
                            toast4.setGravity(Gravity.CENTER, 0, 0);
                            toast4.show();
                        }else {
                            dialog = new AlertDialog.Builder(context).create();
                            dialog.show();  //注意：必须在window.setContentView之前show
                            Window window = dialog.getWindow();
                            window.setContentView(R.layout.is_end_activity);

                            //结束的活动的标题
                            TextView endActivityTitle=window.findViewById(R.id.end_activity_title);
                            endActivityTitle.setText(activity.getTitle());
                            Button yesButton = window.findViewById(R.id.end_ok);
                            Button no = window.findViewById(R.id.end_no);
                            //点击按钮让对话框消失
                            yesButton.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    //确定结束
                                    endActivity(activity.getId(),position);
                                    dialog.dismiss();
                                }
                            });
                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        break;
                        //删除活动
                    case R.id.activity_delete:
                        if(activity.getStatus()==1){
                            Toast toast4 = Toast.makeText(context, "请先结束活动再删除", Toast.LENGTH_SHORT);
                            toast4.setGravity(Gravity.CENTER, 0, 0);
                            toast4.show();
                        }else {
                            dialog = new AlertDialog.Builder(context).create();
                            dialog.show();  //注意：必须在window.setContentView之前show
                            Window window = dialog.getWindow();
                            window.setContentView(R.layout.is_delete_activity);
                            //删除的活动的标题
                            TextView endActivityTitle = window.findViewById(R.id.end_activity_title);
                            endActivityTitle.setText(activity.getTitle());
                            Button yesButton = window.findViewById(R.id.delete_ok);
                            Button no = window.findViewById(R.id.delete_no);
                            //点击按钮让对话框消失
                            yesButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //确定删除
                                    deleteActivity(activity.getId(), position);
                                    dialog.dismiss();
                                }
                            });
                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }

                        break;
                        //修改活动

                 /*   case R.id.activity_edit:
                        Intent intent=new Intent(context,EditActivity.class).putExtra("aId",activity.getId());
                        context.startActivity(intent);
                        break;*/
                        //报名表
                    case R.id.activity_apply_form:
                        if (activity.getIsSetJoinNum()==1){
                            //跳转页面到报名表页面
                            Intent intent2=new Intent(context,ApplyFormActivity.class).putExtra("aId",activity.getId()).putExtra("activityName",activity.getTitle());
                            context.startActivity(intent2);
                        }else {
                            Toast toast4 = Toast.makeText(context, "该活动未设置报名选项", Toast.LENGTH_SHORT);
                            toast4.setGravity(Gravity.CENTER, 0, 0);
                            toast4.show();
                        }

                        break;

                }
                return true;
            }
        });

        inflater.inflate(R.menu.activity_manage, popupMenu.getMenu());
        popupMenu.show();
    }

    private void deleteActivity(Integer id, int position) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body=new FormBody.Builder().add("id", String.valueOf(id)).build();
        final Request request = new
                Request.Builder().url(Constant.url + "activity/delete").post(body).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message message = new Message();
                DeleteRestful restful=new Gson().fromJson(string,DeleteRestful.class);
                if(restful.getCode()==2000){
                    dataSource.remove(position);
                    message.what = 4;
                }else {
                    message.what=3;
                }
                handler.sendMessage(message);
            }
        });
    }

    /*管理点击结束活动弹出框确定结束，结束活动*/
    private void endActivity(int id,int position) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body=new FormBody.Builder().add("id", String.valueOf(id))
                .add("status",String.valueOf(0)).build();
        final Request request = new
                Request.Builder().url(Constant.url + "activity/setStatusById").post(body).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                DeleteRestful restful=new Gson().fromJson(string,DeleteRestful.class);
                Message message = new Message();
                if(restful.getCode()==2000){
                    dataSource.get(position).setStatus(0);
                    message.what = 2;
                    message.obj = string;
                }else {
                    message.what=3;
                }
                handler.sendMessage(message);
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Toast toast4 = Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
            }

            if(msg.what==2){//活动结束
                    Toast toast4 = Toast.makeText(context, "活动已结束", Toast.LENGTH_SHORT);
                    toast4.setGravity(Gravity.CENTER, 0, 0);
                    toast4.show();
                    notifyDataSetChanged();
            }
            if (msg.what==3){//活动结束失败
                Toast toast4 = Toast.makeText(context, "网络错误，请刷新页面再次操作", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
            }if(msg.what==4){//删除活动
                Toast toast4 = Toast.makeText(context, "活动已删除", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
                notifyDataSetChanged();

            }


        }
    };

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }
}
