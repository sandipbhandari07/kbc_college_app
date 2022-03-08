package com.collegeapp.bernhardtinfo;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView uploadnotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadnotice = findViewById(R.id.addNotice);
        uploadnotice.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
     switch (view.getId()){
         case R.id.addNotice:
             Intent intent= new Intent(MainActivity.this,uploadnotice.class);
             startActivity(intent);
             break;
     }
    }
}