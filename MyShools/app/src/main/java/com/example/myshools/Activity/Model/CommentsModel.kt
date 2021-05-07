package com.example.myshools.Activity.Model

import android.util.Log
import com.example.myshools.Activity.GetCommentResful
import com.example.myshools.Activity.ResultComment
import com.example.myshools.Activity.entity.FindApplyResult
import com.example.myshools.entity.Comment
import com.example.myshools.entity.Constant
import com.example.myshools.entity.Restful1212
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class CommentsModel {
    fun getCommentsByAId(callback:getCommentStatus,aId:Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("id", aId.toString()).build()
        val request = Request.Builder().url(Constant.url + "comment/getByNewsId").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            callback.getCommentFailed(1)

            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                var findApplyResult= Gson().fromJson<GetCommentResful>(str, GetCommentResful::class.java)
                if (findApplyResult.code == 2000) {
                  /*  var list=Gson().fromJson<MutableList<ResultComment>>(findApplyResult.data.toString(),object :TypeToken<MutableList<ResultComment>>(){}.type)
                    Log.e("TAG)))", list.size.toString())*/

                  callback.getCommentSuccess(findApplyResult.data)
                }else{
                    callback.getCommentFailed(2)
                }
            }
        })



    }
    //上传评论
    fun sendComment(callback:sendCommentsStatus,comment:Comment,position: Int){
        val okHttpClient = OkHttpClient()
        val body = FormBody.Builder().add("comment", Gson().toJson(comment)).build()
        val request = Request.Builder().url(Constant.url + "comment/add").post(body).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.sendCommendsFailed(1)

            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val str = response.body()!!.string()
                var findApplyResult= Gson().fromJson<GetCommentResful>(str, GetCommentResful::class.java)
                if (findApplyResult.code == 2000) {
                    /*  var list=Gson().fromJson<MutableList<ResultComment>>(findApplyResult.data.toString(),object :TypeToken<MutableList<ResultComment>>(){}.type)
                      Log.e("TAG)))", list.size.toString())*/

                    callback.sendCommendSuccess(position)
                }else{
                    callback.sendCommendsFailed(2)
                }
            }
        })


    }
    interface sendCommentsStatus{
        fun sendCommendsFailed(i:Int)
        //1是回复数据，2是上传评论数据
        fun sendCommendSuccess(position: Int)

    }

    interface getCommentStatus{
        fun getCommentFailed(i:Int)
        fun getCommentSuccess(list: MutableList<ResultComment>)
    }
}