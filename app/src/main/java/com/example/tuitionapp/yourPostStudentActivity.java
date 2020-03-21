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

    }

    private void loadPostsStudent(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("TuitionPosts").child("StudentPosts");
        firebaseAuth = FirebaseAuth.getInstance();

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
                        adapterPosts = new AdapterPosts(yourPostStudentActivity.this, postList);
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







}