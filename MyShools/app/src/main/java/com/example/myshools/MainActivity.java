package com.example.myshools;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.myshools.Activity.Activity.ActivitiesFragment;
import com.example.myshools.Consult.ConsultFragemnt;
import com.example.myshools.Mine.organization.MineFragment;
import com.example.myshools.Mine.student.StudentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private int role=-1;
    private String phone="";
    private BottomNavigationView navigationView;
    private FrameLayout linearLayout;
    //用于记录上个选择的Fragment
    public static String lastFragment="活动";
    private Map<String,Fragment> mFragments;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();
        mFragments=new HashMap<String, Fragment>();
        navigationView = findViewById(R.id.bottom_nav);
        linearLayout = findViewById(R.id.linear_frag_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xff7adfb8);
        }
        phone=getSharedPreferences("nextAccount",MODE_PRIVATE).getString("account","");
        if(phone.length()==11){
            role=getSharedPreferences(phone,MODE_PRIVATE).getInt("role",-1);
        }
        initFragment();
    }
    /**
     * 初始化fragment的展示效果
     */
    private void initFragment() {
        String[] orgnat=new String[]{"活动","我的"};
        String[] student=new String[]{"活动","校友圈","我的"};

        ActivitiesFragment activitiesFragment=new ActivitiesFragment();
        ConsultFragemnt consultFragemnt=new ConsultFragemnt();
        MineFragment mineFragment=new MineFragment();
        StudentFragment studentFragment=new StudentFragment();
        mFragments.put("活动",activitiesFragment);
        if (role==0) {
            navigationView.inflateMenu(R.menu.menu_buttom);
            mFragments.put("我的",mineFragment);
        } else {
            navigationView.inflateMenu(R.menu.menu_buttom1);
            mFragments.put("我的",studentFragment);
            mFragments.put("校友圈",consultFragemnt);

        }
        navigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.linear_frag_container,activitiesFragment)
                .show(activitiesFragment)
                .commit();
    }
    /**
     * 切换fragment的展示页面
     */
    private void switchFragment(String lastIndex, String index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mFragments.get(lastIndex));
        transaction.remove(mFragments.get(lastIndex));

        if (!mFragments.get(index).isAdded()) {
            transaction.add(R.id.linear_frag_container, mFragments.get(index));
        }
        transaction.show(mFragments.get(index)).commit();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.tab_spec_1:
                if(!lastFragment.equals("活动")){
                    switchFragment(lastFragment,"活动");
                    lastFragment="活动";
                }
                return true;
            case R.id.tab_spec_2:
                if(!lastFragment.equals("校友圈")){
                    switchFragment(lastFragment,"校友圈");
                    lastFragment="校友圈";
                }
                return true;
            case R.id.tab_spec_3:
                if(!lastFragment.equals("我的")){
                    switchFragment(lastFragment,"我的");
                    lastFragment="我的";
                }
                return true;
        }
        return false;
    }
}
