package com.example.aditya.mmse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.net.DatagramSocket;


public class ResultActivity extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5,t6,t7,t8;
    Button save1;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        score= getIntent().getExtras().getInt("myscore");

        t1=findViewById(R.id.quesText1);
        t2= findViewById(R.id.quesNo1);
        t3= findViewById(R.id.mkslabel);
        t4= findViewById(R.id.marks);
        t5= findViewById(R.id.label1);
        t6= findViewById(R.id.con1);
        t7= findViewById(R.id.label2);
        t8 = findViewById(R.id.con2);

        save1= findViewById(R.id.save);

        t4.setText(String.valueOf(score));

        if(score>=24 && score<=30)
        {
            t6.setText("NO COGNITIVE IMPAIRMENT");

        }
        else if(score>=18 && score<24)
        {
            t6.setText("MILD COGNITIVE IMPAIRMENT");
        }
        else
        {
            t6.setText("SEVERE COGNITIVE IMPAIRMENT");
        }

        if(score<21)
        {
            t8.setText("ABNORMAL IF YOU HAVE COMPLETED ATLEAST 8 YRS OF SCHOOL EDUCATION");
        }
        else if(score<23 && score>=21 )
        {
            t8.setText("ABNORMAL IF YOU FINISHED HIGH SCHOOL");
        }
        else if(score==23|| score==24)
        {
            t8.setText("ABNORMAL IF YOU FINISHED COLLEGE");
        }
        else {
            t8.setText("NO ABNORMALITY DETECTED");
        }

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(i);
            }
        });
    }
    public void onPause()
    {
        super.onPause();

    }

    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_SHORT).show();
    }
}
