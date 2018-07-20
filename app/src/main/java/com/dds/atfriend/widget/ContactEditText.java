package com.dds.atfriend.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dds.atfriend.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 模仿@联系人 整块删除 不可编辑
 * Created by dds on 2017/11/1.
 * <p>
 * QQ: 710715508
 */

public class ContactEditText extends AppCompatAutoCompleteTextView {
    private int itemPadding;

    public ContactEditText(Context context) {
        super(context);
        init();
    }


    public ContactEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        itemPadding = dip2px(getContext(), 3);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        MyImageSpan[] spans = getText().getSpans(0, getText().length(), MyImageSpan.class);
        for (MyImageSpan myImageSpan : spans) {
            if (getText().getSpanEnd(myImageSpan) - 1 == selStart) {
                selStart = selStart + 1;
                setSelection(selStart);
                break;
            }
        }
        super.onSelectionChanged(selStart, selEnd);
    }

    private void flushSpans() {
        Editable editText = getText();
        Spannable spannableString = new SpannableString(editText);
        MyImageSpan[] spans = spannableString.getSpans(0, editText.length(), MyImageSpan.class);
        List<UnSpanText> texts = getAllTexts(spans, editText);
        for (UnSpanText unSpanText : texts) {
            if (!TextUtils.isEmpty(unSpanText.showText.toString().trim())) {
                generateOneSpan(spannableString, unSpanText);
            }
        }
        setText(spannableString);
        setSelection(spannableString.length());
    }

    private List<UnSpanText> getAllTexts(MyImageSpan[] spans, Editable edittext) {
        List<UnSpanText> texts = new ArrayList<>();
        int start;
        int end;
        CharSequence text;
        List<Integer> sortStartEnds = new ArrayList<>();
        sortStartEnds.add(0);
        for (MyImageSpan myImageSpan : spans) {
            sortStartEnds.add(edittext.getSpanStart(myImageSpan));
            sortStartEnds.add(edittext.getSpanEnd(myImageSpan));
        }
        sortStartEnds.add(edittext.length());
        Collections.sort(sortStartEnds);
        for (int i = 0; i < sortStartEnds.size(); i = i + 2) {
            start = sortStartEnds.get(i);
            end = sortStartEnds.get(i + 1);
            text = edittext.subSequence(start, end);
            if (!TextUtils.isEmpty(text)) {
                texts.add(new UnSpanText(start, end, text));
            }
        }

        return texts;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            flushSpans();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        Log.d("dds", "performFiltering" + text + ":" + keyCode);
        super.performFiltering(text, keyCode);
    }

    //添加一个Span
    public void addSpan(String showText) {
        getText().append(showText);
        SpannableString spannableString = new SpannableString(getText());
        generateOneSpan(spannableString, new UnSpanText(spannableString.length() - showText.length(), spannableString.length(), showText));
        setText(spannableString);
        setSelection(spannableString.length());
    }

    private void generateOneSpan(Spannable spannableString, UnSpanText unSpanText) {
        View spanView = getSpanView(getContext(), unSpanText.showText.toString(), getMeasuredWidth());
        BitmapDrawable bitmapDrawable = (BitmapDrawable) convertViewToDrawable(spanView);
        bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());
        MyImageSpan what = new MyImageSpan(bitmapDrawable, unSpanText.showText.toString());
        final int start = unSpanText.start;
        final int end = unSpanText.end;
        spannableString.setSpan(what, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        cacheBmp.recycle();
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }

    public View getSpanView(Context context, String text, int maxWidth) {
        TextView view = new TextView(context);
        view.setMaxWidth(maxWidth);
        view.setText(text);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setSingleLine(true);
        view.setBackgroundResource(R.drawable.shape_corner_rectangle);
        //view.setBackgroundResource(R.drawable.shape_corner_rectangle);
        view.setTextSize(getTextSize());
        //view.setTextColor(getCurrentTextColor());
        view.setTextColor(Color.BLUE);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
        frameLayout.addView(view);
        return frameLayout;
    }

    private class UnSpanText {
        int start;
        int end;
        CharSequence showText;

        UnSpanText(int start, int end, CharSequence showText) {
            this.start = start;
            this.end = end;
            this.showText = showText;
        }
    }

    private class MyImageSpan extends ImageSpan {
        private String showText;

        public MyImageSpan(Drawable d, String showText) {
            super(d);
            this.showText = showText;
        }

        public String getShowText() {
            return showText;
        }
    }

    /**
     * dip转换px
     */
    public static int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }
}
