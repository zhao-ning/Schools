package com.example.myshools.Mine.Settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.R
import com.example.myshools.entity.Constant
import com.example.myshools.entity.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.accounts_item.view.*


class AccountMangementAdapter(var list: MutableList<UserBean>,var context:Context)
    :RecyclerView.Adapter<AccountMangementAdapter.VH>() {
    class VH (item: View) :RecyclerView.ViewHolder(item){
        var userImg=item.user_img
        var userName=item.user_name
        var userPhone=item.user_role
        var userStatus=item.user_status
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accounts_item, null)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        var user=list[position]
        var requestOptions = RequestOptions().placeholder(R.drawable.loading).fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
        //头像
        Glide.with(context).load(Constant.imgUrl+list[position].userHeadImg).apply(requestOptions).into(holder.userImg)
        //名字
       holder.userName.text=user.userName
        //角色
        if(user.role==0){
            holder.userPhone.text="组织"
        }else{
            holder.userPhone.text="学生"
        }
        if(user.userStatus==1){
            holder.userStatus.visibility=View.VISIBLE
        }else{
            holder.userStatus.visibility=View.GONE
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
                //切换账号信息：
            if(position!=0){
                var e= context.getSharedPreferences("nextAccount", Context.MODE_PRIVATE).edit()
                e.putString("account",list[position].userPhone)
                e.commit()
                val userMap=context.getSharedPreferences("accounts", Context.MODE_PRIVATE).getString("userMap","")
                var account=context.getSharedPreferences("accounts", Context.MODE_PRIVATE).edit()
                var list1= Gson().fromJson<MutableList<UserBean>>(userMap,object : TypeToken<MutableList<UserBean?>?>(){}.type)
                var uu=list[position]
                uu.userStatus=1
                list1.get(0).userStatus=0
                for(u in list){
                    if(u.id==uu.id&& u.role==uu.role){
                        list.remove(u)
                        continue
                    }
                }
                uu.userStatus=0
                list.add(0,uu)
                account.putString("userMap", Gson().toJson(list))
                account.commit()
                notifyDataSetChanged()
            }
        })
    }
}