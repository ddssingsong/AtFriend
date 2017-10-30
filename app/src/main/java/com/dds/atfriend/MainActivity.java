package com.dds.atfriend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private MsgEditText mEditText;
    String[] str = new String[]{"123456789", "123456789", "123456789", "123456789", "123456789", "123456789"};
    private final static String MASK_STR = "@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (MsgEditText) findViewById(R.id.et_user_contact);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1 && s.charAt(s.length() - 1) == MASK_STR.charAt(0)) { //添加一个字
                    //跳转到@界面
                    int aaa = random.nextInt(5);
                    mEditText.addAtSpan(null, str[aaa], 2000);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    Random random = new Random();

    public void addSpan(View view) {
        int aaa = random.nextInt(5);
        mEditText.addAtSpan(MASK_STR, str[aaa], 100000);
        Log.d("ddd", mEditText.getUserIdString());


    }
}
