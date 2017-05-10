package com.example.limin.ehelp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tompkins on 2017/5/9.
 */

public class Information extends AppCompatActivity {
    private Button btn_back;
    private TextView title;
    private TextView next;
    private EditText username;
    private EditText phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setTitle();
        //二次载入需要加载个人信息
        /*
        username.setText();
        phone.setText();
        */

    }

    private void setTitle() {
        btn_back = (Button) findViewById(R.id.btn_back);
        title = (TextView) findViewById(R.id.tv_title);
        username = (EditText)findViewById(R.id.username);
        phone = (EditText)findViewById(R.id.phone);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        title.setText("个人信息");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String un = username.getText().toString();
                String ph = phone.getText().toString();
                //这里需要将个人信息数据传入数据库
                Toast.makeText(Information.this, "修改个人信息成功!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
