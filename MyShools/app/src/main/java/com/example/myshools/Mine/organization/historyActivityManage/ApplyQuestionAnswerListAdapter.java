package com.example.myshools.Mine.organization.historyActivityManage;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myshools.R;
import com.example.myshools.entity.ApplyQuestion;
import com.example.myshools.util.InputTextDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
/*
*
* 申请表：每一个申请人的问题答案列表adapter
*
*
* */
public class ApplyQuestionAnswerListAdapter extends BaseAdapter {
    private int itemResId; //布局
    private Context context;//上下文环境
    private List<ApplyQuestion> dataSource=new ArrayList<>();
    private SharedPreferences sharedPreferences;

    public ApplyQuestionAnswerListAdapter(List<ApplyQuestion> dataSource, Context context, int itemResId) {
        this.dataSource = dataSource;
        this.itemResId = itemResId;
        this.context = context;

    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public ApplyQuestion getItem(int i) {
        return dataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        viewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(itemResId, null);
            holder = new viewHolder();
            holder.question = view.findViewById(R.id.apply_question);
            holder.answer=view.findViewById(R.id.apply_answer);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }
        holder.question.setText(dataSource.get(position).getQuestion());
        holder.answer.setText(dataSource.get(position).getAnswer());
        return view;
    }


    public class viewHolder {
        public TextView question;
        public TextView answer;
    }




}