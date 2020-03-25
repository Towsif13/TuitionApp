package com.example.tuitionapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoCallingActivity extends AppCompatActivity {

    CircleImageView propic;
    private TextView  nameContact;
    private ImageView acceptCall , rejectCall;
    String receiverUserId = "" , reciverUserName ="" ,reciverUserImg= "" ;
    String callerId = ""  , callerName= "" ,callerImg= "" ,checker ="",receiverUser;
    String reciverUserIdStudentPublicPro = " ";
    private DatabaseReference userRef;


    String stuId_call_from_publicStudent_Pro = "" , teaId_ring_FromPublicTeacher_pro="";
    String CurrentUserCallerStu="" ,CurrentUserRingTea="";


    private MediaPlayer mediaPlayer;

    String callingId="" ,RiningId="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_calling);


        propic = findViewById(R.id.vidCallpic);
        nameContact =findViewById(R.id.userNameforVidCall);
        acceptCall = findViewById(R.id.Accpetcall);
        rejectCall = findViewById(R.id.rejectCall);




        // rigntone
       mediaPlayer = MediaPlayer.create(this,R.raw.kgf);
    //    callerId = getIntent().getExtras().getString("CallerId");





        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        CurrentUserCallerStu = FirebaseAuth.getInstance().getCurrentUser().getUid(); //stdentIsCalling


        CurrentUserRingTea = FirebaseAuth.getInstance().getCurrentUser().getUid(); // teacherEnd


        receiverUserId = getIntent().getExtras().getString("ReciverID"); //teacher



        stuId_call_from_publicStudent_Pro = getIntent().getExtras().getString("callerIdFromPubStu"); // callerId

        teaId_ring_FromPublicTeacher_pro = getIntent().getExtras().getString("fromPuBTeaTeaID"); //ringID




        getAndSetVideoCallUserProInfo();

        reciverUserIdStudentPublicPro = getIntent().getExtras().getString("visit_user_id");

        rejectCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                checker ="clicked";
                cancelCallingUse();
            }
        });


        acceptCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mediaPlayer.stop();

                final HashMap<String,Object> callingPickUPMap=new HashMap<>();

                callingPickUPMap.put("picked","picked");

                userRef.child("Teacher").child(CurrentUserRingTea).child("Ringing")
                        .updateChildren(callingPickUPMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){

                            Intent intent = new Intent(VideoCallingActivity.this,MainVideoChatActivity.class);
                            startActivity(intent);

                        }
                    }
                });
            }
        });
    }



    private void cancelCallingUse() {

        // from sender side

        userRef.child("Student").child(CurrentUserCallerStu).child("Calling")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()&& dataSnapshot.hasChild("calling")){

                           callingId = dataSnapshot.child("calling").getValue().toString(); // we will get Teacher ID

                           userRef.child("Teacher").child(callingId).child("Ringing")
                                   .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {

                                   if(task.isSuccessful()){
                                       userRef.child("Student").child(CurrentUserCallerStu).child("Calling")
                                               .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {

                                               Intent intent = new Intent(VideoCallingActivity.this,MainActivity.class);
                                               startActivity(intent);

                                               finish();
                                           }
                                       });
                                   }

                               }
                           });
                        }else{

                            Intent intent = new Intent(VideoCallingActivity.this,MainActivity.class);
                            intent.putExtra("userid", teaId_ring_FromPublicTeacher_pro);
                            intent.putExtra("CurrentStudentId",stuId_call_from_publicStudent_Pro);


                            startActivity(intent);

                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        // reciver Side

        userRef.child("Teacher").child(CurrentUserRingTea).child("Ringing")               // teacher ->
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()&& dataSnapshot.hasChild("ringing")){

                            RiningId = dataSnapshot.child("ringing").getValue().toString(); // we iwill get student ID
                            userRef.child("Student").child(RiningId).child("Calling")
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        userRef.child("Teacher").child(CurrentUserRingTea).child("Ringing")
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Intent intent = new Intent(VideoCallingActivity.this,MainActivity.class);


                                                startActivity(intent );
                                                finish();
                                            }
                                        });
                                    }

                                }
                            });
                        }else{

                            Intent intent = new Intent(VideoCallingActivity.this,MainActivity.class);



                            startActivity(intent );
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void getAndSetVideoCallUserProInfo() {

            // vice versa

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if( teaId_ring_FromPublicTeacher_pro != null && dataSnapshot.child("Teacher").child(teaId_ring_FromPublicTeacher_pro).exists()){     // check teacher -> teacherId


                        if(dataSnapshot.child("Teacher").child(teaId_ring_FromPublicTeacher_pro).hasChild("ProfileImage")){
                            reciverUserImg = dataSnapshot.child("Teacher").child(teaId_ring_FromPublicTeacher_pro).child("ProfileImage").getValue().toString();
                            Picasso.get().load(reciverUserImg).placeholder(R.drawable.ic_person_black_24dp).into(propic);

                        }
                        reciverUserName = dataSnapshot.child("Teacher").child(teaId_ring_FromPublicTeacher_pro).child("FirstName").getValue().toString();
                        Toast.makeText(VideoCallingActivity.this, ""+reciverUserName, Toast.LENGTH_SHORT).show();
                        nameContact.setText(reciverUserName);
                    }
                    else{
                        if(stuId_call_from_publicStudent_Pro!= null && dataSnapshot.child("Student").child(stuId_call_from_publicStudent_Pro).exists()){

                            if(dataSnapshot.child("Student").child(stuId_call_from_publicStudent_Pro).hasChild("ProfileImage")){
                                callerImg = dataSnapshot.child("Student").child(stuId_call_from_publicStudent_Pro).child("ProfileImage").getValue().toString();
                                Picasso.get().load(callerImg).placeholder(R.drawable.ic_person_black_24dp).into(propic);


                            }
                            callerName = dataSnapshot.child("Student").child(stuId_call_from_publicStudent_Pro).child("FirstName").getValue().toString();
                            nameContact.setText(callerName);



                        }
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }

    @Override
    protected void onStart() {
        super.onStart();


        mediaPlayer.start();


        userRef.addListenerForSingleValueEvent(  new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // this is form Student --- studnet call korle tokhn dhukbe

                if (teaId_ring_FromPublicTeacher_pro != null && !checker.equals("checker")&&!dataSnapshot.child("Teacher").child(teaId_ring_FromPublicTeacher_pro).hasChild("Ringing") && !dataSnapshot.child("Student").child(CurrentUserCallerStu).hasChild("Calling")) {

                    final HashMap<String, Object> callingInfo = new HashMap<>();

                    callingInfo.put("calling", teaId_ring_FromPublicTeacher_pro);

                    userRef.child("Student").child(CurrentUserCallerStu).child("Calling").updateChildren(callingInfo).addOnCompleteListener( new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                final HashMap<String, Object> ringingInfo = new HashMap<>();

                                ringingInfo.put("ringing", CurrentUserCallerStu);
                                userRef.child("Teacher").child(teaId_ring_FromPublicTeacher_pro).child("Ringing").updateChildren(ringingInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        finish();
                                        startActivity(getIntent());

                                    }
                                });

                            }
//
                        }
                    });


//
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(VideoCallingActivity.this, "DatabasError", Toast.LENGTH_SHORT).show();
            }
        });

        // this is for teacher end

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Teacher").child(CurrentUserRingTea).hasChild("Ringing") && dataSnapshot.child("Student").child(stuId_call_from_publicStudent_Pro).hasChild("Calling")){
                    acceptCall.setVisibility(View.VISIBLE);

                }

                if(teaId_ring_FromPublicTeacher_pro!= null  && dataSnapshot.child("Teacher").child(teaId_ring_FromPublicTeacher_pro).child("Ringing").hasChild("picked"))
                {
                    mediaPlayer.stop();
                    Intent intent = new Intent(VideoCallingActivity.this,MainVideoChatActivity.class);
                    startActivity(intent);
                }




            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
