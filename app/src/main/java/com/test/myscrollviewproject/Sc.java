package com.test.myscrollviewproject;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class Sc extends NestedScrollView {

    private VelocityTracker velocityTracker;

    public Sc(@NonNull Context context) {
        super(context);
    }

    public Sc(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Sc(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        Log.i("=====","=====onScrollChanged:"+l+"======="+t+"======="+oldl+"======="+oldt);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        Log.i("=====","=====onLayout");
    }

    int a = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        Log.i("=====", "=====onOverScrolled:" + scrollX + "=======" + scrollY + "=======" + clampedX + "=======" + clampedY);
        if(scrollY>0&&clampedY){
            velocityTracker.computeCurrentVelocity(1000,24000);
            float yVelocity = velocityTracker.getYVelocity()*-1;
            View childAt = getChildAt(0);
            startSpringScroll(childAt,0,0,childAt.getMeasuredWidth(),childAt.getMeasuredHeight(),yVelocity/24000);
        }
    }
    public void startSpringScroll(final View view, final int l, final int t, final int r, final int b, float factor) {
        if (view == null) {
            return;
        }
        float maxLength = view.getResources().getDisplayMetrics().density * 150;
        int resultLength= (int) (maxLength*factor);
        if(resultLength<=0){
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, resultLength, 0);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                if (view == null) {
                    return;
                }
                view.layout(l, t-animatedValue, r, b-animatedValue);
            }
        });
        Path path = new Path();
        path.moveTo(0,0);
        path.cubicTo(.14f,.32f,.85f,.67f,1,1);
//        path.cubicTo(0,0.4f,1,0.6f,1,1);
        Interpolator interpolator = PathInterpolatorCompat.create(path);
//        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}
