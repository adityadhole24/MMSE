package com.example.aditya.mmse;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class ImageBased extends AppCompatActivity {

    private int score;
    ImageView imageView;
    EditText answerText;
    TextView qsNo,qstxt;
    String imageUrl_1,imageUrl_2;
    Button next,lis,speak2;
    TextToSpeech t1;
    static int index = 1;
    String rightanswer;
    private final int REQ_CODE_SPEECH_INPUT=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_based);

        qstxt= findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        answerText = findViewById(R.id.editText);
        qsNo = findViewById(R.id.qsno1);
        next = findViewById(R.id.nextBtn);
        lis = findViewById(R.id.lit);
        speak2= findViewById(R.id.sp1);

        score= getIntent().getExtras().getInt("myscore");
        imageUrl_1 = "https://firebasestorage.googleapis.com/v0/b/androidquiz-22fda.appspot.com/o/watch.jpg?alt=media&token=5cf81785-6650-41a5-b435-46433d333c26";
        imageUrl_2 = "https://firebasestorage.googleapis.com/v0/b/androidquiz-22fda.appspot.com/o/pencil.jpg?alt=media&token=b590cc37-1462-46f5-bc37-7a23973d1be9";


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


        lis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t1.speak(qstxt.getText(),TextToSpeech.QUEUE_FLUSH,null,null);

            }
        });

        speak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptSpeech();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               t1.stop();
                if(index>2){
                    Toast.makeText(getApplicationContext(),"Finish in next",Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(getApplicationContext(),ResultActivity.class);
                    i.putExtra("myscore",score);
                    startActivity(i);
                }
                else {
                    nextQuestion();
                }
            }
        });
    }

    private void setImage(){
        if(index<=2){
            qsNo.setText(String.valueOf(index));
        }
        if(index == 1){
            Glide.with(getApplicationContext()).load(imageUrl_1).into(imageView);
        }
        else if (index==2){
            Glide.with(getApplicationContext()).load(imageUrl_2).into(imageView);
        }
    }

    private void checkAnswer(){
        if(TextUtils.isEmpty(answerText.getText())){
            Toast.makeText(getApplicationContext(),"Enter answer",Toast.LENGTH_SHORT).show();
        }
        else{

            if(answerText.getText().toString().replaceAll("\\s+","").equalsIgnoreCase(rightanswer)){
                score= score+3;
                Toast.makeText(getApplicationContext(),"correct"+String.valueOf(score),Toast.LENGTH_SHORT).show();
                Log.e("Image score",String.valueOf(score));

            }
            else{
                Log.e("Image score",String.valueOf(score));

                Toast.makeText(getApplicationContext(),"Wrong"+String.valueOf(score),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void nextQuestion() {

        if (TextUtils.isEmpty(answerText.getText())) {
            Toast.makeText(getApplicationContext(), "Enter answer", Toast.LENGTH_SHORT).show();
        } else {
            checkAnswer();
            answerText.setText("");
            index++;
            setImage();
            rightanswer = "pencil";
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        qsNo.setText(String.valueOf(index));
        setImage();
        rightanswer = "watch";
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_SHORT).show();
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
    public void onPause()
    {
        super.onPause();
        t1.stop();
    }
}
