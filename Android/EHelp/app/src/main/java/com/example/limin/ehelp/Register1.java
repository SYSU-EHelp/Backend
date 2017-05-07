package com.example.limin.ehelp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by limin on 2017/4/30.
 */
public class Register1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register1);

        Button next = (Button)findViewById(R.id.next);
        final EditText phone = (EditText)findViewById(R.id.phone);
        final EditText verified = (EditText)findViewById(R.id.verified);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Toast.makeText(Register1.this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(verified.getText().toString())){
                    Toast.makeText(Register1.this, "请先输入您的手机验证码", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Register1.this,Register2.class);
                    startActivity(intent);
                }
            }
        });
    }
}

