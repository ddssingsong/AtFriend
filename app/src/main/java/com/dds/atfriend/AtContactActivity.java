package com.dds.atfriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.dds.atfriend.widget.ContactEditText;

import java.util.Random;

/**
 * 仿邮件@ 联系人
 */
public class AtContactActivity extends AppCompatActivity {

    private ContactEditText et_user_contact;
    String[] str = new String[]{"710@qq.com", "美女你好", "我很好", "ss@452.com", "ddddd@452.com", "ss@d4552.com"};

    public static void openActivity(Activity activity) {
        Intent intent = new Intent(activity, AtContactActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_contact);
        et_user_contact = (ContactEditText) findViewById(R.id.et_user_contact);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,str);
        et_user_contact.setAdapter(arrayAdapter);

        //我只是想加个输入完成的监听，怎么这么难呢

    }


    Random random = new Random();

    //添加一个块
    public void addSpan(View view) {
        int aaa = random.nextInt(5);
        et_user_contact.addSpan(str[aaa]);


    }
}
