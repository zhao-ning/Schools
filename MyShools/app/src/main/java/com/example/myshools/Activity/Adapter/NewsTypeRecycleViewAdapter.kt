package com.example.myshools.Activity.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshools.Activity.entity.EventBusPostType
import com.example.myshools.Activity.entity.SelectNewsType
import com.example.myshools.R
import kotlinx.android.synthetic.main.school_news_type_item.view.*
import org.greenrobot.eventbus.EventBus

class NewsTypeRecycleViewAdapter(private val typeList:ArrayList<SelectNewsType>): RecyclerView.Adapter<NewsTypeRecycleViewAdapter.ViewHolder>() {

    var list=ArrayList<SelectNewsType>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsTypeRecycleViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.school_news_type_item, null)
        return ViewHolder(view)
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_intnbname = itemView.item_text
    }
    override fun getItemCount(): Int {
        return typeList.size

    }

    override fun onBindViewHolder(holder: NewsTypeRecycleViewAdapter.ViewHolder, position: Int) {

       holder.item_intnbname.text=typeList.get(position).type

        holder.itemView.setOnClickListener{
            val message= EventBusPostType()
            message.type=2
            message.position=position
            EventBus.getDefault().post(message)
        }
    }

}
