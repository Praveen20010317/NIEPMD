package com.example.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

public class ClientLogin extends AppCompatActivity {
    Button clientLogin;
    EditText name, mobileno;
    TextView house_login, admin_login, user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_client_login);
        user = findViewById(R.id.user);
        name = findViewById(R.id.Emp_name);
        clientLogin = findViewById(R.id.pat_log_btn);
        mobileno = findViewById(R.id.Emp_mob);
        house_login = findViewById(R.id.housekeep_login);
        admin_login = findViewById(R.id.admin_login);

        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                startActivity(intent);
            }
        });

        house_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeLogin.class);
                startActivity(intent);
            }
        });

        clientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = name.getText().toString();
                if (str.isEmpty()) {
                    name.setError("Enter the Name");
                    name.requestFocus();
                }

                final String number = mobileno.getText().toString().trim();
                if ((number.isEmpty()) || (number.length() < 10) && !(str.isEmpty())) {
                    mobileno.setError("Valid number is required");
                    mobileno.requestFocus();
                    return;
                } else {
                    String phonenumber = "+91" + number;
                    Upload upload = new Upload(str, phonenumber);
                    mDatabase = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_CLIENT_LOGIN + phonenumber);
                    mDatabase.push().setValue(upload);
                    Intent intent = new Intent(getApplicationContext(), CVerfication.class);
                    intent.putExtra("room", str);
                    intent.putExtra("phonenumber", phonenumber);
                    startActivity(intent);
                }
                String phonenumber = "+91" + number;
            }
        });
    }

}