package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PreviewActivity extends AppCompatActivity {
    private TextView TexViewRoomNo, TexViewPhone, TextViewOther,TextViewIssue;
    public ImageView imageView_pre;
    TextView time, employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);

        imageView_pre = findViewById(R.id.issue_img);
        TexViewRoomNo = findViewById(R.id.room_no_pre);
        TexViewPhone = findViewById(R.id.contactNo_pre);
        TextViewOther = findViewById(R.id.other_per);
        TextViewIssue = findViewById(R.id.issue_per);
        time = findViewById(R.id.time_emp);
        employee = findViewById(R.id.emp_name);

        final Upload upload = (Upload) getIntent().getSerializableExtra("name");
        TexViewRoomNo.setText(upload.getRoomno());
        TexViewPhone.setText(upload.getPhone());
        TextViewIssue.setText(upload.getIssue());
        TextViewOther.setText(upload.getOther());
        time.setText(upload.getTime());
        employee.setText(upload.getAssign());
        Picasso.get().load(upload.getUrl()).into(imageView_pre);

        if (upload.getStatus() == "") {
            time.setText("Not Assigned Yet");
            employee.setText("Not Assigned Yet");
        }
    }
}
