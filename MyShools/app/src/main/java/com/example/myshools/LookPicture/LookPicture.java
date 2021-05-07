package com.example.myshools.LookPicture;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myshools.R;
import com.example.myshools.entity.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class LookPicture extends AppCompatActivity {
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList;//view数组
    private ImageView re;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_picture);
        int position=getIntent().getIntExtra("position",-1);
        String d=getIntent().getStringExtra("dataSource");

        List<String> list= new Gson().fromJson(d,new TypeToken<List<String>>(){}.getType());

        Log.e("ss",position+"");
        re=findViewById(R.id.btn_return_m);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager=findViewById(R.id.viewpager);
        //设置动画
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        LayoutInflater inflater=getLayoutInflater();
        viewList = new ArrayList<View>();
       for (int i = 0; i < list.size() ; i++) {  //for循环将试图添加到list中
            View view = inflater.inflate(
                    R.layout.page_one, null);   //绑定viewpager的item布局文件
            viewList.add(view);
        }
       ViewPagerAdapter adapter=new ViewPagerAdapter(list);
       viewPager.setAdapter(adapter);
       viewPager.setCurrentItem(position);
    }
    private class ViewPagerAdapter extends PagerAdapter{
        private List<String> list;
        public ViewPagerAdapter(List<String> list) {
            this.list=list;
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(LookPicture.this));
        }
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
           ImageView imageView = new ImageView(LookPicture.this);

         //   Glide.with(getBaseContext()).load(Constant.imgUrl+list.get(position)).into(imageView);
            ImageLoader.getInstance().displayImage(Constant.imgUrl+list.get(position), imageView);
            container.addView(imageView);
            return imageView;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
