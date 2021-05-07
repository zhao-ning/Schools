package com.example.myshools.Mine

import android.util.Log
import com.example.myshools.entity.Constant
import com.example.myshools.entity.ResultStatus
import com.example.myshools.entity.School
import com.google.gson.Gson

import okhttp3.*
import java.io.IOException

class MainActivityModel{
    //获取所有学校信息
    fun getAllSchoolsInformation(callback: GetSchoolStatus){
        callback.getting()
        val request = Request.Builder()
                .url(Constant.url+"school/list").get()
                .build()
        var okHttpClient= OkHttpClient()
        val call: Call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.getFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //请求成功 succeed ，，获取返回数据时 回调此方法
                val jsonStr = response.body()!!.string()
                val json=Gson()
                val status=json.fromJson<ResultStatus>(jsonStr,ResultStatus::class.java)
                if(status.code.equals("0")){

                    callback.getSuccess(status.message)
                }else{
                    callback.getFailed(2)
                }
            }
        })
    }
    interface GetSchoolStatus{
        fun getting()
        fun getFailed(i:Int)
        fun getSuccess(schoolList: MutableList<School>)
    }
}