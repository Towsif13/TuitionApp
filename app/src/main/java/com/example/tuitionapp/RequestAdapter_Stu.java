package com.example.tuitionapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;



public class RequestAdapter_Stu extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder>{

    private ArrayList<UserContacts> requestList;
    private FirebaseAuth mAuth;
    DatabaseReference mRootRef;
    private Context ctx;

    public RequestAdapter_Stu(Context ctx,ArrayList<UserContacts>requestList) {
        this.ctx = ctx;
        this.requestList = requestList;
    }

    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.req_row_layout,parent,false);
        return new RequestAdapter.RequestViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder holder, final int position) {

        holder.displayName.setText(requestList.get(position).getFirstName() +  " " +requestList.get(position).getLastName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ctx,PublicStudentProfile.class);
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
