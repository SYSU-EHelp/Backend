package com.example.limin.ehelp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * Created by limin on 2017/4/30.
 */
public class HomeActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FloatingActionButton createBtn;
    private PopUpDialog popDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = this.getIntent().getExtras();

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("求助", AskFragment.class)
                .add("提问", HelpFragment.class)
                .add("状态", StateFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        createBtn = (FloatingActionButton) findViewById(R.id.fab);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        popDialog = new PopUpDialog(this, onClickListener);
        //showAtLocation(View parent, int gravity, int x, int y)
        popDialog.showAtLocation(findViewById(R.id.layout_home), Gravity.CENTER, 0, 0);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_createhelp:
                    // Toast.makeText(HomeActivity.this, "发求助", Toast.LENGTH_SHORT).show();
                    popDialog.dismiss();
                    Intent intent = new Intent(HomeActivity.this, EditHelpActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_createquestion:
                    Toast.makeText(HomeActivity.this, "发提问", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id ==R.id.action_userinfo) {
            /*Intent i=new Intent(this,Information.class) ;
            startActivity(i);*/
            //这里的跳转有问题,未解决!!!
            return true;
        }

        if (id ==R.id.action_urgencycontact) {
            return true;
        }

        if (id ==R.id.action_logout) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

