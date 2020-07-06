package com.example.aditya.mmse;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.RecognizerIntent;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Queue;


public class SpeechBased extends AppCompatActivity {

    TextView qsNo, qsDesc;
TextToSpeech t1;
    FirebaseDatabase database;
    DatabaseReference myRef;
    boolean limitReached = false;
    static int index = 0;
    Button speech, next,lis;
    EditText answerText;
    String rightans;
    static int qsno = 1;
    private final int REQ_CODE_SPEECH_INPUT=100;
    private int score;

    static final ArrayList<String> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_based);


        qsDesc = findViewById(R.id.quesText1);
        qsNo = findViewById(R.id.quesNo1);


        lis= findViewById(R.id.listen);
        speech = findViewById(R.id.save);
        next = findViewById(R.id.next);

        answerText = findViewById(R.id.answerText);


        database = FirebaseDatabase.getInstance("https://androidquiz-22fda.firebaseio.com/");
        myRef = database.getReference().child("root").child("quiz").child("VoiceInput");


        score= getIntent().getExtras().getInt("myscore");


        retrieveCompleteData();

        t1= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    t1.setSpeechRate((float)0.85);

                    if(getResources().getConfiguration().locale.toLanguageTag().equals("en-US") ||getResources().getConfiguration().locale.toLanguageTag().equals("und"))
                    {
                        t1.setLanguage(new Locale("en","IN"));
                    }

                    if(getResources().getConfiguration().locale.toLanguageTag().equals("hi")||getResources().getConfiguration().locale.toLanguageTag().equals("mr"))
                    {
                        t1.setLanguage(Locale.forLanguageTag("hin"));
                    }

                    if(getResources().getConfiguration().locale.toLanguageTag().equals("bn"))
                    {
                        Toast.makeText(getApplicationContext(),"IN BANGLA",Toast.LENGTH_LONG).show();
                        t1.setLanguage(Locale.forLanguageTag("bn-IN"));

                    }

                    //t1.setLanguage(Locale.forLanguageTag("HIN"));
                }
            }
        });


        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptSpeech();
            }
        });

        lis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(qsDesc.getText(),TextToSpeech.QUEUE_FLUSH,null,null);
                t1.speak(arrayList.get(index+2).toString(),TextToSpeech.QUEUE_ADD,null,null);


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t1.stop();

                if(limitReached==false) {
                    if (answerText == null) {
                        Toast.makeText(getApplicationContext(), "Select a choice", Toast.LENGTH_SHORT).show();
                    } else {
                        updateQuestion();
                    }
                }
                else{
                    //go to new activity

                    Intent i = new Intent(getApplicationContext(),ImageBased.class);
                    i.putExtra("myscore",score);
                    startActivity(i);
                    //startActivity(new Intent(getApplicationContext(),ImageBased.class));
                    overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
                }
            }
        });



    }

    private void retrieveCompleteData() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i=1;;i++) {
                    if(dataSnapshot.child("q"+i).exists() == false) {
                        break;
                    }
                    for (DataSnapshot postSnapshot : dataSnapshot.child("q" + i).getChildren()) {

                        arrayList.add(postSnapshot.getValue().toString());
                        Log.e("Added value", postSnapshot.getValue().toString());
                    }

                }

                displayTextqs();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






    void displayTextqs() {


        qsNo.setText(String.valueOf(index/3 + 1));

        if (arrayList.size() != 0) {

                Log.e("q in disMcq", arrayList.get(index).toString());
                //qsDesc.setText(arrayList.get(index).toString());

                if(getResources().getConfiguration().locale.toLanguageTag().equals("hi")||getResources().getConfiguration().locale.toLanguageTag().equals("mr")||getResources().getConfiguration().locale.toLanguageTag().equals("bn"))
                {
                    if(index==0)
                    {
                        qsDesc.setText(getResources().getString(R.string.Question));
                        rightans= arrayList.get(index).toString();
                    }
                    else if(index==3)
                    {
                        qsDesc.setText(getResources().getString(R.string.Question1));
                        rightans= arrayList.get(index).toString();
                    }




                }
                else
                {
                    qsDesc.setText(arrayList.get(index+1).toString());
                    rightans= arrayList.get(index).toString();


                }

            }
         else {
            Toast.makeText(getApplicationContext(), "Arraylist is empty", Toast.LENGTH_SHORT).show();
        }

    }


    private void updateQuestion(){
        try {
            if(answerText == null){
                Toast.makeText(getApplicationContext(),"Enter your response!",Toast.LENGTH_SHORT).show();
            }
            else {

                if(qsno==2){
                    limitReached = true;
                    saveAndScore();
                }
                else {

                    saveAndScore();
                    answerText.setText("");
                    Toast.makeText(getApplicationContext(),"problem in save and score",Toast.LENGTH_SHORT).show();
                    index = index + 3;
                    displayTextqs();

                    qsno++;
                }
            }
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    private void saveAndScore(){


        if(answerText.getText().toString().replaceAll("\\s+","").equalsIgnoreCase(arrayList.get(index).toString()))
        {
            score= score+2;
            Toast.makeText(getApplicationContext(),"in if right"+String.valueOf(score),Toast.LENGTH_LONG).show();
            Log.e("Speech score",String.valueOf(score));

            t1.speak("Congrats your answer is right",TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else
        {
            Log.e("Speech score",String.valueOf(score));
            Toast.makeText(getApplicationContext(),"in if wrong"+String.valueOf(score),Toast.LENGTH_LONG).show();
            t1.speak("Your answer is wrong!",TextToSpeech.QUEUE_FLUSH,null,null);
        }

    }


   @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void PromptSpeech()
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.forLanguageTag("en-IN"));
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speech));
        try{
            startActivityForResult(i,REQ_CODE_SPEECH_INPUT);

        }
        catch(ActivityNotFoundException a)
        {
            Toast.makeText(getApplicationContext(),"Speech not supported",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int rqcode, int rscode, Intent i)
    {
        super.onActivityResult(rqcode,rscode,i);
        switch (rqcode)
        {
            case REQ_CODE_SPEECH_INPUT:
            {
                if(rscode==RESULT_OK &&null!=i) {
                    ArrayList<String> a = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    answerText.setText(a.get(0));


                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_SHORT).show();

    }


    public void onPause()
    {
        super.onPause();
        t1.stop();
    }



}
