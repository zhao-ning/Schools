package com.example.myshools.Mine.student.model

import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.myshools.Mine.student.AddStudentReportResful
import com.example.myshools.Mine.student.StudentReport
import com.example.myshools.Mine.student.UpdateStudentReportResful
import com.example.myshools.entity.Activities
import com.example.myshools.entity.Constant
import com.example.myshools.entity.Restful
import com.example.myshools.entity.RestfulActivity
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class PublishShareModel {
    private var client: OkHttpClient? = OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
            .readTimeout(60000, TimeUnit.MILLISECONDS)
            .writeTimeout(60000, TimeUnit.MILLISECONDS)
            .build()


    //根据组织id创建新的校友圈分享
    fun doAddNewShare(callback: onAddShareStatus, uId:Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", uId.toString()).build()
        val request = Request.Builder().url(Constant.url + "student-report/new").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onAddShareFailed()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()

                val result=Gson().fromJson<AddStudentReportResful>(str,AddStudentReportResful::class.java)
                if (result.code == 2000) {
                    callback.onAddShareSuccess(result.data)
                } else {
                    callback.onAddShareFailed()
                }
            }
        })
    }
    //根据校友圈分享，修改信息
    fun doUpdateShare(callback: onUpdataStaus,Share: StudentReport,result:List<LocalMedia>){
        //将re转为json字符串

        //将re转为json字符串
        val MutilPart_Form_Data = MediaType.parse("application/octet-stream;charset=utf-8")
        var requestBodyBuilder: MultipartBody.Builder? = null
        requestBodyBuilder = MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data;charset=utf-8"))
                .addFormDataPart("studentReport", Gson().toJson(Share)) // 传递表单数据

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
        val request = Request.Builder().url(Constant.url + "student-report/save")
                .post(requestBody)
                .build()
        val call: Call = client!!.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                client!!.dispatcher().cancelAll()
                client!!.connectionPool().evictAll()
                client!!.newCall(request).enqueue(this)
                callback.onUpdateFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                Log.e("RESULTSSS",str.toString())
                val result=Gson().fromJson<UpdateStudentReportResful>(str,UpdateStudentReportResful::class.java)
                if(result.code==2000){
                    callback.onUpdateSuccess(1)
                }else{
                    callback.onUpdateFailed(13)
                }
            }
        })

    }
    //根据分享id，删除活动
    fun doDeleteShareById(callback: onDeleteStatus,aId: Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", aId.toString()).build()
        val request = Request.Builder().url(Constant.url + "student-report/delete").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            callback.onDeleteFailed()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                var restful= Gson().fromJson<Restful>(str, Restful::class.java)

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
    interface onAddShareStatus {
        fun onAddShareFailed()
        fun onAddShareSuccess(studentReport: StudentReport)
    }
    interface onUpdataStaus{
        fun onUpdateFailed(result:Int)
        fun onUpdateSuccess(i:Int)
    }

}