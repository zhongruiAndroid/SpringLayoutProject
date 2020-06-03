package com.test.myscrollviewproject;

import android.view.animation.Interpolator;

class MyBounceInterpolator implements Interpolator {
        double defaultAmplitude = 0.3f;
        double defaultFrequency = 1f;

        public MyBounceInterpolator() {
        }

        public MyBounceInterpolator(double defaultAmplitude, double defaultFrequency) {
            this.defaultAmplitude = defaultAmplitude;
            this.defaultFrequency = defaultFrequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / defaultAmplitude) * Math.cos(defaultFrequency * time) + 1);
        }
    }