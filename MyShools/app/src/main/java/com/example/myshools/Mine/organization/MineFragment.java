package com.example.myshools.Mine.organization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.Mine.Settings.SettingsActivity;
import com.example.myshools.Mine.organization.edit.OrganizationDataActivity;
import com.example.myshools.Mine.organization.historyActivityManage.HistoryActivity;
import com.example.myshools.Mine.organization.publish.PublishNewsActivity;
import com.example.myshools.R;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.Orgnaization;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

public class MineFragment extends Fragment {
    private ImageView headImg;
    private ImageView isRenZ;//认证出现
    private ImageView inToJS;
    private TextView oName;
    private TextView oLevel;
    private TextView isRenZh;
    private LinearLayout upActivity;
    private LinearLayout historyActivity;
//    private LinearLayout myNews;

    private LinearLayout settings;

    private int role;
    private int id;
    private Orgnaization orgnaization = new Orgnaization();
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private static String user_head_path = null;
    private SharedPreferences userSp;
    private RequestOptions requestOptions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.organization_fragment, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(0xff00CCFF);
        }


        return newView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //首先判断是组织还是个人
        //0是组织，1是个人，在登录的时候设置全局变量,获取用户id值
        //获取账号，用户信息

        requestOptions = new RequestOptions().placeholder(R.drawable.loading).fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop();
        findView();
        setView();
        setAdapter();

    }

    private void findView() {
        headImg = getActivity().findViewById(R.id.o_head_img);
        oName = getActivity().findViewById(R.id.o_name);
        oLevel = getActivity().findViewById(R.id.o_level);
        isRenZ = getActivity().findViewById(R.id.is_ren_zheng);
        isRenZh = getActivity().findViewById(R.id.ren_zheng);
        inToJS = getActivity().findViewById(R.id.btn_into_xiangqing);
        upActivity = getActivity().findViewById(R.id.up_activity);
        historyActivity = getActivity().findViewById(R.id.history_activity);
//       ？、 myNews = getActivity().findViewById(R.id.my_news);
        settings = getActivity().findViewById(R.id.settings);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setAdapter() {
        MyClickListener my = new MyClickListener();
        inToJS.setOnClickListener(my);
        upActivity.setOnClickListener(my);
        historyActivity.setOnClickListener(my);
//        myNews.setOnClickListener(my);

        settings.setOnClickListener(my);

    }

    private void setView() {
        //头像
        String phone=getActivity().getSharedPreferences("nextAccount",Context.MODE_PRIVATE).getString("account","");
        userSp=getActivity().getSharedPreferences(phone,Context.MODE_PRIVATE);
        role=userSp.getInt("role",-1);
        id =userSp.getInt("id",-1);
        String headPath=userSp.getString("headPath","");
        if(user_head_path!=null&&user_head_path.length()>0) {
            Glide.with(getContext()).load(Constant.imgUrl+headPath).apply(requestOptions).into(headImg);
        }
            oName.setText(userSp.getString("name",""));
            int identity=userSp.getInt("identity",-1);

            if ( identity== 1) {
                isRenZ.setVisibility(View.VISIBLE);
                isRenZh.setText(" 已认证");

            } else if (identity == 2) {
                isRenZ.setVisibility(View.GONE);
                isRenZh.setText(" 正在认证中");
            } else {
                isRenZ.setVisibility(View.GONE);
                isRenZh.setText(" 未认证");
            }
    }







    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            if (msg.what == 10) {
               setView();
            }


        }
    };

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.btn_into_xiangqing:
                        Intent intent = new Intent(getActivity(), OrganizationDataActivity.class);
                        startActivity(intent);
                        break;
                case R.id.up_activity:
                    Intent intent1 = new Intent(getActivity(), PublishNewsActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.history_activity:
                    Intent intent2=new Intent(getActivity(), HistoryActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.settings:
                    Intent intent3=new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent3);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Message message = new Message();
        message.what = 10;
        handler.sendMessage(message);
        setView();

    }



}

