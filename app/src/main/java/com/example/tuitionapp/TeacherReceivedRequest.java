package com.example.tuitionapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class TeacherReceivedRequest extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference;
    RecyclerView recyclerView;
    List<Request> list;
    RequestAdapter adapter;
    Toolbar mtoolbar;
    String receiverId,senderId,req_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_received_request);

        recyclerView = findViewById(R.id.user_request_recycler_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mtoolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
       // recyclerView.setAdapter(adapter);


        reference = FirebaseDatabase.getInstance().getReference().child("Request");

        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("RequestAdapter", "hello");
        Bundle extras = getIntent().getExtras();
        //  String foo = extras.getString("FOO");
        if (extras != null) {
            receiverId = getIntent().getExtras().getString("ReceiverId");
        }


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    getSupportActionBar().setTitle("Requests List");

                    list = new ArrayList<Request>();
                    list.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Log.d("RequestAdapter", "bye");
                        Request p = dataSnapshot1.getValue(Request.class);
                        list.add(p);
                    }
                    adapter = new RequestAdapter(TeacherReceivedRequest.this, list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





                    //   Toast.makeText(UserActivity.this, "working", Toast.LENGTH_SHORT).show();
                }
              /*  else {
                    getSupportActionBar().setTitle("Student List");



                    reference.child("Users").child("Student").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list = new ArrayList<UserContacts>();
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                            {
                                UserContacts p = dataSnapshot1.getValue(UserContacts.class);
                                list.add(p);
                            }

                            adapter = new UserAdapter(UserActivity.this,list);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(UserActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Toast.makeText(UserActivity.this, "not working", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

*/

