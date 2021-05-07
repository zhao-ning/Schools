package com.example.myshools.Mine.Settings

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.R
import com.example.myshools.entity.Constant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.setting.*

class SettingsActivity:AppCompatActivity() {
    lateinit var userImg:String
    lateinit var userPhone:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          window.statusBarColor= Color.parseColor("#00CCFF")
        }
        setContentView(R.layout.setting)
        initData()


        //进入账号管理页面
        account_manager.setOnClickListener(View.OnClickListener {
            var intent=Intent(this,AccountManageActivity::class.java)
            startActivity(intent)


        })
    }
    private val handler=object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what==1){
                initData()
            }

        }
    }

    private fun initData() {
        var requestOptions = RequestOptions().placeholder(R.drawable.loading).fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()

        userPhone= getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account","")!!
        if(userPhone.length>0){
            userImg= getSharedPreferences(userPhone, Context.MODE_PRIVATE).getString("headPath","")!!
        }
        Glide.with(this).load(Constant.imgUrl+userImg).apply(requestOptions).into(now_user_img)
        now_user_phone.text=userPhone.substring(0,3)+"******"+userPhone.substring(9,11)
    }

    override fun onResume() {
        super.onResume()
        val message=Message()
        message.what=1
        handler.sendMessage(message)
        Log.e("TAGgengxin","Gengxin")
    }
}