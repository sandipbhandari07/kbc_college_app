package com.collegeapp.bernhardtinfo.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.collegeapp.bernhardtinfo.R;
import com.collegeapp.bernhardtinfo.noticedata;
import com.collegeapp.bernhardtinfo.uploadnotice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTeachers extends AppCompatActivity {

    private ImageView addteacherimage;
    private EditText addteachername, addteacheremail,addteacherPost ;
    private Spinner add_teacher_cate;
    private Button addteacherbtntxt;
    private final int REQ = 1;
    private Bitmap bitmap=null;
    private String category;
    private String tname,temail,tpost,downloadurl = "";
    private ProgressDialog pdg;
    private StorageReference storageReference;
    private DatabaseReference reference,dbref;


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
        reference = FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        pdg=new ProgressDialog(this);

        String[] items=new String[]{"select category","BCA","BSc.CSIT","BBM","BASW","BBS"};
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

        addteacherbtntxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        tname=addteachername.getText().toString();
        temail=addteacheremail.getText().toString();
        tpost=addteacherPost.getText().toString();
        if (tname.isEmpty()){
            addteachername.setError("Empty");
            addteachername.requestFocus();
        }else if (temail.isEmpty()){
            addteacheremail.setError("Empty");
            addteacheremail.requestFocus();
        }else if (tpost.isEmpty()){
            addteacherPost.setError("Empty");
            addteacherPost.requestFocus();
        }else if (category.equals("select category")){
            Toast.makeText(this, "please provide teacher category", Toast.LENGTH_SHORT).show();
        }else if (bitmap == null){
            pdg.setMessage("uploading..");
            pdg.show();
            uploaddata();
        }else {
            pdg.setMessage("uploading..");
            pdg.show();
            uploadImage();
        }
    }

    private void uploadImage() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg=baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("Teachers").child(finalimg+"img");
        final UploadTask uploadTask= filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeachers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadurl=String.valueOf(uri);
                                    uploaddata();
                                }
                            });
                        }
                    });
                }else {
                    pdg.dismiss();
                    Toast.makeText(AddTeachers.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void uploaddata() {
        dbref = reference.child(category);
        final String uniquekey = dbref.push().getKey();


        TeacherData teacherData=new TeacherData(tname,temail,tpost,uniquekey,downloadurl);

        reference.child(uniquekey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                pdg.dismiss();
                Toast.makeText(AddTeachers.this, "Teacher Added", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pdg.dismiss();
                Toast.makeText(AddTeachers.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
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