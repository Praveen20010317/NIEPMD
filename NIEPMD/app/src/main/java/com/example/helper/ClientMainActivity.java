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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.List;

public class ClientMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClientAdapter adapter;
    Button add;
    TextView end;
    RelativeLayout relativeLayout;
    TextView username, phone1;

    //database reference
    private DatabaseReference mDatabase, mDatabase2;

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
        setContentView(R.layout.activity_client_main);

        username = findViewById(R.id.username_main);
        phone1 = findViewById(R.id.phone_main);
        end = findViewById(R.id.end_text);
        relativeLayout = findViewById(R.id.layout_empty);
        add = findViewById(R.id.add);
        signout = findViewById(R.id.signout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentMob = currentUser.getPhoneNumber();

        mDatabase2 = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_CLIENT_LOGIN + currentMob);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder dailog = new AlertDialog.Builder(v.getRootView().getContext());
                dailog.setMessage("Are you sure? you want to signout.");
                dailog.setTitle("Sign Out");
                dailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDatabase2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                String userName = postSnapshot.child("userName").getValue().toString();
                                String phone = postSnapshot.child("phone1").getValue().toString();
                                username.setText(userName);
                                phone1.setText(phone);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Error :" + error,Toast.LENGTH_LONG).show();
                        }
                    });
                    FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_CLIENT_LOGIN + currentMob).removeValue();
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
                    String userName = postSnapshot.child("userName").getValue().toString();
                    String phone = postSnapshot.child("phone1").getValue().toString();
                    username.setText(userName);
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
        progressDialog.setCancelable(false);
        recyclerView.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        //adding an event listener to fetch values

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                uploads.clear();
                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    if (upload.getPhone().equals(currentMob)) {
                        uploads.add(upload);
                    }
                }
                //creating adapter
                adapter = new ClientAdapter(getApplicationContext(),uploads);
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddActivity.class);
                intent.putExtra("username",username.getText().toString());
                intent.putExtra("phone1",phone1.getText().toString());
                startActivity(intent);
            }
        });

    }
}
