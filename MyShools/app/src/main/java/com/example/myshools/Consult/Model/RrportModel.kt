package com.example.myshools.Consult.Model

import com.example.myshools.Consult.Entity.GetReportResultInfo
import com.example.myshools.Consult.Entity.GetRrportResult
import com.example.myshools.Mine.student.StudentReport
import com.example.myshools.entity.Constant
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RrportModel {
    fun getStudentREportByShcoolId(callback:getReportListStatuss,schoolId:Int,page:Int){
        var okHttpClient= OkHttpClient()
        val body = FormBody.Builder()

                .add("offset",page.toString())
                .add("limmit",10.toString())
                .add("schoolId", schoolId.toString()).build()
        val request = Request.Builder().url(Constant.url + "student-report/getListBySchoolId").post(body).build()
        val call: Call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.getReportListFailed(1)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val getNewsResult= Gson().fromJson<GetRrportResult>(string, GetRrportResult::class.java)
                if(getNewsResult.code==2000){
                    val resultInfo: GetReportResultInfo = getNewsResult.data as GetReportResultInfo
                    callback.getReportListSuccess(resultInfo.records)
                }else{
                    callback.getReportListFailed(2)
                }
                call.clone()
            }
        })
    }
    fun getStudentREportByUserId(callback:getReportListStatuss,userId:Int,page:Int){
        var okHttpClient= OkHttpClient()
        val body = FormBody.Builder()

                .add("offset",page.toString())
                .add("limmit",10.toString())
                .add("studentId", userId.toString()).build()
        val request = Request.Builder().url(Constant.url + "student-report/getList").post(body).build()
        val call: Call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.getReportListFailed(1)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val string = response.body()!!.string()
                val getNewsResult= Gson().fromJson<GetRrportResult>(string, GetRrportResult::class.java)
                if(getNewsResult.code==2000){
                    val resultInfo: GetReportResultInfo = getNewsResult.data as GetReportResultInfo
                    callback.getReportListSuccess(resultInfo.records)
                }else{
                    callback.getReportListFailed(2)
                }
                call.clone()
            }
        })
    }
    interface getReportListStatuss{
        fun getReportListFailed(i:Int)
        fun  getReportListSuccess(list: MutableList<StudentReport>)
    }
}