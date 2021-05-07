package com.example.myshools.Mine.organization.publish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myshools.Mine.organization.model.PublishModel;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.ClickEventMessage;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.PeopleNumManagement;
import com.example.myshools.util.GlideEngine;

import com.example.myshools.util.InputTextDialog;
import com.example.myshools.util.OnRecyclerItemClickListener;
import com.google.gson.Gson;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/*
 * 组织发布活动页面
 *
 *
 * */

public class PublishNewsActivity extends AppCompatActivity implements PublishModel.onAddNewsStatus,PublishModel.onUpdataStaus,PublishModel.onDeleteStatus {
    private InputTextDialog inputTextDialog;
    private Map<String, Object> map = new HashMap<>();
    private ImageView ivBack;
    private Activities activities;
    //    private TextView upLoad;
    private Button upLoad;
    private RecyclerView rcvImg;
    //标题
    private TextView a_title;
    //活动文字
    private EditText et_input_message;
    //限制字数
    private TextView maxNUm;
    //是否设置报名人数
    private Button toSelect;
    private TextView isSelect;
    private EditText peopleNum;
    private Boolean isSet = false;
    private int joinNum = 0;
    //设置问题表单的LinearLayout
    private LinearLayout setQuestionLinearLayout;
    //是否设置问题
    private Button setQuestion;
    private Boolean isSetQuestions = false;
    private TextView isSetQuestion;
    private List<String> questionList = new ArrayList<>();
    private SharedPreferences questionSharedPreferences;
    //报名人数设置是否正确
    private Boolean is_set_people_ok = true;
    //问题是否设置正确
    private Boolean is_set_question_ok = true;
    private PublishModel publishModel;
    private AddPictureRecyclerAdapter adapter;
    private int userId;     // 用户id
    private int status = 0;//用户是否注册
    private List<LocalMedia> result = new ArrayList<>();
    public static final String GET_IMAGES_FROM_ADD = "getImagesFromAdd";
    public static final String GET_IMAGES_FROM_INTENT = "getImagesFromIntent";
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    public static Activity activity;
    private static List<String> typeList;//总列表
    private List<String> selectList;//选择的type
    private RecyclerView recyclerView;
    private boolean isAddActivity=false;
    private boolean isUpdata=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff00CCFF);
        }
        //重新初始化questionList
        initQustions();
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        findView();
        initDatas();
        setListeners();
        publishModel=new PublishModel();
        publishModel.doAddNewActivity(this,userId);
    }
    //重新初始化questionList
    private void initQustions(){
        questionSharedPreferences = getSharedPreferences("questionList",
                MODE_PRIVATE);
        SharedPreferences.Editor ed = questionSharedPreferences.edit();
        Set<String> strings = new HashSet<>();
        for (String data : questionList) {
            strings.add(data);
        }
        ed.putStringSet("dataSource", strings);
        ed.putInt("update", 0);
        ed.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(isAddActivity&&!isUpdata){
            publishModel.doDeleteActivityById(this,activities.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectPicture(Map<String, Object> map) {
        switch (map.get("type").toString()) {
            case GET_IMAGES_FROM_ADD:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                this.result = (List<LocalMedia>) map.get("res");
                openPhotoSelector(GET_IMAGES_FROM_ADD);
                break;
        }
    }

    //recycleAdapter传递的类型数据
    @Subscribe(threadMode=ThreadMode.MAIN)
    public void setType(ClickEventMessage message){
        if(message.isIs()){
            selectList.add(typeList.get(message.getPosition()));
        }else {
            selectList.remove(typeList.get(message.getPosition()));
        }
    }
    /**
     * 启动图片查看器
     */
    private void openPhotoSelector(String type) {
        // 直接启动图片查看器
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                .theme(R.style.picture_default_style)// xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
                .loadImageEngine(GlideEngine.createGlideEngine())// 图片加载引擎 需要 implements ImageEngine接口
                .selectionMode(PictureConfig.MULTIPLE)//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(false)//开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
                .setPictureWindowAnimationStyle(mWindowAnimationStyle)//相册启动退出动画
                .isCamera(false)//列表是否显示拍照按钮
                .maxSelectNum(9)//最大选择数量,默认9张
                .minSelectNum(0)// 最小选择数量
                .compress(true)//是否压缩
                .compressFocusAlpha(true)//压缩后是否保持图片的透明通道
                .minimumCompressSize(2)// 小于多少kb的图片不压缩
                .imageSpanCount(4)//列表每行显示个数
                .openClickSound(false)//是否开启点击声音
                .selectionMedia(result)//是否传入已选图片
                .previewImage(true)//是否预览图片
                .enableCrop(false)//是否开启裁剪
                .circleDimmedLayer(false)// 是否开启圆形裁剪
                .synOrAsy(true)//开启异步
                .hideBottomControls(false)//显示底部uCrop工具栏
                .setLanguage(LanguageConfig.CHINESE)//国际化语言 LanguageConfig.CHINESE、ENGLISH、JAPAN等
                .isMaxSelectEnabledMask(true)//选择条件达到阀时列表是否启用蒙层效果
                .forResult(new MyResultCallback(type));//结果回调分两种方式onActivityResult()和OnResultCallbackListener方式
    }

    @Override
    public void onDeleteSuccess() {
            finish();
    }

    @Override
    public void onDeleteFailed() {

    }

    @Override
    public void onAddNewsFailed() {

    }

    @Override
    public void onAddNewsSuccess(@NotNull Activities activities) {
        this.activities=activities;
        isAddActivity=true;
    }

    @Override
    public void onUpdateFailed(int i) {
        Message message=new Message();
        if(i==11){
            message.what=11;
        }else if(i==13){
            message.what=13;
        }
        myHandler.sendMessage(message);
    }

    @Override
    public void onUpdateSuccess(int i) {
        if(i==12){
            Message message=new Message();
            message.what=12;
            myHandler.sendMessage(message);
        }
        isUpdata=true;
    }

    /**
     * 返回结果回调
     */
    private class MyResultCallback implements OnResultCallbackListener<LocalMedia> {
        private String type;
        public MyResultCallback(String type) {
            this.type = type;
        }
        @Override
        public void onResult(List<LocalMedia> result1) {
            result = result1;

            if (null == adapter) {
                adapter = new AddPictureRecyclerAdapter(result, PublishNewsActivity.this, R.layout.item_add_picture_recycler);
                rcvImg.setAdapter(adapter);
            } else {
                adapter.updateRes(result);
            }
        }

        @Override
        public void onCancel() {
            // 取消时
            switch (type) {
                case GET_IMAGES_FROM_INTENT:
                    PublishNewsActivity.activity.finish();
                    break;
                case GET_IMAGES_FROM_ADD:
                    result.add(new LocalMedia());
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    private void initDatas() {
        typeList=new ArrayList<String>();
        selectList=new ArrayList<>();
        String type=getSharedPreferences("userSchool",Context.MODE_PRIVATE).getString("typeList","");
        typeList= Arrays.asList(type.split(","));
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);//设置布局管理
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new TypeRecycleViewAdapter(typeList));
        recyclerView.getAdapter().notifyDataSetChanged();


        String s= getSharedPreferences("nextAccount", MODE_PRIVATE).getString("account", null);
        userId=getSharedPreferences(s,MODE_PRIVATE).getInt("id",-1);
        status=getSharedPreferences(s,MODE_PRIVATE).getInt("identity",-1);
        activity = this;
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
    }

    private void findView() {

        //活动标题
        a_title = findViewById(R.id.a_title);
        //活动文字
        et_input_message = findViewById(R.id.et_input_message);
        //输入字数
        maxNUm = findViewById(R.id.max_num);
        //标签
      recyclerView=findViewById(R.id.type_item);

        //选择图片
        rcvImg = findViewById(R.id.recy_add_picture);
        ivBack = findViewById(R.id.iv_add_picture_back);
        upLoad = findViewById(R.id.add_activity_upload);
        adapter = new AddPictureRecyclerAdapter(result, PublishNewsActivity.this, R.layout.item_add_picture_recycler);
        rcvImg.setAdapter(adapter);

        //设置人数
        peopleNum = findViewById(R.id.people_num);
        toSelect = findViewById(R.id.to_select);
        isSelect = findViewById(R.id.selectPeople);
        //设置问题布局
        setQuestionLinearLayout=findViewById(R.id.set_question_layout);
        //设置问题
        setQuestion = findViewById(R.id.set_question);
        isSetQuestion = findViewById(R.id.is_set_question);


    }

    private void setListeners() {
        MyListener myListener = new MyListener();
        setQuestion.setOnClickListener(myListener);
        ivBack.setOnClickListener(myListener);
        upLoad.setOnClickListener(myListener);
        a_title.setOnClickListener(myListener);
        rcvImg.addOnItemTouchListener(new OnRecyclerItemClickListener(rcvImg) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (vh.getAdapterPosition() == result.size() - 1) {
                    result.remove(result.size() - 1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", GET_IMAGES_FROM_ADD);
                    map.put("res", result);
                    EventBus.getDefault().post(map);
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {

            }
        });

        et_input_message.addTextChangedListener(new TextWatcher() {
            private CharSequence word;//文本
            private int selectionEnd;//记录下输入结束时光标的位置，也就是在哪结束的

            //这三个方法那就好比：
            // 老板合伙人现在有个想法，想给老板提些要求，打成文本发给老板，老板知道了感觉不能伤了投资人的心，我给你试试水
            // 最终的结果就是符合现实那就这样实施，不符合的那就及时的pass掉
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //需要注意的是charSequence是光标之前的值
                //输入框内容改变之前被调用，就是输入结束的时候被调用，在原先的文本中也就是charSequence中，从i开始的i1个字符将会被i2个字符所替换
                //但是还没有被替换，也就是说我点进去输入框原先文本是：开始，现在输入两个字例如  “ 嗯嗯 ”，但是还没有显现在输入框，他的内容还是开始两个字
                //参数的值charSequence：开始，i:2，i1：0，i2:2
                //假如现在的文本是：“你好” ， 在你好之间插入数据“很”
                // /参数的值charSequence：你好，i:1，i1：0，i2:1
                // 总结：当你输入进去就会调用这个方法，但是此时内容没有被改变，接下来让onTextChanged方法去做改变
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //执行了上一个方法想要执行的内容，也就是说在输入框之前输进去的嗯嗯，显示在上面
                //从charSequence开始之后的第0个位置的字符串转换成了长度为2，内容为嗯嗯的值
                word = charSequence; //charSequence输入的所有内容：开始嗯嗯
                //这个方法结束那就开始调用afterTextChanged开始检测是否合法，
                // 不对其设置都是合法的，但一般重写这个都是对他输入文本的一个限制，将限制的规则，在方法中进行设置
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String s = editable.toString();
                maxNUm.setText(editable.length() + "/500");
                //et_input_message.getSelectionStart();//输入之前坐标开始的位置
                selectionEnd = et_input_message.getSelectionEnd();//输入之后坐标结束的位置
                //在这里进行判断文本长度，对长度进行了一个限制，还可以对它输入的内容进行一些限制，就不再写了
                if (word.length() > 500) {
                    //删除多余输入的字（不会显示出来）用一个for循环直接删除刚才输入超出文本的内容
                    int num = word.length() - 500;//查看输入超出的个数
                    editable.delete(selectionEnd - num, selectionEnd);//从超出的下标开始进行删除
                    int tempSelection = selectionEnd;
                    et_input_message.setText(editable);//删除之后那继续下面的操作会重新调用第一个方法然后又一次轮回直到满足条件
                    //设置光标在最后
                    et_input_message.setSelection(tempSelection);
                    Toast toast = Toast.makeText(getApplicationContext(), "超出文本限制", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


    }

    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_add_picture_back:
                    //返回
                    finish();
                    break;

                //设置问题
                case R.id.set_question:
                    Intent intent5 = new Intent(PublishNewsActivity.this, SetQuestion.class);
                    startActivity(intent5);
                    break;
                case R.id.add_activity_upload:
                    // 上传
                    if (status == 1) {//已经认证
                        upLoad.setEnabled(false);
                        upLoad.setTextColor(Color.parseColor("#bfbfbf"));
                        upLoad.setText("发布中...");
                        String title = a_title.getText().toString();//标题
                        //内容
                        String context = "";
                        //标签
                        String tab =selectList.toString().substring(1,selectList.toString().length()-1);
                        if (title.length() != 0) {
                            context = et_input_message.getText().toString();
                            if (context.length() > 0) {
                                if (isSet == true) {
                                    if(!peopleNum.getText().toString().equals("")){
                                        joinNum = Integer.parseInt(peopleNum.getText().toString());
                                    }
                                    if(peopleNum.getText().toString().equals("")){
                                        joinNum=0;
                                    }
                                    if (joinNum > 0 && joinNum < 2147483647) {
                                        is_set_people_ok = true;
                                    } else {
                                        is_set_people_ok = false;
                                        Toast toast3 = Toast.makeText(PublishNewsActivity.this, "参加人数设置有误", Toast.LENGTH_SHORT);
                                        toast3.setGravity(Gravity.CENTER, 0, 0);
                                        toast3.show();
                                        upLoad.setEnabled(true);
                                        upLoad.setTextColor(Color.parseColor("#ffffff"));
                                        upLoad.setText("发布");
                                    }
                                }
                                if(isSet==false){
                                    is_set_people_ok = true;
                                }
                                if (isSetQuestions == true) {//是否设置了问题表单
                                    //获取list
                                    questionSharedPreferences = getSharedPreferences("questionList",
                                            MODE_PRIVATE);
                                    Set<String> stringSet=questionSharedPreferences.getStringSet("dataSource",null);
                                    if(stringSet!=null&&stringSet.size()!=0){
                                        for(String str:stringSet){
                                            questionList.add(str);
                                        }
                                    }
                                    if (questionList.size() > 0) {
                                        is_set_question_ok = true;
                                    } else {
                                        is_set_question_ok = false;
                                        Toast toast3 = Toast.makeText(PublishNewsActivity.this, "末尾项问题表单为空，请重新设置", Toast.LENGTH_SHORT);
                                        toast3.setGravity(Gravity.CENTER, 0, 0);
                                        toast3.show();
                                        upLoad.setEnabled(true);
                                        upLoad.setTextColor(Color.parseColor("#ffffff"));
                                        upLoad.setText("发布");
                                    }
                                }
                                if(isSetQuestions==false){
                                    is_set_question_ok = true;
                                }
                                //最终对开关进行判断，格式是否设置正确
                                if (is_set_people_ok == true && is_set_question_ok == true) {
                                    upLoadNews(title, context, tab);
                                }


                            } else {
                                Toast toast2 = Toast.makeText(PublishNewsActivity.this, "内容为空,无法创建", Toast.LENGTH_SHORT);
                                toast2.setGravity(Gravity.CENTER, 0, 0);
                                toast2.show();
                                upLoad.setEnabled(true);
                                upLoad.setTextColor(Color.parseColor("#ffffff"));
                                upLoad.setText("发布");
                            }

                        } else {
                            //标题为空
                            Toast toast = Toast.makeText(PublishNewsActivity.this, "标题为空,无法创建", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            upLoad.setEnabled(true);
                            upLoad.setTextColor(Color.parseColor("#ffffff"));
                            upLoad.setText("发布");
                        }

                    }
                    if (status != 1) {
                        Toast toast7 = Toast.makeText(PublishNewsActivity.this, "完成认证后才能发布", Toast.LENGTH_SHORT);
                        toast7.setGravity(Gravity.CENTER, 0, 0);
                        toast7.show();
                    }
                    break;
                case R.id.a_title:
                    inputTextDialog = new InputTextDialog(PublishNewsActivity.this, R.style.dialog_center, 12);
                    String hint1 = "";
                    if (a_title.getText().toString().length() > 0) {
                        hint1 = a_title.getText().toString();
                    }
                    inputTextDialog.setHint(hint1);
                    inputTextDialog.setMaxNumber(12);//最长十个字
                    inputTextDialog.show();
                    inputTextDialog.setmOnTextSendListener(msg -> {
                        a_title.setText(msg);
                    });

            }
        }

    }

    //开始上传数据
    private void upLoadNews(String title, String context, String tab) {
        if(activities!=null&&activities.getId()!=0){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
            String upTime = df.format(new Date());
            activities.setContent(context);
            activities.setTitle(title);
            activities.setLabel(tab);
            activities.setquestions(questionList.toString().substring(1,questionList.toString().length()-1));
            activities.setUpTime(upTime);
            if (joinNum>0){
                activities.setIsSetJoinNum(1);
                activities.setPeopleNumManagement(new PeopleNumManagement(activities.getId(),joinNum,0,joinNum,0));
            }else {
                activities.setIsSetJoinNum(0);
            }
            publishModel.doUpdateActivity(this,activities,result);

        }else {//重新创建新活动
            publishModel.doAddNewActivity(this,userId);
        }


    }




    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11) {//上传网络连接失败
                upLoad.setEnabled(true);
                upLoad.setTextColor(Color.parseColor("#ffffff"));
                upLoad.setText("发布");
                Toast.makeText(PublishNewsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 12) {//上传成功
                //上传成功后
              /*  Toast toast4 = Toast.makeText(PublishNewsActivity.this, "发布成功", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();*/
                finish();
            }
            if (msg.what == 13) {//网络连接成功，但是上传失败
                upLoad.setEnabled(true);
                upLoad.setTextColor(Color.parseColor("#ffffff"));
                upLoad.setText("发布");
                Toast.makeText(PublishNewsActivity.this, "发布失败，请重新发布", Toast.LENGTH_SHORT).show();
            }


        }
    };

    public void popMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();

        //单机事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.is_select_no:
                        isSelect.setText("否");
                        initQustions();//保持questionList为空
                        isSet = false;
                        peopleNum.setVisibility(View.INVISIBLE);
                        setQuestionLinearLayout.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.is_select_ok:
                        isSelect.setText("是");
                        isSet = true;
                        peopleNum.setVisibility(View.VISIBLE);
                        setQuestionLinearLayout.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });

        inflater.inflate(R.menu.is_select_join_people_num, popupMenu.getMenu());
        popupMenu.show();

    }

    public void setQuestionPopMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();

        //单机事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.is_set_question_no:
                        isSetQuestions = false;
                        isSetQuestion.setText("否");
                        initQustions();//保持questionList为空
                        setQuestion.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.is_set_question_ok:
                        isSetQuestions = true;
                        isSetQuestion.setText("是");
                        setQuestion.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }
        });
        inflater.inflate(R.menu.is_set_question_num, popupMenu.getMenu());
        popupMenu.show();
    }



}
