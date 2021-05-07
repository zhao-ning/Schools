package com.example.myshools.Mine.student

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshools.Consult.Adapter.ShareRecycleViewAdapter
import com.example.myshools.Consult.Model.RrportModel
import com.example.myshools.R
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import kotlinx.android.synthetic.main.my_share_manager.*
import java.util.*

class MyShareManager :AppCompatActivity() ,RrportModel.getReportListStatuss{
    private var identity = -1 //用户是否认证
    private var userId = 0
    private var page = 1
    private var reportList: MutableList<StudentReport> = ArrayList() //所有的数据
    private var newReList: MutableList<StudentReport> = ArrayList() //上拉加载的数据
    private var itemAdapter: ShareRecycleViewAdapter? = null
    private var onLoad = -1 //上拉加载：1，下拉刷新0
    private var rrportModel = RrportModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          window.statusBarColor=Color.parseColor("#00CCFF")
        }
       setContentView(R.layout.my_share_manager)
        in_school_recycle!!.layoutManager = LinearLayoutManager(this)
        itemAdapter = ShareRecycleViewAdapter(reportList, this)
        in_school_recycle!!.adapter = itemAdapter
        //获取用户个人信息
        //获取用户个人信息
        getUserData()
        setView()
        getNowReport()
        
        
    }
    private fun setView() {
        add_share_button.setOnClickListener {
            if (identity == 1) {
                val intent = Intent(this, PublishShareActivity::class.java)
                startActivity(intent)
            } else {
                val message = Message()
                message.what = 44
                handler.sendMessage(message)
            }
        }
        refreshLayout!!.setRefreshHeader(ClassicsHeader(this))
        refreshLayout!!.setRefreshFooter(ClassicsFooter(this))
        refreshLayout!!.setOnRefreshListener { refreshlayout ->
            onLoad = 0
            page = 1
            getNowReport()
            refreshlayout.finishRefresh(2000) ///*,false*/传入false表示刷新失败
        }
        refreshLayout!!.setOnLoadMoreListener { refreshlayout ->
            onLoad = 1
            page = page + 1
            getNowReport()
            refreshlayout.finishLoadMore(2000) ///*,false*/传入false表示加载失败
        }
    }

    private fun getNowReport() {
        rrportModel.getStudentREportByUserId(this, userId, page)
    }

    private fun getUserData() {
        val s: String = getSharedPreferences("nextAccount", Context.MODE_PRIVATE).getString("account", null)!!
        identity = getSharedPreferences(s, Context.MODE_PRIVATE).getInt("identity", 0)
        userId = getSharedPreferences(s, Context.MODE_PRIVATE).getInt("id", 0)
    }

    override fun getReportListFailed(i: Int) {}
    override fun getReportListSuccess(list: MutableList<StudentReport>) {
        val message = Message()
        message.what = 1
        var getList: List<StudentReport> = ArrayList()
        getList = list
        if (onLoad == 1) {
            if (reportList.size > 0 && list != null && list.size > 0) {
                for (i in list.indices) {
                    for (j in reportList.indices) {
                        if (list[i].id == reportList[j].id) {
                            getList.removeAt(i)
                            continue
                        }
                    }
                }
            }
        }
        newReList.clear()
        newReList = getList
        handler.sendMessage(message)
    }


    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                if (onLoad == 1) { //上拉加载更多
                    for (i in newReList.indices) {
                        reportList.add(newReList[i])
                    }
                    onLoad = -1
                    itemAdapter!!.notifyDataSetChanged()
                } else if (onLoad == 0) {
                    reportList.clear() //下拉刷新
                    for (i in newReList.indices) {
                        reportList.add(i, newReList[i])
                    }
                    onLoad = -1
                    itemAdapter!!.notifyDataSetChanged()
                } else {
                    for (i in newReList.indices) {
                        reportList.add(i, newReList[i])
                    }
                    itemAdapter!!.notifyDataSetChanged()
                }
            } else if (msg.what == 44) {
                var toast = Toast.makeText(this@MyShareManager, "未完成认证，无法操作", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onLoad = 0
        page = 1
        getNowReport()
    }
}