package com.example.myshools.Mine.organization.historyActivityManage.model

import com.example.myshools.entity.Activities
import com.example.myshools.entity.Constant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException


class HistoryActivityModel {
    fun dogetHistoryActivities(callback:getListStatus,oId:Int){
        callback.doGettingList()
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("oId", oId.toString()).build()
        val request = Request.Builder().url(Constant.url + "activities/findHistoryActivityByOrgId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
             callback.doGetListFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val gson = Gson()
                val list = gson.fromJson<MutableList<Activities>>(string, object : TypeToken<MutableList<Activities?>?>() {}.type)
                callback.doGetListSuccess(list)
            }
        })
    }

    interface getListStatus {
        fun doGettingList()
        fun doGetListSuccess(list: MutableList<Activities>)
        fun doGetListFailed(i:Int)//1:请检查网络
    }
}


