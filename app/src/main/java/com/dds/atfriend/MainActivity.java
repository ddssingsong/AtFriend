package com.dds.atfriend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void OnClick(View view) {
        AtFriendActivity.openActivity(this);
    }

    public void OnClick1(View view) {
        AtContactActivity.openActivity(this);
    }
}
