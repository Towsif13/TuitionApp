
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


public class PostFragment_Student extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    FirebaseAuth firebaseAuth;


    RecyclerView recyclerView;
    List<Post> postList;
    AdapterPostTeacher adapterPostTeacher;

    EditText seachInput;


    public PostFragment_Student() {
        // Required empty public constructor
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

        loadPostsTeacher();
        seachInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(adapterPostTeacher!= null){
                    adapterPostTeacher.getFilter().filter(charSequence);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return  view;
    }
    private void loadPostsTeacher(){
        // path of all posts
        // String userId = mAuth.getCurrentUser().getUid();
        // FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child(userId).child(userId+"-"+date);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("TeacherPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    postList.add(post);

                    //adapter
                    adapterPostTeacher = new AdapterPostTeacher(getActivity(),postList);
                    // set adapter to recycleview

                    recyclerView.setAdapter(adapterPostTeacher);


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




    private void searchPostsTeacher(final String  query){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("TeacherPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);


                    if(post.getDepartment().toLowerCase().contains(query.toLowerCase())||post.getRegion().toLowerCase().contains(query.toLowerCase())
                            ||post.getAddress().toLowerCase().contains(query.toLowerCase())){

                        postList.add(post);


                    }


                    //adapter
                    adapterPostTeacher = new AdapterPostTeacher(getActivity(),postList);
                    // set adapter to recycleview

                    recyclerView.setAdapter(adapterPostTeacher);


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

