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
    private Button allToOk;//????????????
    private Button outStu;//??????????????????
    private int OkNum = 0;//?????????????????????
    private String filePath;//????????????
    /*d
    ialog ?????????
    * */
    private AlertDialog dialog;
    /*????????????Dialog*/
    private TextView all_ok_tip;
    private TextView to_set_up;
    private TextView succeed_set_up;
    private TextView che_xiao;
    private TextView fail_set_up;
    private Button yesButton;
    private Button no;

    /*
     * ???excel??????Dialog
     * */
    private TextView outNum;//???????????????
    private TextView excel_download_status;//????????????
    private Button excel_download_start;//??????
    private Button excel_download_cancel;//????????????
    private TextView excel_file_position;//?????????????????????
    private Button share_file;//????????????
    private TextView excel_tip;
    private List<SimpleUserDataNoId> excelDataItemList = new ArrayList<>();//excel????????????


    private List<ApplyManage> dataSource = new ArrayList<>();
    private int aId;
    private ApplyFormItemAdapter applyFormItemAdapter;
    private WritableWorkbook wwb;


    /*json??????excel*/
    private WritableSheet sheet;
    private String excelName;//????????????
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

        excelName="???????????????";
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

        //???????????????????????????????????????????????????
        allToOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> integerList = new ArrayList<>();
                for (int i = 0; i < dataSource.size(); i++) {
                    if (dataSource.get(i).getApplyResult() == 2 || dataSource.get(i).getApplyResult() == 4) {
                        //??????????????????????????????
                    } else {
                        integerList.add(dataSource.get(i).getId());
                    }
                }
                dialog = new AlertDialog.Builder(ApplyFormActivity.this).create();
                dialog.setCanceledOnTouchOutside(false);//????????????????????????(?????????dismiss())
                dialog.show();  //??????????????????window.setContentView??????show
                Window window = dialog.getWindow();
                window.setContentView(R.layout.is_set_all_to_ok);
                all_ok_tip = window.findViewById(R.id.all_ok_tip);
                to_set_up = window.findViewById(R.id.to_set_up);
                succeed_set_up = window.findViewById(R.id.succeed_set_up);
                che_xiao = window.findViewById(R.id.che_xiao);
                fail_set_up = window.findViewById(R.id.fail_set_up);
                yesButton = window.findViewById(R.id.set_ok);
                no = window.findViewById(R.id.set_no);

                //?????????????????????
                to_set_up.setText("???????????????" + integerList.size());
                //????????????
                succeed_set_up.setVisibility(View.GONE);
                che_xiao.setVisibility(View.GONE);
                fail_set_up.setVisibility(View.GONE);

                //??????
                if (integerList.size() == 0) {
                    all_ok_tip.setText("????????????????????????0???????????????");
                    yesButton.setEnabled(false);
                    yesButton.setBackgroundColor(Color.parseColor("#bfbfbf"));
                } else {
                    yesButton.setEnabled(true);
                    /*yesButton.setBackground(getResources().getDrawable(R.drawable.bg_apply));*/
                }
                //????????????????????????????????????
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        all_ok_tip.setText("????????????...,???????????????????????????");
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


        //??????????????????
        outStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //??????????????????
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
                dialog.setCanceledOnTouchOutside(false);//????????????????????????(?????????dismiss())
                dialog.show();  //??????????????????window.setContentView??????show
                Window window = dialog.getWindow();
                window.setContentView(R.layout.out_exce_dialogl);

                outNum = window.findViewById(R.id.out_num);
                excel_download_status = window.findViewById(R.id.excel_download_status);
               excel_tip=window.findViewById(R.id.excel_tip);
                excel_download_start = window.findViewById(R.id.excel_download_start);
                excel_download_cancel = window.findViewById(R.id.excel_download_cancel);
                excel_file_position = window.findViewById(R.id.excel_file_position);
                share_file=window.findViewById(R.id.share_file);
                outNum.setText("??????????????????:" + excelDataItemList.size());
                excel_download_status.setText("??????????????????");

                //??????
                excel_download_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(excelDataItemList.size()>0){
                            excel_download_start.setEnabled(false);
                            excel_download_start.setBackgroundColor(Color.parseColor("#bfbfbf"));
                            excel_download_status.setText("?????????????????????...");
                            //????????????excel??????
                            jsonToExcel();
                        }else {
                            excel_tip.setText("?????????????????????0????????????????????????");
                        }


                    }
                });
                //??????
                excel_download_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //????????????
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
//                            startActivity(Intent.createChooser(shareIntent, "?????????"));
//                        }else {
//                            Toast.makeText(ApplyFormActivity.this,"????????????",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });


            }
        });
    }

    /*json????????????excel??????*/
    private void jsonToExcel() {
        //??????????????????
        String[] title = {"??????", "??????", "??????"};
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
// ??????Excel?????????

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
// ??????????????????????????????????????????Sheet?????????
        Label label;
        for (int i = 0; i < title.length; i++) {
// Label(x,y,z) ?????????????????????x+1?????????y+1???, ??????z
// ???Label??????????????????????????????????????????????????????
            label = new Label(i, 0, title[i], getHeader());
// ?????????????????????????????????????????????
            try {
                sheet.addCell(label);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
//exportOrder????????????????????????????????????
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
// ????????????
        try {
            wwb.write();

        } catch (IOException e) {
            e.printStackTrace();
        }
// ????????????
        try {
            excel_file_position.setText(excelPath);
            excel_download_status.setText("?????????????????????");
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
                WritableFont.BOLD);// ????????????
        WritableCellFormat format = new WritableCellFormat(font);
        return format;
    }
    //???????????????????????????
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
                message.what = 1;//??????????????????
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
                Toast toast4 = Toast.makeText(ApplyFormActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();
            }
            if (msg.what == 2) {

                for (ApplyManage applyForm : dataSource) {
                    if (applyForm.getApplyResult() == 2 || applyForm.getApplyResult() == 4) {
                        OkNum = OkNum + 1;
                    }
                }
                resultNum.setText("?????????" + OkNum);
                applyNum.setText("?????????" + dataSource.size());
                applyFormItemAdapter.notifyDataSetChanged();
            }
            if (msg.what == 4) {

                succeed_set_up.setVisibility(View.VISIBLE);
                che_xiao.setVisibility(View.VISIBLE);
                fail_set_up.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                UpDataApplyStatusResul resul=gson.fromJson(msg.obj.toString(), UpDataApplyStatusResul.class);
                List<SetUpResult> list = resul.getData();
                int s = 0;//??????????????????
                int c = 0;//??????????????????
                List<String> cNameList = new ArrayList<>();//?????????????????????
                int f = 0;//???????????????
                List<String> fNameList = new ArrayList<>();//?????????????????????
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
                succeed_set_up.setText("?????????" + s);
                che_xiao.setText("?????????????????????" + c + " ??? " + "(" + cNameList.toString() + ")");
                fail_set_up.setText("?????????" + f + " ??? " + "(" + fNameList.toString() + ")");
                all_ok_tip.setText("????????????");
                no.setEnabled(true);
                no.setBackground(getResources().getDrawable(R.drawable.bg_apply));
                EventBus.getDefault().post("Ok2");
            }
            if (msg.what == 100) {
               /* Toast toast4 = Toast.makeText(ApplyFormActivity.this, "??????????????????", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                toast4.show();*/
                all_ok_tip.setText("????????????????????????????????????");
                yesButton.setBackground(getResources().getDrawable(R.drawable.bg_apply));
                no.setBackground(getResources().getDrawable(R.drawable.bg_apply));
                yesButton.setEnabled(true);
                no.setEnabled(true);

            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMsg(String s) {
        Log.e("EventBus", "????????????");


        if (s.equals("Ok") && OkNum < dataSource.size()) {
            OkNum = OkNum + 1;
            resultNum.setText("?????????" + OkNum);

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
        resultNum.setText("?????????" + ok);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("EventBus", "????????????");
        EventBus.getDefault().unregister(this);
    }
}
