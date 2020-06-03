package com.test.myscrollviewproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingParent;
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
import android.widget.OverScroller;

public class SpringLayout extends ViewGroup implements NestedScrollingParent2 {

    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private VelocityTracker velocityTracker;
    private OverScroller scroller;
    private int layoutLeft = -1;
    private int layoutTop = -1;
    private int layoutRight = -1;
    private int layoutBottom = -1;
    private ValueAnimator autoTranslateAnim;
    private volatile float translateYOffset;
    private View childView;
    private float maxHeight;

    private long durationTime = 550;

    public SpringLayout(Context context) {
        super(context);
        init();
    }

    public int dp2px(int value) {
        return (int) (getResources().getDisplayMetrics().density * value);
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
        scroller = new OverScroller(getContext());
        nestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public float getMaxHeight() {
        if (maxHeight <= 0) {
            maxHeight = getResources().getDisplayMetrics().density * 300;
        }
        return maxHeight;
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
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1 && isInEditMode()) {
            throw new IllegalStateException("SpringLayout no more than one children");
        }
    }

    volatile int totalTranslate;

    public int getMaxTranslate() {
        int height = getHeight() / 2;
        if (height <= 0) {
            height = (int) (getResources().getDisplayMetrics().density * 300);
        }
        return height;
    }

    private ScrollListener scrollListener;

    public ScrollListener getScrollListener() {
        if (scrollListener == null) {
            scrollListener = new ScrollListener() {
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
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, int type) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    public NestedScrollingParentHelper getNestedScrollingParentHelper() {
        return nestedScrollingParentHelper;
    }


    private void startSpringBack(float translateY) {
        pauseAutoTranslateAnim();
        autoTranslateAnim = ValueAnimator.ofFloat(translateY, 0);
        autoTranslateAnim.setDuration(400);
        autoTranslateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                //正数translateYOffset对应负数translateY;
                translateYOffset = -animatedValue;
                Log.i("=====", "=====animatedValue:" + animatedValue);
                changeTranslateY(animatedValue);
            }
        });
        autoTranslateAnim.setInterpolator(new FluidInterpolator());
        autoTranslateAnim.start();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, int type) {
        boolean b = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && type == ViewCompat.TYPE_TOUCH;
        Log.i("=====", type + "====onStartNestedScroll=" + b);
        if (true) {
            return b;
        }
        Log.i("=====", "====onStartNestedScroll=" + type);
        if (axes == ViewCompat.SCROLL_AXIS_VERTICAL && type == ViewCompat.TYPE_TOUCH) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy < 0 && translateYOffset > 0) {
            //内容上滑动之后不松手，此时内容下滑动的逻辑处理
            translateYOffset += dy;
            if (translateYOffset < 0) {
                consumed[1] = (int) (translateYOffset + 0.5f);
                translateYOffset = 0;
            } else {
                consumed[1] = dy;
            }
            changeTranslateY(-translateYOffset);
            return;
        }
        if (dy > 0 && translateYOffset < 0) {
            //内容下滑动之后不松手，此时内容上滑动的逻辑处理
            translateYOffset += dy;
            if (translateYOffset > 0) {
                consumed[1] = Math.abs((int) (translateYOffset - 0.5f));
                translateYOffset = 0;
            } else {
                consumed[1] = dy;
            }
            changeTranslateY(-translateYOffset);
            return;
        }
    }

    /*移动子view视图*/
    private void changeTranslateY(float translateY) {
        if (childView == null) {
            childView = getChildAt(0);
            if (childView == null) {
                return;
            }
        }
        childView.setTranslationY(translateY);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        /*boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (canSpringScroll&&!canScrollUp&&type==ViewCompat.TYPE_NON_TOUCH) {
            canSpringScroll=false;
            velocityTracker.computeCurrentVelocity(1000,2400);
            startSpringScroll(velocityTracker.getYVelocity()*-1);
        }*/
        if (type == ViewCompat.TYPE_NON_TOUCH) {
//            return;
        }
        Log.i("=====", type + "=====onNestedScroll");
        float tempLength = dyUnconsumed / 2f;
        if (tempLength == 0) {
            return;
        }
        pauseAutoTranslateAnim();
        translateYOffset += tempLength;
        changeTranslateY(-translateYOffset);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            return;
        }
        nestedScrollingParentHelper.onStopNestedScroll(target);
        if (scroller != null && scroller.computeScrollOffset()) {
            Log.i("=====", type + "=====onNestedFling222222isOverScrolled");
        } else {
            Log.i("=====", type + "=====onNestedFling222222");
            startSpringBack(-translateYOffset);
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        checkViewIsBottom(true);
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (consumed) {
//            checkViewIsBottom(true);
            Log.i("=====", consumed + "=====onNestedFling11111111");
//            startSpringScroll(velocityY);
//            scroller.fling(0, 0, 0, (int) velocityY, 0, 0, -Integer.MAX_VALUE / 100, Integer.MAX_VALUE / 100);
//            scroller.computeScrollOffset();
        }
        if (!canScrollUp) {
//            velocityTracker.computeCurrentVelocity(1000, 24000);
//            startSpringScroll(velocityTracker.getYVelocity()*-1);

//            return true;
        }
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    private void checkViewIsBottom(boolean continueCheck) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                View childAt = getChildAt(0);
                boolean canScrollUp = ViewCompat.canScrollVertically(childAt, 1);
                if (canScrollUp) {
                    checkViewIsBottom(true);
                    return;
                }
                velocityTracker.computeCurrentVelocity(1000, 24000);
                float yVelocity = velocityTracker.getYVelocity() * -1;
                float v = yVelocity / 24000f;
                final float maxHeight = getMaxHeight() * v;
                pauseAutoTranslateAnim();
                Log.i("=====", v + "=====maxHeight:" + yVelocity);
                autoTranslateAnim = ValueAnimator.ofFloat(0, -maxHeight,0);
                autoTranslateAnim.setDuration(durationTime );
                autoTranslateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        changeTranslateY(animatedValue);
                    }
                });
                autoTranslateAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        /*autoTranslateAnim = ValueAnimator.ofFloat(-maxHeight, 0);
                        autoTranslateAnim.setDuration(durationTime / 2);
                        autoTranslateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float animatedValue = (float) animation.getAnimatedValue();
                                changeTranslateY(animatedValue);
                            }
                        });
                        autoTranslateAnim.setInterpolator(new FluidInterpolator());
                        autoTranslateAnim.start();*/
                    }
                });
//                autoTranslateAnim.setInterpolator(new LinearInterpolator());
//                autoTranslateAnim.setInterpolator(new FluidInterpolator());
                autoTranslateAnim.setInterpolator(new MyBounceInterpolator());
                autoTranslateAnim.start();

            }
        }, 10);
    }

    int preY;

    @Override
    public void computeScroll() {
        boolean canScrollUp = ViewCompat.canScrollVertically(getChildAt(0), 1);
        if (scroller.computeScrollOffset() && !canScrollUp) {
            int startY = scroller.getStartY();
            int currY = scroller.getCurrY();
            Log.i("=====", currY + "=====currY:");
            if (currY == preY && preY != 0) {
                scroller.abortAnimation();
                startSpringBack(-translateYOffset);
                Log.i("=====", currY + "=====currY:1111");
                return;
            }
            translateYOffset = translateYOffset + (currY - preY) / 1f;
            preY = currY;
            changeTranslateY(-translateYOffset);
            Log.i("=====", scroller.getCurrY() + "=====computeScroll:" + translateYOffset);
//            invalidate();
            if (getMaxHeight() >= translateYOffset && scroller.getCurrVelocity() != 0) {
                computeScroll();
            } else {
                Log.i("=====", currY + "=====currY:2222");
                scroller.abortAnimation();
                startSpringBack(-translateYOffset);
            }
        } else {

        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                checkViewIsBottom(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    private void pauseAutoTranslateAnim() {
        if (autoTranslateAnim == null) {
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
