package com.example.tuitionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PublicStudentProfile extends AppCompatActivity {

    ImageView backBtn;
    ImageView send_req;
    Boolean b = false;

    TextView studentName,send_txt,studentEmail, studentPhone, studentRegion, studentAddress, studentDOB, studentGender,
            studentMedium, studentClass;

    private FirebaseAuth mAuth;
    String name;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference reference, myref,sturef;
    private String uid, receiverUserId, Current_state;

    FloatingActionButton floatingActionButton;

    private RelativeLayout add_btn_student_profile, msg_btn_student_profile ,dec_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_student_profile);

        backBtn = findViewById(R.id.student_profile_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Current_state = "not_student";

        add_btn_student_profile = findViewById(R.id.add_btn_student_profile);
        msg_btn_student_profile = findViewById(R.id.msg_btn_student_profile);
        dec_btn = findViewById(R.id.decline_btn_student_profile);


        msg_btn_student_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PublicStudentProfile.this, "MSG", Toast.LENGTH_SHORT).show();
            }
        });
        send_txt = findViewById(R.id.send_txt);
        studentName = findViewById(R.id.studentName);
        studentEmail = findViewById(R.id.studentEmail);
        studentPhone = findViewById(R.id.studentPhone);
        studentRegion = findViewById(R.id.studentRegion);
        studentAddress = findViewById(R.id.studentAddress);
        studentDOB = findViewById(R.id.studentDOB);
        studentGender = findViewById(R.id.studentGender);
        studentMedium = findViewById(R.id.studentMedium);
        studentClass = findViewById(R.id.studentClass);
        send_req = findViewById(R.id.st_send_req);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        sturef = mFirebaseDatabase.getReference().child("AcceptStudent");
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        receiverUserId = getIntent().getExtras().getString("userid");


        myref.child("Users").child("Student").child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    String fname = dataSnapshot.child("FirstName").getValue().toString();
                    String lname = dataSnapshot.child("LastName").getValue().toString();
                    name = fname + " " + lname;
                   // String email = user.getEmail();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                   // String region = dataSnapshot.child("Region").getValue().toString();
                    String address = dataSnapshot.child("Address").getValue().toString();
                    String dob = dataSnapshot.child("Birthday").getValue().toString();
                    String gender = dataSnapshot.child("Gender").getValue().toString();
                    String medium = dataSnapshot.child("Medium").getValue().toString();
                    String classs = dataSnapshot.child("Class").getValue().toString();

                    studentName.setText(name);
                   // studentEmail.setText(email);
                    studentPhone.setText(phone);
                    //studentRegion.setText(region);
                    studentAddress.setText(address);
                    studentDOB.setText(dob);
                    studentGender.setText(gender);
                    studentMedium.setText(medium);
                    studentClass.setText(classs);

                    dec_btn.setEnabled(false);
                    dec_btn.setVisibility(View.GONE);

                    MaintenanceOfButtons();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        add_btn_student_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Current_state.equals("not_student")){
                    b = false;
                    sendRequestToStudent();
                }

                if(Current_state.equals("request_sent")){

                    CancelRequest();
                }

                if(Current_state.equals("request_received")){

                    AcceptRequest();
                }
                if(Current_state.equals("connected")){

                    RemoveTeacher();
                }



            }
        });
    }

    private void RemoveTeacher() {

        sturef.child(uid).child(receiverUserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            sturef.child(receiverUserId).child(uid).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                add_btn_student_profile.setEnabled(true);
                                                Current_state = "not_student";
                                                send_req.setImageResource(R.drawable.ic_person_white);
                                                send_txt.setText("Send Request");
                                            }
                                        }

                                    });
                        }
                    }
                });



    }

    private void AcceptRequest() {


        SimpleDateFormat df1 = new SimpleDateFormat("d-MM-yyyy");
        final String today_date = df1.format(Calendar.getInstance().getTime());
        sturef.child(uid).child(receiverUserId).child("date").setValue(today_date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sturef.child(receiverUserId).child(uid).child("date").setValue(today_date).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                reference = FirebaseDatabase.getInstance().getReference().child("Request");

                                reference.child(uid).child(receiverUserId).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    reference.child(receiverUserId).child(uid).removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if (task.isSuccessful()) {
                                                                        add_btn_student_profile.setEnabled(true);
                                                                        Current_state = "connected";
                                                                        send_req.setImageResource(R.drawable.ic_person_add);
                                                                        send_txt.setText("Remove Student");

                                                                        dec_btn.setEnabled(false);
                                                                        dec_btn.setVisibility(View.GONE);


                                                                    }
                                                                }

                                                            });
                                                }
                                            }
                                        });


                            }
                        }
                    });

                }
            }
        });
    }

    private void CancelRequest() {

            reference = FirebaseDatabase.getInstance().getReference().child("Request");

            reference.child(uid).child(receiverUserId).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        reference.child(receiverUserId).child(uid).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    add_btn_student_profile.setEnabled(true);
                                    Current_state = "not_student";
                                    send_req.setImageResource(R.drawable.ic_person_white);
                                    send_txt.setText("Send Request");
                                }
                            }

                        });
                    }
                }
            });


    }

    private void MaintenanceOfButtons() {

        reference = FirebaseDatabase.getInstance().getReference().child("Request");

        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(receiverUserId)) {

                    String request_type = dataSnapshot.child(receiverUserId).child("request_type").getValue().toString();
                    if (request_type.equals("sent")) {
                        Current_state = "request_sent";
                        send_txt.setText("Cancel Request");
                        send_req.setImageResource(R.drawable.ic_plus_one_black_24dp);
                    }else if(request_type.equals("received")){
                        Current_state = "request_received";
                        send_txt.setText("Accept Request");

                        dec_btn.setVisibility(View.VISIBLE);
                        dec_btn.setEnabled(true);

                        dec_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CancelRequest();
                            }
                        });
                    }
                }
                else{
                    reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(receiverUserId)){

                                Current_state = "connected";
                                send_txt.setText("Remove Teacher");

                                dec_btn.setVisibility(View.GONE);
                                dec_btn.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void sendRequestToStudent() {

        reference = FirebaseDatabase.getInstance().getReference().child("Request");

        reference.child(uid)
                .child(receiverUserId).child("request_type")
                .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(b == false){

                    if (task.isSuccessful()) {
                        reference.child(receiverUserId).child(uid).child("request_type")
                                .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    b = true;
                                    add_btn_student_profile.setEnabled(true);
                                    Current_state = "request_sent";
                                    send_req.setImageResource(R.drawable.ic_plus_one_black_24dp);
                                    send_txt.setText("Cancel Request");

                                }else{
                                    send_req.setImageResource(R.drawable.ic_person_white);
                                }
                            }

                        });
                    }


                }
            }
        });
    }
}
