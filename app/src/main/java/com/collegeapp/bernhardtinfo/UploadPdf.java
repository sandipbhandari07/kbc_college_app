package com.collegeapp.bernhardtinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {


    private CardView addpdf;
    private ImageView pdfview;
    private EditText pdftitle1;
    private Button uploadpdfbottontxt1;

    private TextView pdftextview;
    private String pdfname,title;

    private final int REQ = 1;

    private Uri pdfData;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    String downloadUrl = "";

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        pd=new ProgressDialog(this);
        addpdf = findViewById(R.id.addPdf);
        pdftitle1=findViewById(R.id.pdfTitle);
        uploadpdfbottontxt1 = findViewById(R.id.pdfupdatebtn);
        pdftextview=findViewById(R.id.pdftextview);

        addpdf.setOnClickListener((view) -> {openGallery();});

        uploadpdfbottontxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = pdftitle1.getText().toString();
                if (title.isEmpty()){
                    pdftitle1.setError("Empty");
                    pdftitle1.requestFocus();
                }
                else if (pdfData == null){
                    Toast.makeText(UploadPdf.this, "Please upload notes", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadPdf();
                }

            }
        });


    }

    private void uploadPdf() {
        pd.setTitle("please wait......");
        pd.setMessage("Uploading notes");
        pd.show();
        StorageReference reference = storageReference.child("pdf/"+pdfname+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        UploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UploadData(String valueOf) {
        String uniquekey = databaseReference.child("pdf").push().getKey();

        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);
        databaseReference.child("pdf").child(uniquekey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Notes uploaded successfully", Toast.LENGTH_SHORT).show();
                pdftitle1.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Fail to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
       Intent intent = new Intent();
       intent.setType("pdf/docs/ppt");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Select pdf File"),REQ);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data.getData();

            if (pdfData.toString().startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = UploadPdf.this.getContentResolver().query(pdfData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfname = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (pdfData.toString().startsWith("file://")) {
                pdfname = new File(pdfData.toString()).getName();

            }
            pdftextview.setText(pdfname);
        }
    }
}