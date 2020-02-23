/*
package com.example.tuitionapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TeacherReceivedRequestsFragment extends Fragment {

    private RecyclerView mReqList;
    RequestAdapter requestAdapter;

    private List<Request> requestList = new ArrayList<>();

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mMessageDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    RecyclerView recyclerView;
    Toolbar mtoolbar;

    private View mMainView;

    private RequestAdapter mRequestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_teacher_received_requests, container, false);

        recyclerView = findViewById(R.id.user_recycler_list);
       /* recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mtoolbar = mtoolbar.findViewById(R.id.user_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference();*






        // getting the current user
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mReqList = (RecyclerView)mMainView.findViewById(R.id.recyclerViewRequestList);
        mAuth= FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Request");
        mDatabaseReference.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mReqList.setHasFixedSize(true);
        mReqList.setLayoutManager(linearLayoutManager);

        requestList.clear();
        mRequestAdapter = new RequestAdapter(requestList);
        mReqList.setAdapter(mRequestAdapter);

        mDatabaseReference.child(mCurrent_user_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String userId = dataSnapshot.getKey();
              //  requestList.add(userId);
                mRequestAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mMainView;

        // Inflate the layout for this fragment
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}

*/
