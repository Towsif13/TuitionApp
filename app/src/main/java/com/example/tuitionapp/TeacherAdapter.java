package com.example.tuitionapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewHolder>{

    Context context;
    ArrayList<UserContacts> profiles;
    String last_Msg;

    public TeacherAdapter(Context c , ArrayList<UserContacts> p)
    {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public TeacherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeacherAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final UserContacts user = profiles.get(position);

        holder.name.setText(profiles.get(position).getFirstName());
        holder.lname.setText(profiles.get(position).getLastName());
        //  holder.last_message.setText(profiles.get(position).getAddress());
        // Picasso.get().load(profiles.get(position).getProfilePic()).into(holder.profilePic);
       /* if(profiles.get(position).getPermission()) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);
        }*/
        Intent intent = new Intent(context,Current_teacher.class);
        intent.putExtra("userId",user.getId());
        context.startActivity(intent);

    }


    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,lname, last_message;
        //  ImageView profilePic;
        //  Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_profile_name);
            lname = (TextView)itemView.findViewById(R.id.user_profile_lastName);
            last_message = (TextView) itemView.findViewById(R.id.last_message);
           /* profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            btn = (Button) itemView.findViewById(R.id.checkDetails);*/
        }
    }


}

