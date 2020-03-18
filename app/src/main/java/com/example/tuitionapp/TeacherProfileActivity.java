package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfileActivity extends AppCompatActivity {

    ImageView backBtn;

    TextView teacherName , teacherEmail , teacherPhone , teacherRegion , teacherAddress , teacherDOB , teacherGender ,
            teacherInstitution , teacherDepartment , teacherYear , teacherRating;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference myref;
    private String uid;

    FloatingActionButton floatingActionButton;

    private CircleImageView teacherProPic;
    private String teacherpropiclink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        backBtn = findViewById(R.id.teacher_profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        floatingActionButton = findViewById(R.id.floatingBtnTeacherProfile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherProfileActivity.this , TeacherEditProfileActivity.class);
                finish();
                startActivity(intent);
            }
        });

        teacherName = findViewById(R.id.teacherName);
        teacherEmail = findViewById(R.id.teacherEmail);
        teacherPhone = findViewById(R.id.teacherPhone);
        teacherRegion = findViewById(R.id.teacherRegion);
        teacherAddress = findViewById(R.id.teacherAddress);
        teacherDOB = findViewById(R.id.teacherDOB);
        teacherGender = findViewById(R.id.teacherGender);
        teacherInstitution = findViewById(R.id.teacherInstitution);
        teacherDepartment = findViewById(R.id.teacherDepartment);
        teacherYear = findViewById(R.id.teacherYear);

        teacherProPic = findViewById(R.id.teacher_profile_image);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();


        myref.child("Users").child("Teacher").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fname = dataSnapshot.child("FirstName").getValue().toString();
                    String lname = dataSnapshot.child("LastName").getValue().toString();
                    String name = fname + " " + lname;
                    String email = user.getEmail();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    String region = dataSnapshot.child("Region").getValue().toString();
                    String address = dataSnapshot.child("Address").getValue().toString();
                    String dob = dataSnapshot.child("Birthday").getValue().toString();
                    String gender = dataSnapshot.child("Gender").getValue().toString();
                    String institution = dataSnapshot.child("Institution").getValue().toString();
                    String department = dataSnapshot.child("Department").getValue().toString();
                    String year = dataSnapshot.child("Year").getValue().toString();

                    if (dataSnapshot.child("ProfileImage").exists()) {
                        String propic = dataSnapshot.child("ProfileImage").getValue().toString();
                        teacherpropiclink = propic;
                        Picasso.get().load(propic).into(teacherProPic);
                        //Toast.makeText(StudentProfileActivity.this, propic, Toast.LENGTH_LONG).show();

                    }


                    teacherName.setText(name);
                    teacherEmail.setText(email);
                    teacherPhone.setText(phone);
                    teacherRegion.setText(region);
                    teacherAddress.setText(address);
                    teacherDOB.setText(dob);
                    teacherGender.setText(gender);
                    teacherInstitution.setText(institution);
                    teacherDepartment.setText(department);
                    teacherYear.setText(year);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        teacherRating = findViewById(R.id.tutor_rating_profile);
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("TutorRating");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float ratingSum = 0;
                int ratingTotal = 0;
                float ratingAvg ;
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    for (DataSnapshot child : dataSnapshot.child(uid).getChildren()){
                        ratingSum = ratingSum + Float.valueOf(child.getValue().toString());
                        ratingTotal++;
                    }
                    if (ratingTotal != 0){
                        ratingAvg = ratingSum/ratingTotal;
                        teacherRating.setText(String.valueOf(ratingAvg)+"/5");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
