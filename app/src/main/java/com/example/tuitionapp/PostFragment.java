package com.example.tuitionapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PostFragment extends Fragment {


    FirebaseAuth firebaseAuth;


    RecyclerView recyclerView;
    List<com.example.tuitionapp.Post> postList;
    com.example.tuitionapp.AdapterPosts adapterPosts;

    EditText seachInput;



    public PostFragment(){

        // required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);


        seachInput = view.findViewById(R.id.search_item);

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

        loadPostsStudent();
        seachInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(adapterPosts!= null){
                    adapterPosts.getFilter().filter(charSequence);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return  view;


    }




    private void loadPostsStudent(){
        // path of all posts
       // String userId = mAuth.getCurrentUser().getUid();
        // FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child(userId).child(userId+"-"+date);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("StudentPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                   com.example.tuitionapp.Post post = ds.getValue(com.example.tuitionapp.Post.class);
                   postList.add(post);

                   //adapter
                    adapterPosts = new com.example.tuitionapp.AdapterPosts(getActivity(),postList);
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






    // search option isnt fully done yet

    private void searchPosts(final String  query){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("StudentPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    com.example.tuitionapp.Post post = ds.getValue(com.example.tuitionapp.Post.class);


                    if(post.getsClass().toLowerCase().contains(query.toLowerCase())||post.getPreferredGender().toLowerCase().contains(query.toLowerCase())
                            ||post.getAddress().toLowerCase().contains(query.toLowerCase())){

                        postList.add(post);


                    }


                    //adapter
                    adapterPosts = new com.example.tuitionapp.AdapterPosts(getActivity(),postList);
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
