package com.example.myshools.Activity.Model

import android.os.Message
import android.util.Log
import com.example.myshools.Activity.entity.FindApplyResult
import com.example.myshools.Activity.entity.FindNumDataResult
import com.example.myshools.entity.Constant
import com.example.myshools.entity.PeopleNumManagement
import com.example.myshools.entity.Restful
import com.example.myshools.entity.Restful4
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ApplyModel {
    //根据用户id查看是否参加过该活动
    fun doGetIsApply(callback:doGetIsApplyStatus,uId:Int,aId: Int){
        callback.doGetIsApplying()
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("uId", uId.toString())
                .add("aId",aId.toString()).build()
        val request = Request.Builder().url(Constant.url + "apply/getByUId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                callback.doGetIsApplyDFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val applyResult=Gson().fromJson<FindApplyResult>(str, FindApplyResult::class.java)
                //，0m没参加过,1是已经提交数据正在申请，2是已经通过申请,4拒绝
                if (applyResult.code == 2000) {
                    if(applyResult.data.applyResult==1){
                        callback.doGetIsApplySuccess(1)
                    }else if (applyResult.data.applyResult==2){
                        callback.doGetIsApplySuccess(2)
                    }else if (applyResult.data.applyResult==4){
                        callback.doGetIsApplySuccess(4)
                    }
                } else if (applyResult.code==9000){
                    callback.doGetIsApplySuccess(0)
                }else {
                    callback.doGetIsApplyDFailed(1)
                }
            }
        })
    }
    //根据活动id查询活动信息
    fun doGetThisActivityApplyData(callback:doGetActivityApplyDataStatus,id:Int,position:Int){
        callback.doGetAcyApplyDing()
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", id.toString()).build()
        val request = Request.Builder().url(Constant.url + "people-num-management/getListByAId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.doGetAcyApplyDFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val result=Gson().fromJson<FindNumDataResult>(str, FindNumDataResult::class.java)
                if (result.code == 2000) {
                    callback.doGetAcyApplyDSuccess(result.data,position)
                } else {
                    callback.doGetAcyApplyDFailed(2)
                }
            }
        })

    }
    fun doAddActivityClickNum(callback:doAddClickNumStatus,aId: Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder()
                .add("id",aId.toString())
                .build()
        val request = Request.Builder().url(Constant.url + "activity/addClick").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val restful=Gson().fromJson<Restful4>(str,Restful4::class.java)
                if (restful.code == 2000) {
                    callback.doAddClickNumSuccess(1)
                }
            }
        })
    }

    //没有参加过开启占位操作,根据活动id，开启占位
    fun doZhanWei(callback:doSetZhanWeiStatus,aId:Int){
        callback.startSetZhanwei()
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", aId.toString()).build()
        val request = Request.Builder().url(Constant.url + "people-num-management/addByAId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                callback.SetZhanweiFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                var findApplyResult=Gson().fromJson<FindApplyResult>(str,FindApplyResult::class.java)

                if (findApplyResult.code == 2000) {
                   callback.SetZhanweiSuccess()
                } else {
                  callback.SetZhanweiFailed(2)
                }
            }
        })
    }
    //取消占位操作
    fun doCancelZhanWei(callback:doCancelZhanWeiStatus,aId:Int){
        callback.cancelSetZhanwei()

        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", aId.toString()).build()
        val request = Request.Builder().url(Constant.url + "people-num-management/replaceByAId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
               callback.cancelZhanweiFailed(1)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                var findApplyResult=Gson().fromJson<FindApplyResult>(str,FindApplyResult::class.java)
                if (findApplyResult.code == 2000) {
                    //成功，结束占位
                    callback.cancelZhanweiSuccess(1)
                }else{
                    callback.cancelZhanweiFailed(2)
                }
            }
        })
    }
    //提交用户申请信息
    fun doSendApplyData(callback:doSendApplyDataStatus,aId:Int,uId: Int,answer:String,applyTime:String)
    {   var answer1:String
        if(answer== null){
            answer1=""
        }else{
            answer1=answer
        }
        callback.sendingApplyData()
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("aId", aId.toString())
                .add("aId",aId.toString())
                .add("uId",uId.toString())
                .add("answer",answer1)
                .add("applyTime",applyTime)
                .build()
        val request = Request.Builder().url(Constant.url + "apply/addApply").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                callback.sendApplyDataFailed(1)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                val restful=Gson().fromJson<Restful>(str,Restful::class.java)
                if (restful.code == 2000) {
                    callback.sendApplyDataSuccess(1)
                } else {
                    callback.sendApplyDataFailed(2)
                }
            }
        })
    }

    interface doSendApplyDataStatus {
        fun sendingApplyData()
        fun sendApplyDataFailed(i:Int)//1网络连接失败,2失败，重新设置
        fun sendApplyDataSuccess(i:Int)//1成功
    }


    //取消占位
    interface doCancelZhanWeiStatus {
        fun cancelSetZhanwei()
        fun cancelZhanweiFailed(i:Int)//1网络连接失败,2失败，重新设置
        fun cancelZhanweiSuccess(i:Int)//1成功
    }

    //设置占位
    interface doSetZhanWeiStatus {
        fun startSetZhanwei()
        fun SetZhanweiFailed(i:Int)//1网络连接失败，2失败
        fun SetZhanweiSuccess()//1成功

    }
    //是否参加过该活动
    interface doGetIsApplyStatus {
        fun doGetIsApplying()
        fun doGetIsApplySuccess(re: Int)//1参加过，0没参加过
        fun doGetIsApplyDFailed(re:Int)//1网络连接失败
    }

    interface doGetActivityApplyDataStatus {
        fun doGetAcyApplyDing()
        fun doGetAcyApplyDSuccess(peopleNumManagement: PeopleNumManagement,position: Int)
        fun doGetAcyApplyDFailed(re:Int)
    }
    interface doAddClickNumStatus{
        fun doAddClickNumSuccess(i:Int)
    }

}


