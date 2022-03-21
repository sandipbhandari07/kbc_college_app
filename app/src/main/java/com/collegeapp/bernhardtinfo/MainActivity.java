package com.collegeapp.bernhardtinfo;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.collegeapp.bernhardtinfo.faculty.updatefaculty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    CardView uploadnotice,addGalleryImage,addbook,updatefaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadnotice = findViewById(R.id.addNotice);
        addGalleryImage=findViewById(R.id.addGalleryimage);
        addbook=findViewById(R.id.uploadebook);
        updatefaculty=findViewById(R.id.updatefacuilty);

        updatefaculty.setOnClickListener(this);
        uploadnotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addbook.setOnClickListener(this);


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
         case R.id.updatefacuilty:
             intent = new Intent(MainActivity.this, com.collegeapp.bernhardtinfo.faculty.updatefaculty.class);
             startActivity(intent);
             break;
     }
    }
}