package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class ReviewCurrentTutorsActivity extends AppCompatActivity {

    private RecyclerView currentTutors;
    private DatabaseReference reference,userref;
    private String currentuser;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_current_tutors);

        toolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Review Tutor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        currentTutors = findViewById(R.id.currentTutorReviewRecycler);

        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("AcceptTeacher").child(currentuser);
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");

        currentTutors.setLayoutManager(new LinearLayoutManager(this));
        currentTutors.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        displayCurrentTutors();

    }

    private void displayCurrentTutors() {

        FirebaseRecyclerOptions<Teachers> options =

                new FirebaseRecyclerOptions.Builder<Teachers>()
                        .setQuery(reference, Teachers.class)
                        .build();
        FirebaseRecyclerAdapter<Teachers, ReviewCurrentTutorsActivity.FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Teachers, ReviewCurrentTutorsActivity.FindFriendViewHolder>(options) {

                    @NonNull
                    @Override
                    public ReviewCurrentTutorsActivity.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_tutor_review_row, parent, false);
                        ReviewCurrentTutorsActivity.FindFriendViewHolder viewHolder = new ReviewCurrentTutorsActivity.FindFriendViewHolder(view);
                        return viewHolder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final ReviewCurrentTutorsActivity.FindFriendViewHolder findFriendViewHolder, int i, @NonNull Teachers teachers) {

                        final  String userId = getRef(i).getKey();

                        userref.child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()){

                                    final  String fname = dataSnapshot.child("FirstName").getValue().toString();
                                    final  String lname = dataSnapshot.child("LastName").getValue().toString();
                                    if (dataSnapshot.child("ProfileImage").exists()){
                                        final  String pimg= dataSnapshot.child("ProfileImage").getValue().toString();
                                    }


                                    final String name = fname+" "+lname;
                                    findFriendViewHolder.tutor_name_review.setText(name);

                                    findFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(ReviewCurrentTutorsActivity.this,TeacherRatingActivity.class);
                                            intent.putExtra("userId",userId);
                                            startActivity(intent);
                                        }
                                    });

                                    if(dataSnapshot.child("ProfileImage").getValue() != null) {
                                        Picasso.get().load(dataSnapshot.child("ProfileImage").getValue().toString()).into(findFriendViewHolder.tutor_dp);
                                    }

                                    //holder.userStatus.setText(model.getStatus());
                                    //  Picasso.get().load(pimg).placeholder(R.drawable.ic_account_circle_black_24dp).into(findFriendViewHolder.profileImage);


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });



                    }
                };
        adapter.startListening();
        currentTutors.setAdapter(adapter);
    }

    public static class FindFriendViewHolder extends RecyclerView.ViewHolder{

        TextView tutor_name_review;
        CircleImageView tutor_dp;

        public FindFriendViewHolder(@NonNull View itemView){
            super(itemView);

            tutor_name_review = itemView.findViewById(R.id.tutor_name_review);
            tutor_dp = itemView.findViewById(R.id.tutor_dp_review);
        }
    }
}
