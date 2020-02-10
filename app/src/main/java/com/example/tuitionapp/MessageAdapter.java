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

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {


    public static final int Msg_type_left = 0;
    public static final int Msg_type_right = 1;
    FirebaseUser firebaseUser;


    Context context;
    ArrayList<Chat> chats;

    public MessageAdapter(Context c , ArrayList<Chat> ch)
    {
        context = c;
        chats = ch;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == Msg_type_right){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.MyViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Chat msg = chats.get(position);
        holder.show_msg.setText(msg.getMessage());

        if(position == (chats.size()-1)){
            if(msg.isIsseen()){
                holder.msg_seen.setText("Seen");
            }else {
                holder.msg_seen.setText("Delivered");
            }
        }else{
            holder.msg_seen.setVisibility(View.GONE);
        }

        // Picasso.get().load(profiles.get(position).getProfilePic()).into(holder.profilePic);
       /* if(profiles.get(position).getPermission()) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);
        }*/
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,MessageActivity.class);
                context.startActivity(intent);
            }
        });*/
    }



    @Override
    public int getItemCount() {
        return chats.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView show_msg,msg_seen;
        //  ImageView profilePic;
        //  Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            show_msg = (TextView) itemView.findViewById(R.id.show_msg);
            msg_seen = itemView.findViewById(R.id.msg_seen);

           /* profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            btn = (Button) itemView.findViewById(R.id.checkDetails);*/
        }
    }
    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (chats.get(position).getSender().equals(firebaseUser.getUid())){
            return Msg_type_right;
        }else {
            return Msg_type_left;
        }

    }

}