package com.test.myscrollviewproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TestView extends View {
    public TestView(Context context) {
        super(context);
        init();
    }
    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Path path;
    private Paint paint;
    
    private float[]firstNumX={0,
            0.03018868f,
            0.062264152f,
            0.094339624f,
            0.124528304f,
            0.15660377f,
            0.18867925f,
            0.21886793f,
            0.2509434f,
            0.28301886f,
            0.31320754f,
            0.34528303f,
            0.3773585f,
            0.40754718f,
            0.43962264f,
            0.4716981f,
            0.5018868f,
            0.53396225f,
            0.5660377f,
            0.5962264f,
            0.62830186f,
            0.6603774f,
            0.69056606f,
            0.7226415f,
            0.754717f,
            0.7849057f,
            0.81698114f,
            0.8490566f,
            0.8792453f,
            0.91132075f,
            0.9433962f,
            0.9735849f,
            1,};
    private float[]firstNumY={0,
            0.02696644f,
            0.105850436f,
            0.22499041f,
            0.3657076f,
            0.509389f,
            0.62055725f,
            0.7020938f,
            0.76964796f,
            0.82191294f,
            0.86024684f,
            0.89200705f,
            0.9165792f,
            0.93460155f,
            0.9495334f,
            0.9610858f,
            0.96955895f,
            0.97657907f,
            0.9820103f,
            0.985994f,
            0.9892944f,
            0.99184793f,
            0.99372077f,
            0.99527246f,
            0.99647295f,
            0.9973535f,
            0.998083f,
            0.9986474f,
            0.99906135f,
            0.9994044f,
            0.99966973f,
            0.99986434f,
            1,};



    private void init() {
        resetNumPoint();
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(2);

        path=new Path();

        Log.i("=====",firstNumX.length+"====="+firstNumY.length);

    }

    private void resetNumPoint() {
         firstNumX=new float[]{
                 0,
                0.05666667f,
                0.11333334f,
                0.16666667f,
                0.22333333f,
                0.28f,
                0.34f,
                0.39666668f,
                0.45333335f,
                0.50666666f,
                0.56333333f,
                0.62f,
                0.67333335f,
                0.73f,
                0.7866667f,
                0.84f,
                0.89666665f,
                0.9533333f,
                1.0f,
                 };
         firstNumY=new float[]{0,
                 0.08889092f,
                 0.31071404f,
                 0.5473814f,
                 0.7125683f,
                 0.8175456f,
                 0.88732016f,
                 0.92860144f,
                 0.9548361f,
                 0.9707226f,
                 0.98160434f,
                 0.9885196f,
                 0.9927074f,
                 0.9955757f,
                 0.99739856f,
                 0.99850243f,
                 0.9992585f,
                 0.999739f,
                 1.0f
                 };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int width = getWidth();
        int height = getHeight();

        if(path!=null){
            path.reset();
        }
        path.moveTo(0,height);
        int length = firstNumY.length;
        for (int i = 0; i < length; i++) {
            float firstNumX = this.firstNumX[i];
            float firstNumY = this.firstNumY[i];
            float x = firstNumX * width;
            float y = firstNumY * height;
            path.lineTo(x,height-y);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path,paint);
    }
}
