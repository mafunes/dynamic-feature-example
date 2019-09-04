package com.mafunes.example.java;

import android.os.Bundle;

import com.mafunes.example.sdk.BaseSplitActivity;

public class SecondActivity extends BaseSplitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
