package com.test.myscrollviewproject;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class CustomView2 extends FrameLayout {
    public CustomView2(Context context) {
        super(context);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public void addView(View child) {
        Log.i("=====","=====addView111111");
        super.addView(child);
        Log.i("=====","=====addView2222222");
    }
    @Override
    protected void onFinishInflate() {
        Log.i("=====","=====onFinishInflate1111111111");
        super.onFinishInflate();
        Log.i("=====","=====onFinishInflate2222222222");
    }
}
