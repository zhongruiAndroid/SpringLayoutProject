package com.test.myscrollviewproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

public class Main2Activity extends AppCompatActivity {
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        rv = findViewById(R.id.rv);

        MyAdapter adapter=new MyAdapter();

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);


    }
}
