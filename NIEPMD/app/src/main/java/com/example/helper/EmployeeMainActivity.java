package com.example.helper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmpAdapter adapter;
    TextView end;
    RelativeLayout relativeLayout;
    Button completed;
    //database reference
    private DatabaseReference mDatabase, mDatabase2;
    TextView username, phone1, empId;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;
    TextView signout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_employee_main);

        completed = findViewById(R.id.emp_cp);
        end = findViewById(R.id.end_text);
        empId = findViewById(R.id.emp_id_main);
        relativeLayout = findViewById(R.id.layout_empty);
        recyclerView = findViewById(R.id.recyclerView);
        username = findViewById(R.id.emp_username_main);
        phone1 = findViewById(R.id.emp_phone_main);
        signout = findViewById(R.id.signout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentMob = currentUser.getPhoneNumber();

        mDatabase2 = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_EMP_LOGIN + currentMob);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder dailog = new AlertDialog.Builder(v.getRootView().getContext());
                dailog.setMessage("Are you sure? you want to signout.");
                dailog.setTitle("Sign Out");
                dailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(),ClientLogin.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dailog.create();
                alertDialog.show();
            }
        });

        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String userName = postSnapshot.child("EmpUserName").getValue().toString();
                    String empIdNum = postSnapshot.child("EmpId").getValue().toString();
                    String phone = postSnapshot.child("EmpPhone").getValue().toString();
                    username.setText(userName);
                    empId.setText(empIdNum);
                    phone1.setText(phone);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error :" + error,Toast.LENGTH_LONG).show();
            }
        });


        progressDialog = new ProgressDialog(this);
        uploads = new ArrayList<>();
        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);
        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                uploads.clear();
                String Emp_Id_Assign = empId.getText().toString();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    if (upload.getAssign().equals(Emp_Id_Assign) & !(upload.getStatus().equals("Completed"))) {
                        uploads.add(upload);
                    }
                }
                //creating adapter
                adapter = new EmpAdapter(getApplicationContext(),uploads);
                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
                if (uploads.size() > 0) {
                    relativeLayout.setVisibility(View.GONE);
                    end.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    end.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmpCompleted.class);
                Intent getintent = getIntent();
                String user = getintent.getStringExtra("username");
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });
    }
}
