package com.example.myshools.Mine.organization.model

import android.os.Message
import android.util.Log
import com.example.myshools.Mine.organization.historyActivityManage.entity.GetActivitiesByOIdResult
import com.example.myshools.entity.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class HistotryManageModel {
    //根据组织的id获取组织的所有活动
    fun doGetAllActivityByOId(callback: getAllOAStatus, oId:Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", oId.toString()).build()
        val request = Request.Builder().url(Constant.url + "activity/getListByOrganizationId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
              callback.getAllOAFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val gson = Gson()
                val getActivitiesByOIdResult = gson.fromJson<GetActivitiesByOIdResult>(string,GetActivitiesByOIdResult::class.java)
         if (getActivitiesByOIdResult.code==2000){
             callback.getAllOASuccess(getActivitiesByOIdResult.data)

         }else{
             callback.getAllOAFailed(2)

         }
            }
        })

    }
    //根据用户的id获取所有申请过的活动
    fun doGetAllActivityBySId(callback: getStuApplyStatus, sId:Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", sId.toString()).build()
        val request = Request.Builder().url(Constant.url + "activity/getByUId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.getSAllOAFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val gson = Gson()
                val getActivitiesByOIdResult = gson.fromJson<StudentGetActivitiesRestful>(string,StudentGetActivitiesRestful::class.java)
                if (getActivitiesByOIdResult.code==2000){
                    callback.getSAllOASuccess(getActivitiesByOIdResult.data)

                }else{
                    callback.getSAllOAFailed(2)

                }
            }
        })

    }
    interface getAllOAStatus {
        fun getAllOAFailed(i:Int)
        fun getAllOASuccess(list: MutableList<Activities>)
    }
    interface  getStuApplyStatus{
        fun getSAllOAFailed(i:Int)
        fun getSAllOASuccess(list: List<GetStudentApplyActivitiies>)
    }
}