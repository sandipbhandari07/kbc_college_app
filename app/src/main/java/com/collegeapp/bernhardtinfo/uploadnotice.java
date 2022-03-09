package com.collegeapp.bernhardtinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class uploadnotice extends AppCompatActivity {

    private CardView addimages1;
    private ImageView noticeimageview;
    private EditText noticetitle1;
    private Button uploadbottontxt1;

    private final int REQ = 1;

    private Bitmap bitmap;

    private DatabaseReference reference;
    private StorageReference storageReference;

    String downloadUrl = "";

    private ProgressDialog pdg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadnotice);

        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        pdg=new ProgressDialog(this);


        addimages1 = findViewById(R.id.addNotice1234);
        noticeimageview = findViewById(R.id.noticeImageViewPrv);
        noticetitle1 = findViewById(R.id.noticetitletxt);
        uploadbottontxt1=findViewById(R.id.uploadnoticebtntxt);


        addimages1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        uploadbottontxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noticetitle1.getText().toString().isEmpty()){
                    noticetitle1.setError("Empty");
                    noticetitle1.requestFocus();
                }else if (bitmap == null){
                    uploadData();
                }else{
                    uploadImage();
                }
            }
        });
    }

    private void uploadData() {
        reference = reference.child("Notice");
        final String uniquekey = reference.push().getKey();

        String title= noticetitle1.getText().toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yy");
        String date=currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForTime.getTime());

        noticedata noticedata=new noticedata(title,downloadUrl,date,time,uniquekey);

        reference.child(uniquekey).setValue(noticedata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                pdg.dismiss();
                Toast.makeText(uploadnotice.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pdg.dismiss();
                Toast.makeText(uploadnotice.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadImage() {

        pdg.setMessage("uploading..");
        pdg.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg=baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("Notice").child(finalimg+"img");
        final UploadTask uploadTask= filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(uploadnotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }else {
                    pdg.dismiss();
                    Toast.makeText(uploadnotice.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openGallery() {
        Intent pickimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickimage, REQ);
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
            noticeimageview.setImageBitmap(bitmap);

        }
    }
}