package com.example.myshools.Login.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myshools.Login.Model.LoginModel
import com.example.myshools.MainActivity
import com.example.myshools.Mine.Settings.UserBean
import com.example.myshools.R
import com.example.myshools.entity.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.login_activity.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LoginActivity:AppCompatActivity(), LoginModel.onDoLoginStatusChange {
    var role:Int=-1

    var message:Message= Message()
    private val loginModel by lazy{
        LoginModel()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor= 0xff8f7145.toInt()
        }
        //检查是否登录过
        checkISHaveUserInformation()

        //监听页面按钮
        initListener()

    }

    private fun checkISHaveUserInformation() {
        val phone=getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account","")
        if(phone.toString().length!=0){
            var userInformationSp:SharedPreferences=getSharedPreferences(phone, Context.MODE_PRIVATE)
            val role:Int=userInformationSp.getInt("role",-1)
            val phone: String? =userInformationSp.getString("phone","")
            val password:String?=userInformationSp.getString("password","")
            if(role!=-1){
                user_phone.setText(phone)
                user_password.setText(password)
                login_role_spinner.setSelection(role+1)
                checkUserInformation()
            }
        }
    }

    val handler:Handler= object:Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            user_login.isEnabled=true
            when(msg.what){
                1 -> {Toast.makeText(this@LoginActivity,"密码格式错误",Toast.LENGTH_SHORT).show()}
                2 -> {Toast.makeText(this@LoginActivity,"请检查账号重新输入",Toast.LENGTH_SHORT).show()}
                3 ->{Toast.makeText(this@LoginActivity,"请选择用户角色",Toast.LENGTH_SHORT).show() }
                4 ->{Toast.makeText(this@LoginActivity,"网络连接失败。。。",Toast.LENGTH_SHORT).show() }
                5 ->{//Toast.makeText(this@LoginActivity,"用户不存在，请检查账号是否正确",Toast.LENGTH_SHORT).show()
                     }
                6 ->{Toast.makeText(this@LoginActivity,"密码输入错误",Toast.LENGTH_SHORT).show()}
                7 ->{Toast.makeText(this@LoginActivity,"信息未保存，请重新设置",Toast.LENGTH_SHORT).show()}
                11 ->{
                    user_login.isEnabled=false
                   // Toast.makeText(this@LoginActivity,"开始登陆",Toast.LENGTH_SHORT).show()
                }
                12->{
                    user_login.isEnabled=true


                }
            }
            user_login.isEnabled=true
        }
    }
    private fun initListener() {
        //设置角色
        selectRole()
        //去进行登录
        toLogin()
        //忘记密码
        forgetPassword()
        //用户注册
        userRegister()
    }
/*
* 跳转用户注册页面
* */
    private fun userRegister() {
        user_register.setOnClickListener(View.OnClickListener {
            val startUserRegisterActivityIntent=Intent(this, UserRegisterActivity::class.java)
            startActivity(startUserRegisterActivityIntent)
        })
    }
/*
* 跳转忘记密码页面
* */
    private fun forgetPassword() {
        forget_password.setOnClickListener(View.OnClickListener {
            val startForgetPasswordActivityIntent=Intent(this, ForgetPasswordActivity::class.java)
            startActivity(startForgetPasswordActivityIntent)
        })
    }
    /*
    * 用户选择角色
   * */
    private fun selectRole() {

        login_role_spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2==1){
                    role=1
                }else if (p2==2){
                    role=0
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


    }

    private fun toLogin() {


        user_login.setOnClickListener(View.OnClickListener {
            var user=getSharedPreferences(user_phone.text.toString(), Context.MODE_PRIVATE).edit()

            //是否选择角色
            if(role!=-1){
                //账号格式是否正确
                if(user_phone.text.toString().length==11){
                    //密码长度是否正确
                    if (user_password.text.toString().length>16 || user_password.text.toString().length<8){
                        //提示密码错误
                        message.what=1
                        handler.sendMessage(message)

                    }else{
                        user.putInt("role",role)
                        user.putString("phone",user_phone.text.toString())
                        user.putString("password",user_password.text.toString())
                        user.commit()
                        checkUserInformation()
                    }
                }else{
                    //提示账号格式错误
                   message.what=2
                    handler.sendMessage(message)
                }

            }else{
                //提示未选择用户角色
                message.what=3
                handler.sendMessage(message)
            }
        })
    }

    //连接数据库验证用户信息
    private fun checkUserInformation() {
        //验证正确用户进行登录操作，此操作异步
        //真正使用时去掉注释
       loginModel.doLogin(this,user_phone.text.toString(),user_password.text.toString(),role)
    }

    /*
    * 开始登录
    * */
    override fun onLoading() {
        //禁止按钮
        val message=Message()
        message.what=11
        handler.sendMessage(message)

    }
    override fun onLoadingSuccess(userData: Restful0) {

        var nextAccount=getSharedPreferences("nextAccount", Context.MODE_PRIVATE).edit()
        nextAccount.putString("account",user_phone.text.toString())
        nextAccount.commit()
        var student:Student
        var orgnaization:Orgnaization

        var user=getSharedPreferences(user_phone.text.toString(), Context.MODE_PRIVATE).edit()
        user.putInt("role",role)
        var account=getSharedPreferences("accounts", Context.MODE_PRIVATE).edit()
        val userMap=getSharedPreferences("accounts", Context.MODE_PRIVATE).getString("userMap","")
        var userBean=UserBean()
        //将用户信息保持到sp，同时将账号信息保存到sp
        if(role==1){//存入学生信息
           student=userData.restful.data
            userBean.role=role
            userBean.userHeadImg=student.headPath
            userBean.userPhone=student.phone
            userBean.userName=student.stuNickname
            userBean.userStatus=1
            user.putInt("id",student.id)
            user.putInt("schoolId",student.schoolId)
            user.putString("stuNumber",student.stuNumber)
            user.putString("stuName",student.stuName)
            user.putString("stuNickName",student.stuNickname)
            user.putString("stuCollege",student.stuCollege)
            user.putString("headPath",student.headPath)
            user.putString("recommend",student.recommend)
            user.putString("stuIdentityImgO",student.identityimgObverse)
            user.putString("stuIdentityImgR",student.identityimgReverse)
            user.putInt("identity",student.isIdentity)
            user.putString("stuSex",student.sex)
            user.putString("stuGrade",student.grade)
            user.putString("stuSpecialty",student.specialty)
            user.putString("userData",Gson().toJson(student))
        }else{
            orgnaization=userData.restful2.data
            userBean.role=role
            userBean.userHeadImg=orgnaization.headPath
            userBean.userPhone=orgnaization.phone
            userBean.userName=orgnaization.name
            userBean.userStatus=1
            user.putInt("id",orgnaization.id)
            user.putInt("schoolId", orgnaization.schoolId)
            user.putInt("identity", orgnaization.identity)
            user.putInt("popularity", orgnaization.popularity)
            user.putString("name", orgnaization.name)
            user.putString("level", orgnaization.level)
            user.putString("headPath", orgnaization.headPath)
            user.putString("recommend", orgnaization.recommend)
            user.putString("identityContent", orgnaization.identityContent)
            user.putString("connectionImgPath", orgnaization.connectionImgPath)
            user.putString("connectionContent", orgnaization.connectionContent)
        }
        var list=Gson().fromJson<MutableList<UserBean>>(userMap,object :TypeToken<MutableList<UserBean?>?>(){}.type)
        if(list==null){
           list=ArrayList<UserBean>()
        }else{
            var us=UserBean()
            list.get(0).userStatus=0
            for(u in list){
                if(u.id==userBean.id&& u.role==userBean.role){
                    us=u
                    continue
                }
            }
            if(us.userName!=null){
                list.remove(us)
            }

        }
        list.add(0,userBean)
        account.putString("userMap",Gson().toJson(list))
        account.commit()
        user.commit()
        val message=Message()
        message.what=12
        handler.sendMessage(message)
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)

    }
    override fun onLoadingFailed(filedType:Int){

        var message1:Message=Message()
        when(filedType){
            0 ->message1.what=4
            1 -> message1.what=5
            2 -> message1.what=6
            3 ->message1.what=7
        }
        handler.sendMessage(message1)

    }

}