package com.test.myscrollviewproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SpringLayout extends ViewGroup implements NestedScrollingParent2 {

    private NestedScrollingParentHelper nestedScrollingParentHelper;
    private VelocityTracker velocityTracker;
    private int layoutLeft = -1;
    private int layoutTop = -1;
    private int layoutRight = -1;
    private int layoutBottom = -1;

    public SpringLayout(Context context) {
        super(context);
        init();
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1 && isInEditMode()) {
            throw new IllegalStateException("SpringLayout no more than one children");
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {

        if (type == ViewCompat.TYPE_NON_TOUCH) {
            Log.i("=====", ViewCompat.canScrollVertically(target, 1) + "=====type" + type);
        } else {
            Log.i("=====", ViewCompat.canScrollVertically(target, 1) + "=====type" + type);
        }
        //!ViewCompat.canScrollVertically(target, 1)
        if (ViewCompat.SCROLL_AXIS_VERTICAL == axes) {
            return true;
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, @ViewCompat.ScrollAxis int axes, @ViewCompat.NestedScrollType int type) {
        nestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, @ViewCompat.NestedScrollType int type) {
        nestedScrollingParentHelper.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, @ViewCompat.NestedScrollType int type) {
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (!canScrollUp) {

        }
        if (type == ViewCompat.TYPE_NON_TOUCH) {

        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @ViewCompat.NestedScrollType int type) {
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (!canScrollUp&&type==ViewCompat.TYPE_NON_TOUCH) {

        }
    }
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        boolean canScrollUp = ViewCompat.canScrollVertically(target, 1);
        if (!canScrollUp) {
//            startSpringScroll(velocityY);
        }
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    private void startSpringScroll(float velocityY) {
        if (velocityY <= 0) {
            return;
        }
        View childAt = getChildAt(0);
        if (childAt == null) {
            return;
        }
        if (layoutLeft == -1 && layoutTop == -1 && layoutRight == -1 && layoutBottom == -1) {
            return;
        }
        SpringUtils.startSpringScroll(childAt, layoutLeft, layoutTop, layoutRight, layoutBottom, velocityY / 24000f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);
        return super.dispatchTouchEvent(ev);
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
