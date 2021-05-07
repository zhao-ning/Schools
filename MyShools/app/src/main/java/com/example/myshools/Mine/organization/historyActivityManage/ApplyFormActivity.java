package com.example.myshools.Mine.organization.historyActivityManage;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myshools.Mine.organization.historyActivityManage.entity.ApplyForm;
import com.example.myshools.Mine.organization.historyActivityManage.entity.SetUpResult;
import com.example.myshools.Mine.organization.historyActivityManage.entity.SimpleUserData;
import com.example.myshools.Mine.organization.historyActivityManage.entity.SimpleUserDataNoId;
import com.example.myshools.Mine.student.UpdateStudentReportResful;
import com.example.myshools.R;
import com.example.myshools.entity.ApplyManage;
import com.example.myshools.entity.Constant;
import com.example.myshools.entity.GetApplyRestful;
import com.example.myshools.entity.Student;
import com.example.myshools.entity.UpDataApplyStatusResul;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApplyFormActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView return1;
    private TextView applyNum;
    private TextView resultNum;
    private Button allToOk;//一键同意
    private Button outStu;//导出人员表格
    private int OkNum = 0;//通过的申请数量
    private String filePath;//文件路径
    /*d
    ialog 提示框
    * */
    private AlertDialog dialog;
    /*一键同意Dialog*/
    private TextView all_ok_tip;
    private TextView to_set_up;
    private TextView succeed_set_up;
    private TextView che_xiao;
    private TextView fail_set_up;
    private Button yesButton;
    private Button no;

    /*
     * 转excel表格Dialog
     * */
    private TextView outNum;//同意的人数
    private TextView excel_download_status;//进度状态
    private Button excel_download_start;//开始
    private Button excel_download_cancel;//关闭取消
    private TextView excel_file_position;//导出的文件位置
    private Button share_file;//分享文件
    private TextView excel_tip;
    private List<SimpleUserDataNoId> excelDataItemList = new ArrayList<>();//excel中的数据


    private List<ApplyManage> dataSource = new ArrayList<>();
    private int aId;
    private ApplyFormItemAdapter applyFormItemAdapter;
    private WritableWorkbook wwb;


    /*json生产excel*/
    private WritableSheet sheet;
    private String excelName;//活动名称
    private String data;
    private String excelPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#00CCFF"));
        }
        setContentView(R.layout.apply_form_activity);
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }

        excelName="人员信息表";
        aId = getIntent().getIntExtra("aId", -1);
        findView();
        if (aId != -1) {
            initData();
        }
        applyFormItemAdapter = new ApplyFormItemAdapter(ApplyFormActivity.this, dataSource, R.layout.apply_information_item);
        listView.setAdapter(applyFormItemAdapter);


        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //一键同意：一键完成所有未设置的请求
        allToOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> integerList = new ArrayList<>();
                for (int i = 0; i < dataSource.size(); i++) {
                    if (dataSource.get(i).getApplyResult() == 2 || dataSource.get(i).getApplyResult() == 4) {
                        //已经处理过，不做操作
                    } else {
                        integerList.add(dataSource.get(i).getId());
                    }
                }
                dialog = new AlertDialog.Builder(ApplyFormActivity.this).create();
                dialog.setCanceledOnTouchOutside(false);//点击空白区的时候(不调用dismiss())
                dialog.show();  //注意：必须在window.setContentView之前show
                Window window = dialog.getWindow();
                window.setContentView(R.layout.is_set_all_to_ok);
                all_ok_tip = window.findViewById(R.id.all_ok_tip);
                to_set_up = window.findViewById(R.id.to_set_up);
                succeed_set_up = window.findViewById(R.id.succeed_set_up);
                che_xiao = window.findViewById(R.id.che_xiao);
                fail_set_up = window.findViewById(R.id.fail_set_up);
                yesButton = window.findViewById(R.id.set_ok);
                no = window.findViewById(R.id.set_no);

                //需要处理的个数
                to_set_up.setText("需要处理：" + integerList.size());
                //先隐藏掉
                succeed_set_up.setVisibility(View.GONE);
                che_xiao.setVisibility(View.GONE);
                fail_set_up.setVisibility(View.GONE);

                //提示
                if (integerList.size() == 0) {
                    all_ok_tip.setText("尚未处理的申请为0，无法设置");
                    yesButton.setEnabled(false);
                    yesButton.setBackgroundColor(Color.parseColor("#bfbfbf"));
                } else {
                    yesButton.setEnabled(true);
                    /*yesButton.setBackground(getResources().getDrawable(R.drawable.bg_apply));*/
                }
                //点击确定按钮让对话框消失
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all_ok_tip.setText("正在处理...,处理过程中请勿关闭");
                        updateApplyListStatus(integerList);
                        yesButton.setEnabled(false);
                        no.setEnabled(false);
                        yesButton.setBackgroundColor(Color.parseColor("#bfbfbf"));
                        no.setBackgroundColor(Color.parseColor("#bfbfbf"));
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        applyFormItemAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


        //导出人员表格
        outStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取人员列表
                for (ApplyManage applyForm : dataSource) {
                    if (applyForm.getApplyResult() == 2) {
                        Student userData = applyForm.getStudent();
                        SimpleUserDataNoId userDataNoId = new SimpleUserDataNoId();
                        userDataNoId.setStuName(userData.getStuName());
                        userDataNoId.setStuCollege(userData.getStuCollege());
                        userDataNoId.setStuNumber(userData.getStuNumber());
                        excelDataItemList.add(userDataNoId);
                    }
                }
                dialog = new AlertDialog.Builder(ApplyFormActivity.this).create();
                dialog.setCanceledOnTouchOutside(false);//点击空白区的时候(不调用dismiss())
                dialog.show();  //注意：必须在window.setContentView之前show
                Window window = dialog.getWindow();
                window.setContentView(R.layout.out_exce_dialogl);

                outNum = window.findViewById(R.id.out_num);
                excel_download_status = window.findViewById(R.id.excel_download_status);
               excel_tip=window.findViewById(R.id.excel_tip);
                excel_download_start = window.findViewById(R.id.excel_download_start);
                excel_download_cancel = window.findViewById(R.id.excel_download_cancel);
                excel_file_position = window.findViewById(R.id.excel_file_position);
                share_file=window.findViewById(R.id.share_file);
                outNum.setText("申请同意人数:" + excelDataItemList.size());
                excel_download_status.setText("进度：未开始");

                //开始
                excel_download_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(excelDataItemList.size()>0){
                            excel_download_start.setEnabled(false);
                            excel_download_start.setBackgroundColor(Color.parseColor("#bfbfbf"));
                            excel_download_status.setText("进度：正在生成...");
                            //开始生成excel表格
                            jsonToExcel();
                        }else {
                            excel_tip.setText("当前申请数据为0，请关闭刷新数据");
                        }


                    }
                });
                //关闭
                excel_download_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //系统分享
//                share_file.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        File file=new File(excelPath);
//                        if(file.exists()){
//                            Uri excelURi = Uri.fromFile(file);
//                            Intent shareIntent = new Intent();
//                            shareIntent.setAction(Intent.ACTION_SEND);
//                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            shareIntent.setType("*/*");
//                            shareIntent.putExtra(Intent.EXTRA_STREAM, excelURi);
//                            startActivity(Intent.createChooser(shareIntent, "分享到"));
//                        }else {
//                            Toast.makeText(ApplyFormActivity.this,"文件丢失",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });


            }
        });
    }

    /*json数据生产excel文件*/
    private void jsonToExcel() {
        //要导出的字段
        String[] title = {"姓名", "学院", "学号"};
        File file;
        File dir = new File(ApplyFormActivity.this.getExternalFilesDir(null).getPath());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
            Date date=new Date();
           data=simpleDateFormat.format(date);
        }
        file = new File(dir, excelName+".xls");
        excelPath=file.getPath();
        if (!dir.exists()) {
            dir.mkdirs();
        }
// 创建Excel工作表

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            wwb = Workbook.createWorkbook(os);
            sheet = wwb.createSheet("a", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
// 添加第一个工作表并设置第一个Sheet的名字
        Label label;
        for (int i = 0; i < title.length; i++) {
// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
// 在Label对象的子对象中指明单元格的位置和内容
            label = new Label(i, 0, title[i], getHeader());
// 将定义好的单元格添加到工作表中
            try {
                sheet.addCell(label);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
//exportOrder就是你要导出的对应字段值
        for (int i = 0; i < excelDataItemList.size(); i++) {
            SimpleUserDataNoId userDataNoId = excelDataItemList.get(i);
            Label a = new Label(0, i + 1, userDataNoId.getStuName());
            Label b = new Label(1, i + 1, userDataNoId.getStuCollege());
            Label c = new Label(2, i + 1, userDataNoId.getStuNumber());
            try {
                sheet.addCell(a);
                sheet.addCell(b);
                sheet.addCell(c);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
// 写入数据
        try {
            wwb.write();

        } catch (IOException e) {
            e.printStackTrace();
        }
// 关闭文件
        try {
            excel_file_position.setText(excelPath);
            excel_download_status.setText("进度：已完成！");
            filePath=excelPath;
            excel_download_cancel.setEnabled(true);
           // excel_download_start.setVisibility(View.GONE);
           // share_file.setVisibility(View.VISIBLE);
            wwb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }



    public static WritableCellFormat getHeader() {
        WritableFont font = new WritableFont(WritableFont.TIMES, 10,
                WritableFont.BOLD);// 定义字体
        WritableCellFormat format = new WritableCellFormat(font);
        return format;
    }
    //一键同意：更新数据
    private void updateApplyListStatus(List<Integer> integerList) {
        Message message = new Message();
        OkHttpClient okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        FormBody body = new FormBody.Builder().add("ids", gson.toJson(integerList)).build();
        final Request request = new
                Request.Builder().url(Constant.url + "apply/setStatusByIds").post(body).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                message.what = 100;
                handler.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                message.what = 4;
                message.obj = string;
                handler.sendMessage(message);
            }
        });


    }


    private void findView() {
        listView = findViewById(R.id.apply_form_item);
        return1 = findViewById(R.id.back_i);
        applyNum = findViewById(R.id.apply_num);
        resultNum = findViewById(R.id.apply_result);
        allToOk = findViewById(R.id.all_apply_to_ok);
        outStu = findViewById(R.id.out_stu_information_excel);
    }

    private void initData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("id", String.valueOf(aId)).build();
        final Request request = new
                Request.Builder().url(Constant.url + "apply/getAllById").post(body).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 1;//网络连接失败
                handler.sendMessage(message);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Gson gson = new Gson();
                GetApplyRestful restful = gson.fromJson(string,GetApplyRestful.class);
                if(restful.getCode()==2000){
                    for (ApplyManage applyForm : restful.getData()) {
                        dataSource.add(applyForm);
                    }

                    Message message = new Message();
                    message.what = 2;
                    message.obj = string;
                    handler.sendMessage(message);
                }

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast toast4 = Toast.makeText(ApplyFormActivity.this, "网络连接失败", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
            }
            if (msg.what == 2) {

                for (ApplyManage applyForm : dataSource) {
                    if (applyForm.getApplyResult() == 2 || applyForm.getApplyResult() == 4) {
                        OkNum = OkNum + 1;
                    }
                }
                resultNum.setText("处理：" + OkNum);
                applyNum.setText("申请：" + dataSource.size());
                applyFormItemAdapter.notifyDataSetChanged();
            }
            if (msg.what == 4) {

                succeed_set_up.setVisibility(View.VISIBLE);
                che_xiao.setVisibility(View.VISIBLE);
                fail_set_up.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                UpDataApplyStatusResul resul=gson.fromJson(msg.obj.toString(), UpDataApplyStatusResul.class);
                List<SetUpResult> list = resul.getData();
                int s = 0;//成功的申请数
                int c = 0;//撤销的申请数
                List<String> cNameList = new ArrayList<>();//撤销的人员名单
                int f = 0;//失败的数量
                List<String> fNameList = new ArrayList<>();//失败的人员名单
                for (SetUpResult setUpResult : list) {
                    String name = "";
                    int dataSourcePosition = 0;
                    for (int i = 0; i < dataSource.size(); i++) {
                        if (setUpResult.getApplyId() == dataSource.get(i).getId()) {
                            dataSourcePosition = i;
                            name = dataSource.get(i).getStudent().getStuName();
                            break;
                        }
                    }
                    if (setUpResult.getSetUpResult() == 1) {
                        s = s + 1;
                        dataSource.get(dataSourcePosition).setApplyResult(2);
                    } else if (setUpResult.getSetUpResult() == 0) {
                        f = f + 1;
                        fNameList.add(name);
                    } else if (setUpResult.getSetUpResult() == 2) {
                        c = c + 1;
                        cNameList.add(name);
                        dataSource.remove(dataSourcePosition);
                    }
                }
                succeed_set_up.setText("成功：" + s);
                che_xiao.setText("用户撤销申请：" + c + " 人 " + "(" + cNameList.toString() + ")");
                fail_set_up.setText("失败：" + f + " 人 " + "(" + fNameList.toString() + ")");
                all_ok_tip.setText("处理完毕");
                no.setEnabled(true);
                no.setBackground(getResources().getDrawable(R.drawable.bg_apply));
                EventBus.getDefault().post("Ok2");
            }
            if (msg.what == 100) {
               /* Toast toast4 = Toast.makeText(ApplyFormActivity.this, "无未处理请求", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();*/
                all_ok_tip.setText("网络连接失败，请检查网络");
                yesButton.setBackground(getResources().getDrawable(R.drawable.bg_apply));
                no.setBackground(getResources().getDrawable(R.drawable.bg_apply));
                yesButton.setEnabled(true);
                no.setEnabled(true);

            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(String s) {
        Log.e("EventBus", "接受消息");


        if (s.equals("Ok") && OkNum < dataSource.size()) {
            OkNum = OkNum + 1;
            resultNum.setText("处理：" + OkNum);

        } else if (s.equals("Ok2")) {
            okNum();
        }
    }
    private void okNum() {
        int ok = 0;
        for (int i = 0; i < dataSource.size(); i++) {
            int status = dataSource.get(i).getApplyResult();
            if (status == 2 || status == 4) {
                ok = ok + 1;
            }
        }
        resultNum.setText("处理：" + ok);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("EventBus", "销毁调用");
        EventBus.getDefault().unregister(this);
    }
}
