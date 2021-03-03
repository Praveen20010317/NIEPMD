package com.example.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class EmployeeLogin extends AppCompatActivity {
    Button empLogin;
    EditText userName, mobileno, empId;
    private DatabaseReference mDatabase;
    TextView userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_employee_login);

        userLogin = findViewById(R.id.user_login);
        empId = findViewById(R.id.Emp_Id);
        userName = findViewById(R.id.Housekeep_name);
        empLogin = findViewById(R.id.emp_log_btn);
        mobileno = findViewById(R.id.Housekeep_mob);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientLogin.class);
                startActivity(intent);
            }
        });
        empLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = userName.getText().toString();
                String empId1 = empId.getText().toString();
                if (str.isEmpty()) {
                    userName.setError("Enter Your Name");
                    userName.requestFocus();
                }
                if (empId1.isEmpty()) {
                    empId.setError("Enter Emp.Id");
                    empId.requestFocus();
                }
                final String number = mobileno.getText().toString().trim();
                if ((number.isEmpty()) || (number.length() < 10) && (!(str.isEmpty())) && (!(empId1.isEmpty()))) {
                    mobileno.setError("Valid number is required");
                    mobileno.requestFocus();
                }else {
                    String phonenumber = "+91" + number;
                    Upload upload = new Upload(str, empId1, phonenumber);
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_EMP_LOGIN + phonenumber);
                    mDatabase.push().setValue(upload);
                    Intent intent = new Intent(getApplicationContext(), EmpVerfication.class);
                    intent.putExtra("room", str);
                    intent.putExtra("phonenumber", phonenumber);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        /*
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), EmployeeMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
         */
    }
}