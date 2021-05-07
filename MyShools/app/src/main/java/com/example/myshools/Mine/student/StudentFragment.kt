package com.example.myshools.Mine.student

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.Mine.Settings.SettingsActivity
import com.example.myshools.Mine.organization.publish.PublishNewsActivity
import com.example.myshools.Mine.student.edit.StudentEditActivity
import com.example.myshools.R
import com.example.myshools.entity.Constant
import com.example.myshools.entity.Student
import com.google.gson.Gson
import kotlinx.android.synthetic.main.student_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StudentFragment:Fragment(){
    private lateinit var phone:String
    private lateinit var student:Student
    private lateinit var userData:String
    private var userdataUp=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor=Color.parseColor("#00CCFF")
        }
        return inflater.inflate(R.layout.student_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initData()
        initAdapter()
    }

    private fun initAdapter() {
        btn_into_student_data.setOnClickListener(View.OnClickListener {
            var toEditActivity:Intent=Intent(activity,StudentEditActivity::class.java)
            startActivity(toEditActivity)
        })
        my_activities.setOnClickListener(View.OnClickListener {
            var intent=Intent(activity,MyApplyActivitiesActivity::class.java)
            startActivity(intent)
        })
//        my_news.setOnClickListener(View.OnClickListener {  })
        share_manage.setOnClickListener(View.OnClickListener {  var intent=Intent(activity,MyShareManager::class.java)
            startActivity(intent)
        })
        settings.setOnClickListener(View.OnClickListener {
                    var intent3=Intent(activity, SettingsActivity::class.java);
                    startActivity(intent3);
        })
    }

    private fun initData() {
        phone=activity!!.getSharedPreferences("nextAccount",Context.MODE_PRIVATE).getString("account","").toString()
        userData= activity!!.getSharedPreferences(phone,Context.MODE_PRIVATE).getString("userData",null)!!
        val requestOptions = RequestOptions().placeholder(R.drawable.tou).fallback(R.drawable.tou).error(R.drawable.tou).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()

              if(userData!=null){
            student=Gson().fromJson(userData,Student::class.java)
            //更新姓名
            s_name.text=if(student.stuNickname!=null){student.stuNickname}else{student.phone}
            if(student.isIdentity==1){
                is_ren_zheng.visibility=View.VISIBLE
                ren_zheng.text="已认证"
            }else if (student.isIdentity==2){
                is_ren_zheng.visibility=View.GONE
                ren_zheng.text="正在认证"
            }else{
                is_ren_zheng.visibility=View.GONE
                ren_zheng.text="未认证"
            }
            //加载用户头像
            if(student.headPath!=null){
                Glide.with(this).load(Constant.imgUrl+student.headPath).apply(requestOptions).into(s_head_img)
            }
        }

    }



    override fun onResume() {
        super.onResume()
        initData()
    }




}