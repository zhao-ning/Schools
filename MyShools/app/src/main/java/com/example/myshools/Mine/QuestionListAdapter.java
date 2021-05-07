package com.example.myshools.Mine;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.myshools.R;
import com.example.myshools.util.InputTextDialog;
import java.util.List;

public class QuestionListAdapter extends BaseAdapter {
    private List<String> dataSource;//数据源
    private int itemResId; //布局
    private Context context;//上下文环境
    private InputTextDialog inputTextDialog;//自定义输入框
    private float startX;//滑动监听开始x坐标点
    private float stopX;//滑动监听结束x坐标点

    public QuestionListAdapter(List<String> dataSource, Context context, int itemResId) {
        this.dataSource = dataSource;
        this.itemResId = itemResId;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public String getItem(int i) {
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
            holder.question = view.findViewById(R.id.question_edit);
            view.setTag(holder);
        } else {
            holder = (viewHolder) view.getTag();
        }
        //单击修改问题，左滑删除

        holder.question.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.show();  //注意：必须在window.setContentView之前show
                Window window = dialog.getWindow();
                window.setContentView(R.layout.set_question_is_delete);

                TextView deleteValue=window.findViewById(R.id.delete_values);
                deleteValue.setText(" 问题："+dataSource.get(position));
                Button yesButton = window.findViewById(R.id.question_delete_ok);
                Button no = window.findViewById(R.id.question_delete_no);
                //点击确定按钮让对话框消失
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        dataSource.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                return true;
            }
        });
        holder.question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTextDialog = new InputTextDialog(context, R.style.dialog_center, 100);
                String hint1 = "";
                if (dataSource.get(position).length() > 0) {
                    hint1 = dataSource.get(position);
                }
                inputTextDialog.setHint(hint1);
                inputTextDialog.setMaxNumber(100);//最长十个字
                inputTextDialog.show();
                inputTextDialog.setmOnTextSendListener(msg -> {
                    dataSource.set(position, msg);
                });

                notifyDataSetChanged();
            }
        });
        holder.question.setText(dataSource.get(position));
        return view;
    }
    public class viewHolder {
        public TextView question;
    }
    public List<String> getDataSource() {
        return dataSource;
    }


}
