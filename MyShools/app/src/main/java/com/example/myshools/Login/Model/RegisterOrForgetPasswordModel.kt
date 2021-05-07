package com.example.myshools.Login.Model

import com.example.myshools.Login.SendVerificationCodeToJson
import com.example.myshools.entity.Constant
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RegisterOrForgetPasswordModel{
    fun sendVerifictionCode(callback:sendVerifictionStatus,phone:String){
        callback.sending()
        if(phone.length!=0){

            val builder = FormBody.Builder().add("phone",phone)
            val body = builder
                    .build()
            val request = Request.Builder()
                    .post(body)
                    .url(Constant.url)
                    .build()
            var okHttpClient= OkHttpClient()
            val call: Call = okHttpClient.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.sendFailed(0) }
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    //请求成功 succeed ，，获取返回数据时 回调此方法
                    val jsonStr = response.body()!!.string()
                    val Code: SendVerificationCodeToJson = Gson().fromJson(jsonStr, SendVerificationCodeToJson::class.java)
                    when(Code.resultType){
                        1 ->callback.sendSuccess(Code.result)
                        0 ->callback.sendFailed(1)//请检查手机号重新发送
                    }
                }
            })
        }else{
            callback.sendFailed(1)
        }
    }
    fun doUserRegister(callback:RegisterStatus,role:Int,phone:String,password: String){
        callback.registing()
        val builder= FormBody.Builder().add("role",role.toString()).add("phone",phone).add("password",password)
        val body=builder.build()
        val request= Request.Builder().post(body).url(Constant.url).build()
        val okHttpClient= OkHttpClient()
        val call: Call =okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.registerFailed(0)
            }

            override fun onResponse(call: Call, response: Response) {
                val result=response.body().toString()
                val res=result.toInt()
                if(res==1){
                    callback.registerSuccess()
                }else{
                    callback.registerFailed(1)
                }
            }
        })
    }
    fun doEditPassword(callback:EditPassword,role: Int,phone: String,password: String){
        callback.editing()
        val builder= FormBody.Builder().add("role",role.toString()).add("phone",phone).add("password",password)
        val body=builder.build()
        val request= Request.Builder().post(body).url(Constant.url).build()
        val okHttpClient= OkHttpClient()
        val call: Call =okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.editFailed(0)
            }

            override fun onResponse(call: Call, response: Response) {
                val result=response.body().toString()
                val res=result.toInt()
                if(res==1){
                    callback.editSuccess()
                }else{
                    callback.editFailed(1)
                }
            }
        })

    }
    interface sendVerifictionStatus{
        fun sending()
        fun sendSuccess(code:String)
        fun sendFailed(fialedType:Int)//0网络错误，1检查账号

    }
    interface RegisterStatus{
        fun registing()
        fun registerFailed(FiledType:Int)//0网络连接失败，1手机号已被注册
        fun registerSuccess()
    }
    interface EditPassword{
        fun editing()
        fun editSuccess()
        fun editFailed(FiledType: Int)//0网络连接失败，1不存在该用户
    }
}