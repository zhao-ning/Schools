package com.example.myshools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.entity.Constant;

public class ImgRunnable implements Runnable {
    private ImageView imageView;
    private String imgPath;
    private Context context;
    private RequestOptions requestOptions;
    public ImgRunnable(Context context,ImageView view,String imgPath,RequestOptions requestOptions){
        this.imageView=view;
        this.imgPath=imgPath;
        this.context=context;
        this.requestOptions =requestOptions;
    }
    @Override
    public void run() {
        Glide.with(context).load(imgPath).apply(requestOptions).into(imageView);
    }
}
