package com.example.myshools.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.LookPicture.LookPicture;
import com.example.myshools.R;
import com.example.myshools.entity.Constant;
import com.google.gson.Gson;

import java.util.List;

/*
* 图片布局
*
* */
public class GridViewItemImgAdapter extends BaseAdapter {
    private List<String> dataSource;//数据源
    private int itemResId; //布局
    private Context context;//上下文环境
    public GridViewItemImgAdapter(Context context, List<String> list, int itemResId) {
        this.dataSource = list;
        this.context = context;
        this.itemResId = itemResId;

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
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.back2).fallback(R.drawable.back2).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop();

        if (view == null) {
            view = LayoutInflater.from(context).inflate(itemResId, null);
            holder = new ViewHolder();
            holder.image = view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(Constant.imgUrl+dataSource.get(position)).apply(requestOptions).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转页面
                Gson gson=new Gson();
                Intent intent=new Intent(context, LookPicture.class)
                        .putExtra("position",position)
                        .putExtra("dataSource",gson.toJson(dataSource));
                context.startActivity(intent);

            }
        });

    return view;
    }
    public class ViewHolder{
        public ImageView image;
    }

}
