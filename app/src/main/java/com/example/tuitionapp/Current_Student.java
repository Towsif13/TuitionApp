package com.example.tuitionapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Current_Student extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference, userref;
    RecyclerView recyclerView;
    String currentuser;;
    Toolbar mtoolbar;
    private String lname,fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current__student);

        recyclerView = findViewById(R.id.user_Cur_S_recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mtoolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("My Students");
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("AcceptStudent").child(currentuser);
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");

        // getting the current user


        //

        DisplayAllTeachers();
    }


    private void DisplayAllTeachers() {

        FirebaseRecyclerOptions<Teachers> options =
                new FirebaseRecyclerOptions.Builder<Teachers>()
                        .setQuery(reference, Teachers.class)
                        .build();
        FirebaseRecyclerAdapter<Teachers, FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Teachers, FindFriendViewHolder>(options) {


                    @NonNull
                    @Override
                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
                        FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                        return viewHolder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final FindFriendViewHolder findFriendViewHolder, int i, @NonNull Teachers teachers) {
                        Log.d("Msg", "Hi");
                        final  String userId = getRef(i).getKey();
                        userref.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    final  String fname = dataSnapshot.child("FirstName").getValue().toString();
                                    final  String lname = dataSnapshot.child("LastName").getValue().toString();
                                    findFriendViewHolder.userName.setText(fname);
                                    findFriendViewHolder.userName2.setText(lname);
                                    findFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Current_Student.this,MessageActivity.class);
                                            intent.putExtra("userId",userId);
                                            startActivity(intent);
                                        }
                                    });
                                    if(dataSnapshot.child("ProfileImage").getValue() != null) {
                                        Picasso.get().load(dataSnapshot.child("ProfileImage").getValue().toString()).into(findFriendViewHolder.profileImage);
                                    }

                                    findFriendViewHolder.userStatus.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });



                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);



    }


    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userName2 , userStatus;
        CircleImageView profileImage;


        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userName2 = itemView.findViewById(R.id.user_profile_lastName);
            userStatus = itemView.findViewById(R.id.last_message);
            profileImage = itemView.findViewById(R.id.user_profile_image);
        }
    }
}
