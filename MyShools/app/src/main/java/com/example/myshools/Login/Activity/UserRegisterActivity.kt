package com.example.myshools.Login.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myshools.Login.Model.RegisterOrForgetPasswordModel
import com.example.myshools.R
import kotlinx.android.synthetic.main.user_register.*

class UserRegisterActivity:AppCompatActivity(), RegisterOrForgetPasswordModel.sendVerifictionStatus, RegisterOrForgetPasswordModel.RegisterStatus {
    private var role:Int=-1
    private var verificationCode:String=""
    private val register by lazy{
        RegisterOrForgetPasswordModel()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_register)
        initListener()
    }
    private val handler= @SuppressLint("HandlerLeak")
    object :Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what){
                1,3 -> Toast.makeText(this@UserRegisterActivity,"请检查手机号输入是否正确",Toast.LENGTH_SHORT).show()
                2,8 -> Toast.makeText(this@UserRegisterActivity,"网络连接失败。。。",Toast.LENGTH_SHORT).show()
                4 -> Toast.makeText(this@UserRegisterActivity,"请选择角色",Toast.LENGTH_SHORT).show()
                5 -> Toast.makeText(this@UserRegisterActivity,"验证码错误",Toast.LENGTH_SHORT).show()
                6 -> Toast.makeText(this@UserRegisterActivity,"密码输入格式错误",Toast.LENGTH_SHORT).show()
                7 -> Toast.makeText(this@UserRegisterActivity,"请确认密码是否一致",Toast.LENGTH_SHORT).show()
                9 -> Toast.makeText(this@UserRegisterActivity,"该手机号已被注册",Toast.LENGTH_SHORT).show()
                10 -> Toast.makeText(this@UserRegisterActivity,"注册成功",Toast.LENGTH_SHORT).show()
            }
            register_send_verification_code.isEnabled=true
            register_submit.isEnabled=true
        }
    }
    private fun initListener() {
        selectRole()
        //返回
        register_return_0.setOnClickListener(View.OnClickListener {
            finish()
        })
        //发送验证码
        register_send_verification_code.setOnClickListener(View.OnClickListener {
            register_send_verification_code.isEnabled=false
           if(register_user_phone.text.toString().length==11){
               register.sendVerifictionCode(this,register_user_phone.text.toString())
           }else{
               val message=Message()
               message.what=1
               handler.sendMessage(message)
           }
        })
        //注册
        register_submit.setOnClickListener(View.OnClickListener {
            //检查是否选择角色
            if(role!=-1){
                //检查手机号是否为空，是否变动
                if(register_user_phone.text.toString().length==11){
                    //检查验证码是否正确
                    if(register_verification_code.text.toString().equals(verificationCode)&&verificationCode!=""){
                        //检查密码1，2是否为空，是否一致
                        val password1=register_user_password_1.text.toString()
                        val password2=register_user_password_2.text.toString()
                        if(password1.length<17&&password1.length>7){
                          if(password2.length<17&&password2.length>7){
                              if(password1.equals(password2)){
                                  //开始注册
                                  register_submit.isEnabled=false
                                  register.doUserRegister(this,role,register_user_phone.text.toString(),password1)
                              }else{
                                  val message =Message()
                                  message.what=7
                                  handler.sendMessage(message)
                              }
                          }else{
                              val message =Message()
                              message.what=7
                              handler.sendMessage(message)
                          }
                        }else{
                            val message =Message()
                            message.what=6
                            handler.sendMessage(message)
                        }
                    }else{
                        val message=Message()
                        message.what=5
                        handler.sendMessage(message)
                    }
                }else{
                    val message=Message()
                    message.what=1
                    handler.sendMessage(message)
                }
            }else{
                val message=Message()
                message.what=4
                handler.sendMessage(message)
            }
        })
    }
    /*
   * 用户选择角色
  * */
    private fun selectRole() {

        register_role_spinner.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2==1){
                   role=0
                }else if (p2==2){
                    role=1
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


    }

    override fun sending() {

    }


    override fun sendSuccess(code: String) {
        verificationCode=code

    }

    override fun sendFailed(fialedType: Int) {
        val message=Message()
        if(fialedType==0){
            message.what=2
        }else{
            message.what=3
        }
        handler.sendMessage(message)


    }

    override fun registing() {

    }

    override fun registerFailed(FiledType: Int) {
        if(FiledType==0){
            val message=Message()
            message.what=8
            handler.sendMessage(message)
        }else{
            val message=Message()
            message.what=9
            handler.sendMessage(message)
        }
    }
    override fun registerSuccess() {
        val message=Message()
        message.what=10
        handler.sendMessage(message)
        var editor=getSharedPreferences("nextAccount", Context.MODE_PRIVATE).edit()
        editor.putString("account",register_user_phone.text.toString())
        editor.commit()
        var spEdit=getSharedPreferences(register_user_phone.text.toString(), Context.MODE_PRIVATE).edit()
        spEdit.putString("phone",register_user_phone.text.toString())
        spEdit.putString("password",register_user_password_1.text.toString())
        spEdit.putInt("role",role)
        spEdit.commit()

        finish()
    }

}