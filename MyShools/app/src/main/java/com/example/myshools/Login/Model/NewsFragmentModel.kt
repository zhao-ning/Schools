package com.example.myshools.Login.Model

import android.os.Message
import android.util.Log
import com.example.myshools.Activity.entity.GetActivityResult
import com.example.myshools.Activity.entity.GetActivityResultInfo
import com.example.myshools.entity.Activities
import com.example.myshools.entity.Constant
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class NewsFragmentModel {
    fun doGetSchoolNews(callback: GetSchoolNewsStatus,num:Int,schoolId:Int,type:String){
        callback.doGettingNews()
        var okHttpClient=OkHttpClient()
        val body = FormBody.Builder()
                .add("tag",type)
                .add("offset",num.toString())
                .add("limmit",10.toString())
                .add("schoolId", schoolId.toString()).build()
        val request = Request.Builder().url(Constant.url + "activity/getList").post(body).build()
        val call: Call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
              callback.doGetNewsFailed()
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val getNewsResult=Gson().fromJson<GetActivityResult>(string, GetActivityResult::class.java)
                var resultInfo: GetActivityResultInfo = getNewsResult.data
                callback.doGetNewsSucceed(resultInfo.records)
                call.clone()
            }
        })
    }
}
interface GetSchoolNewsStatus {
    fun doGettingNews()
    fun doGetNewsFailed()
    fun doGetNewsSucceed(dataList:MutableList<Activities>)
}
