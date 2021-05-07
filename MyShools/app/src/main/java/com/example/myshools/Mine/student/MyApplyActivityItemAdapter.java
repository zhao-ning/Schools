package com.example.myshools.Mine.student;

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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.Activity.Adapter.GridViewItemImgAdapter;
import com.example.myshools.Activity.entity.NoScrollGridView;
import com.example.myshools.Mine.organization.historyActivityManage.ApplyFormActivity;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.GetStudentApplyActivitiies;
import com.example.myshools.entity.PeopleNumManagement;
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

public class MyApplyActivityItemAdapter extends BaseAdapter {
    private List<GetStudentApplyActivitiies> dataSource;
    private int resId;//布局
    private Context context;//上下文
    public MyApplyActivityItemAdapter(){}
    public MyApplyActivityItemAdapter(Context context, List<GetStudentApplyActivitiies> dataSource, int resId){
        this.dataSource=dataSource;
        this.context=context;
        this.resId=resId;

    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public GetStudentApplyActivitiies getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataSource.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        RequestOptions requestOptions1 =new RequestOptions().placeholder(R.drawable.toux).fallback(R.drawable.toux).error(R.drawable.toux).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop();
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resId, null);
            holder=new ViewHolder();

          holder.applyTime=view.findViewById(R.id.apply_time);
          holder.applyStatus=view.findViewById(R.id.apply_status);
          holder.AOheadImg=view.findViewById(R.id.a_o_head_img);
          holder.ATimeDAy=view.findViewById(R.id.a_time_day);
          holder.ATimeTime=view.findViewById(R.id.a_time_time);
          holder.AOName=view.findViewById(R.id.a_o_name);
          holder.ATitte=view.findViewById(R.id.ac_title);
          holder.AContent=view.findViewById(R.id.a_content);
            view.setTag(holder);
        } else {
            holder= (ViewHolder) view.getTag();
        }
        final GetStudentApplyActivitiies activity=dataSource.get(position);

        String s=activity.getApplyTime();
        String time=s.substring(0,4)+"."+s.substring(4,6)+"."+s.substring(6,8)+"  "+s.substring(8,10)+":"+s.substring(10,12);
        holder.applyTime.setText("申请："+time);

        if(activity.getApplyResult()==1){
            holder.applyStatus.setText("正在申请...");
        }else if (activity.getApplyResult()==2){
            holder.applyStatus.setText("通过");
            holder.applyTime.setTextColor(Color.parseColor("#00CCFF"));
        }else if(activity.getApplyResult()==4){
            holder.applyStatus.setText("未通过");
            holder.applyStatus.setTextColor(Color.parseColor("#FF0000"));
        }
        if(activity.getActivities().getOrganization()!=null){
            Glide.with(context).load(Constant.imgUrl+activity.getActivities().getOrganization().getHeadPath()).apply(requestOptions1).into(holder.AOheadImg);
            holder.AOName.setText(activity.getActivities().getOrganization().getName());
        }

       //活动发布时间
        String time1 = activity.getActivities().getUpTime().toString();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        String currentTime = df.format(new Date());
        String a_day = time1.substring(0, 8);
        int d = Integer.parseInt(a_day);
        String c_day = currentTime.substring(0, 8);
        int c = Integer.parseInt(c_day);
        if (c==d) {//表示是当天发的活动
            holder.ATimeDAy.setText("今天");
        } else if (c - 1 == d) {
            holder.ATimeDAy.setText("昨天");
        } else if (c - 2 == d) {
            holder.ATimeDAy.setText("前天");
        } else {
            holder.ATimeDAy.setText(a_day.substring(0, 4) + "." + a_day.substring(4, 6) + "." + a_day.substring(6, 8));
        }
       holder.ATimeTime.setText(time1.substring(8, 10) + ":" + time1.substring(10, 12));


        holder.ATitte.setText(activity.getActivities().getTitle());
        //活动内容
        String content1 = "";
        if (activity.getActivities().getContent().length() > 100) {
            content1 =activity.getActivities().getContent().substring(0, 100) + "...";
        } else {
            content1 = activity.getActivities().getContent();
        }
        holder.AContent.setText(content1);
        notifyDataSetChanged();
        return view;
    }
    public class ViewHolder{
        private TextView applyTime;
        private TextView applyStatus;
        private ImageView AOheadImg ;
        private TextView AOName;
        private TextView ATimeDAy;
        private TextView ATimeTime;
        private TextView ATitte;
        private TextView AContent;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }
}
