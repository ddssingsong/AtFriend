package com.dds.atfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.dds.atfriend.widget.MsgEditText;

import java.util.Random;

/**
 * 仿微信@ 好友测试
 */
public class AtFriendActivity extends AppCompatActivity {

    private MsgEditText mEditText;
    //String[] str = new String[]{"123456789", "123456789", "123456789", "123456789", "123456789", "123456789"};
    String[] str = new String[]{"dsdsd", "fdfdfd", "hghghgh", "gtrtrt", "rtrtrt", "sdsdsd"};
    private final static String MASK_STR = "@";


    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, AtFriendActivity.class);
        activity.startActivity(intent);
    }

    private class MyInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source.toString().equalsIgnoreCase("@") || source.toString().equalsIgnoreCase("＠")) {
            }
            return source;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_friend);
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
