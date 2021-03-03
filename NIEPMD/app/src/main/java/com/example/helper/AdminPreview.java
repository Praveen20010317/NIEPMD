package com.example.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AdminPreview extends AppCompatActivity {

    private TextView TexViewRoomNo, TexViewPhone, TextViewOther,TextViewIssue, TextViewDate;
    public ImageView imageView_pre;
    public EditText AssignEmp;
    public Button AssignEmpButton;
    private Spinner spinnerTime;
    ProgressDialog progressDialog;
    String roomNO, phone, other, issue, assignEmp, imgLink;
    Uri filePath;

    String[] time = {"5 Mins", "10 Mins"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_preview);

        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);
        imageView_pre = findViewById(R.id.issue_img);
        spinnerTime = findViewById(R.id.spinner_time);
        TexViewRoomNo = findViewById(R.id.room_no_pre);
        TexViewPhone = findViewById(R.id.contactNo_pre);
        TextViewOther = findViewById(R.id.other_per);
        TextViewIssue = findViewById(R.id.issue_per);
        AssignEmp = findViewById(R.id.issue_ad);
        TextViewDate = findViewById(R.id.link_ad_pre);
        AssignEmpButton = findViewById(R.id.assign_bt);

        final Upload upload = (Upload) getIntent().getSerializableExtra("name");
        TexViewRoomNo.setText(upload.getRoomno());
        TexViewPhone.setText(upload.getPhone());
        TextViewIssue.setText(upload.getIssue());
        TextViewOther.setText(upload.getOther());
        AssignEmp.setText(upload.getAssign());
        Picasso.get().load(upload.getUrl()).into(imageView_pre);
        TextViewDate.setText(upload.getDateTime());
        final String link = upload.getUrl();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(spinnerArrayAdapter);

        AssignEmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignEmp = AssignEmp.getText().toString();

                if (assignEmp.isEmpty()) {
                    AssignEmp.setError("Assign the Employee");
                    AssignEmp.requestFocus();
                }
                if (!(assignEmp.isEmpty())) {
                    String status = "Assigned";
                    final String key = upload.getKey();
                    Upload upload = new Upload(TexViewRoomNo.getText().toString(), TexViewPhone.getText().toString()
                            , TextViewIssue.getText().toString(), TextViewDate.getText().toString(), TextViewOther.getText().toString()
                            ,link , status, AssignEmp.getText().toString(), spinnerTime.getSelectedItem().toString());
                   // mDatabaseReference.push().setValue(upload);
                    FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS).child(key).setValue(upload)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    finish();
                                }
                            });
                    //Toast.makeText(getApplicationContext(), "Uploaded Sucessful ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
