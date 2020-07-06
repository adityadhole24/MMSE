package com.example.aditya.mmse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class imageinMCQ extends AppCompatActivity {

    ImageView i1,i2,i3;
    Button next;
    TextView desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagein_mcq);

        i1 = findViewById(R.id.imageView);
        i2 = findViewById(R.id.imageView2);
        i3 = findViewById(R.id.imageView3);
        next = findViewById(R.id.button);
        desc = findViewById(R.id.textView);

        String url1 = "https://firebasestorage.googleapis.com/v0/b/androidquiz-22fda.appspot.com/o/apple.jpg?alt=media&token=d8c82530-2841-4930-81ac-6a56c71fd731";
        String url2 = "https://firebasestorage.googleapis.com/v0/b/androidquiz-22fda.appspot.com/o/circular%20table.jpg?alt=media&token=a88a8365-004a-41d3-a0df-4b00e6dd0056";
        String url3 = "https://firebasestorage.googleapis.com/v0/b/androidquiz-22fda.appspot.com/o/One%20Rupee%20Coin.jpg?alt=media&token=2dfd14bc-171f-4f36-b3c0-1e35d8f0877c";


        Glide.with(getApplicationContext()).load(url1).into(i1);
        Glide.with(getApplicationContext()).load(url2).into(i2);
        Glide.with(getApplicationContext()).load(url3).into(i3);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
