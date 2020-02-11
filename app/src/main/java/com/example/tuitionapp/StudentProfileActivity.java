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

public class StudentProfileActivity extends AppCompatActivity {

    ImageView backBtn;

    TextView studentName , studentEmail , studentPhone , studentRegion , studentAddress , studentDOB , studentGender ,
    studentMedium , studentClass ;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference myref;
    private String uid;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        backBtn = findViewById(R.id.student_profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        floatingActionButton = findViewById(R.id.floatingBtnStudentProfile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfileActivity.this , StudentEditProfileActivity.class);
                startActivity(intent);
            }
        });

        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        studentPhone = findViewById(R.id.studentPhone);
        studentRegion = findViewById(R.id.studentRegion);
        studentAddress = findViewById(R.id.studentAddress);
        studentDOB = findViewById(R.id.studentDOB);
        studentGender = findViewById(R.id.studentGender);
        studentMedium = findViewById(R.id.studentMedium);
        studentClass = findViewById(R.id.studentClass);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        myref.child("Users").child("Student").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("FirstName").getValue().toString();
                String lname = dataSnapshot.child("LastName").getValue().toString();
                String name = fname+" "+lname;
                String email = user.getEmail();
                String phone = dataSnapshot.child("Phone").getValue().toString();
                String region = dataSnapshot.child("Region").getValue().toString();
                String address = dataSnapshot.child("Address").getValue().toString();
                String dob = dataSnapshot.child("Birthday").getValue().toString();
                String gender = dataSnapshot.child("Gender").getValue().toString();
                String medium = dataSnapshot.child("Medium").getValue().toString();
                String classs = dataSnapshot.child("Class").getValue().toString();

                studentName.setText(name);
                studentEmail.setText(email);
                studentPhone.setText(phone);
                studentRegion.setText(region);
                studentAddress.setText(address);
                studentDOB.setText(dob);
                studentGender.setText(gender);
                studentMedium.setText(medium);
                studentClass.setText(classs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
