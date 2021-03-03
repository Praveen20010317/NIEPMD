package com.example.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //view objects
    private Button buttonChoose;
    private Button buttonUpload;
    private EditText editTexRoomNo,editTexPhone,editTextDateTime,editTextOther;
    private Spinner spinnerIssue;
    private ImageView imageView;
    ProgressDialog progressDialog1;
    StorageTask mstorage;
    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase, mDatabase2;

    String[] issue = {"Room Cleaning", "Water Supply", "Electricity"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add);

        editTexRoomNo = findViewById(R.id.room_no_add);
        editTexPhone = findViewById(R.id.contactNo_add);
        editTextOther = findViewById(R.id.other_add);
        spinnerIssue = findViewById(R.id.issue_add);
        editTextDateTime = findViewById(R.id.dateTime_add);
        imageView = findViewById(R.id.imageView_add);
        buttonChoose = findViewById(R.id.buttonChoose);
        buttonUpload = findViewById(R.id.buttonUpload);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,issue);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIssue.setAdapter(spinnerArrayAdapter);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date date = new Date();
        editTextDateTime.setText(formatter.format(date));


       /* final Upload upload = (Upload) getIntent().getSerializableExtra("name");
        editTexPhone.setText(upload.getPhone1());
        editTexRoomNo.setText(upload.getUserName());*/
        String userName1, phone1;

        userName1 = getIntent().getStringExtra("username");
        phone1 = getIntent().getStringExtra("phone1");
        editTexRoomNo.setText(userName1);
        editTexPhone.setText(phone1);
        mDatabase2 = FirebaseDatabase.getInstance().getReference(phone1);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        String room = editTexRoomNo.getText().toString();
        String phone = editTexPhone.getText().toString();


        if (filePath != null & !(phone.isEmpty()) ) {
            //displaying progress dialog while image is uploading
            progressDialog1=new ProgressDialog(this);
            progressDialog1.setTitle("Uploading...");
            progressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog1.setProgress(0);
            progressDialog1.setMax(100);
            progressDialog1.setCancelable(false);
            progressDialog1.show();
            StorageReference sRef = storageReference.child(Constant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));
            //adding the file to reference
            mstorage= sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult();
                            String status = "Pending";
                            String assignEmp ="Not Assigned Yet";
                            String time = "Not Assigned Yet";
                            Upload upload = new Upload(editTexRoomNo.getText().toString()
                                    , editTexPhone .getText().toString()
                                    , spinnerIssue.getSelectedItem().toString()
                                    , editTextDateTime.getText().toString()
                                    , editTextOther.getText().toString()
                                    , url.toString(), status, assignEmp, time);

                            mDatabase.child(mDatabase.push().getKey()).setValue(upload);
                            //dismissing the progress dialog
                            progressDialog1.dismiss();
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "Uploaded Sucessful ", Toast.LENGTH_LONG).show();
                            editTexRoomNo.setText(null);
                            editTexPhone.setText(null);
                            editTextOther.setText(null);
                            finish();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            int currentProgress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog1.setProgress(currentProgress);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please select the photo...", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onClick (View view){
        if (view == buttonChoose) {
            showFileChooser();
        } else if (view == buttonUpload) {
            storageReference = FirebaseStorage.getInstance().getReference();
            mDatabase = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);
            uploadFile();
        }
    }
}
