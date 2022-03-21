package com.collegeapp.bernhardtinfo.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.collegeapp.bernhardtinfo.R;

import java.io.IOException;

public class AddTeachers extends AppCompatActivity {

    private ImageView addteacherimage;
    private EditText addteachername, addteacheremail,addteacherPost ;
    private Spinner add_teacher_cate;
    private Button addteacherbtntxt;
    private final int REQ = 1;
    private Bitmap bitmap=null;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);
        addteacherimage=findViewById(R.id.addteacherimage);
        addteachername=findViewById(R.id.addteachername);
        addteacheremail=findViewById(R.id.addteacheremail);
        addteacherPost=findViewById(R.id.addteacherPost);
        addteacherbtntxt=findViewById(R.id.addteacherbtntxt);
        add_teacher_cate=findViewById(R.id.add_teacher_cate);


        String[] items=new String[]{"select category","BCA","BSc.CSIT","BBM","BASW","BBS","MBS"};
        add_teacher_cate.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

        add_teacher_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=add_teacher_cate.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        addteacherimage.setOnClickListener((view) -> { openGallery();});

    }

    private void openGallery() {
        Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage,REQ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addteacherimage.setImageBitmap(bitmap);

        }
    }
}