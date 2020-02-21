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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment_Teacher extends Fragment {


    ConstraintLayout teacherProfile, tutorPostOffer;

    private TextView teacherName;
    private CircleImageView teacherImage;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myref;
    private String uid;

    public HomeFragment_Teacher() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        teacherProfile = getActivity().findViewById(R.id.teacherProfile);
        teacherProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),TeacherProfileActivity.class);
                startActivity(intent);
            }
        });

        tutorPostOffer = getActivity().findViewById(R.id.tutor_post_offer);
        tutorPostOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),TeacherOfferPostActivity.class);
                startActivity(intent);
            }
        });

        teacherName = getActivity().findViewById(R.id.userNameTeacherHome);
        teacherImage = getActivity().findViewById(R.id.userPhotoTeacherHome);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        uid = mAuth.getUid();

        myref.child("Users").child("Teacher").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("FirstName").getValue().toString();
                String lname = dataSnapshot.child("LastName").getValue().toString();
                String name = fname+" "+lname;

                if (dataSnapshot.child("ProfileImage").exists()){
                    String propic = dataSnapshot.child("ProfileImage").getValue().toString();
                    Picasso.get().load(propic).into(teacherImage);;
                }

                teacherName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
