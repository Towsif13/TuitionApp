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

public class Current_teacher extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference, userref;
    RecyclerView recyclerView;
    String currentuser;;
    Toolbar mtoolbar;
    private String lname,fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_teacher);

        recyclerView = findViewById(R.id.user_Cur_recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mtoolbar = findViewById(R.id.user_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("My Tutors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("AcceptStudent").child(currentuser);
        userref = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");

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
                        Log.d("Msg", "Hi");
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
                                    final  String pimg= dataSnapshot.child("ProfileImage").getValue().toString();

                                    findFriendViewHolder.userName.setText(fname);
                                    findFriendViewHolder.userName2.setText(lname);
                                    findFriendViewHolder.userStatus.setVisibility(View.GONE);

                                  findFriendViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(Current_teacher.this,MessageActivity.class);
                                          intent.putExtra("userId",userId);
                                          startActivity(intent);
                                      }
                                  });

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
        profileImage = itemView.findViewById(R.id.profile_image);
    }
}
}
