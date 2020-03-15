package com.example.tuitionapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class yourPostStudentActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    String uid;
    RecyclerView recyclerView;
    List<Post> postList;
    AdapterPosts adapterPosts;

    EditText seachInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_post_student);
        seachInput = findViewById(R.id.search_item);

        //init
        firebaseAuth =FirebaseAuth.getInstance();


        // recycler view and its properties

        recyclerView = findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

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
    }

    private void loadPostsStudent(){
        // path of all posts
        // String userId = mAuth.getCurrentUser().getUid();
        // FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child(userId).child(userId+"-"+date);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("StudentPosts");
        firebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        myref = mFirebaseDatabase.getReference();
        uid = firebaseAuth.getUid();
        Log.i("yourbahirerPost",uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    if(uid.equals(post.getId())) {
                        postList.add(post);

                        //adapter
                        adapterPosts = new com.example.tuitionapp.AdapterPosts(yourPostStudentActivity.this, postList);
                        // set adapter to recycleview

                        recyclerView.setAdapter(adapterPosts);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error handlle
                Toast.makeText(yourPostStudentActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




    // search option isnt fully done yet




    // search option isnt fully done yet

    private void searchPosts(final String  query){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("StudentPosts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);


                    if(post.getsClass().toLowerCase().contains(query.toLowerCase())||post.getPreferredGender().toLowerCase().contains(query.toLowerCase())
                            ||post.getAddress().toLowerCase().contains(query.toLowerCase())){

                        postList.add(post);


                    }


                    //adapter
                    adapterPosts = new AdapterPosts(yourPostStudentActivity.this,postList);
                    // set adapter to recycleview

                    recyclerView.setAdapter(adapterPosts);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error handlle
                Toast.makeText(yourPostStudentActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }





}