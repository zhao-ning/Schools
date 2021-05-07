package com.example.myshools.Mine.student.edit

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.Activity.Adapter.PopWindowListAdapter
import com.example.myshools.Activity.entity.EventBusPostType
import com.example.myshools.Mine.organization.publish.PublishNewsActivity.activity
import com.example.myshools.Mine.student.model.EditStudentModel
import com.example.myshools.R
import com.example.myshools.entity.Constant
import com.example.myshools.entity.School
import com.example.myshools.entity.Student
import com.example.myshools.util.GlideEngine
import com.example.myshools.util.InputTextDialog
import com.example.myshools.util.InputTextDialog.OnTextSendListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureWindowAnimationStyle
import kotlinx.android.synthetic.main.mine_student_data.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/*
* isUpdata:全局监听是否修改
isUpIdentity:监听是否修改认证状态

如果修改认证信息
	与认证向相关的数据不能为空
如果不修改认证信息，不需要修改认证状态，直接提交数据即可
*
* */
class StudentEditActivity :AppCompatActivity(), View.OnClickListener,EditStudentModel.getStuDataStatus,EditStudentModel.doEditStudentStataus {


    private lateinit var student: Student
    var mWindowAnimationStyle= PictureWindowAnimationStyle()

    //headImg(1):头像  renZO：renZR学生证件 正反面\
    var headImg = ArrayList<LocalMedia>()
    var renZO = ArrayList<LocalMedia>()
    var renZR = ArrayList<LocalMedia>()
    var result=ArrayList<LocalMedia>()
    private lateinit var schools:MutableList<School>
    private lateinit var inputTextDialog //文字输入框
            : InputTextDialog
  var isUpdate = false//是否数据更新
  var isUpIdentity = false//是否更新了认证相关的信息
    private lateinit var onStudentData: Student
    lateinit var now: ImageView
    var nowStatus: Boolean = false
    private var who = 0
    private lateinit var phone:String
    private lateinit var popupView:View
    private var popupWindow: PopupWindow? = null
    private var editStudentModel=EditStudentModel()
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 21) {
                val position = msg.obj as Int
                val school = schools.get(position)
                if (school.id != student.schoolId) {
                    isUpdate = true
                    activity_select_school_name.setText(school.schoolName)
                    onStudentData.schoolId=school.id
                    isUpIdentity=true
                }
            }else if (msg.what==99){
                val i:Int= msg.obj as Int
                if (i==1){
                    Toast.makeText(this@StudentEditActivity,"网络错误",Toast.LENGTH_SHORT).show()
                }else if (i==0){
                    Toast.makeText(this@StudentEditActivity,"找不到上传图片地址，请重新上传",Toast.LENGTH_SHORT).show()
                }else if(i==2){
                    Toast.makeText(this@StudentEditActivity,"接口无法访问",Toast.LENGTH_SHORT).show()
                }
            }
            if(msg.what==81){//数据更新完毕，开始重新获取数据更新用户本地信息
                upUserData()
            }

            //初始化页面时加载三个图片
            if (msg.what == 100) {
                if (student.headPath != null) {
                    val requestOptions = RequestOptions().placeholder(R.drawable.tou).fallback(R.drawable.tou).error(R.drawable.tou).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
                    Glide.with(this@StudentEditActivity).load(Constant.imgUrl+student.headPath).apply(requestOptions).into(s_img)
                }
            }
            if (msg.what == 101) {
                if (student.identityimgObverse != null) {
                    val requestOptions1 = RequestOptions().placeholder(R.drawable.onglide).fallback(R.drawable.onglide).error(R.drawable.error).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter()
                    Glide.with(this@StudentEditActivity).load(Constant.imgUrl+student.identityimgObverse).apply(requestOptions1).into(s_ren_z1)
                }
            }
            if (msg.what == 102) {
                if (student.identityimgReverse != null) {
                    val requestOptions2 = RequestOptions().placeholder(R.drawable.onglide).fallback(R.drawable.onglide).error(R.drawable.error).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter()
                    Glide.with(this@StudentEditActivity).load(Constant.imgUrl+student.identityimgReverse).apply(requestOptions2).into(s_ren_z2)
                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor =Color.parseColor("#00CCFF")
        }
        setContentView(R.layout.mine_student_data)
        EventBus.getDefault().register(this)

        if (Build.VERSION.SDK_INT >= 23) {
            val mPermissionList = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS)
            ActivityCompat.requestPermissions(this, mPermissionList, 123)
        }
        initStudentData()
    }

    private fun initStudentData() {
        onStudentData = Student()
        phone = getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account", "").toString()
        val studentData = getSharedPreferences(phone, Context.MODE_PRIVATE).getString("userData", "");
        student = Gson().fromJson(studentData, Student::class.java)
        if (student != null) {
            s_nickname.text = student.stuNickname
            s_information.text = student.recommend
            s_name.text = student.stuName
            s_college.text = student.stuCollege
            s_number.text = student.stuNumber
        }
        popupView = LayoutInflater.from(applicationContext).inflate(R.layout.select_school_pop_window, null)
        popupWindow = PopupWindow(600, 800)
        val list = getSharedPreferences("schoolList", Context.MODE_PRIVATE).getString("schoolsList", "")
        schools = Gson().fromJson<MutableList<School>>(list, object : TypeToken<MutableList<School?>?> () {}.type)
        for (school in schools) {
            if (school.id == student.schoolId) {
                activity_select_school_name.text = school.schoolName
            }
        }
        //开启线程加载图片
        val message00 = Message()
        message00.what = 100
        handler.sendMessage(message00)
        val message11 = Message()
        message11.what = 101
        handler.sendMessage(message11)
        val message22 = Message()
        message22.what = 102
        handler.sendMessage(message22)
    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.s_img -> {
                    who=1
                    nowStatus = true
                    openPictureSelect()
                }
                R.id.s_ren_z1 -> {
                    //修改认证图
                    who=2
                    nowStatus = false //不开启裁
                    openPictureSelect()
                }
                R.id.s_ren_z2 -> {
                    //修改认证图
                    who=3
                    nowStatus = false //不开启裁剪
                    openPictureSelect()
                }
                R.id.return_mine ->                 //EventBus.getDefault().post(new Message());
                    finish()
                R.id.s_nickname -> {
                    inputTextDialog = InputTextDialog(this@StudentEditActivity, R.style.dialog_center, 8)
                    var hint = ""
                    hint = if (s_nickname.text.toString().length > 0) {
                        s_nickname.text.toString()
                    } else {
                        "请输入昵称"
                    }
                    inputTextDialog.setHint(hint)
                    inputTextDialog.setMaxNumber(10)
                    inputTextDialog.show()
                    inputTextDialog.setmOnTextSendListener(OnTextSendListener { msg: String ->
                        if (!msg.equals(student.stuNickname)) {
                            isUpdate=true
                            s_nickname.text = msg
                            onStudentData.stuNickname = msg
                        }

                    })
                }
                R.id.s_name -> {
                    inputTextDialog = InputTextDialog(this@StudentEditActivity, R.style.dialog_center, 20)
                    var hint1 = ""
                    hint1 = if (s_name.text.toString().length > 0) {
                        s_name.text.toString()
                    } else {
                        "请输入名字"
                    }
                    inputTextDialog.setHint(hint1)
                    inputTextDialog.setMaxNumber(20) //最长十个字
                    inputTextDialog.show()
                    inputTextDialog.setmOnTextSendListener(OnTextSendListener { msg: String ->
                        if (!student.stuName.equals(msg)) {
                            isUpdate=true
                            s_name.text = msg
                            isUpIdentity=true
                            onStudentData.stuName = msg
                        }

                    })
                }
                R.id.s_number -> {
                    inputTextDialog = InputTextDialog(this@StudentEditActivity, R.style.dialog_center, 20)
                    var hint2 = ""
                    hint2 = if (s_number.text.toString().length > 0) {
                        s_number.text.toString()
                    } else {
                        "请输入学号"
                    }
                    inputTextDialog.setHint(hint2)
                    inputTextDialog.setMaxNumber(20) //最长十个字
                    inputTextDialog.show()
                    inputTextDialog.setmOnTextSendListener(OnTextSendListener { msg: String ->
                        if (!student.stuNumber.equals(msg)) {
                            isUpdate=true
                            isUpIdentity=true
                            s_number.text = msg
                            onStudentData.stuNumber = msg
                        }

                    })
                }
                R.id.s_college -> {
                    inputTextDialog = InputTextDialog(this@StudentEditActivity, R.style.dialog_center, 20)
                    var hint4 = ""
                    hint4 = if (s_college.text.toString().length > 0) {
                        s_college.text.toString()
                    } else {
                        "请输入学院名称"
                    }
                    inputTextDialog.setHint(hint4)
                    inputTextDialog.setMaxNumber(200)
                    inputTextDialog.show()
                    inputTextDialog.setmOnTextSendListener(OnTextSendListener { msg: String ->
                        if (!student.stuCollege.equals(msg)) {
                            isUpdate=true
                            isUpIdentity=true

                            s_college.text = msg
                            onStudentData.stuCollege = msg
                        }

                    })
                }
                R.id.s_information -> {
                    inputTextDialog = InputTextDialog(this@StudentEditActivity, R.style.dialog_center, 200)
                    var hint3 = ""
                    hint3 = if (s_information.text.toString().length > 0) {
                        s_information.text.toString()
                    } else {
                        "建议修改文本后复制粘贴到这里"
                    }
                    inputTextDialog.setHint(hint3)
                    inputTextDialog.setMaxNumber(200)
                    inputTextDialog.show()
                    inputTextDialog.setmOnTextSendListener(OnTextSendListener { msg: String ->
                        if (!student.recommend.equals(msg)) {
                            isUpdate=true
                            s_information.text = msg
                            onStudentData.recommend = msg
                        }
                    })
                }
                R.id.read_provide -> {

                    //跳转到规范页面
                }
                R.id.s_up_data -> {
                    if(isUpdate){
                        JudgeWhereUpdata()
                    }else{
                        Toast.makeText(this,"资料未修改", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.activity_select_school_button ->{
                    initPop(schools)
                }
            }
        }
    }
    private fun initPop(data: MutableList<School>) {
        val listView: ListView = popupView.findViewById(R.id.select_school_list_view)
        val adapter = PopWindowListAdapter(data, this)
        listView.adapter = adapter
        popupWindow!!.setContentView(popupView)
        popupWindow!!.isFocusable()
        popupWindow!!.isClippingEnabled()
        popupWindow!!.showAsDropDown(activity_select_school_name, -0, 0)
    }
    //判断更新需求
    private fun JudgeWhereUpdata() {
        //判断是否修改认证图片
        var isUpdaImg:Boolean=false
        var isOk=true//是否可以进行更新数据请求
        if((renZO.size>0&&renZR.size>0)||student.identityimgReverse.length>1){
            isUpdaImg=true
        }
        if(onStudentData.stuNickname!=null){
            student.stuNickname=onStudentData.stuNickname
        }
        if(onStudentData.recommend!=null){
            student.recommend=onStudentData.recommend
        }
        if(isUpIdentity){//检查信息是否有空值
            if(s_name.text.toString().length>1&&s_college.text.toString().length>1&&s_number.text.toString().length>1
                    &&activity_select_school_name.text.toString().length>1
                    &&isUpdaImg){
                student.isIdentity=2
                student.stuName=s_name.text.toString()
                student.stuNumber=s_number.text.toString()
                student.stuCollege=s_college.text.toString()
                if(onStudentData.schoolId!=1){
                    student.schoolId=onStudentData.schoolId
                }
            }else{//提示认证数据为空
                isOk=false
                Toast.makeText(this,"认证信息不完善,若需认证请完善信息",Toast.LENGTH_SHORT).show()
            }
        }
        if(isOk){
            s_up_data.isEnabled=false
            s_up_data.setBackgroundColor(Color.parseColor("#bfbfbf"))
            if(headImg.size>0&&renZR.size>0&&renZO.size>0){
                editStudentModel.doEditStudentData(this,student,headImg[0],renZO[0],renZR[0])
            }else if (headImg.size>0&&(renZO.size==0||renZR.size==0)){
                editStudentModel.doEditStudentData(this,student,headImg[0])
            }else if (headImg.size==0&&renZR.size>0&&renZO.size>0){
                editStudentModel.doEditStudentData(this,student,renZO[0],renZR[0])
            }else if (headImg.size==0&&(renZO.size==0||renZR.size==0)){
                editStudentModel.doEditStudentData(this,student)
            }
        }
    }
    //图片选择，上传图片
    private fun openPictureSelect() {
        result.clear()
        var j = false
        if(!nowStatus){
            j=true
        }
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage()) //相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                //.openCamera()//单独使用相机 媒体类型 PictureMimeType.ofImage()、ofVideo()
                .theme(R.style.picture_default_style) // xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
                .loadImageEngine(GlideEngine.createGlideEngine()) // 图片加载引擎 需要 implements ImageEngine接口
                .selectionMode(PictureConfig.SINGLE) //单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(false) //开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
                // .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回
                // .isWeChatStyle()//开启R.style.picture_WeChat_style样式
                //.setPictureStyle()//动态自定义相册主题
                //.setPictureCropStyle()//动态自定义裁剪主题
                .setPictureWindowAnimationStyle(mWindowAnimationStyle) //相册启动退出动画
                .isCamera(false) //列表是否显示拍照按钮
                //.isZoomAnim(pi)//图片选择缩放效果
                // .imageFormat()//拍照图片格式后缀,默认jpeg, PictureMimeType.PNG，Android Q使用PictureMimeType.PNG_Q
                .maxSelectNum(1) //最大选择数量,默认9张
                .minSelectNum(1) // 最小选择数量
                .compress(true) //是否压缩
                .compressFocusAlpha(true) //压缩后是否保持图片的透明通道
                .minimumCompressSize(2) // 小于多少kb的图片不压缩
                .imageSpanCount(4) //列表每行显示个数
                .openClickSound(false) //是否开启点击声音
                .selectionMedia(result) //是否传入已选图片
                .previewImage(true) //是否预览图片
                .enableCrop(true) //是否开启裁剪
                .freeStyleCropEnabled(true) //裁剪框是否可拖拽
                .circleDimmedLayer(nowStatus) // 是否开启圆形裁剪
                .setCircleDimmedBorderColor(R.color.textColorBlue) //设置圆形裁剪边框色值
                .setCircleStrokeWidth(2) //设置圆形裁剪边框粗细
                .showCropFrame(j) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(j) //是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(true) //裁剪是否可旋转图片
                .scaleEnabled(true) //裁剪是否可放大缩小图片
                .isDragFrame(true) //是否可拖动裁剪框(固定)
                .hideBottomControls(false) //显示底部uCrop工具栏
                .cutOutQuality(70) //裁剪后输出质量，默认100
                .setLanguage(LanguageConfig.CHINESE) //国际化语言 LanguageConfig.CHINESE、ENGLISH、JAPAN等
                .isMaxSelectEnabledMask(true) //选择条件达到阀时列表是否启用蒙层效果
                .forResult(MyResultCallback()) //结果回调分两种方式onActivityResult()和OnResultCallbackListener方式

    }

     inner class MyResultCallback : OnResultCallbackListener<LocalMedia?> {
        override fun onResult(result1: List<LocalMedia?>) {
            if(result1.size>0){
                isUpdate=true
            }
            if(who==1){
                if(result1[0]!=null){
                    headImg.add(result1[0]!!)
                    val requestOptions = RequestOptions().circleCrop()
                    Glide.with(this@StudentEditActivity).load(headImg[0].compressPath).apply(requestOptions).into(s_img)
                }
            }else if (who==2){
                if(result1[0]!=null){
                    isUpIdentity=true
                    renZO.add(result1[0]!!)
                    Glide.with(this@StudentEditActivity).load(renZO[0].compressPath).into(s_ren_z1)
                }
            }else if (who==3){
                if(result1[0]!=null){
                    isUpIdentity=true
                    renZR.add(result1[0]!!)
                    Glide.with(this@StudentEditActivity).load(renZR[0].compressPath).into(s_ren_z2)
                }
            }

        }

        override fun onCancel() {}
    }
    private fun upUserData() {
        editStudentModel.GetStudentDataByPhone(this,phone)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveMsg(message: EventBusPostType) {
        if (message.type == 1) {
            popupWindow!!.dismiss()
            val message1 = Message()
            message1.what = 21
            message1.obj = message.position
            handler.sendMessage(message1)
        }
    }

    override fun getStuDataFailed(int: Int) {

        val message=Message()
        message.what=99
        message.obj=int
        handler.sendMessage(message)
    }

    override fun getStuDataSuccess(student: Student) {
        var user = getSharedPreferences(student.phone, Context.MODE_PRIVATE).edit()
        user.putInt("id", student.id)
        user.putInt("schoolId", student.schoolId)
        user.putString("stuNumber", student.stuNumber)
        user.putString("stuName", student.stuName)
        user.putString("stuNickName", student.stuNickname)
        user.putString("stuCollege", student.stuCollege)
        user.putString("headPath", student.headPath)
        user.putString("recommend", student.recommend)
        user.putString("stuIdentityImgO", student.identityimgObverse)
        user.putString("stuIdentityImgR", student.identityimgReverse)
        user.putInt("stuIsIdentity", student.isIdentity)
        user.putString("stuSex", student.sex)
        user.putString("stuGrade", student.grade)
        user.putString("stuSpecialty", student.specialty)
        user.putString("userData", Gson().toJson(student))
        user.commit()

        finish()
    }
    override fun doEditStudentFailed(int: Int) {

        val message=Message()
        message.what=99
        message.obj=int
        handler.sendMessage(message)
    }

    override fun doEditStudentSuccess() {

        //获取最新信息
        val message=Message()
        message.what=81
        handler.sendMessage(message)

    }

}