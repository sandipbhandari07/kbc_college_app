package com.collegeapp.bernhardtinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class UploadPdf extends AppCompatActivity {


    private CardView addpdf;
    private ImageView pdfview;
    private EditText pdftitle1;
    private Button uploadpdfbottontxt1;

    private final int REQ = 1;

    private Uri pdfData;

    private DatabaseReference reference;
    private StorageReference storageReference;

    String downloadUrl = "";

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        pd=new ProgressDialog(this);
        addpdf = findViewById(R.id.addPdf);
        pdftitle1=findViewById(R.id.pdfTitle);
        uploadpdfbottontxt1 = findViewById(R.id.pdfupdatebtn);

        addpdf.setOnClickListener((view) -> {openGallery();});


    }
    private void openGallery() {
       Intent intent = new Intent();
       intent.setType("pdf/docs/ppt");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Select pdf File"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData=data.getData();

            Toast.makeText(this, ""+pdfData, Toast.LENGTH_SHORT).show();
        }
    }
}