package com.example.myshools.Activity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.myshools.Activity.entity.EventBusPostType
import com.example.myshools.R
import com.example.myshools.entity.School
import kotlinx.android.synthetic.main.select_school_list_item.view.*
import org.greenrobot.eventbus.EventBus

class PopWindowListAdapter : BaseAdapter {
    var dataSource: MutableList<School>
    var context: Context

    constructor(dataSource: MutableList<School>, context: Context) {
        this.context = context
        this.dataSource = dataSource

    }
    override fun getView(p0: Int, view: View?, p2: ViewGroup?): View {

        var view = LayoutInflater.from(context).inflate(R.layout.select_school_list_item, null);
        view.select_school_item.setText(dataSource.get(p0).schoolName)
        view.setOnClickListener(View.OnClickListener {
            val message= EventBusPostType()
            message.type=1
            message.school=dataSource.get(p0)
            message.position=p0
            EventBus.getDefault().post(message)
        })
        return view
    }
    override fun getItem(p0: Int): Any {
        return dataSource.get(p0)
    }
    override fun getItemId(p0: Int): Long {
        return dataSource.get(p0).id.toLong()
    }
    override fun getCount(): Int {
        return dataSource.size
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }
}