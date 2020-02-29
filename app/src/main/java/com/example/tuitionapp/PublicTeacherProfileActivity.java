package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

public class PublicTeacherProfileActivity extends AppCompatActivity {

    ImageView backBtn;

    TextView teacherName , teacherEmail , teacherPhone , teacherRegion , teacherAddress , teacherDOB , teacherGender ,
            teacherInstitution , teacherDepartment , teacherYear;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference myref;
    private String uid;

    private RelativeLayout rate_btn_teacher , msg_btn_teacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_teacher_profile);

        rate_btn_teacher = findViewById(R.id.rate_btn_teacher);
        msg_btn_teacher = findViewById(R.id.msg_btn_teacher);

        rate_btn_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicTeacherProfileActivity.this , TeacherRatingActivity.class);
                startActivity(intent);
                //Toast.makeText(PublicTeacherProfileActivity.this, "RATE", Toast.LENGTH_SHORT).show();
            }
        });

        msg_btn_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PublicTeacherProfileActivity.this, "MSG", Toast.LENGTH_SHORT).show();
            }
        });

        backBtn = findViewById(R.id.teacher_profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();

        myref.child("Users").child("Teacher").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String fname = dataSnapshot.child("FirstName").getValue().toString();
                    String lname = dataSnapshot.child("LastName").getValue().toString();
                    String name = fname+" "+lname;
                    String email = user.getEmail();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    String region = dataSnapshot.child("Region").getValue().toString();
                    String address = dataSnapshot.child("Address").getValue().toString();
                    String dob = dataSnapshot.child("Birthday").getValue().toString();
                    String gender = dataSnapshot.child("Gender").getValue().toString();
                    String institution = dataSnapshot.child("Institution").getValue().toString();
                    String department = dataSnapshot.child("Department").getValue().toString();
                    String year = dataSnapshot.child("Year").getValue().toString();

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

//        SmileRating smileRating = findViewById(R.id.smile_rating);
//
//        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
//            @Override
//            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
//                // reselected is false when user selects different smiley that previously selected one
//                // true when the same smiley is selected.
//                // Except if it first time, then the value will be false.
//                switch (smiley) {
//                    case SmileRating.BAD:
//                        Toast.makeText(PublicTeacherProfileActivity.this, "Bad", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmileRating.GOOD:
//                        Toast.makeText(PublicTeacherProfileActivity.this, "Good", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmileRating.GREAT:
//                        Toast.makeText(PublicTeacherProfileActivity.this, "Great", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmileRating.OKAY:
//                        Toast.makeText(PublicTeacherProfileActivity.this, "Okay", Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmileRating.TERRIBLE:
//                        Toast.makeText(PublicTeacherProfileActivity.this, "Terrible", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });
    }
}
