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
    private Orgnaization orgnaization;//??????????????????
    private Orgnaization updateOrg;//??????????????????????????????
    private List<LocalMedia> result = new ArrayList<>();
    private InputTextDialog inputTextDialog;//???????????????
    private Boolean nowStatus;
    private Boolean isup = false;//????????????????????????
    private boolean isSetIdentityData = false;//??????????????????????????????????????????
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
                    Toast.makeText(OrganizationDataActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();

                }
            }
            if (msg.what == 12) {
                Toast.makeText(OrganizationDataActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
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
                up_data.setText("????????????");
                Toast.makeText(OrganizationDataActivity.this, "????????????", Toast.LENGTH_SHORT).show();

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
        //??????????????????????????????
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
        up_data.setEnabled(false);//???????????????????????????????????????
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
        //??????
        return_mine.setOnClickListener(this::onClick);
        //????????????
        o_name.setOnClickListener(this::onClick);
        //?????????????????????
        o_img.setOnClickListener(this::onClick);
        //??????????????????
        o_recommend.setOnClickListener(this::onClick);
        o_identity_content.setOnClickListener(this::onClick);
        //????????????
        up_data.setOnClickListener(this::onClick);
        selectSchool.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.o_img:
                //?????????????????????
                nowStatus = true;
                openPictureSelect();
                break;
            case R.id.o_identity_content:
                //??????????????????
                inputTextDialog = new InputTextDialog(OrganizationDataActivity.this, R.style.dialog_center, 255);
                String hint0 = "";
                if (o_identity_content.getText().length() > 0) {
                    hint0 = o_identity_content.getText().toString();
                } else {
                    hint0 = "";
                }
                inputTextDialog.setHint(hint0);
                inputTextDialog.setMaxNumber(255);//????????????
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
                    hint = "?????????????????????";
                }
                inputTextDialog.setHint(hint);
                inputTextDialog.setMaxNumber(20);//??????20??????
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
                    hint2 = "??????????????????????????????????????????";
                }
                inputTextDialog.setHint(hint2);
                inputTextDialog.setMaxNumber(2048);//???????????????
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
                if (isup) {//???????????????????????????
                    if (isSetIdentityData) {//??????????????????????????????????????????
                        orgnaization.setIdentity(2);
                    }
                    if (orgnaization.getName().length() > 0 && orgnaization.getIdentityContent().length() > 0 && orgnaization.getRecommend().length() > 0
                            && (result.size() > 0 || orgnaization.getHeadPath().length() > 0)) {
                        up_data.setEnabled(false);
                        up_data.setBackgroundColor(Color.parseColor("#bfbfbf"));
                        up_data.setText("??????????????????");
                        getOrgData();
                    } else {
                        Toast.makeText(OrganizationDataActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();

                    }
                     } else {//?????????????????????
                }

                break;
            case R.id.activity_select_school_button:
                initPop(schools);
                break;
        }
    }

    //???????????????????????????
    private void getOrgData() {
        perisonalDataModel.GetOrganizationById( this, orgnaization.getPhone());

    }
    //??????????????????????????????
    private void  updateOrgData(){
        //???????????????????????????
        if (updateOrg != null) {//?????????????????????
            if(!isOk){//?????????????????????????????????
                if(isupRecommend){updateOrg.setRecommend(orgnaization.getRecommend());}
                if(isUpDataName){updateOrg.setName(orgnaization.getName());}
                if(isupidentityContent){updateOrg.setIdentityContent(orgnaization.getIdentityContent());}
                if(isupSchool){updateOrg.setSchoolId(orgnaization.getSchoolId());}
                if(isSetIdentityData){updateOrg.setIdentity(orgnaization.getIdentity());}
                sendOrgData();
            }else{//???????????????????????????
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
        }else {//????????????????????????????????????????????????
            Message message =new Message();
            message.what=23;
            handler.sendMessage(message);

        }

    }
    //??????????????????????????????
    private void sendOrgData(){
        if(isupDataImg){
            perisonalDataModel.updataOrgData(this,updateOrg,result.get(0));
        }else {
            perisonalDataModel.updataOrgData(this,updateOrg);
        }
    }
    private void openPictureSelect() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//?????? ???????????? PictureMimeType.ofAll()???ofImage()???ofVideo()???ofAudio()
                //.openCamera()//?????????????????? ???????????? PictureMimeType.ofImage()???ofVideo()
                .theme(R.style.picture_default_style)// xml???????????? R.style.picture_default_style???picture_WeChat_style or ????????????Demo
                .loadImageEngine(GlideEngine.createGlideEngine())// ?????????????????? ?????? implements ImageEngine??????
                .selectionMode(PictureConfig.SINGLE)//??????or?????? PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(false)//?????????????????????????????????????????????????????????pageSize???????????????isFilterInvalidFile????????????????????????
                // .isSingleDirectReturn(true)//PictureConfig.SINGLE???????????????????????????
                // .isWeChatStyle()//??????R.style.picture_WeChat_style??????
                //.setPictureStyle()//???????????????????????????
                //.setPictureCropStyle()//???????????????????????????
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)//????????????????????????
                .isCamera(false)//??????????????????????????????
                //.isZoomAnim(pi)//????????????????????????
                // .imageFormat()//????????????????????????,??????jpeg, PictureMimeType.PNG???Android Q??????PictureMimeType.PNG_Q
                .maxSelectNum(1)//??????????????????,??????9???
                .minSelectNum(1)// ??????????????????
                .compress(true)//????????????
                .compressFocusAlpha(true)//??????????????????????????????????????????
                .minimumCompressSize(0)// ????????????kb??????????????????
                .imageSpanCount(4)//????????????????????????
                .openClickSound(false)//????????????????????????
                .selectionMedia(result)//????????????????????????
                .previewImage(true)//??????????????????
                .enableCrop(nowStatus)//??????????????????
                .freeStyleCropEnabled(true)//????????????????????????
                .circleDimmedLayer(nowStatus)// ????????????????????????
                .setCircleDimmedBorderColor(R.color.textColorBlue)//??????????????????????????????
                .setCircleStrokeWidth(2)//??????????????????????????????
                .showCropFrame(false)// ?????????????????????????????? ???????????????????????????false
                .showCropGrid(false)//?????????????????????????????? ???????????????????????????false
                .rotateEnabled(false)//???????????????????????????
                .scaleEnabled(true)//?????????????????????????????????
                .isDragFrame(true)//????????????????????????(??????)
                .hideBottomControls(false)//????????????uCrop?????????


                .setLanguage(LanguageConfig.CHINESE)//??????????????? LanguageConfig.CHINESE???ENGLISH???JAPAN???

                .isMaxSelectEnabledMask(true)//??????????????????????????????????????????????????????
                .forResult(new MyResultCallback());//???????????????????????????onActivityResult()???OnResultCallbackListener??????
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