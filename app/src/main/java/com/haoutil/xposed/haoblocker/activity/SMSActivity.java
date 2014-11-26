package com.haoutil.xposed.haoblocker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.haoutil.xposed.haoblocker.R;
import com.haoutil.xposed.haoblocker.model.SMS;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        SMS sms = (SMS) bundle.get("sms");

        ((TextView) findViewById(R.id.tv_caller)).setText(sms.getSender());
        ((TextView) findViewById(R.id.tv_content)).setText(sms.getContent());
        ((TextView) findViewById(R.id.tv_date)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(sms.getCreated())));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_sms;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}