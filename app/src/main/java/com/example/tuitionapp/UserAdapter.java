package com.example.tuitionapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserContacts> profiles;

    public UserAdapter(Context c , ArrayList<UserContacts> p)
    {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final UserContacts user = profiles.get(position);
        holder.name.setText(profiles.get(position).getFirstName());
        holder.lname.setText(profiles.get(position).getLastName());
        holder.address.setText(profiles.get(position).getAddress());
       // Picasso.get().load(profiles.get(position).getProfilePic()).into(holder.profilePic);
       /* if(profiles.get(position).getPermission()) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);
        }*/
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(context,MessageActivity.class);
               intent.putExtra("userId",user.getId());
               context.startActivity(intent);



           }
       });
    }



    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,lname, address;
      //  ImageView profilePic;
      //  Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_profile_name);
            lname = (TextView)itemView.findViewById(R.id.user_profile_lastName);
            address = (TextView) itemView.findViewById(R.id.user_address);
           /* profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            btn = (Button) itemView.findViewById(R.id.checkDetails);*/
        }
    }
}