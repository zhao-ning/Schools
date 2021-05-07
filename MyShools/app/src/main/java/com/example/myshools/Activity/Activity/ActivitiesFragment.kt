package com.example.myshools.Activity.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.Activity.Adapter.NewsTypeRecycleViewAdapter
import com.example.myshools.Activity.Adapter.PopWindowListAdapter
import com.example.myshools.Activity.entity.EventBusPostType
import com.example.myshools.Activity.entity.SelectNewsType
import com.example.myshools.Mine.MainActivityModel
import com.example.myshools.Mine.organization.publish.PublishNewsActivity
import com.example.myshools.R
import com.example.myshools.entity.Constant
import com.example.myshools.entity.School
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_fragment.*
import kotlinx.android.synthetic.main.select_school_pop_window.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ActivitiesFragment : Fragment(), MainActivityModel.GetSchoolStatus{
    var nowSchool = School()
    var beforeSchool = School()
    var studentSchool = School()
    val schoolModel by lazy {
        MainActivityModel()
    }
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                initPop(schoolList)

            } else if (msg.what == 2) {

                var linearLayoutManager = LinearLayoutManager(activity)//设置布局管理
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                school_news_type.layoutManager = linearLayoutManager
                school_news_type.adapter = NewsTypeRecycleViewAdapter(typeList)
                (school_news_type.adapter as NewsTypeRecycleViewAdapter).notifyDataSetChanged()

                for (s in typeList) {
                    map.put(s.type, MyTabSpec())
                    map.get(s.type)?.setFragment(NewFragment(s.type))
                }

                changeTab(0)

            } else if (msg.what == 3) {

            } else if (msg.what == 4) {
                setNowSchoolInformation(nowSchool)
            }

        }
    }

    //学校新闻类型
    lateinit var typeList: ArrayList<SelectNewsType>
    lateinit var schoolList: MutableList<School>
    var curFragment = NewFragment()
    lateinit var popupView: View
    lateinit var popupWindow: PopupWindow
    lateinit var phone: String
    var stuSchoolId = -1
    lateinit var stuInformation: SharedPreferences
    lateinit var gestureDetector: GestureDetector
    lateinit var map: MutableMap<String, MyTabSpec>
    var userIdentity=0
    lateinit var adapter: NewsTypeRecycleViewAdapter
    lateinit var requestOptions:RequestOptions
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor= 0xffe6e6e6.toInt()
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity!!.window.statusBarColor=Color.parseColor("#00CCFF")
        }
        val newView = inflater.inflate(R.layout.activity_fragment, container, false)
        return newView
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        map = HashMap<String, MyTabSpec>()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //首先判断是组织还是个人
        //0是组织，1是个人，在登录的时候设置全局变量,获取用户id值
        //获取账号，用户信息
        requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()

        typeList=ArrayList()
        initData()
        popupView = LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.select_school_pop_window, null)
        popupWindow = PopupWindow(500, 800)
    }

    private fun initData() {
        phone = activity?.getSharedPreferences("nextAccount", Context.MODE_PRIVATE)?.getString("account", "").toString()
        stuInformation = activity!!.getSharedPreferences(phone, Context.MODE_PRIVATE)
        stuSchoolId = stuInformation.getInt("schoolId", 1)
        //获取用户选择的学校信息
        val role = activity?.getSharedPreferences(phone, Context.MODE_PRIVATE)?.getInt("role", -1)

        if (role == 1) {
            add_activity_button.visibility = View.GONE
        } else {

            add_activity_button.visibility = View.VISIBLE
                add_activity_button.setOnClickListener(View.OnClickListener {
                    if(stuInformation.getInt("identity",-1)==1) {
                        val intent = Intent(activity, PublishNewsActivity::class.java)
                        startActivity(intent)
                    }else{
                        val toast = Toast.makeText(context, "尚未通过认证，不能添加活动", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                })


        }

        var sp = activity!!.getSharedPreferences("nowSchool", Context.MODE_PRIVATE).edit()
        sp.putInt("nowSchoolId", stuSchoolId)
        sp.putInt("studentSchoolId", stuSchoolId)
        sp.commit()
        //获取学校数据
        initSchoolsData()
        //设置用户的学校，默认id=1，获取当前所有学校列表
        setSelectedSchool()

    }
    //获取学校列表数据
    private fun initSchoolsData() {
        schoolModel.getAllSchoolsInformation(this)
    }

    //选择学校
    private fun setSelectedSchool() {

    }
    //更新当前学校信息，活动类型以及当前学校信息
    fun setNowSchoolInformation(school: School) {
        if(school.id==stuSchoolId){
            var sp = activity!!.getSharedPreferences("userSchool", Context.MODE_PRIVATE).edit()
            sp.putString("typeList", school.schoolNewsType)
            sp.commit()
        }
        var isUpdate: Boolean = false
        if (beforeSchool.id == -1) {
            isUpdate = true
        } else if (beforeSchool == nowSchool) {
            isUpdate = false
        } else if (beforeSchool != nowSchool) {
            isUpdate = true
        }
        if (isUpdate) {
            activity_select_school_name.text = school.schoolName
            //设置学校网络图片
            Glide.with(this).load(Constant.imgUrl+school.schoolImgpath).apply(requestOptions).into(school_img_path)
            //存储当前学校信息
            var sp = activity!!.getSharedPreferences("nowSchool", Context.MODE_PRIVATE).edit()
            sp.putInt("nowSchoolId", nowSchool.id)
            sp.putInt("studentSchoolId", studentSchool.id)
            sp.commit()
            //刷新当前学校信息
            //设置新闻类型
            var k=0
            for (i in school.schoolNewsType.split(",")){
                if (k==0){
                    typeList.add(SelectNewsType(i,true))
                }else{
                    typeList.add(SelectNewsType(i,false))
                }
            }




            if (typeList.size > 0) {
                //更新新闻类型UI

                val message = Message()
                message.what = 2
                handler.sendMessage(message)
            }
        }
    }
    //初始化学校列表弹窗
    fun initPop(data: MutableList<School>) {
        popupView.select_school_list_view.adapter = PopWindowListAdapter(data, context!!)
        popupWindow.contentView = popupView
        popupWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow.isClippingEnabled = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(activity_select_school_name, -0, 0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveMsg(message: EventBusPostType) {
        if (message.type == 1) {
            popupWindow.dismiss()
            beforeSchool = nowSchool
            nowSchool = message.school
            setNowSchoolInformation(message.school)
        } else {
            changeTab(message.position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus().unregister(this)
    }

    class MyTabSpec {
        private var textView: TextView? = null
        private var fragment: NewFragment? = null
        fun setSelect(b: Boolean) {
            if (b) {
                textView?.setTextColor(Color.parseColor("#00ff00"));
            } else {
                textView?.setTextColor(Color.parseColor("#000000"));
            }
        }

        fun getFragment(): NewFragment? {
            return this.fragment;
        }

        fun setFragment(fragment: NewFragment) {
            this.fragment = fragment;
        }
    }

    private fun changeTab(i: Int) {
        changeFragment(i)
        changeColor(i)
    }

    fun changeColor(i: Int) {
        for (key in map.keys) {
            map.get(key)?.setSelect(false);
        }
        map.get(typeList.get(i).type)?.setSelect(true);
    }

    private fun changeFragment(i: Int) {
        val transaction = childFragmentManager.beginTransaction()
        var fragments = getChildFragmentManager().getFragments() as MutableList<Fragment>
        val fragment = map.get(typeList.get(i).type)?.getFragment()
        if (curFragment.type != null && curFragment.type == fragment?.type) {
            return
        }
        for (fragment1 in fragments) {
            transaction.remove(fragment1)
        }
        if (curFragment.type != null) {
            transaction.remove(curFragment)
        }
        if (!fragment?.isAdded()!!) {
            transaction.add(R.id.tab_head_content, fragment)
        }
        transaction.show(fragment)
        curFragment = fragment
        transaction.commit()
    }



    override fun getting() {

    }

    override fun getFailed(i:Int) {
        if(i==1){


        }else{

        }

    }

    override fun getSuccess(schoolList: MutableList<School>) {
        this.schoolList = schoolList
        val edit: SharedPreferences.Editor = activity!!.getSharedPreferences("schoolList", Context.MODE_PRIVATE).edit()
        edit.putString("schoolsList", Gson().toJson(schoolList))
        edit.commit()
        studentSchool.id = stuSchoolId

        var have = false
        for (s in schoolList) {

            if (s.id == studentSchool.id) {
                studentSchool = s
                have = true
            }
        }
        if (!have) {
            nowSchool = schoolList.get(0)
        } else {
            nowSchool = studentSchool
        }
        val message = Message()
        message.what = 4
        handler.sendMessage(message)
        //设置选择学校点击按钮
        activity_select_school_button.setOnClickListener(View.OnClickListener {
            val message = Message()
            message.what = 1
            handler.sendMessage(message)
        })
    }

   /* fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector!!.onTouchEvent(event)
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}



