package com.example.minidouyin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.util.Objects;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout relativelayout;
    /**
     * 首页
     */
    private TextView tv_home;
    private ImageView iv_home;
    private ImageView iv_shoot;
    /**
     * 消息
     */
    private TextView tv_msg;
    private ImageView iv_msg;
    private TextView tv_me;
    private ImageView iv_me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, new Home()).commit();
    }

    private void initView() {
        relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_shoot = (ImageView) findViewById(R.id.iv_shoot);
        iv_shoot.setOnClickListener(this);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        iv_msg = (ImageView) findViewById(R.id.iv_msg);
        tv_msg.setOnClickListener(this);

        tv_me = (TextView) findViewById(R.id.tv_me);
        iv_me = (ImageView) findViewById(R.id.iv_me);
        tv_me.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, new Home()).commit();
                iv_msg.setBackgroundResource(R.drawable.linen);
                tv_msg.setTextColor(Color.parseColor("#ffffff"));
                iv_home.setBackgroundResource(R.drawable.linew);
                tv_home.setTextColor(Color.parseColor("#4C1733"));
                iv_me.setBackgroundResource(R.drawable.linen);
                tv_me.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.iv_shoot:
                startActivity(new Intent(MainActivity.this, Capsture.class));
                break;
            case R.id.tv_msg: getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, new News()).commit();
                iv_home.setBackgroundResource(R.drawable.linen);
                tv_home.setTextColor(Color.parseColor("#ffffff"));
                iv_msg.setBackgroundResource(R.drawable.linew);
                tv_msg.setTextColor(Color.parseColor("#4C1733"));
                iv_me.setBackgroundResource(R.drawable.linen);
                tv_me.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.tv_me: getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, new Login()).commit();
                iv_home.setBackgroundResource(R.drawable.linen);
                tv_home.setTextColor(Color.parseColor("#ffffff"));
                iv_me.setBackgroundResource(R.drawable.linew);
                tv_me.setTextColor(Color.parseColor("#4C1733"));
                iv_msg.setBackgroundResource(R.drawable.linen);
                tv_msg.setTextColor(Color.parseColor("#ffffff"));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent envet) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onAppExit();
            return true;
        }
        return false;
    }

    private long firstClick;
    public void onAppExit() {
        if (System.currentTimeMillis() - this.firstClick > 2000L) {
            this.firstClick = System.currentTimeMillis();
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_LONG).show();
            return;
        }
        finish();
    }

}
