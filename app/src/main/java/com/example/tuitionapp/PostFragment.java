package com.example.tuitionapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;


public class PostFragment extends Fragment {


    FirebaseAuth firebaseAuth;


    RecyclerView recyclerView;
    List<Post> postList;
    AdapterPosts adapterPosts;



    public PostFragment(){

        // required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);


        //init

        firebaseAuth =FirebaseAuth.getInstance();


        // recycler view and its properties

        recyclerView = view.findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        //set layout to recycleview
        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();
        loadPosts();



        return  view;


    }

    private void loadPosts(){
        // path of all posts
       // String userId = mAuth.getCurrentUser().getUid();
        // FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child(userId).child(userId+"-"+date);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                   Post post = ds.getValue(Post.class);
                   postList.add(post);

                   //adapter
                    adapterPosts = new AdapterPosts(getActivity(),postList);
                    // set adapter to recycleview

                    recyclerView.setAdapter(adapterPosts);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error handlle
                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
