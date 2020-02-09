package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<UserContacts> list;
    UserAdapter adapter;
    Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        recyclerView = findViewById(R.id.user_recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mtoolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView.setAdapter(adapter);

        reference = FirebaseDatabase.getInstance().getReference();






        // getting the current user
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // checking the current user is  in the studnet node or teacher node
        reference.child("Users").child("Student").child( currentuser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    getSupportActionBar().setTitle("Tutors List");
                    recyclerView.setAdapter(adapter);
                    reference.child("Users").child("Teacher").addValueEventListener(new ValueEventListener() {
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
                 //   Toast.makeText(UserActivity.this, "working", Toast.LENGTH_SHORT).show();
                }
                else {
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






    }
}
