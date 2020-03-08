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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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



       if(requestList.get(position).getProfileImage() != null) {
           Picasso.get().load(requestList.get(position).getProfileImage()).into(holder.displayImage);
       }


        //String user_id = requestList.get(position);
       /* mDatabaseReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String userName = dataSnapshot.child("name").getValue().toString();
               // String userThumbImage = dataSnapshot.child("thumb_image").getValue().toString();
               // String userStatus =dataSnapshot.child("status").getValue().toString();

                holder.displayName.setText(userName);
                Picasso.with(holder.displayImage.getContext()).load(userThumbImage).placeholder(R.drawable.user_img).into(holder.displayImage);
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


    public static class RequestViewHolder extends RecyclerView.ViewHolder {

        private final Context ctx;
        public TextView displayName;
        View mView;
        Button request_accept,request_decline;
        public ImageView displayImage;
        //public ImageView imageView;

        public RequestViewHolder(View itemView) {
            super(itemView);

            ctx = itemView.getContext();
            mView = itemView;

            displayName = (TextView)itemView.findViewById(R.id.user_firstName);
            displayImage = (CircleImageView)itemView.findViewById(R.id.profile_image);
            //imageView = (ImageView)itemView.findViewById(R.id.userSingleOnlineIcon);
            //imageView.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
