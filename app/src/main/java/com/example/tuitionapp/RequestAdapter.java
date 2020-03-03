package com.example.tuitionapp;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * ------FOR REQUEST FRAGMENT------
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    private ArrayList<UserContacts> requestList;
    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    private Context ctx;

    public RequestAdapter(Context ctx,ArrayList<UserContacts>requestList) {
        this.ctx = ctx;
        this.requestList = requestList;
    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.req_row_layout,parent,false);
        return new RequestViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RequestViewHolder holder, final int position) {
       holder.displayName.setText(requestList.get(position).getFirstName() +  " " +requestList.get(position).getLastName());


        // Picasso.get().load(profiles.get(position).getProfilePic()).into(holder.profilePic);
       /* if(profiles.get(position).getPermission()) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);
        }*/


        //String user_id = requestList.get(position);
       /* mDatabaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.child("name").getValue().toString();
               // String userThumbImage = dataSnapshot.child("thumb_image").getValue().toString();
               // String userStatus =dataSnapshot.child("status").getValue().toString();

                holder.displayName.setText(userName);
                //Picasso.with(holder.displayImage.getContext()).load(userThumbImage).placeholder(R.drawable.user_img).into(holder.displayImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx,PublicTeacherProfileActivity.class);
                intent.putExtra("user_id",requestList.get(position).getId());
                ctx.startActivity(intent);


            }
        });




    }


    public class RequestViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName;
        View mView;
        Button request_accept,request_decline;
        //public CircleImageView displayImage;
        public ImageView imageView;

        public RequestViewHolder(View itemView) {
            super(itemView);

            ctx = itemView.getContext();
            mView = itemView;

            displayName = (TextView)itemView.findViewById(R.id.user_firstName);
            //displayImage = (CircleImageView)itemView.findViewById(R.id.circleImageViewUserImage);
           // imageView = (ImageView)itemView.findViewById(R.id.userSingleOnlineIcon);
            //imageView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
