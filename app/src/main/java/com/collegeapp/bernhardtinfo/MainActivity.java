package com.collegeapp.bernhardtinfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView uploadnotice=findViewById(R.id.uploadnoticebtn);
        TextView uploadnoticetxt=findViewById(R.id.uploadnotice);

        uploadnoticetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Intent intent=new Intent(MainActivity.this,uploadnotice.class);
            }
        });

        uploadnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,uploadnotice.class);
                startActivity(intent);
            }
        });
    }
}