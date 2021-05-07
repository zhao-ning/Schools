package com.example.myshools.Consult.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.Activity.Adapter.GridViewItemImgAdapter
import com.example.myshools.Consult.Activity.ShareDetail
import com.example.myshools.Consult.Activity.ShareItemActivity
import com.example.myshools.Consult.Activity.StudentActivity
import com.example.myshools.Consult.Entity.Share
import com.example.myshools.Mine.student.StudentReport
import com.example.myshools.R
import com.example.myshools.entity.Constant
import com.example.myshools.entity.Student
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.share_item1.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/*
* 校园动态item
*
* */



class ShareRecycleViewAdapter(var dataList: MutableList<StudentReport>, var context: Context):
        RecyclerView.Adapter<ShareRecycleViewAdapter.ViewHolder>(){
    var requestOptions = RequestOptions().placeholder(R.drawable.loading).fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgHeader=itemView.img_header
        var nickname=itemView.nick_Name
        var upDay=itemView.up_day
        var upTime=itemView.up_time
        var gridVview=itemView.grid_view
        var contentText=itemView.content_text
        var viewNum=itemView.num_liulan
        var clickNum=itemView.num_liulan
        var share_item_comments_item=itemView.share_item_comments_item
        var editComment=itemView.to_comment
        var sendComment=itemView.share_send_comment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.share_item1, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ShareRecycleViewAdapter.ViewHolder, position: Int) {
        if(dataList.size>0){
            val studentReport: StudentReport = dataList.get(position)
            val time = studentReport.publishTime
            if (time!= null){
                val df :SimpleDateFormat= SimpleDateFormat("yyyyMMddHHmm") //设置日期格式
                val currentTime = df.format(Date())
                val a_day = time.substring(0, 8)
                val d = a_day.toInt()
                val c_day = currentTime.substring(0, 8)
                val c = c_day.toInt()
                if (a_day == c_day) { //表示是当天发的活动
                    holder.upDay.text = "今天"
                } else if (c - 1 == d) {
                    holder.upDay.text = "昨天"
                } else if (c - 2 == d) {
                    holder.upDay.text = "前天"
                } else {
                    holder.upDay.text = a_day.substring(0, 4) + "." + a_day.substring(4, 6) + "." + a_day.substring(6, 8)
                }

                holder.upTime.text = time.substring(8, 10) + ":" + time.substring(10, 12)
            }

            //发布者信息
            //发布者信息
            val student: Student = dataList.get(position).student
            //   val requestOptions1 = RequestOptions().placeholder(R.drawable.toux).fallback(R.drawable.toux).error(R.drawable.toux).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
           if(student!= null){
               if(student.headPath!=null&&student.headPath.length>0){
                   Glide.with(context).load(Constant.imgUrl+student.headPath).apply(requestOptions).into(holder.imgHeader)
               }
               if(student.stuNickname!=null&&student.stuNickname.length>0){
                   holder.nickname.text=student.stuNickname
               }else{
                   holder.nickname.text="用户"+student.phone.substring(3,9)
               }
           }

            //图片
            var imgList: MutableList<String>
            if(studentReport.imgList!=null&&studentReport.imgList.length>0){
                imgList=studentReport.imgList.split(",") as MutableList<String>
                val gridViewItemImgAdapter = GridViewItemImgAdapter(context, imgList, R.layout.grid_view_item)
                holder.gridVview.adapter=gridViewItemImgAdapter
            }
            //文本
            var content1: String? = ""
            content1 = if (studentReport.content.length > 200) {
                studentReport.content.substring(0, 200) + "..."
            } else {
                studentReport.content
            }
            holder.contentText.text=content1

            //点击文本进入详情页面
           /* holder.contentText.setOnClickListener {
                val intent2=Intent(context,ShareDetail::class.java).putExtra("share",Gson().toJson(studentReport))
                context.startActivity(intent2)
            }*/

            //点赞量
            holder.clickNum.text="点击量 " + studentReport.clickNum
            //浏览量
            holder.viewNum.text="浏览量 "+studentReport.viewNum
            //点击头像进入学生详情页
            val gson = GsonBuilder().serializeNulls().create()
            holder.imgHeader.setOnClickListener(View.OnClickListener {
                val o = gson.toJson(student)
                val intent = Intent(context, StudentActivity::class.java).putExtra("student", o)
                context.startActivity(intent)
            })
            holder.contentText.setOnClickListener(View.OnClickListener {
                val a = gson.toJson(studentReport)
                val intent1 = Intent(context, ShareDetail::class.java).putExtra("share", a)
                context.startActivity(intent1)
            })
            holder.editComment.setOnClickListener(View.OnClickListener {

            })
        }

    }

}