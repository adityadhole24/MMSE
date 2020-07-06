package com.example.aditya.mmse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

import android.os.Build;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.speech.tts.TextToSpeech;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;


public class ChooseLang extends AppCompatActivity {

    Button save;
    TextView ctext;
    Spinner choose;
    Locale myLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lang);

      save= findViewById(R.id.save);
      ctext= findViewById(R.id.quesText1);
      choose= findViewById(R.id.spinner);

        List<String>mylist= new ArrayList<String>();
        mylist.add("Select your language");
        mylist.add("Hindi");
        mylist.add("Marathi");
        mylist.add("Bangla");

        ArrayAdapter<String> myadapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mylist);
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose.setAdapter(myadapter);

       choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

               switch(position)
               {
                   case 0:
                   {
                       break;
                   }
                   case 1:
                   {
                       SetLocale("hi");
                       break;
                   }
                   case 2:
                   {
                       SetLocale("mr");
                       break;
                   }
                   case 3:
                   {
                       SetLocale("bn");
                       break;
                   }
               }


           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });


       save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent j = new Intent(getApplicationContext(),DashboardActivity.class);
               startActivity(j);
           }
       });


    }

    public int SetLocale(String loc)
    {
        int flag=0;
        myLoc= new Locale(loc);

        //Locale.setDefault(myLoc);

        if(loc=="hi")
        {
            flag = 1;
        }
        if(loc=="mr")
        {
            flag=2;
        }
        if(loc=="bn")
        {
            flag=3;
        }



        Resources res= getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale= myLoc;
        res.updateConfiguration(conf,dm);
        Intent refresh = new Intent(this,ChooseLang.class);
        startActivity(refresh);
        return flag;
    }
}
