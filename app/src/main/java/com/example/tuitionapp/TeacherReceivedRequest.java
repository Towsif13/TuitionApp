package com.example.tuitionapp;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherReceivedRequest extends AppCompatActivity {

    Toolbar mtoolbar;
    private RecyclerView mRequestList;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mRequestDatabase;
    private DatabaseReference mRootRef;
    DatabaseReference mFriendRequestDatabase;
    private String mCurrent_user_id, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_received_request);

        mtoolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRequestList = findViewById(R.id.user_request_recycler_list);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrent_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Request");
        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Request").child(mCurrent_user_id);

        mRequestList.setLayoutManager(new LinearLayoutManager(this));
        mRequestList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        DisplayAllTeachers();

    }

    private void DisplayAllTeachers() {

        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(mRequestDatabase, Request.class)
                        .build();
        FirebaseRecyclerAdapter<Request, TeacherReceivedRequest.FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Request, TeacherReceivedRequest.FindFriendViewHolder>(options) {


                    @NonNull
                    @Override
                    public TeacherReceivedRequest.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        Log.d("Msg", "Hi");
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.req_row_layout, parent, false);
                        TeacherReceivedRequest.FindFriendViewHolder viewHolder = new TeacherReceivedRequest.FindFriendViewHolder(view);
                        return viewHolder;

                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final TeacherReceivedRequest.FindFriendViewHolder findFriendViewHolder, int i, @NonNull Request teachers) {

                        Log.d("Msg", "Hi");
                        final  String userId = getRef(i).getKey();
                        findFriendViewHolder.txt.setVisibility(View.GONE);

                        final String Request_type = teachers.getRequest_type();

                        Log.d("LOG","Friend ID: "+ userId);
                        Log.d("LOG","Request Type: "+Request_type);

                        if(Request_type.equals("received")) {

                            mUserDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {

                                        final String fname = dataSnapshot.child("FirstName").getValue().toString();
                                        final String lname = dataSnapshot.child("LastName").getValue().toString();
                                        final String pimg = dataSnapshot.child("ProfileImage").getValue().toString();

                                        findFriendViewHolder.userName.setText(fname + " " + lname);

                                        //holder.userStatus.setText(model.getStatus());

                                        //Picasso.get().load(pimg).placeholder(R.drawable.ic_account_circle_black_24dp).into(findFriendViewHolder.profileImage);

                                    }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });


                            findFriendViewHolder.request_accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    findFriendViewHolder.request_accept.setEnabled(false);
                                    SimpleDateFormat df1 = new SimpleDateFormat("d-MM-yyyy");
                                    final String CurrentDate= df1.format(Calendar.getInstance().getTime());


                                    Map friendsMap = new HashMap();
                                    friendsMap.put("AcceptTeacher/" + mCurrent_user_id + "/" + userId + "/date", CurrentDate);
                                    friendsMap.put("AcceptTeacher/" + userId + "/" + mCurrent_user_id + "/date", CurrentDate);

                                    friendsMap.put("Friend_req/" + mCurrent_user_id + "/" + userId, null);
                                    friendsMap.put("Friend_req/" + userId + "/" + mCurrent_user_id, null);

                                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                            if (databaseError == null) {
                                                //remove data from recyclerview
                                            } else {
                                                findFriendViewHolder.request_accept.setEnabled(true);

                                            }

                                        }
                                    });
                                    findFriendViewHolder.request_decline.setVisibility(View.GONE);
                                    findFriendViewHolder.txt.setVisibility(View.VISIBLE);
                                    findFriendViewHolder.txt.setText(" You are now connected");
                                    findFriendViewHolder.request_accept.setVisibility(View.GONE);

                                }
                            });

                            findFriendViewHolder.request_decline.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    findFriendViewHolder.request_decline.setEnabled(false);

                                    mRequestDatabase.child(userId).child("request_type").setValue(null)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        mFriendRequestDatabase.child(userId).child(mCurrent_user_id).child("request_type").setValue("declined")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if(task.isSuccessful()) {
                                                                            //Toast.makeText(ProfileActivity.this,"Request Sent.",Toast.LENGTH_LONG).show();
                                                                        }
                                                                        else
                                                                        {
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        findFriendViewHolder.request_decline.setEnabled(true);
                                                    }
                                                }
                                            });

                                }
                            });

                            findFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent profileintent = new Intent(TeacherReceivedRequest.this, PublicTeacherProfileActivity.class);
                                    profileintent.putExtra("userid", userId);
                                    startActivity(profileintent);

                                }
                            });

                        }
                        else
                        {
                            findFriendViewHolder.request_decline.setEnabled(false);
                            findFriendViewHolder.request_accept.setEnabled(false);
                        }


                    }
                };
        adapter.startListening();
        mRequestList.setAdapter(adapter);


    }



    // checking the current user is  in the studnet node or teacher node
/*
        mRequestDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    getSupportActionBar().setTitle("Request");

                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<UserContacts>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                UserContacts p = dataSnapshot1.getValue(UserContacts.class);
                                list.add(p);
                            }
                            adapter = new RequestAdapter(TeacherReceivedRequest.this, list);
                            mRequestList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(TeacherReceivedRequest.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
*/
    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName ,txt;
        CircleImageView profileImage;
        Button request_accept,request_decline;


        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txt = itemView.findViewById(R.id.txt);
            userName = itemView.findViewById(R.id.user_firstName);
            request_accept = itemView.findViewById(R.id.acpt);
            request_decline = itemView.findViewById(R.id.dec);
            profileImage = itemView.findViewById(R.id.user_profile_image);
        }
    }

}