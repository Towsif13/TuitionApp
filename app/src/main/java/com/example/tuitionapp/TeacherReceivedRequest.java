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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Map;

import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherReceivedRequest extends AppCompatActivity {

    ArrayList<UserContacts> list;
    RequestAdapter adapter;
    Toolbar mtoolbar;
    private RecyclerView mRequestList;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mRequestDatabase;
    private DatabaseReference mRootRef;
    private String mCurrent_user_id, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_received_request);

        mtoolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRequestList = findViewById(R.id.user_request_recycler_list);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrent_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");

        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Request");

        mRequestList.setLayoutManager(new LinearLayoutManager(this));
        mRequestList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        // checking the current user is  in the studnet node or teacher node
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
    }
}