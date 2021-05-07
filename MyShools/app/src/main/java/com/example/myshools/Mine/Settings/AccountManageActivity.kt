package com.example.myshools.Mine.Settings

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.account_manage.*

class AccountManageActivity:AppCompatActivity() {
    lateinit var userImg:String
    lateinit var userPhone:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor= Color.parseColor("#00CCFF")
        }
        setContentView(R.layout.account_manage)
        userPhone= getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account","")!!
        if(userPhone.length>0){
            userImg= getSharedPreferences(userPhone, Context.MODE_PRIVATE).getString("headPath","")!!
        }

        var requestOptions = RequestOptions().placeholder(R.drawable.loading).fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()

        val userMap=getSharedPreferences("accounts", Context.MODE_PRIVATE).getString("userMap","")
        Log.e("TAG",userMap)
        var list= Gson().fromJson<MutableList<UserBean>>(userMap,object: TypeToken<MutableList<UserBean>>(){}.type)
        account_recycleview.layoutManager=LinearLayoutManager(this)
        account_recycleview.adapter=AccountMangementAdapter(list,this)


    }
}