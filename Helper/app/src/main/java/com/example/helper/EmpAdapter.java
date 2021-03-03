package com.example.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmpAdapter extends RecyclerView.Adapter<EmpAdapter.ViewHolder>  {

    private Context context;
    private List<Upload> uploads;
    FirebaseStorage storage;

    public EmpAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {

        final Upload upload = uploads.get(position);
        holder.TexViewRoomNo.setText(upload.getRoomno());
        holder.TextViewDateTime.setText(upload.getDateTime());
        holder.TextViewIssue.setText(upload.getIssue());
        //holder.TextViewStatus.setText(upload.getStatus());
        if (upload.getStatus().equals( "Pending")) {
            holder.pending.setChecked(true);
            // holder.TextViewStatus.setTextColor(Color.parseColor("#D50000"));
        }if(upload.getStatus().equals("Assigned")) {
            holder.pending.setChecked(true);
            holder.assigned.setChecked(true);
            // holder.TextViewStatus.setTextColor(Color.parseColor("#64DD17"));
        }if ( upload.getStatus().equals("Completed")) {
            holder.pending.setChecked(true);
            holder.assigned.setChecked(true);
            holder.completed.setChecked(true);
            // holder.TextViewStatus.setTextColor(Color.parseColor("#D50000"));
        }
        //Picasso.get().load(upload.getUrl()).into(holder.imageViewpost);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),EmployeePreview.class);
                intent.putExtra("name", upload);
                v.getContext().startActivity(intent);
                //Toast.makeText(context,""+upload.getRoomno() ,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        private TextView TexViewRoomNo, TextViewDateTime,TextViewIssue, TextViewStatus;
        public ImageView  menu_btn;
        RadioButton pending, assigned, completed;

        public ViewHolder(View itemView) {
            super(itemView);
            menu_btn = itemView.findViewById(R.id.menu_btn);
            TexViewRoomNo = itemView.findViewById(R.id.room_blog);
            TextViewDateTime = itemView.findViewById(R.id.dateTime_blog);
            TextViewIssue = itemView.findViewById(R.id.issue_blog);
            TextViewStatus = itemView.findViewById(R.id.status_blog);
            pending = itemView.findViewById(R.id.pending_bt);
            assigned = itemView.findViewById(R.id.assigned_bt);
            completed = itemView.findViewById(R.id.completed_btn);
        }
    }
}