package com.example.myshools.Activity.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.Activity.Model.CommentsModel
import com.example.myshools.Activity.ReplyComment
import com.example.myshools.Activity.ResultComment
import com.example.myshools.R
import com.example.myshools.entity.Comment
import com.example.myshools.entity.Constant
import com.example.myshools.entity.User
import com.example.myshools.util.InputTextDialog
import com.example.myshools.util.InputTextDialog.OnTextSendListener
import kotlinx.android.synthetic.main.comment_item.view.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(var list: MutableList<ResultComment>,
                     var context: Context)
    :RecyclerView.Adapter<CommentAdapter.VH>(),CommentsModel.sendCommentsStatus{
    private lateinit var userPhone:String
    private lateinit var userSharedPreferences:SharedPreferences
    private  var role=-1
    private var  userId=-1
    private var isIdentity=-1
    private lateinit var requestOptions:RequestOptions
    private lateinit var adapter: CommentReplyAdapter
    private var inputTextDialog //文字输入框
            : InputTextDialog? = null
    private var commentsModel=CommentsModel()
    private lateinit var comment:Comment

    inner  class VH(view: View) :RecyclerView.ViewHolder(view)  {
        var headImg=view.user_img//用户头像
        var userName=view.user_name//用户名称
        var comment=view.ac_comment//用户评论内容
        var replyComment=view.comment_reply_recycle //子集评论
        var commentTime=view.comment_time //评论时间
        var commentReply=view.comment_reply //回复

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.e("TAG","创建")
        val view = LayoutInflater.from(context).inflate(R.layout.comment_item, null)
        userPhone = context.getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account","")!!
        userSharedPreferences=context.getSharedPreferences(userPhone, Context.MODE_PRIVATE)
        role=userSharedPreferences.getInt("role",-1)
        userId=userSharedPreferences.getInt("id",-1)
        isIdentity=userSharedPreferences.getInt("identity",-1)
        requestOptions = RequestOptions().fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
        //修改认证资料
        return VH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        var resultComment:ResultComment=list[position]
        if(resultComment!=null){
            Log.e("TAG123",resultComment.toString())
            Glide.with(context).load(Constant.imgUrl+resultComment.user.headImgUrl).apply(requestOptions).into(holder.headImg)
            holder.userName.text=resultComment.user.name
            if(resultComment.newsId==resultComment.user.id){
                holder.userName.setTextColor(Color.parseColor("#FF0000"))
            }else{
                holder.userName.setTextColor(Color.parseColor("#000000"))
            }
            holder.comment.text=resultComment.content

            val time:String = resultComment.createdTime
            val df = SimpleDateFormat("yyyyMMddHHmm") //设置日期格式
            val currentTime:String? = df.format(Date())
            val a_day:String= time.substring(0, 8)
            val d = a_day.toInt()
            val c_day = currentTime!!.substring(0, 8)
            val c = c_day.toInt()
            var ss:String=""
            if (a_day == c_day) { //表示是当天发的活动
               ss="今天"
            } else if (c - 1 == d) {
                ss="昨天"
            } else if (c - 2 == d) {
               ss="前天"
            } else {
                ss=a_day.substring(0, 4) + "." + a_day.substring(4, 6) + "." + a_day.substring(6, 8)
            }
            ss=ss+" "+time.substring(8, 10) + ":" + time.substring(10, 12)
            holder.commentTime.text=ss
            if(resultComment.children!=null&&resultComment.children.size>0){
                adapter=CommentReplyAdapter(resultComment.children,context)
                holder.replyComment.setLayoutManager(LinearLayoutManager(context))
                holder.replyComment.adapter=adapter
            }
            holder.commentReply.setOnClickListener {
                inputTextDialog = InputTextDialog(context, R.style.dialog_center, 255)
                var hint0 = "回复"
                inputTextDialog!!.setHint(hint0)
                inputTextDialog!!.setMaxNumber(255) //最长字数

                inputTextDialog!!.show()
                val finalHint = hint0
                inputTextDialog!!.setmOnTextSendListener(OnTextSendListener { msg: String ->
                    if (msg.length>0) {
                        //上传评论
                        var comment=Comment()
                        comment.content=msg
                        comment.newsId=resultComment.newsId
                        comment.parentId=resultComment.id
                        comment.status=0
                        comment.type=role
                        comment.userId=userId
                        comment.id=-1
                        var s=SimpleDateFormat("yyyyMMddHHmm")
                        val upTime = df.format(Date())
                        comment.createTime=upTime
                        this.comment=comment
                        commentsModel.sendComment(this,comment,1)
                    }
                })
            }
        }
    }

    override fun sendCommendsFailed(i: Int) {

    }

    override fun sendCommendSuccess(position: Int) {
        EventBus.getDefault().post(true)
    }
}