package com.example.myshools.Activity.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myshools.Activity.Activity.ActivityDetail
import com.example.myshools.Activity.Activity.ApplyActivities
import com.example.myshools.Activity.Model.ApplyModel
import com.example.myshools.ImgRunnable
import com.example.myshools.Mine.organization.information.OrganizationMainActivity
import com.example.myshools.R
import com.example.myshools.entity.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.list_content_item.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class ActivitiesRecycleViewItemAdapter(var dataSources:MutableList<Activities>,var context:
Context,var isUserSchoolNews:Boolean)
    :RecyclerView.Adapter<ActivitiesRecycleViewItemAdapter.ViewHolder>(),
        ApplyModel.doCancelZhanWeiStatus,
        ApplyModel.doGetActivityApplyDataStatus,
        ApplyModel.doGetIsApplyStatus,
        ApplyModel.doAddClickNumStatus,
        ApplyModel.doSetZhanWeiStatus{
    private var randomPosition = -1
    private var isCancel = 1 //用来设置是否需要再次取消占位
    private lateinit var  userPhone :String
    lateinit var  userSharedPreferences:SharedPreferences
    lateinit var peopleNumManagement: PeopleNumManagement
     var role=-1
     var userId=-1
     var isIdentity=-1
    var clickposition=-1;
    val applyModel by lazy {
        ApplyModel()
    }
    private var timer: CountDownTimer? = null
    private var dialog: AlertDialog? = null
    private lateinit var requestOptions:RequestOptions
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesRecycleViewItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_content_item, null)
        userPhone = context.getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account","")!!
        userSharedPreferences=context.getSharedPreferences(userPhone, Context.MODE_PRIVATE)
        role=userSharedPreferences.getInt("role",-1)
        userId=userSharedPreferences.getInt("id",-1)
        isIdentity=userSharedPreferences.getInt("identity",-1)
        requestOptions = RequestOptions().placeholder(R.drawable.head_img_bg).fallback(R.drawable.loading).error(R.drawable.headfail).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
       return dataSources.size
    }
    override fun onBindViewHolder(holder: ActivitiesRecycleViewItemAdapter.ViewHolder, position: Int) {
        val activities: Activities =dataSources.get(position)
        val time:String = activities.upTime
        val df = SimpleDateFormat("yyyyMMddHHmm") //设置日期格式
        val currentTime:String? = df.format(Date())
        val a_day:String= time.substring(0, 8)
        if (activities.status == 0) {
            holder.ac_status.text="活动结束"
        } else if (activities.status == 1) {
            holder.ac_status.text="正在进行中"
        }
        val d = a_day.toInt()
        val c_day = currentTime!!.substring(0, 8)
        val c = c_day.toInt()
        if (a_day == c_day) { //表示是当天发的活动
            holder.upDay.text="今天"
        } else if (c - 1 == d) {
            holder.upDay.text="昨天"
        } else if (c - 2 == d) {
            holder.upDay.text="前天"
        } else {
            holder.upDay.text=a_day.substring(0, 4) + "." + a_day.substring(4, 6) + "." + a_day.substring(6, 8)
        }
        holder.upTime.text=time.substring(8, 10) + ":" + time.substring(10, 12)
        //发布者信息
        //发布者信息
        val orgnat: Orgnaization = dataSources.get(position).organization
        val requestOptions1 = RequestOptions().placeholder(R.drawable.toux).fallback(R.drawable.toux).error(R.drawable.toux).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()
        //开启子线程线程加载图片
       /* var imgRunnable = ImgRunnable(context, holder.headImg, Constant.url + orgnat.headPath,requestOptions1)
        imgRunnable.run()*/
        Glide.with(context).load(Constant.imgUrl+orgnat.headPath).apply(requestOptions1).into(holder.headImg)
        holder.name.text=orgnat.name
        holder.lev.text=orgnat.level
        //图片
        //图片
        var imgList: MutableList<String>

             if(dataSources[position].imgs!=null&&dataSources[position].imgs.length>0){
                 imgList= dataSources[position].imgs.split(",") as MutableList<String>
                 val gridViewItemImgAdapter = GridViewItemImgAdapter(context, imgList, R.layout.grid_view_item)
                 holder.gridView.setAdapter(gridViewItemImgAdapter)
             }
        //文本
        holder.title.text="/*" + activities.title + "*/"
        var content1: String? = ""
        content1 = if (activities.content.length > 200) {
            activities.content.substring(0, 200) + "..."
        } else {
            activities.content
        }
        holder.content.text=content1
        //标签
        //标签
        val strings = activities.label.split(",").toTypedArray()
        var s = ""
        for (i in strings.indices) {
            s = s + strings[i] + "\u3000"
        }
        holder.list_biaoqian.text=s
        //点击量
        //点击量
        holder.num_views.text="点击量 " + activities.viewNum
        //点击头像进入组织介绍页面
        //点击头像进入组织介绍页面
        val gson = GsonBuilder().serializeNulls().create()
        holder.headImg.setOnClickListener(View.OnClickListener {
            val o = gson.toJson(orgnat)
            val intent = Intent(context, OrganizationMainActivity::class.java).putExtra("organt", o)
            context.startActivity(intent)
        })
        holder.content_main.setOnClickListener(View.OnClickListener {
            val a = gson.toJson(activities)
            clickposition=position
            holder.num_views.text="点击量 " + (activities.viewNum+1)
            applyModel.doAddActivityClickNum(this,activities.id)
            val intent1 = Intent(context, ActivityDetail::class.java).putExtra("activity", a)
            context.startActivity(intent1)
        })
        //申请参加活动
        if (role == 1 && isUserSchoolNews) {
            if (activities.isSetJoinNum == 1 && activities.status == 1) {
                holder.button.setVisibility(View.VISIBLE)
                val is_apply = activities.userApplyStatus
                holder.button.setOnClickListener(View.OnClickListener {
                    randomPosition=position

                    if (isIdentity == 1) {
                        //查看用户是否申请过
                        applyModel.doGetIsApply(this,userId,activities.id)
                    } else {
                        val toast4 = Toast.makeText(context, "您尚未实名验证，请先实名验证才可申请", Toast.LENGTH_SHORT)
                        toast4.setGravity(Gravity.CENTER, 0, 0)
                        val v = toast4.view.findViewById<View>(android.R.id.message) as TextView
                        v.setTextColor(Color.parseColor("#0d4ff4"))
                        toast4.show()
                    }
                })
            } else if (activities.isSetJoinNum == 1 && activities.status == 0) { //活动结束
                holder.button.setVisibility(View.VISIBLE)
                holder.button.setOnClickListener(View.OnClickListener {
                    val toast4 = Toast.makeText(context, "活动已结束，请关注下期活动", Toast.LENGTH_SHORT)
                    toast4.setGravity(Gravity.CENTER, 0, 0)
                    val v = toast4.view.findViewById<View>(android.R.id.message) as TextView
                    v.setTextColor(Color.parseColor("#0d4ff4"))
                    toast4.show()
                })
            } else {
                holder.button.setVisibility(View.VISIBLE)
                holder.button.setVisibility(View.INVISIBLE)
            }
        } else {
            holder.button.setVisibility(View.VISIBLE)
            holder.button.setVisibility(View.INVISIBLE)
        }
    }
    fun whetherToApply(id: Int, position: Int) {
        applyModel.doGetThisActivityApplyData(this,id,position)
        randomPosition = position
    }
    fun quXiaoZhanWei(aId: Int) {
        Log.e("取消占位", "取消占位")
         applyModel.doCancelZhanWei(this,aId)

    }
     inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
         var title=itemView.ac_title
         var headImg=itemView.img_header
         var name=itemView.name
         var lev=itemView.lev
         var upDay=itemView.up_day
         var upTime=itemView.up_time
         var content=itemView.content_text
         var gridView=itemView.grid_view
         var list_biaoqian=itemView.list_biao_qian
         var num_views=itemView.num_liulan
         var content_main=itemView.content_main
         var button=itemView.a_apply
         var ac_status=itemView.ac_status

    }
    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 71) {
                val toast3 = Toast.makeText(context, "请检查网络状况", Toast.LENGTH_SHORT)
                toast3.setGravity(Gravity.CENTER, 0, 0)
                val v = toast3.view.findViewById<View>(android.R.id.message) as TextView
                v.setTextColor(Color.parseColor("#0d4ff4"))
                toast3.show()
            }
            //占位成功，开始进入申请页面
            if (msg.what == 73) {
                val applying = peopleNumManagement.applyingNum
                val join = peopleNumManagement.joinNum
                val free = peopleNumManagement.freeNum
                dialog = AlertDialog.Builder(context).create()
                dialog!!.setCancelable(false)//设置手机侧滑等其他物理返回键无效
                dialog!!.setCanceledOnTouchOutside(false) //点击空白区的时候(不调用dismiss())
                dialog!!.show() //注意：必须在window.setContentView之前show
                val window = dialog!!.getWindow()
                window!!.setContentView(R.layout.is_apply)
                val a_ing = window.findViewById<TextView>(R.id.applying)
                val succeed = window.findViewById<TextView>(R.id.success_apply)
                val a_free = window.findViewById<TextView>(R.id.apply_free)
                val dao_ji_shi = window.findViewById<TextView>(R.id.dao_ji_shi)
                /**
                 * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
                 * 第一个参数表示总时间，第二个参数表示间隔时间。
                 * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
                 */
                timer = object : CountDownTimer(30000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        dao_ji_shi.text=(millisUntilFinished / 1000).toString() + "秒"
                    }

                    override fun onFinish() {
                        //如果占位成功，取消占位
                        Log.e("被调用", "被调用")
                        quXiaoZhanWei(dataSources.get(randomPosition).id)
                        dialog!!.dismiss()
                    }
                }
                //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
                timer!!.start()
                a_ing.text = "正在提交:" + applying + "人"
                succeed.text = "提交申请:" + join + "人"
                a_free.text = "空余:" + free + "人"
                val yesButton = window.findViewById<Button>(R.id.apply_ok)
                val no = window.findViewById<Button>(R.id.apply_no)
                //点击确定按钮让对话框消失
                yesButton.setOnClickListener { //进入申请页面判断当前是否可以申请
                    isCancel = 0
                    //点击确定后进入申请详情页面
                    Log.e("已准备完毕，开始进入表单页面", "ss")
                    timer!!.cancel()
                    dialog!!.dismiss()
                    val intent = Intent(context, ApplyActivities::class.java)
                            .putExtra("position", randomPosition)
                            .putExtra("userId",userId)
                            .putExtra("question",dataSources.get(randomPosition).getquestions())
                            .putExtra("aId",dataSources.get(randomPosition).id)
                    context.startActivity(intent)
                }
                no.setOnClickListener { //取消占位
                    timer!!.cancel()
                    quXiaoZhanWei(dataSources.get(randomPosition).id)
                    dialog!!.dismiss()
                }

            }
            //申请已满
            if (msg.what == 74) {
                val toast4 = Toast.makeText(context, "申请已满，关注下次活动或稍后再试", Toast.LENGTH_SHORT)
                toast4.setGravity(Gravity.CENTER, 0, 0)
                toast4.show()
            }
            else if (msg.what==200){
                whetherToApply(dataSources.get(randomPosition).id,randomPosition)
            }
            //检测已经提交过申请
            else if(msg.what==201){
                val toast4 = Toast.makeText(context, "已提交申请，申请结果请在消息中查看通知", Toast.LENGTH_SHORT)
                toast4.setGravity(Gravity.CENTER, 0, 0)
                val v = toast4.view.findViewById<View>(android.R.id.message) as TextView
                v.setTextColor(Color.parseColor("#0d4ff4"))
                toast4.show()
            }
            //检测到该活动提交的申请已成功提示
            else if(msg.what==202){//
                val toast4 = Toast.makeText(context, "已申请成功，记着按时参加活动哟", Toast.LENGTH_SHORT)
                toast4.setGravity(Gravity.CENTER, 0, 0)
                val v = toast4.view.findViewById<View>(android.R.id.message) as TextView
                v.setTextColor(Color.parseColor("#0d4ff4"))
                toast4.show()
            }
            else if(msg.what==203){//
                val intent = Intent(context, ApplyActivities::class.java).putExtra("aId", dataSources[randomPosition].id).putExtra("position", randomPosition)
                context.startActivity(intent)

            }
            //修改点击数量
            else if (msg.what==77){

            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveMsg(message: EventMessage) {

        dataSources.get(message.position).setUserApplyStatus(message.applyStatus)
        dataSources.get(message.position).setToApply(0)

        notifyDataSetChanged()
    }


    override fun cancelSetZhanwei() {
        dataSources.get(randomPosition).toApply=0
    }

    override fun cancelZhanweiFailed(i: Int) {
        dataSources.get(randomPosition).toApply=1
    }

    override fun cancelZhanweiSuccess(i: Int) {
        isCancel=0
        dataSources.get(randomPosition).toApply=0
    }

    override fun startSetZhanwei() {
        dataSources.get(randomPosition).toApply=1
    }

    override fun SetZhanweiFailed(i: Int) {
        dataSources.get(randomPosition).toApply=0
       val message=Message()
        if(i==1){//网络连接失败
            message.what=71
        }else if(i==2){//没有找到相关数据，提示用户该活动信息已被更改，请重新操作

        }
        handler.sendMessage(message)
    }

    override fun SetZhanweiSuccess() {
        dataSources.get(randomPosition).toApply=1
       var message=Message()
        message.what=73
        handler.sendMessage(message)
    }

    override fun doGetIsApplying() {

    }
    //0m没参加过,1是已经提交数据正在申请，2是已经通过申请,4拒绝
    override fun doGetIsApplySuccess(is_apply: Int) {

        var message:Message= Message()
        if (is_apply == 0 || is_apply == 4) { //未申请，可以申请,申请失败可以再次申请
            //点击之后就从数据库获取数据，活动申请状况
            message.what=200
        } else if (is_apply == 1) { //已经提交申请，简单提示即可
            message.what=201
        } else if (is_apply == 2) { //申请成功
            message.what=202
        } else if (is_apply == 3) { //该活动已经占位，尚未提交,直接进入
            message.what=203
        }
      handler.sendMessage(message)
    }
    override fun doGetIsApplyDFailed(re: Int) {
      val message=Message()
        message.what=71;
        handler.sendMessage(message)
    }

    override fun doGetAcyApplyDing() {
    }

    override fun doGetAcyApplyDSuccess(peopleNumManagement: PeopleNumManagement,position: Int) {
        if(peopleNumManagement!=null){
            this.peopleNumManagement=peopleNumManagement
            if (peopleNumManagement.freeNum > 0) { //占住一个申请位置
                    Log.e("sss", "占位")
                applyModel.doZhanWei(this,dataSources.get(randomPosition).id)
            } else {
            Log.e("sss", "活动已满")
            val message = Message()
            message.what = 74
            message.obj = peopleNumManagement
            handler.sendMessage(message)
            }
        }
    }

    override fun doGetAcyApplyDFailed(re: Int) {

    }

    //点击量加一
    override fun doAddClickNumSuccess(i: Int) {
    }


}
