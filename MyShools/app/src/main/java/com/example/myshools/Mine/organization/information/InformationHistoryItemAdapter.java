package com.example.myshools.Mine.organization.information;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.Activity.Activity.ActivityDetail;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class InformationHistoryItemAdapter extends BaseAdapter {
    private List<Activities> dataSource=new ArrayList<>();
    private int resId;//布局
    private Context context;//上下文
    public InformationHistoryItemAdapter(Context context, List<Activities> dataSource, int resId){
        this.dataSource=dataSource;
        this.context=context;
        this.resId=resId;
    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dataSource.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resId, null);
            holder=new ViewHolder();
            holder.first_img1=view.findViewById(R.id.first_img1);
            holder.a_name=view.findViewById(R.id.a_name);
            holder.a_time=view.findViewById(R.id.a_time);
            holder.a_num=view.findViewById(R.id.a_num);
            view.setTag(holder);
        } else {
            holder= (ViewHolder) view.getTag();
        }
        RequestOptions requestOptions=new RequestOptions().placeholder(R.drawable.huodong).error(R.drawable.huodong).fallback(R.drawable.huodong);

        Activities activities=dataSource.get(position);

        //图片
        if(activities.getImgs().length()>0){
            holder.first_img1.setVisibility(View.VISIBLE);
            Glide.with(context).load(Constant.imgUrl+activities.getImgs().split(",")[0]).apply(requestOptions).into(holder.first_img1);
        }else {
            holder.first_img1.setVisibility(View.INVISIBLE);
        }
        //时间
        String s=activities.getUpTime();
        holder.a_time.setText(s.substring(0,4)+"."+s.substring(4,6)+"."+s.substring(6,8));
        //标题
        String title="";
        if(activities.getTitle().length()>8){
            title=activities.getTitle().substring(0,8);
        }else {
            title=activities.getTitle().toString();
        }
        holder.a_name.setText(title);
        //点击
        holder.a_num.setText(activities.getViewNum()+"");




        final Gson gson=new Gson();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ActivityDetail.class).putExtra("activity",gson.toJson(activities));
                context.startActivity(intent);
            }
        });
        return view;
    }
    public class ViewHolder{
        private ImageView first_img1;
        private TextView a_time;
        private TextView a_name;
        private TextView a_num;
    }


}
