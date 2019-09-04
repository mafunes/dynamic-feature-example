package com.mafunes.example.java;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.mafunes.example.sdk.BaseSplitActivity;

public class FirstActivity extends BaseSplitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                Test aTest = new Test();
                aTest.aString = "test";
                aTest.aTest = new OtherTest();
                aTest.aTest.aString = "aString";
                Intent intent = new Intent();
                intent.setData(Uri.parse("meli://second"));
                intent.putExtra("key", aTest);
                FirstActivity.this.startActivity(intent);
            }
        }, 1000);

    }
}
