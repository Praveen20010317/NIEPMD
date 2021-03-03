package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EmployeePreview extends AppCompatActivity {

    private TextView TexViewRoomNo, TexViewPhone, TextViewOther,TextViewIssue, TextViewLink;
    public ImageView imageView_pre;
    public Button AssignEmpButton;
    ProgressDialog progressDialog;
    String  assignEmp;
    private Context context;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_employee_preview);

        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);
        imageView_pre = findViewById(R.id.issue_img);
        TexViewRoomNo = findViewById(R.id.room_no_pre);
        TexViewPhone = findViewById(R.id.contactNo_pre);
        TextViewOther = findViewById(R.id.other_per);
        TextViewIssue = findViewById(R.id.issue_per);
        TextViewLink = findViewById(R.id.link_ad_pre);
        AssignEmpButton = findViewById(R.id.done_emp);


        final Upload upload = (Upload) getIntent().getSerializableExtra("name");
        TexViewRoomNo.setText(upload.getRoomno());
        TexViewPhone.setText(upload.getPhone());
        TextViewIssue.setText(upload.getIssue());
        TextViewOther.setText(upload.getOther());
        Picasso.get().load(upload.getUrl()).into(imageView_pre);

        final String dateTime = upload.getDateTime();
        final String link = upload.getUrl();
        final String assign = upload.getAssign();
        AssignEmpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String key = upload.getKey();
                String status = "Completed";
                String time = "";
                final Upload upload = new Upload(TexViewRoomNo.getText().toString(), TexViewPhone.getText().toString()
                            , TextViewIssue.getText().toString(), dateTime , TextViewOther.getText().toString()
                            ,link , status, assign, time);
                //mDatabaseReference.push().setValue(upload);
                FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS).child(key).setValue(upload)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();

                            }
                        });
            }
        });
    }
}
