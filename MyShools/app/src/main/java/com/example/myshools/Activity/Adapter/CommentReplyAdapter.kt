package com.example.myshools.Activity.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.Activity.ReplyComment
import com.example.myshools.Activity.ResultComment
import com.example.myshools.R
import com.example.myshools.entity.Constant
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.comment_reply_item.view.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class CommentReplyAdapter(var list: MutableList<ReplyComment>, var context: Context):RecyclerView.Adapter<CommentReplyAdapter.VH>(){
    class VH(view: View) :RecyclerView.ViewHolder(view)  {

        var userName=view.reply_user_name//用户名称
        var comment=view.user_reply_comment//用户评论内容

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_reply_item, null)
        return VH(view)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        var replyComment:ReplyComment=list[position]
        if(replyComment!=null){
            Log.e("user",replyComment.toString())
            holder.comment.text=replyComment.content
            holder.userName.text=replyComment.user.name
            if(replyComment.newsId==replyComment.user.id){
                holder.comment.setTextColor(Color.parseColor("#FF0000"))
            }
        }
    }
}