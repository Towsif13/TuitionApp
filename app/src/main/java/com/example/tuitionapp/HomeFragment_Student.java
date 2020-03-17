package com.example.tuitionapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment_Student extends Fragment {

    ConstraintLayout postOffer , curr_tutor,studentProfile,receivedRequestsStudent, reviewTutor;

    private TextView studentName;
    private CircleImageView studentImage;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragment__student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postOffer = getActivity().findViewById(R.id.PostOfferLayout);
        postOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),TuitionPostActivity.class);
                startActivity(intent);
            }
        });

        curr_tutor = getActivity().findViewById(R.id.student_curr_tutor);
        curr_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Current_teacher.class);
                startActivity(intent);
            }
        });
        studentProfile = getActivity().findViewById(R.id.studentProfile);
        studentProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),StudentProfileActivity.class);
                startActivity(intent);
            }
        });

        receivedRequestsStudent = getActivity().findViewById(R.id.requests_student);
        receivedRequestsStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),TeacherReceivedRequest.class);
                startActivity(intent);
            }
        });

        reviewTutor = getActivity().findViewById(R.id.review_tutor);
        reviewTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ReviewCurrentTutorsActivity.class);
                startActivity(intent);
            }
        });

        studentName = getActivity().findViewById(R.id.userNameStudent);
        studentImage = getActivity().findViewById(R.id.userPhotoStudentHome);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        uid = mAuth.getUid();

        myref.child("Users").child("Student").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("FirstName").getValue().toString();
                String lname = dataSnapshot.child("LastName").getValue().toString();
                String name = fname+" "+lname;

                if (dataSnapshot.child("ProfileImage").exists()){
                    String propic = dataSnapshot.child("ProfileImage").getValue().toString();
                    Picasso.get().load(propic).into(studentImage);;
                }

                studentName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
