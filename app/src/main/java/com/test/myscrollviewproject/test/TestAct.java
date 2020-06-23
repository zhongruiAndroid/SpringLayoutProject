package com.test.myscrollviewproject.test;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.test.myscrollviewproject.R;


public class TestAct extends AppCompatActivity implements View.OnClickListener {

    private Button bt;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button btStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        bt = findViewById(R.id.bt);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);

        btStart = findViewById(R.id.btStart);
        btStart.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btStart:
                startAnim1();
                break;
        }
    }
    int finalPosition=0;
    int startValue=1000;
    private void startAnim1() {
        SpringAnimation springAnimation = new SpringAnimation(bt, DynamicAnimation.TRANSLATION_Y, finalPosition);
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                Log.i("=====", value + "==onAnimationUpdate===" + velocity);
            }
        });
        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                Log.i("=====", canceled + "===" + value + "==onAnimationEnd===" + velocity);
            }
        });
//        springAnimation.setStartValue(startValue);
//        springAnimation.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);
        springAnimation.start();


        SpringAnimation springAnimation2 = new SpringAnimation(bt2, DynamicAnimation.TRANSLATION_Y, finalPosition);
        springAnimation2.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                Log.i("=====", value + "==onAnimationUpdate===" + velocity);
            }
        });
        springAnimation2.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                Log.i("=====", canceled + "===" + value + "==onAnimationEnd===" + velocity);
            }
        });

        springAnimation2.setStartValue(startValue);
//        springAnimation2.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        springAnimation2.getSpring().setStiffness(SpringForce.STIFFNESS_MEDIUM);
        springAnimation2.start();





        SpringAnimation springAnimation3 = new SpringAnimation(bt3, DynamicAnimation.TRANSLATION_Y, finalPosition);
        springAnimation3.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                Log.i("=====", value + "==onAnimationUpdate===" + velocity);
            }
        });
        springAnimation3.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                Log.i("=====", canceled + "===" + value + "==onAnimationEnd===" + velocity);
            }
        });
        springAnimation3.setStartValue(startValue);
//        springAnimation3.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
        springAnimation3.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        springAnimation3.start();





        SpringAnimation springAnimation4 = new SpringAnimation(bt4, DynamicAnimation.TRANSLATION_Y, finalPosition);
        springAnimation4.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
            @Override
            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                Log.i("=====", value + "==onAnimationUpdate===" + velocity);
            }
        });
        springAnimation4.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                Log.i("=====", canceled + "===" + value + "==onAnimationEnd===" + velocity);
            }
        });
        springAnimation4.setStartValue(startValue);
//        springAnimation4.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY);
        springAnimation4.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
        springAnimation4.animateToFinalPosition(1);
        springAnimation4.start();
    }
}
