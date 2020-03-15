package com.example.tuitionapp;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserContacts> profiles;
    String last_Msg;

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
      //  holder.last_message.setText(profiles.get(position).getAddress());
       // Picasso.get().load(profiles.get(position).getProfilePic()).into(holder.profilePic);
       /* if(profiles.get(position).getPermission()) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);
        }*/

       Intent intent = new Intent(context,MessageActivity.class);
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

    private void lastMessage(final String userid, final TextView last_message){

        last_Msg = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Chat chat = dataSnapshot1.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        last_Msg = chat.getMessage();

                    }
                }
                switch (last_Msg){
                    case "default":
                        last_message.setText("No Message");
                        break;
                    default:
                        last_message.setText(last_Msg);
                        break;
                }
                last_Msg = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}