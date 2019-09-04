package com.mafunes.example.dynamicfeaturetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.mafunes.example.sdk.BaseSplitActivity;


public class DynamicMainActivity extends BaseSplitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_main);
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                DynamicMainActivity.this.startActivity(new Intent().setData(Uri.parse("meli://first")));
                finish();
            }
        }, 1000);

    }
}
