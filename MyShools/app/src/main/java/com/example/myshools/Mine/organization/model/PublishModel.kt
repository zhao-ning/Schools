package com.example.myshools.Mine.organization.model

import android.os.Message
import com.example.myshools.entity.*
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class PublishModel {
    private var client: OkHttpClient? = OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .build()
    //根据组织id创建新的活动

    fun doAddNewActivity(callback:onAddNewsStatus,oId:Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", oId.toString()).build()
        val request = Request.Builder().url(Constant.url + "activity/new").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onAddNewsFailed()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                var findApplyResult= Gson().fromJson<RestfulActivity>(str, RestfulActivity::class.java)
                if (findApplyResult.code == 2000) {
                    callback.onAddNewsSuccess(findApplyResult.data)
                } else {
                    callback.onAddNewsFailed()
                }
            }
        })

    }
    //根据活动id，修改活动信息
    fun doUpdateActivity(callback: onUpdataStaus,activities: Activities,result:List<LocalMedia>){
        //将re转为json字符串

        //将re转为json字符串
        val MutilPart_Form_Data = MediaType.parse("application/octet-stream;charset=utf-8")
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("activities", Gson().toJson(activities)) // 传递表单数据

        // 可使用for循环添加img file
        if (result.size > 1) {
            for (i in 0 until result.size - 1) {
                val file = File(result.get(i).getCompressPath())
                val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file) //把文件与类型放入请求体
                if (file.exists()) {
                    requestBodyBuilder.addFormDataPart("file", file.name, requestBody) //文件名,请求体里的文件
                }
            }
        }
        // 3.3 其余一致
        val requestBody: RequestBody = requestBodyBuilder.build()
        val request = Request.Builder().url(Constant.url + "activity/save")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.onUpdateFailed(11)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()

                val result=Gson().fromJson<Restful4>(str,Restful4::class.java)
                if(result.code==2000){
                    callback.onUpdateSuccess(12)
                }else{
                    callback.onUpdateFailed(13)
                }
            }
        })

    }
    //根据活动id，删除活动
    fun doDeleteActivityById(callback: onDeleteStatus,aId: Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", aId.toString()).build()
        val request = Request.Builder().url(Constant.url + "activity/delete").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            callback.onDeleteFailed()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                var restful= Gson().fromJson<DeleteRestful>(str, DeleteRestful::class.java)

                if (restful.code == 2000) {
                    callback.onDeleteSuccess()
                } else {
                    callback.onDeleteFailed()
                }
            }
        })

    }



    interface onDeleteStatus{
        fun onDeleteSuccess()
        fun onDeleteFailed()
    }
    interface onAddNewsStatus {
        fun onAddNewsFailed()
        fun onAddNewsSuccess(activities: Activities)
    }
    interface onUpdataStaus{
        fun onUpdateFailed(result:Int)
        fun onUpdateSuccess(i:Int)
    }

}