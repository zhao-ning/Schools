package com.example.myshools.Mine.organization.publish

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshools.Activity.entity.EventBusPostType
import com.example.myshools.R
import com.example.myshools.entity.ClickEventMessage
import kotlinx.android.synthetic.main.school_news_type_item.view.*
import kotlinx.android.synthetic.main.type_item.view.*
import org.greenrobot.eventbus.EventBus

class TypeRecycleViewAdapter(private val typeList:List<String>): RecyclerView.Adapter<TypeRecycleViewAdapter.ViewHolder>() {
    //计算点击次数，根据点击次数改变type选择，点击一次选中，更改颜色背景框，再次点击取消，更改原色
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeRecycleViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.type_item, null)
        return ViewHolder(view)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_intnbname = itemView.text
        var clickNum=0
    }
    override fun getItemCount(): Int {
        return typeList.size
    }
    override fun onBindViewHolder(holder: TypeRecycleViewAdapter.ViewHolder, position: Int) {

        holder.item_intnbname.text=typeList.get(position)
        holder.itemView.setOnClickListener{
            //点击item，通知该类型是否被选中
            val message= ClickEventMessage()
            message.position=position
            holder.clickNum++
            if(holder.clickNum%2==1){//选中状态
                holder.item_intnbname.setBackgroundColor(Color.parseColor("#00CCFF"))
                message.isIs=true

            }else{
                message.isIs=false
                holder.item_intnbname.setBackgroundColor(Color.parseColor("#e6e6e6"))
            }
            EventBus.getDefault().post(message)
        }
    }

}
