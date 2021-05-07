package com.example.myshools.Mine.organization.edit;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshools.Activity.Adapter.PopWindowListAdapter;
import com.example.myshools.Activity.entity.EventBusPostType;
import com.example.myshools.Mine.PerisonalDataModel;
import com.example.myshools.Mine.organization.historyActivityManage.entity.ApplyForm;
import com.example.myshools.Mine.organization.publish.IdentificationResult;
import com.example.myshools.R;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.Orgnaization;
import com.example.myshools.entity.School;
import com.example.myshools.util.GlideEngine;
import com.example.myshools.util.InputTextDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.myshools.Mine.organization.publish.PublishNewsActivity.activity;

public class OrganizationDataActivity extends AppCompatActivity implements View.OnClickListener, PerisonalDataModel.getOrgDataStatus,PerisonalDataModel.updataOrgDataStatus {
    private TextView o_name;
    private ImageView o_img;
    private List<School> schools;
    private TextView o_identity_content;
    private TextView o_recommend;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private Button up_data;
    private ImageButton return_mine;
    private Orgnaization orgnaization;//已经有的数据
    private Orgnaization updateOrg;//点击更新时获取的数据
    private List<LocalMedia> result = new ArrayList<>();
    private InputTextDialog inputTextDialog;//文字输入框
    private Boolean nowStatus;
    private Boolean isup = false;//页面信息是否更改
    private boolean isSetIdentityData = false;//是否更改了有关认证相关的信息
    private PerisonalDataModel perisonalDataModel;
    private boolean isupDataImg=false,isUpDataName=false,isupSchool=false,isupidentityContent=false,
            isupRecommend=false;
    private View popupView;
    private boolean isOk=false;
    private PopupWindow popupWindow;
    private ImageButton selectSchool;
    private TextView slectedSchoolName;
    private SharedPreferences user;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 2) {
                RequestOptions requestOptions = new RequestOptions().circleCrop();
                Glide.with(OrganizationDataActivity.this).load(msg.obj.toString()).apply(requestOptions).into(o_img);
                isup = true;
                isupDataImg=true;
                isSetIdentityData = true;
                up_data.setEnabled(true);
            }


            if (msg.what == 11) {

                int re = Integer.parseInt(msg.obj.toString());
                if (re == 1) {
                    Intent intent = new Intent(OrganizationDataActivity.this, IdentificationResult.class).putExtra("status", 2);
                    startActivity(intent);
                    // EventBus.getDefault().post(new Message());
                    finish();
                } else {
                    Toast.makeText(OrganizationDataActivity.this, "在设置中反馈", Toast.LENGTH_SHORT).show();

                }
            }
            if (msg.what == 12) {
                Toast.makeText(OrganizationDataActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }if(msg.what==21){
                int position= (int) msg.obj;
                School school=schools.get(position);
                if(school.getId()!=orgnaization.getSchoolId()){
                    isup=true;
                    isSetIdentityData=true;
                    isupSchool=true;
                    slectedSchoolName.setText(school.getSchoolName());
                    orgnaization.setSchoolId(school.getId());

                }
            }if(msg.what==22){
                updateOrgData();
            }if(msg.what==23){
                up_data.setEnabled(true);
                up_data.setBackgroundColor(Color.parseColor("#00CCFF"));
                up_data.setText("开始验证");
                Toast.makeText(OrganizationDataActivity.this, "网络错误", Toast.LENGTH_SHORT).show();

            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff00CCFF);
        }

        setContentView(R.layout.mine_data);
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        schools = new ArrayList<>();
        orgnaization = new Orgnaization();
        perisonalDataModel = new PerisonalDataModel();
        //从本地获取初始化数据
        initUserData();
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

        findView();
        setView();
        setListener();
    }

    private void initPop(List<School> data) {
        ListView listView = popupView.findViewById(R.id.select_school_list_view);
        PopWindowListAdapter adapter=new PopWindowListAdapter(data,this);
        listView.setAdapter(adapter);
        popupWindow.setContentView(popupView);
        popupWindow.isFocusable();
        popupWindow.isClippingEnabled();
        popupWindow.showAsDropDown(slectedSchoolName, -0, 0);
    }

    private void initUserData() {
        popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.select_school_pop_window, null);
        popupWindow = new PopupWindow(600, 800);
        String phone = getSharedPreferences("nextAccount", MODE_PRIVATE).getString("account", "");
        String list = getSharedPreferences("schoolList", MODE_PRIVATE).getString("schoolsList", "");
        schools = new Gson().fromJson(list, new TypeToken<List<School>>() {
        }.getType());
        if (phone != null && phone.length() == 11) {
            user = getSharedPreferences(phone, MODE_PRIVATE);
            orgnaization.setPhone(phone);
            orgnaization.setId(user.getInt("id", -1));
            orgnaization.setIdentity(user.getInt("identity", -1));
            orgnaization.setName(user.getString("name", ""));
            orgnaization.setHeadPath(user.getString("headPath", ""));
            orgnaization.setRecommend(user.getString("recommend", ""));
            orgnaization.setIdentityContent(user.getString("identityContent", ""));
            orgnaization.setSchoolId(user.getInt("schoolId", 1));
        }
    }


    private void findView() {
        slectedSchoolName = findViewById(R.id.activity_select_school_name);
        return_mine = findViewById(R.id.btn_return_setting);
        o_name = findViewById(R.id.o_name);
        o_img = findViewById(R.id.o_img);
        o_identity_content = findViewById(R.id.o_identity_content);
        o_recommend = findViewById(R.id.o_recommend);
        up_data = findViewById(R.id.up_data);
        up_data.setEnabled(false);//初始化时使验证按钮不可点击
        selectSchool = findViewById(R.id.activity_select_school_button);
    }


    private void setView() {
        o_name.setText(orgnaization.getName());
        if (orgnaization.getHeadPath().length() > 0) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.loading).error(R.drawable.headfail).fallback(R.drawable.loading).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop();
            Glide.with(this).load(Constant.imgUrl+orgnaization.getHeadPath()).apply(requestOptions).into(o_img);
        }
        o_recommend.setText(orgnaization.getRecommend());
        o_identity_content.setText(orgnaization.getIdentityContent());
        up_data.setEnabled(false);
        for(School school:schools){
            if (school.getId()==orgnaization.getSchoolId()){
                slectedSchoolName.setText(school.getSchoolName());
            }
        }

    }

    private void setListener() {
        //返回
        return_mine.setOnClickListener(this::onClick);
        //名字修改
        o_name.setOnClickListener(this::onClick);
        //头像点击修改：
        o_img.setOnClickListener(this::onClick);
        //资料简介修改
        o_recommend.setOnClickListener(this::onClick);
        o_identity_content.setOnClickListener(this::onClick);
        //开始认证
        up_data.setOnClickListener(this::onClick);
        selectSchool.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.o_img:
                //打开图片选择器
                nowStatus = true;
                openPictureSelect();
                break;
            case R.id.o_identity_content:
                //修改认证资料
                inputTextDialog = new InputTextDialog(OrganizationDataActivity.this, R.style.dialog_center, 255);
                String hint0 = "";
                if (o_identity_content.getText().length() > 0) {
                    hint0 = o_identity_content.getText().toString();
                } else {
                    hint0 = "";
                }
                inputTextDialog.setHint(hint0);
                inputTextDialog.setMaxNumber(255);//最长字数
                inputTextDialog.show();
                String finalHint = hint0;
                inputTextDialog.setmOnTextSendListener(msg -> {
                    o_identity_content.setText(msg);
                    String s = msg.toString();
                    if (!s.equals(finalHint)) {
                        orgnaization.setIdentityContent(msg);
                        isup = true;
                        isSetIdentityData = true;
                        isupidentityContent=true;
                        up_data.setEnabled(true);
                        up_data.setBackgroundColor(Color.parseColor("#00CCFF"));
                    }
                });
                break;


            case R.id.btn_return_setting:
                //EventBus.getDefault().post(new Message());
                finish();
                break;
            case R.id.o_name:
                inputTextDialog = new InputTextDialog(OrganizationDataActivity.this, R.style.dialog_center, 20);
                String hint = new String();
                if (o_name.getText().toString().length() > 0) {
                    hint = o_name.getText().toString();
                } else {
                    hint = "请输入组织名字";
                }
                inputTextDialog.setHint(hint);
                inputTextDialog.setMaxNumber(20);//最长20个字
                inputTextDialog.show();
                String s = hint;
                inputTextDialog.setmOnTextSendListener(msg -> {
                    o_name.setText(msg);
                    if (!msg.equals(s)) {
                        orgnaization.setName(msg);
                        isup = true;
                        isUpDataName=true;
                        isSetIdentityData = true;
                        up_data.setEnabled(true);
                        up_data.setBackgroundColor(Color.parseColor("#00CCFF"));
                    }
                });
                break;
            case R.id.o_recommend:
                inputTextDialog = new InputTextDialog(OrganizationDataActivity.this, R.style.dialog_center, 2048);
                String hint2 = "";
                if (o_recommend.getText().toString().length() > 0) {
                    hint2 = o_recommend.getText().toString();
                } else {
                    hint2 = "建议修改文本后复制粘贴到这里";
                }
                inputTextDialog.setHint(hint2);
                inputTextDialog.setMaxNumber(2048);//最长十个字
                inputTextDialog.show();
                String s1 = hint2;
                inputTextDialog.setmOnTextSendListener(msg -> {
                    o_recommend.setText(msg);
                    if (!msg.equals(s1)) {
                        orgnaization.setRecommend(msg);
                        isup = true;
                        isupRecommend=true;
                        up_data.setEnabled(true);
                        up_data.setBackgroundColor(Color.parseColor("#00CCFF"));
                    }
                });
                break;
            case R.id.up_data:
                if (isup) {//页面做了信息的更改
                    if (isSetIdentityData) {//页面修改了关于认证相关的信息
                        orgnaization.setIdentity(2);
                    }
                    if (orgnaization.getName().length() > 0 && orgnaization.getIdentityContent().length() > 0 && orgnaization.getRecommend().length() > 0
                            && (result.size() > 0 || orgnaization.getHeadPath().length() > 0)) {
                        up_data.setEnabled(false);
                        up_data.setBackgroundColor(Color.parseColor("#bfbfbf"));
                        up_data.setText("正在发送请求");
                        getOrgData();
                    } else {
                        Toast.makeText(OrganizationDataActivity.this, "请完善组织信息", Toast.LENGTH_SHORT).show();

                    }
                     } else {//不进行任何操作
                }

                break;
            case R.id.activity_select_school_button:
                initPop(schools);
                break;
        }
    }

    //第一步获取组织信息
    private void getOrgData() {
        perisonalDataModel.GetOrganizationById( this, orgnaization.getPhone());

    }
    //第二步：修改组织信息
    private void  updateOrgData(){
        //第二步修改组织信息
        if (updateOrg != null) {//获取到用户信息
            if(!isOk){//发送到后端请求修改数据
                if(isupRecommend){updateOrg.setRecommend(orgnaization.getRecommend());}
                if(isUpDataName){updateOrg.setName(orgnaization.getName());}
                if(isupidentityContent){updateOrg.setIdentityContent(orgnaization.getIdentityContent());}
                if(isupSchool){updateOrg.setSchoolId(orgnaization.getSchoolId());}
                if(isSetIdentityData){updateOrg.setIdentity(orgnaization.getIdentity());}
                sendOrgData();
            }else{//修改本地保存的数据
                SharedPreferences.Editor editor=user.edit();
                if(isupRecommend){
                    editor.putString("recommend",updateOrg.getRecommend());
                }
                if(isUpDataName){
                    editor.putString("name",updateOrg.getName());
                }
                if(isupidentityContent){
                    editor.putString("identityContent",updateOrg.getIdentityContent());
                }
                if(isupSchool){
                    editor.putInt("schoolId",updateOrg.getSchoolId());
                }if(isupDataImg){
                    editor.putString("headPath",updateOrg.getHeadPath());
                }
                if(isSetIdentityData){
                    editor.putInt("identity",updateOrg.getIdentity());
                }
                editor.commit();
            }
        }else {//没有获取到数据，提示用户网络错误
            Message message =new Message();
            message.what=23;
            handler.sendMessage(message);

        }

    }
    //第三步：发送组织信息
    private void sendOrgData(){
        if(isupDataImg){
            perisonalDataModel.updataOrgData(this,updateOrg,result.get(0));
        }else {
            perisonalDataModel.updataOrgData(this,updateOrg);
        }
    }
    private void openPictureSelect() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                //.openCamera()//单独使用相机 媒体类型 PictureMimeType.ofImage()、ofVideo()
                .theme(R.style.picture_default_style)// xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .selectionMode(PictureConfig.SINGLE)//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(false)//开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
                // .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回
                // .isWeChatStyle()//开启R.style.picture_WeChat_style样式
                //.setPictureStyle()//动态自定义相册主题
                //.setPictureCropStyle()//动态自定义裁剪主题
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)//相册启动退出动画
                .isCamera(false)//列表是否显示拍照按钮
                //.isZoomAnim(pi)//图片选择缩放效果
                // .imageFormat()//拍照图片格式后缀,默认jpeg, PictureMimeType.PNG，Android Q使用PictureMimeType.PNG_Q
                .maxSelectNum(1)//最大选择数量,默认9张
                .minSelectNum(1)// 最小选择数量
                .compress(true)//是否压缩
                .compressFocusAlpha(true)//压缩后是否保持图片的透明通道
                .minimumCompressSize(0)// 小于多少kb的图片不压缩
                .imageSpanCount(4)//列表每行显示个数
                .openClickSound(false)//是否开启点击声音
                .selectionMedia(result)//是否传入已选图片
                .previewImage(true)//是否预览图片
                .enableCrop(nowStatus)//是否开启裁剪
                .freeStyleCropEnabled(true)//裁剪框是否可拖拽
                .circleDimmedLayer(nowStatus)// 是否开启圆形裁剪
                .setCircleDimmedBorderColor(R.color.textColorBlue)//设置圆形裁剪边框色值
                .setCircleStrokeWidth(2)//设置圆形裁剪边框粗细
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(false)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .isDragFrame(true)//是否可拖动裁剪框(固定)
                .hideBottomControls(false)//显示底部uCrop工具栏


                .setLanguage(LanguageConfig.CHINESE)//国际化语言 LanguageConfig.CHINESE、ENGLISH、JAPAN等

                .isMaxSelectEnabledMask(true)//选择条件达到阀时列表是否启用蒙层效果
                .forResult(new MyResultCallback());//结果回调分两种方式onActivityResult()和OnResultCallbackListener方式
    }

    @Override
    public void updataOrgFailed(int i) {
        Message message=new Message();
        message.what=23;
        handler.sendMessage(message);
    }

    @Override
    public void updateOrgSuccess() {
        isOk=true;
        getOrgData();
        finish();
    }

    @Override
    public void getOrgDataFailed(int i) {
        Message message=new Message();
        message.what=23;
        handler.sendMessage(message);

    }

    @Override
    public void getOrgDataSuccess(@NotNull Orgnaization orgnaization) {
        this.updateOrg = orgnaization;
        Log.e("TAGOOO",orgnaization.toString());
        Message message=new Message();
        message.what=22;
        handler.sendMessage(message);
    }

    private class MyResultCallback implements OnResultCallbackListener<LocalMedia> {

        @Override
        public void onResult(List<LocalMedia> result1) {
            result = result1;
            Message message = new Message();
            message.what = 2;
            message.obj = result.get(0).getCompressPath();
            handler.sendMessage(message);
        }

        @Override
        public void onCancel() {

        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(EventBusPostType message) {
        if (message.getType() == 1) {
            popupWindow.dismiss();
            Message message1=new Message();
            message1.what=21;
            message1.obj=message.getPosition();
            handler.sendMessage(message1);
        }
    }
}