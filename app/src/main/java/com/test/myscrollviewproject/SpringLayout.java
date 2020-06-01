package com.test.myscrollviewproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

public class SpringLayout extends ViewGroup implements NestedScrollingParent2 {

    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private VelocityTracker velocityTracker;
    private int layoutLeft = -1;
    private int layoutTop = -1;
    private int layoutRight = -1;
    private int layoutBottom = -1;
    private ValueAnimator autoTranslateAnim;
    private float initTranslateY;

    public SpringLayout(Context context) {
        super(context);
        init();
    }

    public int dp2px(int value){
        return (int) (getResources().getDisplayMetrics().density*value);
    }

    public SpringLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpringLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpringLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
        }
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == null || child.getVisibility() == View.GONE) {
                continue;
            }
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int childLeft = left + paddingLeft;
            int childTop = top + paddingTop;

            layoutLeft = childLeft;
            layoutTop = childTop;
            layoutRight = childLeft + width;
            layoutBottom = childTop + height;
            child.layout(layoutLeft, layoutTop, layoutRight, layoutBottom);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initTranslateY = getTranslationY();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1 && isInEditMode()) {
            throw new IllegalStateException("SpringLayout no more than one children");
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
        boolean b = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        if(true){
            return b;
        }
        Log.i("=====","====onStartNestedScroll="+type);
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL&&type==ViewCompat.TYPE_TOUCH) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    public NestedScrollingParentHelper getNestedScrollingParentHelper() {
        return nestedScrollingParentHelper;
    }

    boolean isAnim;

    private void startHuiTan() {
        isAnim=true;
       final View childAt = getChildAt(0);
        float translationY = childAt.getTranslationY();
        if(translationY-initTranslateY==0){
            return;
        }
        pauseAutoTranslateAnim();
        autoTranslateAnim = ValueAnimator.ofFloat( translationY, 0);
        autoTranslateAnim.setDuration(400);
        autoTranslateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                totalTranslate-=animatedValue;
                Log.i("=====","=====animatedValue:"+animatedValue);
                childAt.setTranslationY(animatedValue);
            }
        });
        autoTranslateAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnim=false;
            }
        });
//        autoTranslateAnim.setInterpolator(new LinearInterpolator());
        autoTranslateAnim.setInterpolator(new FluidInterpolator());
        autoTranslateAnim.start();
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, @ViewCompat.NestedScrollType int type) {
    }

    boolean canSpringScroll=true;
    volatile int totalTranslate;
    public int getMaxTranslate(){
        int height = getHeight()/2;
        if(height<=0){
            height = (int) (getResources().getDisplayMetrics().density * 300);
        }
        return  height;
    }
    private ScrollListener scrollListener;

    public ScrollListener getScrollListener() {
        if(scrollListener==null){
            scrollListener=new ScrollListener() {
                @Override
                public void notify(String string) {

                }
            };
        }
        return scrollListener;
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type) {
        /*boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (canSpringScroll&&!canScrollUp&&type==ViewCompat.TYPE_NON_TOUCH) {
            canSpringScroll=false;
            velocityTracker.computeCurrentVelocity(1000,2400);
            startSpringScroll(velocityTracker.getYVelocity()*-1);
        }*/
        if(dyUnconsumed<=0||type==ViewCompat.TYPE_NON_TOUCH){
            return;
        }
        pauseAutoTranslateAnim();
        Log.i("=====","=====dyUnconsumed:"+dyUnconsumed);
        View childAt = getChildAt(0);
       /* if(totalTranslate>getMaxTranslate()){
            getScrollListener().notify(totalTranslate+"划不动"+getMaxTranslate());
            return;
        }*/
        int tempLength = dyUnconsumed / 2;
        getScrollListener().notify(tempLength+"===="+totalTranslate);
        totalTranslate-=tempLength;
        childAt.setTranslationY(initTranslateY+totalTranslate);
    }
    @Override
    public void onStopNestedScroll(@NonNull View target, @ViewCompat.NestedScrollType int type) {
        nestedScrollingParentHelper.onStopNestedScroll(target, type);
        Log.i("=====","=====onStopNestedScrollxxyy:"+type);
        /*if(!fingerUp ||type==ViewCompat.TYPE_NON_TOUCH){
            return;
        }*/
        totalTranslate=0;
//        Log.i("=====","=====onStopNestedScroll:"+type);
        Log.i("=====","=====onStopNestedScrollxxss:"+type);
        startHuiTan();
    }
    /*@Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (!canScrollUp) {
//            startSpringScroll(velocityY);
        }
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (canSpringScroll&&!canScrollUp*//*&&type==ViewCompat.TYPE_NON_TOUCH*//*) {
            canSpringScroll=false;
            velocityTracker.computeCurrentVelocity(1000,24000);
//            startSpringScroll(velocityTracker.getYVelocity()*-1);

//            return true;
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }*/

    private void startSpringScroll(float velocityY) {
        if (velocityY <= 0) {
//            canSpringScroll=false;
            return;
        }
        final  View childAt = getChildAt(0);
        if (childAt == null) {
//            canSpringScroll=false;
            return;
        }
        if (layoutLeft == -1 && layoutTop == -1 && layoutRight == -1 && layoutBottom == -1) {
//            canSpringScroll=false;
            return;
        }
//        SpringUtils.startSpringScroll(childAt, layoutLeft, layoutTop, layoutRight, layoutBottom, velocityY / 24000f);

        float maxLength = getResources().getDisplayMetrics().density * 120;
        int resultLength= (int) (maxLength*velocityY / 24000f);
        if(resultLength<=0){
//            canSpringScroll=false;
            return;
        }
        Log.i("=====","=====ValueAnimator");
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, resultLength, 0);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                if (childAt == null) {
                    return;
                }
                childAt.layout(layoutLeft, layoutTop-animatedValue, layoutRight, layoutBottom-animatedValue);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });
        valueAnimator.setInterpolator(new FluidInterpolator());
        valueAnimator.start();
    }
    private void startSpringScroll2( ) {

        final  View childAt = getChildAt(0);


        Log.i("=====","=====ValueAnimator");
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) childAt.getTranslationY(), 0);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                if (childAt == null) {
                    return;
                }
                childAt.setTranslationY(animatedValue);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                totalTranslate=0;
                Log.i("=====","=====totalTranslate"+totalTranslate);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
    private boolean fingerUp;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(true){
            return super.dispatchTouchEvent(ev);
        }
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            canSpringScroll=true;
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                fingerUp=false;
                pauseAutoTranslateAnim();
            break;
            case MotionEvent.ACTION_MOVE:
                View childAt = getChildAt(0);
                boolean canScroll = ViewCompat.canScrollVertically(childAt, 1);
                if(!canScroll){

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                fingerUp=true;
                startHuiTan();
            break;
        }
        return super.dispatchTouchEvent(ev);
    }



    private void pauseAutoTranslateAnim() {
        if(autoTranslateAnim==null){
            return;
        }
        autoTranslateAnim.cancel();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (velocityTracker != null) {
            velocityTracker.recycle();
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
        }

        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull LayoutParams source) {
            super(source);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) lp);
        } else if (lp instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
}
