package com.collegeapp.bernhardtinfo;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView uploadnotice,addGalleryImage,uploadebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadnotice = findViewById(R.id.addNotice);
        addGalleryImage=findViewById(R.id.addGalleryimage);
        uploadebook=findViewById(R.id.uploadebook);

        uploadnotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        Intent intent;
     switch (view.getId()){
         case R.id.addNotice:
             intent = new Intent(MainActivity.this,uploadnotice.class);
             startActivity(intent);
             break;
         case R.id.addGalleryimage:
             intent = new Intent(MainActivity.this,UploadImage.class);
             startActivity(intent);
             break;
         case R.id.uploadebook:
             intent = new Intent(MainActivity.this,UploadPdf.class);
             startActivity(intent);
             break;
     }
    }
}