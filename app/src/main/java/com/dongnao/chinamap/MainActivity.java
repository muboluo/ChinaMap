package com.dongnao.chinamap;

import android.graphics.drawable.AnimatedVectorDrawable;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chinamap.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final ImageView imageView = findViewById(R.id.iv);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               AnimatedVectorDrawable drawable= (AnimatedVectorDrawable) imageView.getDrawable();
//               drawable.start();
//
//            }
//        });
    }
}
