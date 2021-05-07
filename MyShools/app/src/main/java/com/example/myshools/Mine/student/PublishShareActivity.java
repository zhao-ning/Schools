package com.example.myshools.Mine.student;

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
import com.example.myshools.Mine.organization.publish.AddPictureRecyclerAdapter;
import com.example.myshools.Mine.organization.publish.SetQuestion;
import com.example.myshools.Mine.organization.publish.TypeRecycleViewAdapter;
import com.example.myshools.Mine.student.model.PublishShareModel;
import com.example.myshools.R;
import com.example.myshools.entity.Activities;
import com.example.myshools.entity.ClickEventMessage;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
 * 学生发布校友圈页面
 *
 *
 * */

public class PublishShareActivity extends AppCompatActivity implements  PublishShareModel.onAddShareStatus,PublishShareModel.onUpdataStaus,PublishShareModel.onDeleteStatus {
    private InputTextDialog inputTextDialog;
    private Map<String, Object> map = new HashMap<>();
    private TextView ivBack;
    private StudentReport studentReport;
    private TextView upLoad;
    private RecyclerView rcvImg;
    //文字
    private EditText et_input_message;
    //限制字数
    private TextView maxNUm;
    private int userId;     // 用户id
    private List<LocalMedia> result = new ArrayList<>();
    public static final String GET_IMAGES_FROM_ADD = "getImagesFromAdd";
    public static final String GET_IMAGES_FROM_INTENT = "getImagesFromIntent";
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private RecyclerView recyclerView;
    private AddPictureRecyclerAdapter adapter;
    private boolean isAddShare=false;
    private boolean isUpdata=false;
    private PublishShareModel shareModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_share);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff00CCFF);
        }
        //重新初始化questionList
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        findView();
        initDatas();
        setListeners();
        shareModel=new PublishShareModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(isAddShare&&!isUpdata){
            shareModel.doDeleteShareById(this,studentReport.getId());
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
    public void onUpdateFailed(int result) {
        isUpdata=false;
        Message message=new Message();
        message.what=result;
        myHandler.sendMessage(message);

    }

    @Override
    public void onUpdateSuccess(int i) {
        isUpdata=true;
        Message message=new Message();
        message.what=12;
        myHandler.sendMessage(message);
        finish();
    }

    @Override
    public void onDeleteSuccess() {
        isAddShare=false;
        finish();
    }

    @Override
    public void onDeleteFailed() {

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
                adapter = new AddPictureRecyclerAdapter(result, PublishShareActivity.this, R.layout.item_add_picture_recycler);
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
                    finish();
                    break;
                case GET_IMAGES_FROM_ADD:
                    result.add(new LocalMedia());
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    private void initDatas() {
        String s= getSharedPreferences("nextAccount", MODE_PRIVATE).getString("account", null);
        userId=getSharedPreferences(s,MODE_PRIVATE).getInt("id",-1);
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);

    }

    private void findView() {
        //活动文字
        et_input_message = findViewById(R.id.et_input_message);
        //输入字数
        maxNUm = findViewById(R.id.max_num);
        //标签
      recyclerView=findViewById(R.id.type_item);

        //选择图片
        rcvImg = findViewById(R.id.recy_add_picture);
        ivBack = findViewById(R.id.cancel_share_upload);
        upLoad = findViewById(R.id.add_share_upload);
        adapter = new AddPictureRecyclerAdapter(result, this, R.layout.item_add_picture_recycler);
        rcvImg.setAdapter(adapter);



    }

    private void setListeners() {
        MyListener myListener = new MyListener();
        ivBack.setOnClickListener(myListener);
        upLoad.setOnClickListener(myListener);
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

    private  class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel_share_upload:
                    //返回
                    finish();
                    break;
                case R.id.add_share_upload:

                    // 上传
                    if(et_input_message.getText().toString().length()>0){
                        if(studentReport==null){
                            doAddShare();
                        }else{
                            doUpdata();
                        }
                        upLoad.setEnabled(false);
                        upLoad.setTextColor(Color.parseColor("#ffffff"));
                        upLoad.setText("发布中");
                    }
                    break;
            }
        }


    }

    private void doAddShare() {
        shareModel.doAddNewShare(this,userId);
    }

    @Override
    public void onAddShareFailed() {
        isAddShare=false;
        Message message=new Message();
        message.what=1;
        myHandler.sendMessage(message);
    }

    @Override
    public void onAddShareSuccess(@NotNull StudentReport studentReport) {
        //创建成功：
        isAddShare=true;
        this.studentReport=studentReport;
        doUpdata();
    }
    private void doUpdata() {

        studentReport.setContent(et_input_message.getText().toString());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");//设置日期格式
        String upTime = df.format(new Date());
        studentReport.setPublishTime(upTime);
        studentReport.setStatus(1);
        shareModel.doUpdateShare(this,studentReport,result);

    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){//网络错误
                upLoad.setEnabled(true);
                upLoad.setTextColor(Color.parseColor("#ffffff"));
                upLoad.setText("发布");
                Toast.makeText(PublishShareActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 13) {//网络连接成功，但是上传失败
                upLoad.setEnabled(true);
                upLoad.setTextColor(Color.parseColor("#ffffff"));
                upLoad.setText("发布");
                Toast.makeText(PublishShareActivity.this, "发布失败，请重新发布", Toast.LENGTH_SHORT).show();
            }
        }
    };


}
