package com.example.limin.ehelp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yunzhao on 2017/5/10.
 */

public class EditHelpActivity extends AppCompatActivity {

    private Button btn_back;
    private TextView tv_title;
    private TextView tv_nextope;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edithelp);

        setTitle();

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
                Toast.makeText(EditHelpActivity.this, "发送了求助", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
