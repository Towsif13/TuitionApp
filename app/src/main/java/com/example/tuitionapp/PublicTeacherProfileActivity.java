package com.example.tuitionapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicTeacherProfileActivity extends AppCompatActivity {

    ImageView backBtn;
    ImageView send_req;
    Boolean b =false;
    TextView teacherName , teacherEmail , teacherPhone , teacherRegion , teacherAddress , teacherDOB , teacherGender ,
            teacherInstitution , teacherDepartment , teacherYear,send_txt , tutorRating , studentCount;
    ImageView videocallBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;

    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference myref,reference,sturef ,forValidateDBref;
    private String uid, receiverUserId, Current_state ;
    private  String calledBy="";

    RelativeLayout VideoCAMRL;
    private RelativeLayout add_btn_teacher , msg_btn_teacher, dec_btn;
    private RecyclerView reviewRecycler;
    private TextView teacherReview;
    private ArrayList<ReviewCurrentTutorList> arrayList;
    private FirebaseRecyclerOptions<ReviewCurrentTutorList> options;
    private FirebaseRecyclerAdapter<ReviewCurrentTutorList,FirebaseViewHolder> adapter;
    private DatabaseReference databaseReference;
    private int stu_count = 0;


    CircleImageView propic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_teacher_profile);

        propic = findViewById(R.id.publicProPic);
        reviewRecycler = findViewById(R.id.review_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        //reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setLayoutManager(layoutManager);
        arrayList = new ArrayList<ReviewCurrentTutorList>();

        add_btn_teacher = findViewById(R.id.add_btn_teacher);
        msg_btn_teacher = findViewById(R.id.msg_btn_teacher);
        dec_btn = findViewById(R.id.decline_btn_teacher_profile);

        videocallBtn = findViewById(R.id.VideoCamIV); // video call

        Current_state = "not_student";


        VideoCAMRL =findViewById(R.id.VideoCAMRL);

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
        send_req = findViewById(R.id.t_send_req);
        send_txt = findViewById(R.id.send_txt_t);
        final TextView student_count = findViewById(R.id.student_count_tutor_public_prof);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myref = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        sturef = mFirebaseDatabase.getReference().child("AcceptStudent");
        receiverUserId = getIntent().getExtras().getString("userid");



        msg_btn_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicTeacherProfileActivity.this,MessageActivity.class);
                intent.putExtra("userId",receiverUserId);
                startActivity(intent);
            }
        });




        // video call
        validateUser( receiverUserId , VideoCAMRL ,uid);


        VideoCAMRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //important

                Intent intent = new Intent(PublicTeacherProfileActivity.this, VideoCallingActivity.class);
                intent.putExtra("fromPuBTeaTeaID",receiverUserId);

                finish();
                startActivity(intent);

            }


        });

        sturef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    stu_count = (int)dataSnapshot.getChildrenCount();
                    student_count.setText(Integer.toString(stu_count));

                }else{

                    student_count.setText("0");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DisplayReview();

        tutorRating = findViewById(R.id.tutor_rating_public_prof);
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("TutorRating");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float ratingSum = 0;
                int ratingTotal = 0;
                float ratingAvg ;
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    for (DataSnapshot child : dataSnapshot.child(receiverUserId).getChildren()){
                        ratingSum = ratingSum + Float.valueOf(child.getValue().toString());
                        ratingTotal++;
                    }
                    if (ratingTotal != 0){
                        ratingAvg = ratingSum/ratingTotal;
                        tutorRating.setText(String.valueOf(ratingAvg)+"/5");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //TODO:
        studentCount = findViewById(R.id.student_count_tutor_public_prof);

        myref.child("Users").child("Teacher").child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String fname = dataSnapshot.child("FirstName").getValue().toString();
                    String lname = dataSnapshot.child("LastName").getValue().toString();
                    String name = fname+" "+lname;
                   // String email = receiverUserId;
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                   // String region = dataSnapshot.child("Region").getValue().toString();
                    String address = dataSnapshot.child("Address").getValue().toString();
                    String dob = dataSnapshot.child("Birthday").getValue().toString();
                    String gender = dataSnapshot.child("Gender").getValue().toString();
                    String institution = dataSnapshot.child("Institution").getValue().toString();
                    String department = dataSnapshot.child("Department").getValue().toString();
                    String year = dataSnapshot.child("Year").getValue().toString();

                    if (dataSnapshot.child("ProfileImage").exists()){
                        String propicc = dataSnapshot.child("ProfileImage").getValue().toString();
                        Picasso.get().load(propicc).into(propic);
                        //Toast.makeText(PublicStudentProfile.this, propicc, Toast.LENGTH_LONG).show();
                    }

                    teacherName.setText(name);
                   // teacherEmail.setText(email);
                    teacherPhone.setText(phone);
                    //teacherRegion.setText(region);
                    teacherAddress.setText(address);
                    teacherDOB.setText(dob);
                    teacherGender.setText(gender);
                    teacherInstitution.setText(institution);
                    teacherDepartment.setText(department);
                    teacherYear.setText(year);

                    dec_btn.setEnabled(false);
                    dec_btn.setVisibility(View.GONE);

                    MaintenanceOfButtons();


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_btn_teacher.setOnClickListener(new View.OnClickListener() {
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

    private void DisplayReview(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Review").child(receiverUserId);
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<ReviewCurrentTutorList>().setQuery(databaseReference,ReviewCurrentTutorList.class).build();

        adapter = new FirebaseRecyclerAdapter<ReviewCurrentTutorList, FirebaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, int i, @NonNull ReviewCurrentTutorList reviewCurrentTutorList) {

                firebaseViewHolder.review.setText(reviewCurrentTutorList.getReview());

                String overall = reviewCurrentTutorList.getOverall();
                Float overallScore = Float.valueOf(overall);
                if (overallScore<=1){
                    firebaseViewHolder.reviewTitle.setText("Terrible");
                    firebaseViewHolder.review_bg.setBackgroundColor(getResources().getColor(R.color.Terrible));
                }
                else if (overallScore>1 && overallScore<=2){
                    firebaseViewHolder.reviewTitle.setText("Bad");
                    firebaseViewHolder.review_bg.setBackgroundColor(getResources().getColor(R.color.Bad));
                }
                else if (overallScore>2 && overallScore<=3){
                    firebaseViewHolder.reviewTitle.setText("Average");
                    firebaseViewHolder.review_bg.setBackgroundColor(getResources().getColor(R.color.Average));
                }
                else if (overallScore>3 && overallScore<=4){
                    firebaseViewHolder.reviewTitle.setText("Good");
                    firebaseViewHolder.review_bg.setBackgroundColor(getResources().getColor(R.color.Good));
                }
                else if (overallScore>4){
                    firebaseViewHolder.reviewTitle.setText("Excellent");
                    firebaseViewHolder.review_bg.setBackgroundColor(getResources().getColor(R.color.Excellent));
                }
                //firebaseViewHolder.reviewTitle.setText(reviewCurrentTutorList.getOverall());
            }

            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FirebaseViewHolder(LayoutInflater.from(PublicTeacherProfileActivity.this).inflate(R.layout.review_row,parent,false));
            }
        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        reviewRecycler.setAdapter(adapter);





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
                                                                        add_btn_teacher.setEnabled(true);
                                                                        Current_state = "connected";
                                                                        send_req.setImageResource(R.drawable.ic_person_add);
                                                                        send_txt.setText("Remove Student");
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
                                                add_btn_teacher.setEnabled(true);
                                                Current_state = "not_student";
                                                 send_req.setImageResource(R.drawable.ic_add_person_req);
                                                 send_txt.setText("SEND REQUEST");
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
                        send_req.setImageResource(R.drawable.ic_cancel_req);
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
                                send_txt.setText("Remove Student");

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
                Toast.makeText(PublicTeacherProfileActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                add_btn_teacher.setEnabled(true);
                                                Current_state = "not_student";
                                                send_req.setImageResource(R.drawable.ic_add_person_req);
                                                send_txt.setText("SEND REQUEST");
                                            }
                                        }

                                    });
                        }
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

                                    add_btn_teacher.setEnabled(true);
                                    Current_state = "request_sent";
                                    send_req.setImageResource(R.drawable.ic_cancel_req);
                                    send_txt.setText("Cancel Request");

                                } else {
                                    send_req.setImageResource(R.drawable.ic_add_person_req);
                                }
                            }

                        });

                    }
                }


                }
            });
        }

    public static class FirebaseViewHolder extends RecyclerView.ViewHolder{

        public TextView review , reviewTitle;
        public LinearLayout review_bg;
        public FirebaseViewHolder(@NonNull View itemView) {
            super(itemView);

            review = itemView.findViewById(R.id.teacher_review_show);
            reviewTitle = itemView.findViewById(R.id.teacher_review_title);
            review_bg = itemView.findViewById(R.id.review_bg);
        }
    }



    // video chat validation of user
    public void validateUser(final String receiverUserId , final RelativeLayout VideoCAMRL , final String uid){

        forValidateDBref= FirebaseDatabase.getInstance().getReference();

        forValidateDBref.child("AcceptTeacher").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(receiverUserId)) {

                    VideoCAMRL.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        forValidateDBref.child("AcceptStudent").child(receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(uid)) {

                    VideoCAMRL.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PublicTeacherProfileActivity.this, "student Is not", Toast.LENGTH_SHORT).show();
            }
        });



    }



}










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


