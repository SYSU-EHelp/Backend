package com.example.limin.ehelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Yunzhao on 2017/5/10.
 */

public class EditHelpActivity extends AppCompatActivity {
    // 标题栏控件
    private Button btn_back;
    private TextView tv_title;
    private TextView tv_nextope;

    // 页面控件
    private EditText et_helptitle;
    private TextView tv_wordcount;
    private EditText et_helpcontent;
    private EditText et_helplocation;
    private TextView tv_useloc;

    // 高德地图
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private String curLoc = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edithelp);

        setTitle();
        findView();

        //初始化定位
        initLocation();

        // 更新求助标题字数统计
        et_helptitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_wordcount.setText(et_helptitle.getText().toString().length() + "/20");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_useloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_helplocation.setText(curLoc);
            }
        });

    }

    private void setTitle() {
        btn_back = (Button) findViewById(R.id.btn_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_nextope = (TextView) findViewById(R.id.tv_nextope);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_title.setText("发求助");

        tv_nextope.setText("发送");
        tv_nextope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(EditHelpActivity.this, "发送了求助", Toast.LENGTH_SHORT).show();
                if (et_helptitle.getText().toString().isEmpty()) {
                    Toast.makeText(EditHelpActivity.this, "求助标题不能为空", Toast.LENGTH_SHORT).show();
                } else if (et_helptitle.getText().toString().length() > 20) {
                    Toast.makeText(EditHelpActivity.this, "求助标题不能超过20个字", Toast.LENGTH_SHORT).show();
                } else if (et_helpcontent.getText().toString().isEmpty()) {
                    Toast.makeText(EditHelpActivity.this, "求助描述不能为空", Toast.LENGTH_SHORT).show();
                } else if (et_helplocation.getText().toString().isEmpty()) {
                    Toast.makeText(EditHelpActivity.this, "求助地点不能为空", Toast.LENGTH_SHORT).show();
                } else if (!isValidLoc(et_helplocation.getText().toString())) {
                    Toast.makeText(EditHelpActivity.this, "找不到您输入的求助地点，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditHelpActivity.this, "发求助成功", Toast.LENGTH_SHORT).show();
                    // 跳转至正在求助页面，还没写那部分，暂时跳转至主页
                    Intent intent = new Intent(EditHelpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean isValidLoc(String mLoc) {

        return true;
    }

    private void startLocation(){
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    private void stopLocation(){
        // 停止定位
        locationClient.stopLocation();
    }

    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append(location.getAddress());
                } else {
                    //定位失败
//                    sb.append("定位失败" + "\n");
//                    sb.append("错误码:" + location.getErrorCode() + "\n");
//                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
//                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    Toast.makeText(EditHelpActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                }
                //定位之后的回调时间
                //sb.append("回调时间: " + PositionUtils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                curLoc = sb.toString();
            } else {
                Toast.makeText(EditHelpActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    private void findView() {
        et_helptitle = (EditText) findViewById(R.id.et_helptitle);
        tv_wordcount = (TextView) findViewById(R.id.tv_wordcount);
        et_helpcontent = (EditText) findViewById(R.id.et_helpcontent);
        et_helplocation = (EditText) findViewById(R.id.et_helplocation);
        tv_useloc = (TextView) findViewById(R.id.tv_useloc);
    }

    @Override
    protected void onPause() {
        stopLocation();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startLocation();
        super.onResume();
    }
}
