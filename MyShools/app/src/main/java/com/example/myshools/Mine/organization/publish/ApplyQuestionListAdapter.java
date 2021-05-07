package com.example.myshools.Mine.organization.publish;

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

public class ApplyQuestionListAdapter extends BaseAdapter {
   /* private List<String> dataSource;//问题数据源*/
    private int itemResId; //布局
    private Context context;//上下文环境
    private InputTextDialog inputTextDialog;//自定义输入框
    private List<ApplyQuestion> dataSource=new ArrayList<>();
    private SharedPreferences sharedPreferences;
    public ApplyQuestionListAdapter(List<ApplyQuestion> dataSource, Context context, int itemResId) {
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
            holder.question = view.findViewById(R.id.question);
            holder.editQuestion=view.findViewById(R.id.edit_answer);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }
        holder.question.setText(dataSource.get(position).getQuestion());
        holder.editQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTextDialog = new InputTextDialog(context, R.style.dialog_center, 100);
                inputTextDialog.setMaxNumber(100);
                String hint="";
                if(dataSource.get(position).getAnswer().length()!=0){
                    hint=dataSource.get(position).getAnswer();
                }
                inputTextDialog.setHint(hint);
                inputTextDialog.show();
                inputTextDialog.setmOnTextSendListener(msg -> {
                    dataSource.get(position).setAnswer(msg);
                });


            }
        });
        holder.editQuestion.setText(dataSource.get(position).getAnswer());
        //数据更新
        changed(dataSource);
        return view;
    }

    private void changed(List<ApplyQuestion> dataS) {

        Gson gson=new Gson();
        String s=gson.toJson(dataS);
        sharedPreferences =context.getSharedPreferences("ApplyQuestion",
                MODE_PRIVATE);
        SharedPreferences.Editor ed =sharedPreferences.edit();
        ed.putString("data",s);
        ed.commit();
        notifyDataSetChanged();

    }

    public class viewHolder {
        public TextView question;
        public TextView editQuestion;
    }




}