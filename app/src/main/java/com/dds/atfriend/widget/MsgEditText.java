package com.dds.atfriend.widget;


import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;

/**
 * 模仿微信的@功能 整块删除 可编辑
 * Created by dds on 2017/10/25.
 * QQ: 710715508
 */

public class MsgEditText extends AppCompatEditText {


    public MsgEditText(Context context) {
        super(context);
    }

    public MsgEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MsgEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private StringBuilder builder;

    /**
     * 添加一个块,在文字的后面添加
     *
     * @param showText 显示到界面的内容
     * @param userId   附加属性，比如用户id,邮件id之类的，如果不需要可以去除掉
     */
    public void addAtSpan(String maskText, String showText, long userId) {
        builder = new StringBuilder();
        if (!TextUtils.isEmpty(maskText)) {
            //已经添加了@
            builder.append(maskText).append(showText).append(" ");
        } else {
            builder.append(showText).append(" ");
        }
        getText().insert(getSelectionStart(), builder.toString());
        SpannableString sps = new SpannableString(getText());

        int start = getSelectionEnd() - builder.toString().length() - (TextUtils.isEmpty(maskText) ? 1 : 0);
        int end = getSelectionEnd();
        makeSpan(sps, new UnSpanText(start, end, builder.toString()), userId);
        setText(sps);
        setSelection(end);
    }

    //获取用户Id列表
    public String getUserIdString() {
        MyTextSpan[] spans = getText().getSpans(0, getText().length(), MyTextSpan.class);
        StringBuilder builder = new StringBuilder();
        for (MyTextSpan myTextSpan : spans) {
            String realText = getText().toString().substring(getText().getSpanStart(myTextSpan), getText().getSpanEnd(myTextSpan));
            String showText = myTextSpan.getShowText();
            if (realText.equals(showText)) {
                builder.append(myTextSpan.getUserId()).append(",");
            }
        }
        if (!TextUtils.isEmpty(builder.toString())) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    //生成一个需要整体删除的Span
    private void makeSpan(Spannable sps, UnSpanText unSpanText, long userId) {
        MyTextSpan what = new MyTextSpan(unSpanText.returnText, userId);
        int start = unSpanText.start;
        int end = unSpanText.end;
        sps.setSpan(what, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        //向前删除一个字符，@后的内容必须大于一个字符，可以在后面加一个空格
        if (lengthBefore == 1 && lengthAfter == 0) {
            MyTextSpan[] spans = getText().getSpans(0, getText().length(), MyTextSpan.class);
            for (MyTextSpan myImageSpan : spans) {
                if (getText().getSpanEnd(myImageSpan) == start && !text.toString().endsWith(myImageSpan.getShowText())) {
                    getText().delete(getText().getSpanStart(myImageSpan), getText().getSpanEnd(myImageSpan));
                    break;
                }
            }
        }

    }

    private class MyTextSpan extends MetricAffectingSpan {
        private String showText;
        private long userId;

        public MyTextSpan(String showText, long userId) {
            this.showText = showText;
            this.userId = userId;
        }


        public String getShowText() {
            return showText;
        }

        public long getUserId() {
            return userId;
        }

        @Override
        public void updateMeasureState(TextPaint p) {

        }

        @Override
        public void updateDrawState(TextPaint tp) {

        }
    }

    private class UnSpanText {
        int start;
        int end;
        String returnText;

        UnSpanText(int start, int end, String returnText) {
            this.start = start;
            this.end = end;
            this.returnText = returnText;
        }
    }


}
