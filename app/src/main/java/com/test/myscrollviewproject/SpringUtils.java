package com.test.myscrollviewproject;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

class SpringUtils {
    public static void startSpringScroll(final View view, final int l, final int t, final int r, final int b, float factor) {
        if (view == null) {
            return;
        }
        float maxLength = view.getResources().getDisplayMetrics().density * 120;
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
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}
