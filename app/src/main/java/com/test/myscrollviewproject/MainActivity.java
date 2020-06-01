package com.test.myscrollviewproject;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvNotifyStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNotifyStr = findViewById(R.id.tvNotifyStr);
        SpringLayout sl = findViewById(R.id.sl );
        sl.setScrollListener(new ScrollListener() {
            @Override
            public void notify(String string) {
                tvNotifyStr.setText(string);
            }
        });

    }
}
