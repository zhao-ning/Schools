package com.example.myshools.Login.Model

import android.util.Log
import com.example.myshools.entity.Constant
import com.example.myshools.entity.Restful
import com.example.myshools.entity.Restful0
import com.example.myshools.entity.Restful2
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.net.URLEncoder.encode
import java.text.Annotation
import java.util.*
import kotlin.collections.HashMap

class LoginModel {


    fun doLogin(callback:onDoLoginStatusChange,phone:String,password: String,role:Int) {
        callback.onLoading()
        //开始登录
        if(phone.length!=0){
           val builder = FormBody.Builder()
                   .add("role",role.toString())
                   .add("userName",phone)
                   .add("password",password)

            val body = builder
                    .build()
            val request = Request.Builder()
                    .post(body)
                    .url(Constant.url+"user/login")
                    .build()
            var okHttpClient=OkHttpClient()
            val call: Call = okHttpClient.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onLoadingFailed(0)

                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    //请求成功 succeed ，，获取返回数据时 回调此方法
                    val jsonStr = response.body()?.string()
                    var restful:Restful= Restful()
                    var restful2:Restful2= Restful2()
                    var restful0=Restful0()
                    if (role==0){
                        restful2=Gson().fromJson(jsonStr,Restful2::class.java)
                        restful0.code=restful2.code
                        restful0.restful2=restful2

                    }else if (role==1){
                        restful=Gson().fromJson(jsonStr,Restful::class.java)
                        restful0.code=restful.code
                        restful0.restful=restful

                    }

                    if(restful0.code==2000){
                        callback.onLoadingSuccess(restful0)
                    }else{
                        callback.onLoadingFailed(1)
                    }
                }
            })
        }else{
            callback.onLoadingFailed(3)
        }


    }

    interface onDoLoginStatusChange{
         fun onLoading()

         fun onLoadingSuccess(o:Restful0)
         //失败类型0：网络连接失败，1用户不存在，2密码错误
         fun onLoadingFailed(failedType:Int)
     }
}